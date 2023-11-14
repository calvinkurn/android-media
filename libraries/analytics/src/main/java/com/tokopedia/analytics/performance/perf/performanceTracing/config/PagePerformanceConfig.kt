package com.tokopedia.analytics.performance.perf.performanceTracing.config

data class PagePerformanceConfig(
    val traceName: String,
    val activityName: String,
    val traceType: TraceType
)
