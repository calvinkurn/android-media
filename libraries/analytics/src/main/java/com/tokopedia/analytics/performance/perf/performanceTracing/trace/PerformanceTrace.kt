package com.tokopedia.analytics.performance.perf.performanceTracing.trace

import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData

interface PerformanceTrace {
    fun traceId(): String

    fun setTraceId(id: String)

    fun startMonitoring()

    fun stopMonitoring(result: Result<PerformanceTraceData>)

    fun recordPerformanceData(result: Result<PerformanceTraceData>)
}

sealed class Result<out T>

data class Success<T>(val data: T, val message: String) : Result<T>()
data class Error(val message: String) : Result<Nothing>()
