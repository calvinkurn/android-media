package com.tokopedia.talk.common.analytics

interface TalkPerformanceMonitoringListener {
    fun startPerformanceMonitoring()
    fun stopPerformanceMonitoring()
    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()
    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()
}