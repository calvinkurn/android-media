package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel

/**
 * Created by jegul on 12/10/21
 */
interface PlayBroadcastPinnedMessageRepository {

    suspend fun getActivePinnedMessage(channelId: String): PinnedMessageUiModel?

    suspend fun setPinnedMessage(
        id: String? = null,
        channelId: String,
        message: String,
    ): PinnedMessageUiModel
}