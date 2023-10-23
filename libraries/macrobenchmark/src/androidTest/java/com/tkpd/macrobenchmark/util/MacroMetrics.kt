package com.tkpd.macrobenchmark.util

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric

object MacroMetrics {
    const val PLT_TTFL = "PageLoadTime.AsyncTTFL"
    const val PLT_TTIL = "PageLoadTime.AsyncTTIL"

    @ExperimentalMetricApi
    fun getPltMetrics(tracePageName: String): List<TraceSectionMetric> {
        return listOf(
            TraceSectionMetric("$PLT_TTFL$tracePageName"),
            TraceSectionMetric("$PLT_TTIL$tracePageName")
        )
    }
}
