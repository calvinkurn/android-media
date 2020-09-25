package com.tokopedia.statistic.analytics.performance

/**
 * Created By @ilhamsuaib on 27/08/20
 */

interface StatisticPerformanceMonitoringListener {

    fun startNetworkPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopPerformanceMonitoring()
}