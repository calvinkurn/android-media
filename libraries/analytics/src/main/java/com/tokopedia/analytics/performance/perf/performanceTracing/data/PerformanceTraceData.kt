package com.tokopedia.analytics.performance.perf.performanceTracing.data

data class PerformanceTraceData(
    val traceName: String = "",
    val activityName: String = "",
    val timeToFirstLayout: Long = 0L,
    val timeToInitialLoad: Long = 0L,
    val blocksModel: Map<String, BlocksModel> = mapOf()
) {
    fun ttflMeasured() = timeToFirstLayout != 0L
    fun ttilMeasured() = timeToInitialLoad != 0L
}

data class BlocksModel(
    val name: String,
    val startTraceTime: Long = 0L,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
) {
    fun inflateTime(): Long? {
        if (startTime == 0L || endTime == 0L) return null
        return endTime - startTime
    }

    fun inflateTimeAfterTrace(): Long? {
        if (startTime == 0L || endTime == 0L) return null
        return endTime - startTime
    }
}

data class DevState(
    val activityName: String = "",
    val state: State
)

enum class State{
    PERF_RESUMED,
    PERF_DISABLED,
    PERF_ENABLED,
    PERF_MEASURING,
    PERF_ERROR
}
