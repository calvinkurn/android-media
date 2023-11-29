package com.tokopedia.analytics.performance.perf.performanceTracing.config

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType

data class PagePerformanceConfig<T : PerfParsingType>(
    val traceName: String,
    val activityName: String,
    val parsingType: T,
    val fragmentConfigs: List<FragmentPerfConfig<T>> = listOf()
)

data class FragmentPerfConfig<T : PerfParsingType>(
    val traceName: String,
    val fragmentTag: String,
    val parsingType: T
)