package com.tokopedia.digital_product_detail.presentation.monitoring

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import javax.inject.Inject

@DigitalPDPScope
class DigitalPDPDataPlanPerformanceCallback @Inject constructor(): PageLoadTimePerformanceCallback(
    DIGITAL_PDP_DATA_PLAN_TRACE_PREPARE_PAGE,
    DIGITAL_PDP_DATA_PLAN_TRACE_REQUEST_NETWORK,
    DIGITAL_PDP_DATA_PLAN_TRACE_RENDER_PAGE
) {

    fun startPerformanceMonitoring() {
        startMonitoring(DIGITAL_PDP_DATA_PLAN_TRACE_PAGE)
    }

    companion object {

        private const val DIGITAL_PDP_DATA_PLAN_TRACE_PAGE = "plt_pdp_data_plan_page"

        private const val DIGITAL_PDP_DATA_PLAN_TRACE_PREPARE_PAGE = "plt_pdp_data_plan_prepare_page"

        private const val DIGITAL_PDP_DATA_PLAN_TRACE_REQUEST_NETWORK = "plt_pdp_data_plan_request_network"

        private const val DIGITAL_PDP_DATA_PLAN_TRACE_RENDER_PAGE = "plt_pdp_data_plan_render_page"
    }
}
