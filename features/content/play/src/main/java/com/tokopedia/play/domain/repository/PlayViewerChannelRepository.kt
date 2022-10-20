package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayChatHistoryUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play_common.model.ui.PlayChatUiModel

interface PlayViewerChannelRepository {

    fun getChannelData(channelId: String): PlayChannelData?

    fun setChannelData(data: PlayChannelData)

    suspend fun getChannelStatus(channelId: String): PlayChannelStatus

    suspend fun getChatHistory(
        channelId: String,
        cursor: String = "",
    ): PlayChatHistoryUiModel
}
