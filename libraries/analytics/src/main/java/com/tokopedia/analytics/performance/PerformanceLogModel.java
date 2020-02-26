package com.tokopedia.analytics.performance;

import java.util.HashMap;
import java.util.Map;

public class PerformanceLogModel {
    private long startTime;
    private long endTime;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Long> metrics = new HashMap<>();

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void putAtrribute(String attribute, String value) {
        attributes.put(attribute, value);
    }

    public void putMetric(String parameter, long value) {
        metrics.put(parameter, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, Long> getMetrics() {
        return metrics;
    }
}
