package com.tokopedia.analyticsdebugger.debugger.domain.model;

import java.util.HashMap;
import java.util.Map;

public class PerformanceLogModel {
    private String traceName;
    private long startTime;
    private long endTime;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Long> metrics = new HashMap<>();

    public PerformanceLogModel(String traceName) {
        this.traceName = traceName;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

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

    public void setAttributes(Map<String, String> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
    }

    public void setMetrics(Map<String, Long> metrics) {
        this.metrics.clear();
        this.metrics.putAll(metrics);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, Long> getMetrics() {
        return metrics;
    }

    public long getDuration() {
        return endTime - startTime;
    }

    public String getData() {
        return "Duration: " + getDuration() + "ms" +
                "\r\nAttributes: " + attributes +
                "\r\nMetrics: " + metrics;
    }
}
