package com.tokopedia.digital.home.presentation.monitoring.callback

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback


class RechargeHomepagePerformanceCallback: PageLoadTimePerformanceCallback(
    RECHARGE_HOMEPAGE_TRACE_PREPARE_PAGE,
    RECHARGE_HOMEPAGE_TRACE_REQUEST_NETWORK,
    RECHARGE_HOMEPAGE_TRACE_RENDER_PAGE
) {

    fun startPerformanceMonitoring() {
        startMonitoring(RECHARGE_HOMEPAGE_TRACE_PAGE)
    }

    companion object {

        private const val RECHARGE_HOMEPAGE_TRACE_PAGE = "plt_recharge_home_page"

        private const val RECHARGE_HOMEPAGE_TRACE_PREPARE_PAGE = "plt_recharge_home_prepare_page"

        private const val RECHARGE_HOMEPAGE_TRACE_REQUEST_NETWORK = "plt_recharge_home_request_network"

        private const val RECHARGE_HOMEPAGE_TRACE_RENDER_PAGE = "plt_recharge_home_render_page"
    }
}
