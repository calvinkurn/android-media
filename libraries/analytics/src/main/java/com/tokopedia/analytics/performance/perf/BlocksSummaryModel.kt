package com.tokopedia.analytics.performance.perf

data class BlocksSummaryModel(
    var timeToFirstLayout: BlocksPerformanceModel? = BlocksPerformanceModel(),
    var timeToInitialLayout: BlocksPerformanceModel? = BlocksPerformanceModel()
)
