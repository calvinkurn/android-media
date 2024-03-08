package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType

interface AppPerformanceConfigRepository {
    fun getConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>?
}
