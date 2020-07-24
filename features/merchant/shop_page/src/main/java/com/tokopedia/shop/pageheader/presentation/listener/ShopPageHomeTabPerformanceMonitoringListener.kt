package com.tokopedia.shop.pageheader.presentation.listener;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

interface ShopPageHomeTabPerformanceMonitoringListener : ShopPagePerformanceMonitoringListener {
    fun initShopPageHomeTabPerformanceMonitoring()
    fun getShopPageHomeTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface?
}
