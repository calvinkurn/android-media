package com.tokopedia.sellerhome.analytic.performance

interface SellerHomeLoadTimeMonitoringListener {
    fun stopPerformanceMonitoringSellerHomeLayout()
    fun getPerformanceMonitoringSellerHomeLayoutPlt(): HomeLayoutLoadTimeMonitoring?
}