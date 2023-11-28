package com.tokopedia.analytics.performance.perf.performanceTracing.config.mapper

import com.google.gson.Gson
import com.tokopedia.analytics.performance.perf.performanceTracing.config.DefaultAppPerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.PagePerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.RecyclerViewPageParsingStrategy

object ConfigMapper {

    fun updatePerfConfig(json: String) {
        try {
            val list = mapToConfig(json)
            DefaultAppPerformanceConfig.configs =
                list.map {
                    Pair(
                        it.activityName,
                        PagePerformanceConfig(
                            traceName = it.traceName,
                            activityName = it.activityName,
                            when (it.strategy) {
                                "FullRecyclerViewContent" -> PerfParsingType.XML(
                                    RecyclerViewPageParsingStrategy()
                                )
                                else -> PerfParsingType.XML(
                                    RecyclerViewPageParsingStrategy()
                                )
                            }
                        )
                    )
                }.toMap()
        } catch (e: Exception) {}
    }

    private fun mapToConfig(json: String): List<PerfConfig> {
        val gson = Gson()
        val perfConfigList: PerfConfigList = gson.fromJson(json, PerfConfigList::class.java)

        return perfConfigList.perfConfigs
    }
}
