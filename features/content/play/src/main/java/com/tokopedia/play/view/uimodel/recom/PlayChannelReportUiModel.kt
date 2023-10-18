package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 22/07/21
 */
data class PlayChannelReportUiModel(
        val totalViewFmt: String = "0",
        val totalLike: Long = 0L,
        val totalLikeFmt: String = "0",
        val shouldTrack: Boolean = true,
        val sourceType: String = "",
        val performanceSummaryPageLink: String = "",
)
