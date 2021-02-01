package com.tokopedia.sellerhomecommon.common.plt.som

import com.tokopedia.sellerhomecommon.common.plt.LoadTimeMonitoringActivity

interface SomListLoadTimeMonitoringActivity: LoadTimeMonitoringActivity {
    var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring?

    fun initSomListLoadTimeMonitoring()
    fun getSomListLoadTimeMonitoring(): SomListLoadTimeMonitoring?
}