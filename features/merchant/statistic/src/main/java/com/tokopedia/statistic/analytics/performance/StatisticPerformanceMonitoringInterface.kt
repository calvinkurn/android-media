package com.tokopedia.statistic.analytics.performance

import com.tokopedia.analytics.performance.util.PltPerformanceData

/**
 * Created By @ilhamsuaib on 27/08/20
 */

interface StatisticPerformanceMonitoringInterface {

    fun initPerformanceMonitoring()
    fun startNetworkPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopPerformanceMonitoring()

    fun getPltResult(): PltPerformanceData?
}