package com.tokopedia.analytics.performance.perf.performanceTracing.config.mapper

data class PerfConfig(
    val activityName: String,
    val traceName: String,
    val parsingType: String,
    val strategy: String
)

data class PerfConfigList(
    val perfConfigs: List<PerfConfig>
)
