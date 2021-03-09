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
    fun getNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.requestNetworkDuration.orZero()
    fun getRenderDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.renderDuration.orZero()
    fun getCardWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE).orZero()
    fun getLineGraphWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE).orZero()
    fun getProgressWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE).orZero()
    fun getPostListWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE).orZero()
    fun getCarouselWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE).orZero()
    fun getTableWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE).orZero()
    fun getPieChartWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE).orZero()
    fun getBarChartWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE).orZero()
    fun getMultiLineGraphWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE).orZero()
    fun getAnnouncementWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE).orZero()
    fun getTickerWidgetNetworkDuration(): Long = (pageLoadTimePerformanceMonitoring as? PageLoadTimePerformanceCallback)?.customMetric?.get(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE).orZero()
}