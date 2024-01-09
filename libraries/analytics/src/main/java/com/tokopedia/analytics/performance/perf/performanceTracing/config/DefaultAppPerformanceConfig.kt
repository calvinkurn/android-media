package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType

class DefaultAppPerformanceConfig : AppPerformanceConfigRepository {
    companion object {
        var configs: Map<String, PagePerformanceConfig<out PerfParsingType>> = mapOf()
    }
    override fun getConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>? {
        return configs.get(activityName)
    }
}
