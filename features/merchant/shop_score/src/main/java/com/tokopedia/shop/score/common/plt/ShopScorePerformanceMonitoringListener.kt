package com.tokopedia.shop.score.common.plt

interface ShopScorePerformanceMonitoringListener {
    fun startPerformanceMonitoring()
    fun stopPerformanceMonitoring()
    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()
}