package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerChannelRepositoryImpl @Inject constructor(
    private val channelStorage: PlayChannelStateStorage,
    private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
    private val getChannelStatusUseCase: GetChannelStatusUseCase,
    private val mapper: PlayChannelDetailsWithRecomMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayViewerChannelRepository {

    override fun getChannelData(
        channelId: String
    ): PlayChannelData? {
        return channelStorage.getData(channelId)
    }

    override fun setChannelData(data: PlayChannelData) {
        channelStorage.setData(data.id, data)
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
            waitingDuration = response.playGetChannelsStatus.waitingDuration
        )
    }

    override suspend fun getChannelList(
        key: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey,
        extraParams: PlayChannelDetailsWithRecomMapper.ExtraParams,
    ): PlayViewerChannelRepository.ChannelListResponse = withContext(dispatchers.io) {
        val response = getChannelDetailsWithRecomUseCase.apply {
            setRequestParams(
                GetChannelDetailsWithRecomUseCase.createParams(key)
            )
        }.executeOnBackground()

        return@withContext PlayViewerChannelRepository.ChannelListResponse(
            mapper.map(response, extraParams),
            response.channelDetails.meta.cursor,
        )
    }
}
