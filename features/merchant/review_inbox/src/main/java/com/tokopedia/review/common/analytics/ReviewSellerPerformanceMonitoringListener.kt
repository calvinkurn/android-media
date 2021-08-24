package com.tokopedia.review.common.analytics

interface ReviewSellerPerformanceMonitoringListener {
    fun startPerformanceMonitoring()
    fun stopPerformanceMonitoring()
    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()
}