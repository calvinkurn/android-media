package com.tokopedia.search.result.product.performancemonitoring

import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface

internal const val SEARCH_RESULT_TRACE = "search_result_trace"
internal const val SEARCH_RESULT_PLT_PREPARE_METRICS = "search_result_plt_prepare_metrics"
internal const val SEARCH_RESULT_PLT_NETWORK_METRICS = "search_result_plt_network_metrics"
internal const val SEARCH_RESULT_PLT_RENDER_METRICS = "search_result_plt_render_metrics"
internal const val SEARCH_RESULT_PLT_RENDER_RECYCLER_VIEW = "search_result_plt_render_recycler_view"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC = "search_result_plt_render_logic"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_MAP_PRODUCT_DATA_VIEW =
    "search_result_plt_render_logic_map_product_data_view"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_SHOW_PRODUCT_LIST =
    "search_result_plt_render_logic_show_product_list"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_PROCESS_FILTER =
    "search_result_plt_render_logic_process_filter"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS =
    "search_result_plt_render_logic_headline_ads"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL =
    "search_result_plt_render_logic_inspiration_carousel"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET =
    "search_result_plt_render_logic_inspiration_widget"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH =
    "search_result_plt_render_logic_broadmatch"
internal const val SEARCH_RESULT_PLT_RENDER_LOGIC_TDN =
    "search_result_plt_render_logic_tdn"

internal const val SEARCH_RESULT_PLT_NETWORK_USE_CASE_ATTRIBUTION = "search_result_plt_network_use_case_attribution"
internal const val SEARCH_RESULT_PLT_NETWORK_USE_CASE_NORMAL = "normal"
internal const val SEARCH_RESULT_PLT_NETWORK_USE_CASE_TYPO_CORRECTED = "typo_corrected"

internal fun searchProductPerformanceMonitoring(): PageLoadTimePerformanceInterface =
    PageLoadTimePerformanceCallback(
        SEARCH_RESULT_PLT_PREPARE_METRICS,
        SEARCH_RESULT_PLT_NETWORK_METRICS,
        SEARCH_RESULT_PLT_RENDER_METRICS,
        0,
        0,
        0,
        0,
        null
    )


internal fun stopPerformanceMonitoring(
    performanceMonitoring: PageLoadTimePerformanceInterface?,
    recyclerView: RecyclerView?,
) {
    performanceMonitoring ?: return
    recyclerView ?: return

    performanceMonitoring.startCustomMetric(SEARCH_RESULT_PLT_RENDER_RECYCLER_VIEW)

    recyclerView.viewTreeObserver?.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                performanceMonitoring.run {
                    stopCustomMetric(SEARCH_RESULT_PLT_RENDER_RECYCLER_VIEW)
                    stopRenderPerformanceMonitoring()
                    stopMonitoring()
                }

                recyclerView.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        }
    )
}

internal fun <T> runCustomMetric(
    performanceMonitoring: PageLoadTimePerformanceInterface?,
    tag: String,
    action: () -> T,
): T {
    performanceMonitoring?.startCustomMetric(tag)
    val returnValue = action()
    performanceMonitoring?.stopCustomMetric(tag)

    return returnValue
}

fun interface PerformanceMonitoringProvider {
    fun get(): PageLoadTimePerformanceInterface?
}
