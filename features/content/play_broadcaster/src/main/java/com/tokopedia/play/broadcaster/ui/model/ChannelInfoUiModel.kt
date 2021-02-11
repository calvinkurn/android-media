package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 24/05/20.
 */
data class ChannelInfoUiModel(
        val channelId: String,
        val title: String,
        val description: String,
        val coverUrl: String,
        val ingestUrl: String,
        val status: PlayChannelStatusType
)