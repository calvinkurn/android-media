package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.RecyclerViewPageParsingStrategy

class DebugAppPerformanceConfig : AppPerformanceConfigRepository {
    override fun getConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>? {
        return PagePerformanceConfig(
            "debug_trace_$activityName",
            activityName,
            PerfParsingType.XML(
                RecyclerViewPageParsingStrategy()
            )
        )
    }
}
