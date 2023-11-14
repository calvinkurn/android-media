package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.AppPerformanceTrace

class DefaultAppPerformanceConfig: AppPerformanceConfigRepository {

    val defaultConfigs: Map<String, PagePerformanceConfig> = mapOf(
        Pair(HOME_ACTIVITY_NAME, PagePerformanceConfig(HOME_TRACE_NAME, HOME_ACTIVITY_NAME, TraceType.XML))
    )
    override fun getConfig(activityName: String): PagePerformanceConfig? {
        return defaultConfigs.get(activityName)
    }
}
