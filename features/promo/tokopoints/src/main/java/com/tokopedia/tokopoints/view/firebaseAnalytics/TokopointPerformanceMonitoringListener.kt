package com.tokopedia.tokopoints.view.firebaseAnalytics

interface TokopointPerformanceMonitoringListener {

    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()

}