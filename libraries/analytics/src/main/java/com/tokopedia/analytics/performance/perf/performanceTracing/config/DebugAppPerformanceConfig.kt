package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.RecyclerViewPageParsingStrategy

class DebugAppPerformanceConfig : AppPerformanceConfigRepository {
    override fun getConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>? {
        return PagePerformanceConfig(
            traceName = "debug_trace_$activityName",
            activityName = activityName,
            parsingType = PerfParsingType.XML(
                RecyclerViewPageParsingStrategy()
            ),
            fragmentConfigs = listOf()
        )
    }
}
