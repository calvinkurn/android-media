package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.tokopedia.analytics.performance.perf.performanceTracing.data.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData

class AppPerformanceRepository(
    val name: String
) : PerformanceRepository {
    var firebasePerformance: FirebasePerformance? = null
    var trace: Trace? = null
    var startTime = 0L

    override fun startRecord() {
        startRecordFirebase()
    }

    override fun stopRecord(blocks: Map<String, BlocksModel>) {
        stopRecordFirebase()
    }

    override fun recordPerfData(performanceTraceData: PerformanceTraceData, blocks: Map<String, BlocksModel>) {
        // no op
    }

    override fun getTraceName(): String {
        return name
    }

    private fun stopRecordFirebase() {
        try {
            trace?.stop()
        } catch (ignored: Exception) {
        }
    }

    private fun startRecordFirebase() {
        startTime = System.currentTimeMillis()
        try {
            firebasePerformance = FirebasePerformance.getInstance()
            firebasePerformance?.let {
                trace = firebasePerformance?.newTrace(name)
                trace?.let {
                    it.start()
                }
            }
        } catch (ignored: Exception) {
        }
    }
}
