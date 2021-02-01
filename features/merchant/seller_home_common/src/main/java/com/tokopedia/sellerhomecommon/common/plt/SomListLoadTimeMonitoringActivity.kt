package com.tokopedia.sellerhomecommon.common.plt

interface SomListLoadTimeMonitoringActivity: LoadTimeMonitoringActivity {
    var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring?

    fun initSomListLoadTimeMonitoring()
    fun getSomListLoadTimeMonitoring(): SomListLoadTimeMonitoring?
}