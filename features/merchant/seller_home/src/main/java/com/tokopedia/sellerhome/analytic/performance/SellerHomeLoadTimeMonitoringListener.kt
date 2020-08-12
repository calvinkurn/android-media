package com.tokopedia.sellerhome.analytic.performance

interface SellerHomeLoadTimeMonitoringListener {
    fun onStartPltMonitoring()
    fun onStopPltMonitoring()
}