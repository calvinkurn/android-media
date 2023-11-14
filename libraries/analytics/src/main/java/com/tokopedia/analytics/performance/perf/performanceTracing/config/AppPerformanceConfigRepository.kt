package com.tokopedia.analytics.performance.perf.performanceTracing.config

interface AppPerformanceConfigRepository {
    fun getConfig(activityName: String): PagePerformanceConfig?
}
