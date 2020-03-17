package com.tokopedia.analyticsdebugger.debugger.domain.model

import java.util.HashMap

class PerformanceLogModel(var traceName: String?) {
    var startTime: Long = 0
    var endTime: Long = 0
    private val attributes = HashMap<String, String>()
    private val metrics = HashMap<String, Long>()

    val duration: Long
        get() = endTime - startTime

    val data: String
        get() = "Duration: " + duration + "ms" +
                "\r\nAttributes: " + attributes +
                "\r\nMetrics: " + metrics

    fun setAttributes(attributes: Map<String, String>) {
        this.attributes.clear()
        this.attributes.putAll(attributes)
    }

    fun setMetrics(metrics: Map<String, Long>) {
        this.metrics.clear()
        this.metrics.putAll(metrics)
    }

    fun getAttributes(): Map<String, String> {
        return attributes
    }

    fun getMetrics(): Map<String, Long> {
        return metrics
    }
}
