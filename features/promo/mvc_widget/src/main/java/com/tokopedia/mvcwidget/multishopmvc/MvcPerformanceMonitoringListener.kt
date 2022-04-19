package com.tokopedia.mvcwidget.multishopmvc

interface MvcPerformanceMonitoringListener {

    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()

}