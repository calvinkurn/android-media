package com.tokopedia.shop.pageheader.presentation.listener;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;

interface ShopPageProductTabPerformanceMonitoringListener : ShopPagePerformanceMonitoringListener {
    fun initShopPageProductTabPerformanceMonitoring()
    fun getShopPageProductTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface?
}
