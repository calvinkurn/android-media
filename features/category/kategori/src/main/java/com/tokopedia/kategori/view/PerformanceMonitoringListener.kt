package com.tokopedia.kategori.view

interface PerformanceMonitoringListener {
    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun startPreparePagePerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()
}
