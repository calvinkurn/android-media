package com.tokopedia.analytics.performance.perf.performanceTracing.repository

import android.content.Context
import android.os.Build
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.IrisPerformanceData

class AppPerformanceRepository(
    val name: String,
    val appContext: Context
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
            "AppPerfTrace.AsyncTTFL$name",
            COOKIE_TTFL
        )
        beginAsyncSystraceSection(
            "AppPerfTrace.AsyncTTIL$name",
            COOKIE_TTIL
        )
    }

    override fun stopRecord(performanceTraceData: PerformanceTraceData) {
        if (performanceTraceData.ttilMeasured()) {
            stopRecordFirebase()
            recordPerfData(performanceTraceData)
            putFullyDrawnTrace(name)
            endAsyncSystraceSection(
                "AppPerfTrace.AsyncTTIL$name",
                COOKIE_TTIL
            )
        } else if (performanceTraceData.ttflMeasured()) {
            endAsyncSystraceSection(
                "AppPerfTrace.AsyncTTFL$name",
                COOKIE_TTFL
            )
        }
    }

    override fun recordPerfData(performanceTraceData: PerformanceTraceData) {
        trackIris(performanceTraceData)
    }

    override fun getTraceName(): String {
        return name
    }

    private fun trackIris(performanceTraceData: PerformanceTraceData) {
        val ctx = appContext ?: return
        val ttfl = performanceTraceData.timeToFirstLayout
        val ttil = performanceTraceData.timeToInitialLoad
        if (ttfl > 0 && ttil > 0) {
            IrisAnalytics.getInstance(ctx)
                .trackPerformance(IrisPerformanceData(name, ttfl, ttil))
        }
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
