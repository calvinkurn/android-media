package com.tokopedia.seller.active.common.plt.som

import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringActivity

interface SomListLoadTimeMonitoringActivity: LoadTimeMonitoringActivity {
    var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring?

    fun initSomListLoadTimeMonitoring()
    fun getSomListLoadTimeMonitoring(): SomListLoadTimeMonitoring?
}