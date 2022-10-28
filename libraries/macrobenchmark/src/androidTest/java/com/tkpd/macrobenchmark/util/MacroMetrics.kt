package com.tkpd.macrobenchmark.util

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric

object MacroMetrics {
    const val PLT_PREPARE_PAGE = "PageLoadTime.AsyncPreparePage"
    const val PLT_NETWORK_REQUEST = "PageLoadTime.AsyncNetworkRequest"
    const val PLT_RENDER = "PageLoadTime.AsyncRenderPage"

    @OptIn(ExperimentalMetricApi::class)
    fun getPltMetrics(tracePageName: String): List<TraceSectionMetric> {
        return listOf(
            TraceSectionMetric("$PLT_PREPARE_PAGE${tracePageName}"),
            TraceSectionMetric("$PLT_NETWORK_REQUEST${tracePageName}"),
            TraceSectionMetric("$PLT_RENDER${tracePageName}")
        )
    }
}