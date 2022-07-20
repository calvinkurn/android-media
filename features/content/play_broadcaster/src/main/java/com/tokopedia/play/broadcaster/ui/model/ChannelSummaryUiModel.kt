package com.tokopedia.play.broadcaster.ui.model

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
data class ChannelSummaryUiModel(
    val title: String,
    val coverUrl: String,
    val date: String,
    val duration: String,
    val isEligiblePostVideo: Boolean,
) {
    companion object {
        fun empty() = ChannelSummaryUiModel("", "","", "", false)
    }
}