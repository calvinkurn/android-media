package com.tokopedia.analyticsdebugger.debugger.domain.model

import java.util.HashMap

class FpmFileLogModel {

    private var timestampMs: Long = 0
    private var timestampFormatted: String? = null
    private var traceName: String? = null
    private var durationMs: Long = 0
    private var metrics: Map<String, Long> = HashMap()
    private var attributes: Map<String, String> = HashMap()

    fun setTimestampMs(timestampMs: Long) {
        this.timestampMs = timestampMs
    }

    fun setTimestampFormatted(timestampFormatted: String) {
        this.timestampFormatted = timestampFormatted
    }

    fun setTraceName(traceName: String) {
        this.traceName = traceName
    }

    fun setDurationMs(durationMs: Long) {
        this.durationMs = durationMs
    }

    fun setMetrics(metrics: Map<String, Long>) {
        this.metrics = metrics
    }

    fun setAttributes(attributes: Map<String, String>) {
        this.attributes = attributes
    }
}
