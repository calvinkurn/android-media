package com.tokopedia.seller.search.common.plt

interface GlobalSearchSellerPerformanceMonitoringListener {
    fun startNetworkPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun finishMonitoring()
}