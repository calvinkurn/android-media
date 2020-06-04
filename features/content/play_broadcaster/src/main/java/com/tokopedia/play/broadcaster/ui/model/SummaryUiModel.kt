package com.tokopedia.play.broadcaster.ui.model

/**
 * @author by jessica on 26/05/20
 */

data class SummaryUiModel(
        val coverImage: String = "",
        val tickerContent: TickerContent = TickerContent(),
        val liveTitle: String = "",
        val liveDuration: String = "",
        val liveTrafficMetrics: List<LiveTrafficMetric> = listOf(),
        val finishRedirectUrl: String = ""
) {
    data class TickerContent(
            val tickerTitle: String = "",
            val tickerDescription: String = "",
            val showTicker: Boolean = false
    )
    data class LiveTrafficMetric(
            val liveTrafficMetricIcon: String = "",
            val liveTrafficMetricDescription: String = "",
            val liveTrafficMetricCount: String = "",
            val liveTrafficMetricEnum: LiveTrafficMetricsEnum? = null
    )
}