package com.tokopedia.sellerhome.analytic.performance

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

abstract class SellerHomeLoadTimeMonitoring {

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
}