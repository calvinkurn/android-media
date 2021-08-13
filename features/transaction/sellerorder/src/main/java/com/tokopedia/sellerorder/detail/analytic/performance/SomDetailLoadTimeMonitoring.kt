package com.tokopedia.sellerorder.detail.analytic.performance

import com.tokopedia.seller.active.common.plt.LoadTimeMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback

class SomDetailLoadTimeMonitoring : LoadTimeMonitoring() {
    companion object {
        private const val SOM_DETAIL_PLT_TRACE = "som_detail_trace"
        private const val SOM_DETAIL_PLT_PREPARE_METRICS = "som_detail_plt_prepare_metrics"
        private const val SOM_DETAIL_PLT_NETWORK_METRICS = "som_detail_plt_network_metrics"
        private const val SOM_DETAIL_PLT_RENDER_METRICS = "som_detail_plt_render_metrics"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SOM_DETAIL_PLT_PREPARE_METRICS,
                SOM_DETAIL_PLT_NETWORK_METRICS,
                SOM_DETAIL_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SOM_DETAIL_PLT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }
}