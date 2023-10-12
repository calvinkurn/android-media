package com.tokopedia.tokopedianow.searchcategory.analytics

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_DISABLED
import com.tokopedia.tokopedianow.common.constant.ConstantKey.EXPERIMENT_ENABLED

class SearchCategoryPageLoadTimeMonitoring(
) {
    companion object {
        private const val TOKONOW_SEARCH_DEFAULT_PLT_TRACE = "mp_tokonow_search_default"
        private const val TOKONOW_SEARCH_EXPERIMENT_PLT_TRACE = "mp_tokonow_search_experiment"

        private const val TOKONOW_SEARCH_DEFAULT_PLT_PREPARE_METRICS = "tokonow_search_default_plt_prepare_metrics"
        private const val TOKONOW_SEARCH_DEFAULT_PLT_NETWORK_METRICS = "tokonow_search_default_plt_network_metrics"
        private const val TOKONOW_SEARCH_DEFAULT_PLT_RENDER_METRICS = "tokonow_search_default_plt_render_metrics"

        private const val TOKONOW_SEARCH_EXPERIMENT_PLT_PREPARE_METRICS = "tokonow_search_experiment_plt_prepare_metrics"
        private const val TOKONOW_SEARCH_EXPERIMENT_PLT_NETWORK_METRICS = "tokonow_search_experiment_plt_network_metrics"
        private const val TOKONOW_SEARCH_EXPERIMENT_PLT_RENDER_METRICS = "tokonow_search_experiment_plt_render_metrics"

        private const val TOKONOW_CATEGORY_DEFAULT_PLT_TRACE = "mp_tokonow_category_default"
        private const val TOKONOW_CATEGORY_EXPERIMENT_PLT_TRACE = "mp_tokonow_category_experiment"

        private const val TOKONOW_CATEGORY_DEFAULT_PLT_PREPARE_METRICS = "tokonow_category_default_plt_prepare_metrics"
        private const val TOKONOW_CATEGORY_DEFAULT_PLT_NETWORK_METRICS = "tokonow_category_default_plt_network_metrics"
        private const val TOKONOW_CATEGORY_DEFAULT_PLT_RENDER_METRICS = "tokonow_category_default_plt_render_metrics"

        private const val TOKONOW_CATEGORY_EXPERIMENT_PLT_PREPARE_METRICS = "tokonow_category_experiment_plt_prepare_metrics"
        private const val TOKONOW_CATEGORY_EXPERIMENT_PLT_NETWORK_METRICS = "tokonow_category_experiment_plt_network_metrics"
        private const val TOKONOW_CATEGORY_EXPERIMENT_PLT_RENDER_METRICS = "tokonow_category_experiment_plt_render_metrics"
    }

    private var pltPerformanceMonitoring: PageLoadTimePerformanceInterface? = null

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
        val experiment = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.TOKOPEDIA_NOW_PAGINATION, EXPERIMENT_DISABLED)
        if (experiment == EXPERIMENT_ENABLED) {
            if (isCategoryPage) {
                setPeformanceMonitoring(
                    tagPrepare = TOKONOW_CATEGORY_EXPERIMENT_PLT_PREPARE_METRICS,
                    tagNetwork = TOKONOW_CATEGORY_EXPERIMENT_PLT_NETWORK_METRICS,
                    tagRender = TOKONOW_CATEGORY_EXPERIMENT_PLT_RENDER_METRICS,
                    traceName = TOKONOW_CATEGORY_EXPERIMENT_PLT_TRACE
                )
            } else {
                setPeformanceMonitoring(
                    tagPrepare = TOKONOW_SEARCH_EXPERIMENT_PLT_PREPARE_METRICS,
                    tagNetwork = TOKONOW_SEARCH_EXPERIMENT_PLT_NETWORK_METRICS,
                    tagRender = TOKONOW_SEARCH_EXPERIMENT_PLT_RENDER_METRICS,
                    traceName = TOKONOW_SEARCH_EXPERIMENT_PLT_TRACE
                )
            }
        } else {
            if (isCategoryPage) {
                setPeformanceMonitoring(
                    tagPrepare = TOKONOW_CATEGORY_DEFAULT_PLT_PREPARE_METRICS,
                    tagNetwork = TOKONOW_CATEGORY_DEFAULT_PLT_NETWORK_METRICS,
                    tagRender = TOKONOW_CATEGORY_DEFAULT_PLT_RENDER_METRICS,
                    traceName = TOKONOW_CATEGORY_DEFAULT_PLT_TRACE
                )
            } else {
                setPeformanceMonitoring(
                    tagPrepare = TOKONOW_SEARCH_DEFAULT_PLT_PREPARE_METRICS,
                    tagNetwork = TOKONOW_SEARCH_DEFAULT_PLT_NETWORK_METRICS,
                    tagRender = TOKONOW_SEARCH_DEFAULT_PLT_RENDER_METRICS,
                    traceName = TOKONOW_SEARCH_DEFAULT_PLT_TRACE
                )
            }
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
        pltPerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pltPerformanceMonitoring?.stopMonitoring()
    }

    fun stopPerformanceMonitoring() {
        pltPerformanceMonitoring?.stopMonitoring()
    }
}
