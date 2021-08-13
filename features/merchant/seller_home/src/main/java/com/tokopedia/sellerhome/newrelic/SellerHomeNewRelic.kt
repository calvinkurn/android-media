package com.tokopedia.sellerhome.newrelic

import android.app.Application
import android.content.Context
import android.os.Build
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.pocnewrelic.*
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

class SellerHomeNewRelic @Inject constructor() {
    companion object {
        private const val WIDGET_NAME_ANNOUNCEMENT = "Announcement"
        private const val WIDGET_NAME_BAR_CHART = "BarChart"
        private const val WIDGET_NAME_CARD = "Card"
        private const val WIDGET_NAME_CAROUSEL = "Carousel"
        private const val WIDGET_NAME_LINE_GRAPH = "LineGraph"
        private const val WIDGET_NAME_MULTI_LINE_GRAPH = "MultiLineGraph"
        private const val WIDGET_NAME_PIE_CHART = "PieChart"
        private const val WIDGET_NAME_POST_LIST = "PostList"
        private const val WIDGET_NAME_PROGRESS = "Progress"
        private const val WIDGET_NAME_TABLE = "Table"
        private const val WIDGET_NAME_TICKER = "Ticker"
    }

    private var isNewRelicDataSent: Boolean = false

    fun sendSellerHomeNewRelicData(
            application: Application,
            screenName: String,
            userId: String,
            performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring?) {
        application.applicationContext?.run {
            if (!isNewRelicDataSent) {
                isNewRelicDataSent = true
                val data = mutableMapOf<String, Any>(
                        KEY_EVENT_TYPE to ANDROID,
                        KEY_APP_VERSION to GlobalConfig.VERSION_NAME,
                        KEY_BROWSER to "",
                        KEY_DEVICE_ID to GlobalConfig.DEVICE_ID,
                        KEY_DEVICE_NAME to Build.MODEL,
                        KEY_DEVICE_TYPE to ANDROID,
                        KEY_NETWORK_TYPE to DeviceConnectionInfo.getConnectionType(this).orEmpty(),
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
                        PLT_PREPARE to performanceMonitoringSellerHomePlt?.getPrepareDuration().orZero(),
                        PLT_NETWORK to performanceMonitoringSellerHomePlt?.getNetworkDuration().orZero(),
                        PLT_RENDER to performanceMonitoringSellerHomePlt?.getRenderDuration().orZero()
                )
                data.putWidgetPLTDuration(WIDGET_NAME_ANNOUNCEMENT, performanceMonitoringSellerHomePlt?.getAnnouncementWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_BAR_CHART, performanceMonitoringSellerHomePlt?.getBarChartWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_CARD, performanceMonitoringSellerHomePlt?.getCardWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_CAROUSEL, performanceMonitoringSellerHomePlt?.getCarouselWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_LINE_GRAPH, performanceMonitoringSellerHomePlt?.getLineGraphWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_MULTI_LINE_GRAPH, performanceMonitoringSellerHomePlt?.getMultiLineGraphWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_PIE_CHART, performanceMonitoringSellerHomePlt?.getPieChartWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_POST_LIST, performanceMonitoringSellerHomePlt?.getPostListWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_PROGRESS, performanceMonitoringSellerHomePlt?.getProgressWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_TABLE, performanceMonitoringSellerHomePlt?.getTableWidgetNetworkDuration().orZero())
                data.putWidgetPLTDuration(WIDGET_NAME_TICKER, performanceMonitoringSellerHomePlt?.getTickerWidgetNetworkDuration().orZero())
                NewRelic.getInstance(application).sendData(data)
            }
        }
    }

    private fun MutableMap<String, Any>.putWidgetPLTDuration(widgetName: String, networkDuration: Long) {
        put(String.format(PLT_COMPONENT_NETWORK, widgetName), networkDuration)
    }
}