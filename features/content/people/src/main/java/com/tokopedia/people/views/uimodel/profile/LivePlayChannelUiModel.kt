package com.tokopedia.people.views.uimodel.profile

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class LivePlayChannelUiModel(
    val isLive: Boolean,
    val channelId: String,
    val channelLink: LinkUiModel,
) {
    companion object {
        val Empty: LivePlayChannelUiModel
            get() = LivePlayChannelUiModel(
                isLive = false,
                channelId = "",
                channelLink = LinkUiModel.Empty,
            )
    }
}
