package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType

data class PagePerformanceConfig<T : PerfParsingType>(
    val traceName: String,
    val activityName: String,
    val parsingType: T
)
