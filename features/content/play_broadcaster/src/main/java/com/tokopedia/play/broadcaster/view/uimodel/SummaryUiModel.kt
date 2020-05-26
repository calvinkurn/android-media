package com.tokopedia.play.broadcaster.view.uimodel

/**
 * @author by jessica on 26/05/20
 */

data class SummaryUiModel(
        val coverImage: String = "",
        val liveTitle: String = "",
        val liveDuration: String = "",
        val liveInfos: List<LiveInfo> = listOf(),
        val finishRedirectUrl: String = ""
) {
    data class LiveInfo(
            val liveInfoIcon: String = "",
            val liveInfoDescription: String = "",
            val liveInfoCount: String = ""
    )
}