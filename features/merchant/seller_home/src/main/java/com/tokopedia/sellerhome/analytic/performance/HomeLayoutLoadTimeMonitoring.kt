package com.tokopedia.sellerhome.analytic.performance

import com.tokopedia.seller.active.common.plt.LoadTimeMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback

class HomeLayoutLoadTimeMonitoring: LoadTimeMonitoring() {

    companion object {
        private const val SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE = "dataSource"
        private const val SELLER_HOME_LAYOUT_PLT_CACHE_VALUE = "Cache"
        private const val SELLER_HOME_LAYOUT_PLT_NETWORK_VALUE = "Network"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_PREPARE_METRICS,
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_NETWORK_METRICS,
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    fun addDataSourceAttribution(fromCache: Boolean) {
        if (fromCache) {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE,
                SELLER_HOME_LAYOUT_PLT_CACHE_VALUE
            )
        } else {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE,
                SELLER_HOME_LAYOUT_PLT_NETWORK_VALUE
            )
        }
    }
}