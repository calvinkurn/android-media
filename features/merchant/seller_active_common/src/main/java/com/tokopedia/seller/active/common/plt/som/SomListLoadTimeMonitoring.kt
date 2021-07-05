package com.tokopedia.seller.active.common.plt.som

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoring

class SomListLoadTimeMonitoring: LoadTimeMonitoring() {
    companion object {
        private const val SOM_LIST_PLT_TRACE = "som_list_trace"
        private const val SOM_LIST_PLT_PREPARE_METRICS = "som_list_plt_prepare_metrics"
        private const val SOM_LIST_PLT_NETWORK_METRICS = "som_list_plt_network_metrics"
        private const val SOM_LIST_PLT_RENDER_METRICS = "som_list_plt_render_metrics"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SOM_LIST_PLT_PREPARE_METRICS,
                SOM_LIST_PLT_NETWORK_METRICS,
                SOM_LIST_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SOM_LIST_PLT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }
}