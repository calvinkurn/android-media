package com.tokopedia.analytics.performance.util

interface PageLoadTimePerformanceInterface {
    fun addAttribution(attribution: String, value: String);

    fun startMonitoring(traceName: String = "")

    fun startPreparePagePerformanceMonitoring()
    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()
    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()
    fun stopRenderPerformanceMonitoring()

    fun startCustomMetric(tag: String)
    fun stopCustomMetric(tag: String)

    fun invalidate()

    fun getPltPerformanceData(): PltPerformanceData

    fun getAttribution(): HashMap<String, String>
    fun stopMonitoring(onStop: ((overallDuration: Long) -> Unit)? = null)
}
