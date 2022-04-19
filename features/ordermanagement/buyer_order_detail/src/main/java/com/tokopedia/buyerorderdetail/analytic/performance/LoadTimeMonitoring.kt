package com.tokopedia.buyerorderdetail.analytic.performance

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData

abstract class LoadTimeMonitoring {
    protected var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    abstract fun initPerformanceMonitoring()

    fun startNetworkPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
    }

    fun startCustomMetric(tag: String) {
        pageLoadTimePerformanceMonitoring?.startCustomMetric(tag)
    }

    fun stopCustomMetric(tag: String) {
        pageLoadTimePerformanceMonitoring?.stopCustomMetric(tag)
    }

    fun getPltPerformanceMonitoring(): PltPerformanceData? = pageLoadTimePerformanceMonitoring?.getPltPerformanceData()
}