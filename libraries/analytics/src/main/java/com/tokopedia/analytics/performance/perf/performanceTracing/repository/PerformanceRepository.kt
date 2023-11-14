package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import com.tokopedia.analytics.performance.perf.performanceTracing.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.PerformanceTraceData

interface PerformanceRepository {
    fun startRecord()
    fun stopRecord(blocks: Map<String, BlocksModel>)
    
    fun getTraceName(): String
    fun recordPerfData(performanceTraceData: PerformanceTraceData, blocks: Map<String, BlocksModel>)
}
