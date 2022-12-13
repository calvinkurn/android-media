package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.GetChatHistoryUseCase
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.uimodel.PlayChatHistoryUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerChannelRepositoryImpl @Inject constructor(
    private val channelStorage: PlayChannelStateStorage,
    private val getChannelStatusUseCase: GetChannelStatusUseCase,
    private val getChannelDetailsUseCase: GetChannelDetailsWithRecomUseCase,
    private val getChatHistory: GetChatHistoryUseCase,
    private val uiMapper: PlayUiModelMapper,
    private val dispatchers: CoroutineDispatchers,
    private val channelMapper: PlayChannelDetailsWithRecomMapper,
) : PlayViewerChannelRepository {

    override fun getChannelData(
        channelId: String
    ): PlayChannelData? {
        return channelStorage.getData(channelId)
    }

    override suspend fun getChannelStatus(channelId: String) = withContext(dispatchers.io) {
        val response = getChannelStatusUseCase.apply {
            setRequestParams(GetChannelStatusUseCase.createParams(arrayOf(channelId)))
        }.executeOnBackground()

        val statusType = PlayStatusType.getByValue(
            response.playGetChannelsStatus.data.firstOrNull()?.status.orEmpty()
        )

        return@withContext PlayChannelStatus(
            statusType = statusType,
            statusSource = PlayStatusSource.Network,
            waitingDuration = response.playGetChannelsStatus.waitingDuration,
        )
    }

    override suspend fun getChannels(
        key: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey,
        extraParams: PlayChannelDetailsWithRecomMapper.ExtraParams
    ): PagingChannel = withContext(dispatchers.io) {
        val response = getChannelDetailsUseCase.apply {
            setRequestParams(GetChannelDetailsWithRecomUseCase.createParams(key))
        }.executeOnBackground()

        return@withContext PagingChannel(
            channelList = channelMapper.map(response, extraParams),
            cursor = response.channelDetails.meta.cursor,
        )
    }

    override suspend fun getChatHistory(
        channelId: String,
        cursor: String,
    ): PlayChatHistoryUiModel = withContext(dispatchers.io) {
        val response = getChatHistory.executeOnBackground(
            channelId = channelId,
            cursor = cursor,
        )

        uiMapper.mapHistoryChat(response)
    }
}
