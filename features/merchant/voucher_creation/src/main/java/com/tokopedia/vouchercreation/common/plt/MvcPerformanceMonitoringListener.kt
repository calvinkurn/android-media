package com.tokopedia.vouchercreation.common.plt

interface MvcPerformanceMonitoringListener {

    fun startNetworkPerformanceMonitoring()
    fun startRenderPerformanceMonitoring()
    fun finishMonitoring()

}