package com.tokopedia.product.addedit.analytics

interface AddEditProductPerformanceMonitoringListener {

    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()

}