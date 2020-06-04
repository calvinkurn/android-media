package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.analytics.performance.util.PltPerformanceData

interface SearchPerformanceMonitoringListener {

    fun startPerformanceMonitoring()

    fun stopPerformanceMonitoring()

    fun startPreparePagePerformanceMonitoring()

    fun stopPreparePagePerformanceMonitoring()

    fun startNetworkRequestPerformanceMonitoring()

    fun stopNetworkRequestPerformanceMonitoring()

    fun startRenderPerformanceMonitoring()

    fun stopRenderPerformanceMonitoring()

    fun getPltPerformanceResultData(): PltPerformanceData?
}