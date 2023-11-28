package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.config.constant.HOME_ACTIVITY_NAME
import com.tokopedia.analytics.performance.perf.performanceTracing.config.constant.HOME_TRACE_NAME
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.RecyclerViewPageParsingStrategy

class DefaultAppPerformanceConfig : AppPerformanceConfigRepository {
    companion object {
        var configs: Map<String, PagePerformanceConfig<out PerfParsingType>> = mapOf(
            Pair(
                HOME_ACTIVITY_NAME,
                PagePerformanceConfig(
                    HOME_TRACE_NAME, HOME_ACTIVITY_NAME,
                    PerfParsingType.XML(
                        RecyclerViewPageParsingStrategy()
                    )
                )
            )
        )
    }
    override fun getConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>? {
        return configs.get(activityName)
    }
}
