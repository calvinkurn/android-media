package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
data class ChannelSummaryUiModel(
    val title: String,
    val coverUrl: String,
    val date: String,
    val duration: String,
    val isEligiblePostVideo: Boolean,
    val author: ContentAccountUiModel,
) {
    companion object {
        fun empty() = ChannelSummaryUiModel("", "","", "", false, ContentAccountUiModel.Empty)
    }
}
