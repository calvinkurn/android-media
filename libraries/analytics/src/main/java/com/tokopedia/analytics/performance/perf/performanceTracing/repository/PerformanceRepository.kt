package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import com.tokopedia.analytics.performance.perf.performanceTracing.data.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData

interface PerformanceRepository {
    fun startRecord()
    fun stopRecord(blocks: Map<String, BlocksModel>)
    fun getTraceName(): String
    fun recordPerfData(performanceTraceData: PerformanceTraceData, blocks: Map<String, BlocksModel>)
}
