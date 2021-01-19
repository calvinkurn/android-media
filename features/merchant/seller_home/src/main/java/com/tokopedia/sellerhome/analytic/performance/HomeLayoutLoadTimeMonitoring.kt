package com.tokopedia.sellerhome.analytic.performance

import com.example.sellerhomenavigationcommon.plt.LoadTimeMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PltPerformanceData

class HomeLayoutLoadTimeMonitoring: LoadTimeMonitoring() {

    companion object {
        private const val SELLER_HOME_LAYOUT_PLT_TRACE = "seller_home_layout_trace"
        private const val SELLER_HOME_LAYOUT_PLT_PREPARE_METRICS = "seller_home_layout_plt_prepare_metrics"
        private const val SELLER_HOME_LAYOUT_PLT_NETWORK_METRICS = "seller_home_layout_plt_network_metrics"
        private const val SELLER_HOME_LAYOUT_PLT_RENDER_METRICS = "seller_home_layout_plt_render_metrics"

        private const val SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE = "dataSource"
        private const val SELLER_HOME_LAYOUT_PLT_CACHE_VALUE = "Cache"
        private const val SELLER_HOME_LAYOUT_PLT_NETWORK_VALUE = "Network"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            SELLER_HOME_LAYOUT_PLT_PREPARE_METRICS,
            SELLER_HOME_LAYOUT_PLT_NETWORK_METRICS,
            SELLER_HOME_LAYOUT_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SELLER_HOME_LAYOUT_PLT_TRACE)
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