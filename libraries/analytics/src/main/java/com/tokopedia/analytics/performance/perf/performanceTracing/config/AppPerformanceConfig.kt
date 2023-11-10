package com.tokopedia.analytics.performance.perf.performanceTracing.config

class AppPerformanceConfig {
    companion object {
        val configs: Map<String, PagePerformanceConfig> = mapOf(
            Pair(HOME_ACTIVITY_NAME, PagePerformanceConfig(HOME_TRACE_NAME, HOME_ACTIVITY_NAME, TraceType.XML))
        )
    }
}

data class PagePerformanceConfig(
    val traceName: String,
    val activityName: String,
    val traceType: TraceType
)

enum class TraceType {
    XML
}
