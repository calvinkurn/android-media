package com.tokopedia.sellerhome.newrelic

import android.content.Context
import android.os.Build
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.pocnewrelic.*
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhomecommon.presentation.model.*

object SellerHomeNewRelic {
    private const val WIDGET_NAME_ANNOUNCEMENT = "Announcement"
    private const val WIDGET_NAME_BAR_CHART = "BarChart"
    private const val WIDGET_NAME_CARD = "Card"
    private const val WIDGET_NAME_CAROUSEL = "Carousel"
    private const val WIDGET_NAME_DESCRIPTION = "Description"
    private const val WIDGET_NAME_LINE_GRAPH = "LineGraph"
    private const val WIDGET_NAME_MULTI_LINE_GRAPH = "MultiLineGraph"
    private const val WIDGET_NAME_PIE_CHART = "PieChart"
    private const val WIDGET_NAME_POST_LIST = "PostList"
    private const val WIDGET_NAME_PROGRESS = "Progress"
    private const val WIDGET_NAME_SECTION = "Section"
    private const val WIDGET_NAME_TABLE = "Table"
    private const val WIDGET_NAME_TICKER = "Ticker"

    fun sendSellerHomeNewRelicData(
            context: Context?,
            screenName: String,
            userId: String,
            widgets: List<BaseWidgetUiModel<BaseDataUiModel>>,
            performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring?) {
        context?.run {
            val data = mutableMapOf<String, Any>(
                    KEY_EVENT_TYPE to ANDROID,
                    KEY_APP_VERSION to GlobalConfig.VERSION_NAME,
                    KEY_BROWSER to "",
                    KEY_DEVICE_ID to GlobalConfig.DEVICE_ID,
                    KEY_DEVICE_NAME to Build.MODEL,
                    KEY_DEVICE_TYPE to ANDROID,
                    KEY_NETWORK_TYPE to DeviceConnectionInfo.getConnectionType(context).orEmpty(),
                    KEY_OS to ANDROID,
                    KEY_OS_VERSION to Build.VERSION.RELEASE,
                    KEY_PAGE_NAME to screenName,
                    KEY_REFERRER to "",
                    KEY_TENANT_ID to ANDROID,
                    KEY_USER_AGENT to ANDROID,
                    KEY_PACKAGE_NAME to GlobalConfig.PACKAGE_SELLER_APP,
                    KEY_USER_ID to userId,
                    KEY_VERSION_CODE to GlobalConfig.VERSION_CODE,
                    KEY_PLT to performanceMonitoringSellerHomePlt?.getOverallDuration().orZero(),
                    PLT_NETWORK to performanceMonitoringSellerHomePlt?.getNetworkDuration().orZero(),
                    PLT_RENDER to performanceMonitoringSellerHomePlt?.getRenderDuration().orZero()
            )
            widgets.getAverageWidgetRenderDuration<AnnouncementWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_ANNOUNCEMENT, performanceMonitoringSellerHomePlt?.getAnnouncementWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<BarChartWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_BAR_CHART, performanceMonitoringSellerHomePlt?.getBarChartWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<CardWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_CARD, performanceMonitoringSellerHomePlt?.getCardWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<CarouselWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_CAROUSEL, performanceMonitoringSellerHomePlt?.getCarouselWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<DescriptionWidgetUiModel>().takeIf { it != 0L }?.let {
                data[String.format(PLT_COMPONENT_RENDER, WIDGET_NAME_DESCRIPTION)] = it
            }
            widgets.getAverageWidgetRenderDuration<LineGraphWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_LINE_GRAPH, performanceMonitoringSellerHomePlt?.getLineGraphWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<MultiLineGraphWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_MULTI_LINE_GRAPH, performanceMonitoringSellerHomePlt?.getMultiLineGraphWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<PieChartWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_PIE_CHART, performanceMonitoringSellerHomePlt?.getPieChartWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<PostListWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_POST_LIST, performanceMonitoringSellerHomePlt?.getPostListWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<ProgressWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_PROGRESS, performanceMonitoringSellerHomePlt?.getProgressWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<SectionWidgetUiModel>().takeIf { it != 0L }?.let {
                data[String.format(PLT_COMPONENT_RENDER, WIDGET_NAME_SECTION)] = it
            }
            widgets.getAverageWidgetRenderDuration<TableWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_TABLE, performanceMonitoringSellerHomePlt?.getTableWidgetNetworkDuration().orZero(), it)
            }
            widgets.getAverageWidgetRenderDuration<TickerWidgetUiModel>().takeIf { it != 0L }?.let {
                data.putWidgetPLTDuration(WIDGET_NAME_TICKER, performanceMonitoringSellerHomePlt?.getTickerWidgetNetworkDuration().orZero(), it)
            }
            NewRelic.getInstance(context).sendData(data)
        }
    }

    private inline fun <reified T : BaseWidgetUiModel<*>> List<BaseWidgetUiModel<BaseDataUiModel>>.getAverageWidgetRenderDuration(): Long {
        return filterIsInstance<T>()
                .filter { it.renderDuration != 0L }
                .map { it.renderDuration.toDouble() }
                .average()
                .toLong()
    }

    private fun MutableMap<String, Any>.putWidgetPLTDuration(widgetName: String, networkDuration: Long, renderDuration: Long) {
        put(String.format(PLT_COMPONENT_NETWORK, widgetName), networkDuration)
        put(String.format(PLT_COMPONENT_RENDER, widgetName), renderDuration)
    }
}