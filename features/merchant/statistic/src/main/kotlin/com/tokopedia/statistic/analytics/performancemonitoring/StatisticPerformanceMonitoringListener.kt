package com.tokopedia.statistic.analytics.performancemonitoring

/**
 * Created By @ilhamsuaib on 27/08/20
 */

interface StatisticPerformanceMonitoringListener {

    fun startNetworkPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun stopPerformanceMonitoring()
}