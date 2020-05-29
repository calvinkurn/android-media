package com.tokopedia.play.broadcaster.view.uimodel


/**
 * Created by mzennis on 24/05/20.
 */
data class ChannelInfoUiModel(
        val channelId: String,
        val shareUrl: String,
        val ingestUrl: String,
        val status: PlayChannelStatus
)