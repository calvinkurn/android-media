package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import android.os.Build
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData

class AppPerformanceRepository(
    val name: String
) : PerformanceRepository {
    var firebasePerformance: FirebasePerformance? = null
    var trace: Trace? = null
    var startTime = 0L

    override fun startRecord() {
        beginTrace()
        startRecordFirebase()
    }

    private fun beginTrace() {
        beginAsyncSystraceSection(
                "PageLoadTime.AsyncTTFL$name",
                COOKIE_TTFL
        )
        beginAsyncSystraceSection(
                "PageLoadTime.AsyncTTIL$name",
                COOKIE_TTIL
        )
    }

    override fun stopRecord(performanceTraceData: PerformanceTraceData) {
        if (performanceTraceData.ttilMeasured()) {
            stopRecordFirebase()
            recordPerfData(performanceTraceData)
            putFullyDrawnTrace(name)
            endAsyncSystraceSection(
                "PageLoadTime.AsyncTTIL$name",
                COOKIE_TTIL
            )
        } else if (performanceTraceData.ttflMeasured()) {
            endAsyncSystraceSection(
                "PageLoadTime.AsyncTTFL$name",
                COOKIE_TTFL
            )
        }
    }

    override fun recordPerfData(performanceTraceData: PerformanceTraceData) {
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


    private fun beginAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.os.Trace.beginAsyncSection(methodName, cookie)
        }
    }

    private fun endAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            android.os.Trace.endAsyncSection(methodName, cookie)
        }
    }

    private fun putFullyDrawnTrace(traceName: String) {
        try {
            android.os.Trace.beginSection(
                String.format(
                    BlocksPerformanceTrace.ANDROID_TRACE_FULLY_DRAWN,
                    traceName
                )
            )
        } finally {
            android.os.Trace.endSection()
        }
    }
    
    companion object {
        const val COOKIE_TTFL = 77
        const val COOKIE_TTIL = 78
    }
}
