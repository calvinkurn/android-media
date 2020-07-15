package com.tokopedia.search.result.presentation.view.listener

interface SearchPerformanceMonitoringListener {

    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun startPreparePagePerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()
}