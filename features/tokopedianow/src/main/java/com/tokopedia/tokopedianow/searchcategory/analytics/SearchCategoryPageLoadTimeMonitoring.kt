package com.tokopedia.tokopedianow.searchcategory.analytics

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.tokopedianow.common.constant.ConstantKey.DEFAULT_ROWS
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_DISABLED
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_ENABLED
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_ROWS

class SearchCategoryPageLoadTimeMonitoring(
) {
    companion object {
        private const val TOKONOW_ATTRIBUTION = "itemCount"
        private const val TOKONOW_SEARCH_PLT_TRACE = "mp_tokonow_search"
        private const val TOKONOW_CATEGORY_PLT_TRACE = "mp_tokonow_category"

        private const val TOKONOW_SEARCH_PLT_PREPARE_METRICS = "tokonow_search_plt_prepare_metrics"
        private const val TOKONOW_SEARCH_PLT_NETWORK_METRICS = "tokonow_search_plt_network_metrics"
        private const val TOKONOW_SEARCH_PLT_RENDER_METRICS = "tokonow_search_plt_render_metrics"

        private const val TOKONOW_CATEGORY_PLT_PREPARE_METRICS = "tokonow_category_plt_prepare_metrics"
        private const val TOKONOW_CATEGORY_PLT_NETWORK_METRICS = "tokonow_category_plt_network_metrics"
        private const val TOKONOW_CATEGORY_PLT_RENDER_METRICS = "tokonow_category_plt_render_metrics"
    }

    private var pltPerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    private fun getPaginationExperiment(): Boolean {
        val experiment = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.TOKOPEDIA_NOW_PAGINATION, EXPERIMENT_DISABLED)
        return experiment == EXPERIMENT_ENABLED
    }

    private fun getRows(): String = if (getPaginationExperiment()) EXPERIMENT_ROWS else DEFAULT_ROWS

    private fun setPeformanceMonitoring(
        tagPrepare: String,
        tagNetwork: String,
        tagRender: String,
        traceName: String
    ) {
        pltPerformanceMonitoring = PageLoadTimePerformanceCallback(
            tagPrepare,
            tagNetwork,
            tagRender
        )
        pltPerformanceMonitoring?.startMonitoring(traceName)
    }

    fun initPerformanceMonitoring(
        isCategoryPage: Boolean
    ) {
        if (isCategoryPage) {
            setPeformanceMonitoring(
                tagPrepare = TOKONOW_CATEGORY_PLT_PREPARE_METRICS,
                tagNetwork = TOKONOW_CATEGORY_PLT_NETWORK_METRICS,
                tagRender = TOKONOW_CATEGORY_PLT_RENDER_METRICS,
                traceName = TOKONOW_CATEGORY_PLT_TRACE
            )
        } else {
            setPeformanceMonitoring(
                tagPrepare = TOKONOW_SEARCH_PLT_PREPARE_METRICS,
                tagNetwork = TOKONOW_SEARCH_PLT_NETWORK_METRICS,
                tagRender = TOKONOW_SEARCH_PLT_RENDER_METRICS,
                traceName = TOKONOW_SEARCH_PLT_TRACE
            )
        }
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
        pltPerformanceMonitoring?.addAttribution(TOKONOW_ATTRIBUTION, getRows())
        pltPerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pltPerformanceMonitoring?.stopMonitoring()
    }

    fun stopPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopMonitoring()
    }
}
