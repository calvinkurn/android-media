package com.tokopedia.analyticsdebugger.debugger.domain.model;

import java.util.HashMap;
import java.util.Map;

public class FpmFileLogModel {

    private long timestampMs;
    private String timestampFormatted;
    private String traceName;
    private long durationMs;
    private Map<String, Long> metrics = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public void setTimestampFormatted(String timestampFormatted) {
        this.timestampFormatted = timestampFormatted;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public void setMetrics(Map<String, Long> metrics) {
        this.metrics = metrics;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
