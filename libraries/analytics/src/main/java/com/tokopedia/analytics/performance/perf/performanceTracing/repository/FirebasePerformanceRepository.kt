package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import android.util.Log
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.tokopedia.analytics.performance.perf.performanceTracing.BlocksModel
import com.tokopedia.analytics.performance.perf.performanceTracing.PerformanceTraceData

class AppPerformanceRepository(
    val traceName: String
) : PerformanceRepository {
    var firebasePerformance: FirebasePerformance? = null
    var trace: Trace? = null
    var startTime = 0L

    override fun startRecord() {
        startRecordFirebase()
    }

    override fun stopRecord(blocks: Map<String, BlocksModel>) {
        Log.d("AppPerformanceTrace", blocks.toString())
        stopRecordFirebase()
    }

    override fun recordPerfData(performanceTraceData: PerformanceTraceData, blocks: Map<String, BlocksModel>) {
        // no op
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
                trace = firebasePerformance?.newTrace(traceName)
                trace?.let {
                    it.start()
                }
            }
        } catch (ignored: Exception) {
        }
    }
}
