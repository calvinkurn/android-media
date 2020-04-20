package com.tokopedia.analytics.performance.util

interface PageLoadTimePerformanceInterface {
    fun addAttribution(attribution: String, value: String);

    fun startMonitoring(traceName: String = "")
    fun stopMonitoring()

    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()

    fun getPltPerformanceData(): PltPerformanceData
}