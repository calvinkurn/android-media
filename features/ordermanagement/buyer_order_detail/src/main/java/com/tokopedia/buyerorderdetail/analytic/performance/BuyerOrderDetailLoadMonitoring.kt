package com.tokopedia.buyerorderdetail.analytic.performance

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback

class BuyerOrderDetailLoadMonitoring: LoadTimeMonitoring() {

    companion object {
        const val BUYER_ORDER_DETAIL_LAYOUT_PLT_TRACE = "buyer_order_detail_layout_trace"
        const val BUYER_ORDER_DETAIL_PLT_PREPARE_METRICS = "buyer_order_detail_layout_plt_prepare_metrics"
        const val BUYER_ORDER_DETAIL_PLT_NETWORK_METRICS = "buyer_order_detail_layout_plt_network_metrics"
        const val BUYER_ORDER_DETAIL_PLT_RENDER_METRICS = "buyer_order_detail_layout_plt_render_metrics"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                BUYER_ORDER_DETAIL_PLT_PREPARE_METRICS,
                BUYER_ORDER_DETAIL_PLT_NETWORK_METRICS,
                BUYER_ORDER_DETAIL_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(BUYER_ORDER_DETAIL_LAYOUT_PLT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }
}