package com.tokopedia.analytics.performance.perf.performanceTracing.config.mapper

data class PerfConfig(
    val activityName: String,
    val strategy: String,
    val traceName: String,
    val fragmentTrace: List<FragmentTrace> = listOf()
)

data class FragmentTrace(
    val fragmentTag: String,
    val traceName: String,
    val strategy: String
)

data class PerfConfigList(
    val perfConfigs: List<PerfConfig>
)
