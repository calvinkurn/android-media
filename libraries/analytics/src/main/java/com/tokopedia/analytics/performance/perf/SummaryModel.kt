package com.tokopedia.analytics.performance.perf

data class SummaryModel(
    var timeToFirstLayout: ViewPerformanceModel? = ViewPerformanceModel(),
    var timeToInitialLayout: ViewPerformanceModel? = ViewPerformanceModel()
)
