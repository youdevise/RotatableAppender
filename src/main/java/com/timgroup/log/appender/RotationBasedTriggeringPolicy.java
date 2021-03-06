package com.timgroup.log.appender;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;

public class RotationBasedTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
    protected static class Clock {
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    private final Clock clock;
    private long checkCachePeriod;
    private long lastExisted;

    protected RotationBasedTriggeringPolicy(Clock clock) {
        this.clock = clock;
        this.checkCachePeriod = 1000;
    }

    public RotationBasedTriggeringPolicy() {
        this(new Clock());
    }

    public long getCheckCachePeriod() {
        return checkCachePeriod;
    }

    public void setCheckCachePeriod(long checkCachePeriod) {
        this.checkCachePeriod = checkCachePeriod;
    }

    @Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        return !activeFileExists(activeFile);
    }

    private boolean activeFileExists(File activeFile) {
        long now = clock.currentTimeMillis();
        if (now - lastExisted < checkCachePeriod) {
            return true;
        } else {
            boolean exists = activeFile.exists();
            if (exists) {
                lastExisted = now;
            }
            return exists;
        }
    }

}
