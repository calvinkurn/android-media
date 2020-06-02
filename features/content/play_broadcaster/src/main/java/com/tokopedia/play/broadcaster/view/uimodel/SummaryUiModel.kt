package com.tokopedia.play.broadcaster.view.uimodel

/**
 * @author by jessica on 26/05/20
 */

data class SummaryUiModel(
        val coverImage: String = "",
        val tickerContent: TickerContent = TickerContent(),
        val liveTitle: String = "",
        val liveDuration: String = "",
        val liveInfos: List<LiveInfo> = listOf(),
        val finishRedirectUrl: String = ""
) {
    data class TickerContent(
            val tickerTitle: String = "",
            val tickerDescription: String = "",
            val showTicker: Boolean = false
    )
    data class LiveInfo(
            val liveInfoIcon: String = "",
            val liveInfoDescription: String = "",
            val liveInfoCount: String = ""
    )
}