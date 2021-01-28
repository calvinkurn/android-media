package com.tokopedia.sellerhomenavigationcommon.plt

interface SomListLoadTimeMonitoringActivity: LoadTimeMonitoringActivity {
    var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring?

    fun initSomListLoadTimeMonitoring()
    fun getSomListLoadTimeMonitoring(): SomListLoadTimeMonitoring?
}