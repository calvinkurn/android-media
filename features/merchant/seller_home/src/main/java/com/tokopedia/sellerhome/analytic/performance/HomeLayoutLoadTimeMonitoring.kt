package com.tokopedia.sellerhome.analytic.performance

import com.tokopedia.seller.active.common.plt.LoadTimeMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.kotlin.extensions.view.orZero

class HomeLayoutLoadTimeMonitoring: LoadTimeMonitoring() {

    companion object {
        private const val SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE = "dataSource"
        private const val SELLER_HOME_LAYOUT_PLT_CACHE_VALUE = "Cache"
        private const val SELLER_HOME_LAYOUT_PLT_NETWORK_VALUE = "Network"
    }

    override fun initPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_PREPARE_METRICS,
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_NETWORK_METRICS,
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_RENDER_METRICS
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_PLT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    private fun getCustomMetricsDurationIfCompleted(key: String): Long {
        return (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.let { pltMonitoring ->
            pltMonitoring.customMetric[key].takeIf {
                pltMonitoring.isCustomMetricDone[key] == true
            }
        }.orZero()
    }

    fun addDataSourceAttribution(fromCache: Boolean) {
        if (fromCache) {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE,
                SELLER_HOME_LAYOUT_PLT_CACHE_VALUE
            )
        } else {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                SELLER_HOME_LAYOUT_PLT_DATA_SOURCE_ATTRIBUTE,
                SELLER_HOME_LAYOUT_PLT_NETWORK_VALUE
            )
        }
    }

    fun getOverallDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.overallDuration.orZero()
    fun getPrepareDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.preparePageDuration.orZero()
    fun getNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.requestNetworkDuration.orZero()
    fun getRenderDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.renderDuration.orZero()
    fun getCardWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE)
    fun getLineGraphWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE)
    fun getProgressWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE)
    fun getPostListWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE)
    fun getCarouselWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE)
    fun getTableWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE)
    fun getPieChartWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE)
    fun getBarChartWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE)
    fun getMultiLineGraphWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE)
    fun getAnnouncementWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE)
    fun getTickerWidgetNetworkDuration(): Long = getCustomMetricsDurationIfCompleted(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE)
}