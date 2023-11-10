package com.tokopedia.analytics.performance.perf.performanceTracing

data class PerformanceTraceData(
    val timeToFirstLayout: Long = 0L,
    val timeToInitialLoad: Long = 0L,
    val blocksModel: Map<String, BlocksModel> = mapOf()
) {
    fun ttflMeasured() = timeToFirstLayout != 0L
    fun ttilMeasured() = timeToInitialLoad != 0L
}

data class BlocksModel(
    val name: String,
    val startTime: Long = 0L,
    val endTime: Long = 0L
) {
    fun inflateTime(): Long? {
        if (startTime == 0L || endTime == 0L) return null
        return endTime - startTime
    }
}
