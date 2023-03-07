package com.tokopedia.play.domain.repository

import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayChatHistoryUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus

interface PlayViewerChannelRepository {

    fun getChannelData(channelId: String): PlayChannelData?

    suspend fun getChannelStatus(channelId: String): PlayChannelStatus

    suspend fun getChannels(
        key: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey,
        extraParams: PlayChannelDetailsWithRecomMapper.ExtraParams,
    ): PagingChannel

    suspend fun getChatHistory(
        channelId: String,
        cursor: String = "",
    ): PlayChatHistoryUiModel
}
