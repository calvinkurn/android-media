package com.tokopedia.shop.pageheader.presentation.listener

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

interface ShopPageHeaderPerformanceMonitoringListener {
    fun getShopPageLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface?
    fun stopMonitoringPltPreparePage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun startMonitoringPltNetworkRequest(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun startMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun stopMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun invalidateMonitoringPlt(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun startCustomMetric(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface, tag: String)
    fun stopCustomMetric(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface, tag: String)
    fun startTraceMonitoring(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface, traceName: String)
    fun stopTraceMonitoring(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
}
