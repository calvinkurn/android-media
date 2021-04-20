package com.tokopedia.seller.search.common.plt

interface GlobalSearchSellerPerformanceMonitoringInterface {
    fun initGlobalSearchSellerPerformanceMonitoring()
    fun startNetworkGlobalSearchSellerPerformanceMonitoring()
    fun startRenderGlobalSearchSellerPerformanceMonitoring()
    fun stopPerformanceMonitoring()
}