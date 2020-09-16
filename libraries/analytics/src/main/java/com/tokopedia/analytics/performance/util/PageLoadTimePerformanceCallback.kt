package com.tokopedia.analytics.performance.util

import android.os.Build
import android.os.Debug
import android.os.Trace
import android.util.Log
import androidx.annotation.RequiresApi
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.config.GlobalConfig

open class PageLoadTimePerformanceCallback(
        val tagPrepareDuration: String,
        val tagNetworkRequestDuration: String,
        val tagRenderDuration: String,
        var overallDuration: Long = 0,
        var preparePageDuration: Long = 0,
        var requestNetworkDuration: Long = 0,
        var renderDuration: Long = 0,
        var performanceMonitoring: PerformanceMonitoring? = null
): PageLoadTimePerformanceInterface {
    var isPrepareDone = false
    var isNetworkDone = false
    var isRenderDone = false
    var traceName = ""

    override fun getPltPerformanceData(): PltPerformanceData {
        return PltPerformanceData(
                startPageDuration = preparePageDuration,
                networkRequestDuration = requestNetworkDuration,
                renderPageDuration = renderDuration,
                overallDuration = overallDuration,
                isSuccess = (isNetworkDone && isRenderDone)
        )
    }

    override fun addAttribution(attribution: String, value: String) {
        performanceMonitoring?.putCustomAttribute(attribution, value)
    }

    override fun startMonitoring(traceName: String) {
        this.traceName = traceName
        performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring?.startTrace(traceName)
        if (overallDuration == 0L) overallDuration = System.currentTimeMillis()
        startMethodTracing(traceName);
    }

    override fun stopMonitoring() {
        if (!isNetworkDone) requestNetworkDuration = 0
        if (!isRenderDone) renderDuration = 0
        if (!isNetworkDone) requestNetworkDuration = 0

        performanceMonitoring?.stopTrace()
        overallDuration = System.currentTimeMillis() - overallDuration
        stopMethodTracing(traceName);
    }

    override fun startPreparePagePerformanceMonitoring() {
        if (preparePageDuration == 0L) {
            beginAsyncSystraceSection("PageLoadTime.AsyncPreparePage$traceName",11)
            preparePageDuration = System.currentTimeMillis()
        }
    }

    override fun stopPreparePagePerformanceMonitoring() {
        if (!isPrepareDone && preparePageDuration != 0L) {
            preparePageDuration = System.currentTimeMillis() - preparePageDuration
            performanceMonitoring?.putMetric(tagPrepareDuration, preparePageDuration)
            isPrepareDone = true
            endAsyncSystraceSection("PageLoadTime.AsyncPreparePage$traceName",11)
        }
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        if (requestNetworkDuration == 0L) {
            beginAsyncSystraceSection("PageLoadTime.AsyncNetworkRequest$traceName",22)
            requestNetworkDuration = System.currentTimeMillis()
        }
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        if (!isNetworkDone && requestNetworkDuration != 0L) {
            requestNetworkDuration = System.currentTimeMillis() - requestNetworkDuration
            performanceMonitoring?.putMetric(tagNetworkRequestDuration, requestNetworkDuration)
            isNetworkDone = true
            endAsyncSystraceSection("PageLoadTime.AsyncNetworkRequest$traceName",22)
        }
    }

    override fun startRenderPerformanceMonitoring() {
        if (renderDuration == 0L) {
            beginAsyncSystraceSection("PageLoadTime.AsyncRenderPage$traceName",33)
            renderDuration = System.currentTimeMillis()
        }
    }

    override fun stopRenderPerformanceMonitoring() {
        if (!isRenderDone && renderDuration != 0L) {
            renderDuration = System.currentTimeMillis() - renderDuration
            performanceMonitoring?.putMetric(tagRenderDuration, renderDuration)
            isRenderDone = true
            endAsyncSystraceSection("PageLoadTime.AsyncRenderPage$traceName",33)
        }
    }

    fun beginAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && GlobalConfig.DEBUG) {
            Trace.beginAsyncSection(methodName, cookie)
        }
    }

    fun endAsyncSystraceSection(methodName: String, cookie: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && GlobalConfig.DEBUG) {
            Trace.endAsyncSection(methodName, cookie)
        }
    }

    override fun invalidate() {
        performanceMonitoring = null
        isPrepareDone = true
        isNetworkDone = true
        isRenderDone = true
    }

    private fun beginSystraceSection(sectionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && GlobalConfig.DEBUG) {
            Trace.beginSection(sectionName)
        }
    }

    private fun endSystraceSection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && GlobalConfig.DEBUG) {
            Trace.endSection()
        }
    }

    private fun startMethodTracing(traceName: String){
        if(GlobalConfig.ENABLE_DEBUG_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.i("PageLoadTimePerformanceCallback" , "startMethodTracing ==> "+traceName)
            Debug.startMethodTracingSampling(traceName , 50 * 1024 * 1024 , 500);
        }
    }

    private fun stopMethodTracing(traceName: String){
        if(GlobalConfig.ENABLE_DEBUG_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.i("PageLoadTimePerformanceCallback" , "stopMethodTracing ==> "+traceName)
            Debug.stopMethodTracing();
        }
    }
}
