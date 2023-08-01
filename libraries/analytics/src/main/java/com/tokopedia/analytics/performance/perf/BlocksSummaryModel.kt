package com.tokopedia.analytics.performance.perf

data class BlocksSummaryModel(
    var timeToFirstLayout: BlocksPerformanceModel? = BlocksPerformanceModel(),
    var timeToInitialLayout: BlocksPerformanceModel? = BlocksPerformanceModel()
) {
    fun ttfl() = timeToFirstLayout?.inflateTime ?: 0

    fun ttil() = timeToInitialLayout?.inflateTime ?: 0
}
