package com.tokopedia.tokopedianow.home.analytic

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

class HomePageLoadTimeMonitoring {

    companion object {
        private const val TOKONOW_HOME_PLT_TRACE = "mp_tokonow_home"
        private const val TOKONOW_HOME_PLT_PREPARE_METRICS = "tokonow_home_plt_prepare_metrics"
        private const val TOKONOW_HOME_PLT_NETWORK_METRICS = "tokonow_home_plt_network_metrics"
        private const val TOKONOW_HOME_PLT_RENDER_METRICS = "tokonow_home_plt_render_metrics"
    }

    private var pltPerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    fun initPerformanceMonitoring() {
        pltPerformanceMonitoring = PageLoadTimePerformanceCallback(
            TOKONOW_HOME_PLT_PREPARE_METRICS,
            TOKONOW_HOME_PLT_NETWORK_METRICS,
            TOKONOW_HOME_PLT_RENDER_METRICS
        )

        pltPerformanceMonitoring?.startMonitoring(TOKONOW_HOME_PLT_TRACE)
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
