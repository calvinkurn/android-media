package com.example.sellerhomenavigationcommon.plt

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

    fun getPltPerformanceMonitoring(): PltPerformanceData? = pageLoadTimePerformanceMonitoring?.getPltPerformanceData()
}