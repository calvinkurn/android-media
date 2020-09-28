package com.tokopedia.review.common.analytics

interface ReviewPerformanceMonitoringListener {
    fun startPerformanceMonitoring()
    fun stopPerformanceMonitoring()
    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()
}