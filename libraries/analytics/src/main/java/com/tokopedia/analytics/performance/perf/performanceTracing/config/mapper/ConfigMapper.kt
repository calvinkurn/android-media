package com.tokopedia.analytics.performance.perf.performanceTracing.config.mapper

import com.google.gson.Gson
import com.tokopedia.analytics.performance.perf.performanceTracing.config.DefaultAppPerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.FragmentPerfConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.PagePerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.RecyclerViewPageParsingStrategy

object ConfigMapper {
    @Suppress("SwallowedException")
    fun updatePerfConfig(json: String) {
        try {
            val list = mapToConfig(json)
            DefaultAppPerformanceConfig.configs =
                list.map { perfConfig ->
                    Pair(
                        perfConfig.activityName,
                        PagePerformanceConfig(
                            traceName = perfConfig.traceName,
                            activityName = perfConfig.activityName,
                            parsingType = parseStrategy(perfConfig.strategy),
                            fragmentConfigs = perfConfig.fragmentTrace.map {
                                FragmentPerfConfig(
                                    traceName = it.traceName,
                                    parsingType = parseStrategy(it.strategy),
                                    fragmentTag = it.fragmentTag
                                )
                            }
                        )
                    )
                }.toMap()
        } catch (e: Exception) {}
    }

    private fun parseStrategy(strategy: String) =
        when (strategy) {
            "XMLFullRecyclerViewContent" -> PerfParsingType.XML(
                RecyclerViewPageParsingStrategy()
            )

            else -> PerfParsingType.XML(
                RecyclerViewPageParsingStrategy()
            )
        }

    private fun mapToConfig(json: String): List<PerfConfig> {
        val gson = Gson()
        val perfConfigList: PerfConfigList = gson.fromJson(json, PerfConfigList::class.java)

        return perfConfigList.perfConfigs
    }
}
