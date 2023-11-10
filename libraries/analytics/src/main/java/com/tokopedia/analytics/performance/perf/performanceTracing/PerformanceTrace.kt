package com.tokopedia.analytics.performance.perf.performanceTracing

interface PerformanceTrace {
    fun traceId(): String

    fun setTraceId(id: String)

    fun startMonitoring()

    fun stopMonitoring(result: Result<PerformanceTraceData>)

    fun recordPerformanceData(performanceTraceData: PerformanceTraceData)
}

sealed class Result<out T>

data class Success<T>(val data: T) : Result<T>()
data class Error(val message: String) : Result<Nothing>()
