package com.tokopedia.tokofood.feature.home.analytics

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

class TokoFoodHomePageLoadTimeMonitoring {

    companion object {
        private const val TOKOFOOD_HOME_PLT_TRACE = "mp_tokofood_home"
        private const val TOKOFOOD_HOME_PLT_PREPARE_METRICS = "tokofood_home_plt_prepare_metrics"
        private const val TOKOFOOD_HOME_PLT_NETWORK_METRICS = "tokofood_home_plt_network_metrics"
        private const val TOKOFOOD_HOME_PLT_RENDER_METRICS = "tokofood_home_plt_render_metrics"
    }

    private var pltPerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    fun initPerformanceMonitoring() {
        pltPerformanceMonitoring = PageLoadTimePerformanceCallback(
            TOKOFOOD_HOME_PLT_PREPARE_METRICS,
            TOKOFOOD_HOME_PLT_NETWORK_METRICS,
            TOKOFOOD_HOME_PLT_RENDER_METRICS
        )

        pltPerformanceMonitoring?.startMonitoring(TOKOFOOD_HOME_PLT_TRACE)
        pltPerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    fun startNetworkPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
        pltPerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    fun startRenderPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
        pltPerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    fun stopRenderPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pltPerformanceMonitoring?.stopMonitoring()
    }

    fun stopPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopMonitoring()
    }
}