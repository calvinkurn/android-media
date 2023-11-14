package com.tokopedia.analytics.performance.perf.performanceTracing.config

class DebugAppPerformanceConfig: AppPerformanceConfigRepository {
    var debugConfigs: Map<String, PagePerformanceConfig> = mapOf(
        Pair(HOME_ACTIVITY_NAME, PagePerformanceConfig(HOME_TRACE_NAME, HOME_ACTIVITY_NAME, TraceType.XML))
    )

    override fun getConfig(activityName: String): PagePerformanceConfig? {
        return PagePerformanceConfig(
            "debug_trace_$activityName",
            activityName,
            TraceType.XML
        )
    }
}
