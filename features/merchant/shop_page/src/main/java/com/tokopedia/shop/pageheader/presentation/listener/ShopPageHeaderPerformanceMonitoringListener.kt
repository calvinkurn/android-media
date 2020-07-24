package com.tokopedia.shop.pageheader.presentation.listener;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

interface ShopPageHeaderPerformanceMonitoringListener : ShopPagePerformanceMonitoringListener {
    fun initShopPageHeaderPerformanceMonitoring()
    fun getShopPageHeaderLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface?
}
