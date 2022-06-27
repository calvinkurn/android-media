package com.tkpd.macrobenchmark.util

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric

object MacroMetrics {
    const val PLT_PREPARE_PAGE = "plt_prepare_page"
    const val PLT_NETWORK_REQUEST = "plt_network_request"
    const val PLT_RENDER = "plt_render"

    @OptIn(ExperimentalMetricApi::class)
    fun getPltMetrics(): List<TraceSectionMetric> {
        return listOf(
            TraceSectionMetric(PLT_PREPARE_PAGE),
            TraceSectionMetric(PLT_NETWORK_REQUEST),
            TraceSectionMetric(PLT_RENDER)
        )
    }
}