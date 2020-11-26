package com.tokopedia.vouchercreation.common.plt

interface MvcPerformanceMonitoringInterface {

    fun initMvcPerformanceMonitoring()
    fun startNetworkMvcPerformanceMonitoring()
    fun startRenderMvcPerformanceMonitoring()
    fun stopPerformanceMonitoring()

}