package com.tokopedia.analytics.performance.util

import com.tokopedia.analytics.performance.PerformanceMonitoring

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
        performanceMonitoring = PerformanceMonitoring()
        performanceMonitoring?.startTrace(traceName)
        if (overallDuration == 0L) overallDuration = System.currentTimeMillis()
    }

    override fun stopMonitoring() {
        if (!isNetworkDone) requestNetworkDuration = 0
        if (!isRenderDone) renderDuration = 0

        performanceMonitoring?.stopTrace()
        overallDuration = System.currentTimeMillis() - overallDuration
        if (!isNetworkDone) requestNetworkDuration = 0
    }

    override fun startPreparePagePerformanceMonitoring() {
        if (preparePageDuration == 0L) preparePageDuration = System.currentTimeMillis()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        if (!isPrepareDone) {
            preparePageDuration = System.currentTimeMillis() - preparePageDuration
            performanceMonitoring?.putMetric(tagPrepareDuration, preparePageDuration)
            isPrepareDone = true
        }
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        if (requestNetworkDuration == 0L) requestNetworkDuration = System.currentTimeMillis()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        if (!isNetworkDone) {
            requestNetworkDuration = System.currentTimeMillis() - requestNetworkDuration
            performanceMonitoring?.putMetric(tagNetworkRequestDuration, requestNetworkDuration)
            isNetworkDone = true
        }
    }

    override fun startRenderPerformanceMonitoring() {
        if (renderDuration == 0L) renderDuration = System.currentTimeMillis()
    }

    override fun stopRenderPerformanceMonitoring() {
        if (!isRenderDone) {
            renderDuration = System.currentTimeMillis() - renderDuration
            performanceMonitoring?.putMetric(tagRenderDuration, renderDuration)
            isRenderDone = true
        }
    }
}