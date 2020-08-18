package com.tokopedia.shop.pageheader.presentation.listener;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

interface ShopPagePerformanceMonitoringListener {
    fun getShopPageLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface?
    fun startMonitoringPltNetworkRequest(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun startMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
    fun stopMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface)
}
