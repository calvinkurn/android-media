package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 01/10/21
 */
class PlayBroadcastChannelRepositoryImpl @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: PlayBroadcastMapper,
    private val dispatchers: CoroutineDispatchers,
): PlayBroadcastChannelRepository {

    override suspend fun getChannelConfiguration(): ConfigurationUiModel = withContext(dispatchers.io) {
        val response = getConfigurationUseCase.apply {
            params = GetConfigurationUseCase.createParams(userSession.shopId)
        }.executeOnBackground()

        return@withContext mapper.mapConfiguration(response)
    }

    override suspend fun createChannel(): String = withContext(dispatchers.io) {
        val response = createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                authorId = userSession.shopId
            )
        }.executeOnBackground()
        return@withContext response.id
    }

    override suspend fun updateChannelStatus(channelId: String, status: PlayChannelStatusType): String = withContext(dispatchers.io) {
        val response = updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = channelId,
                    authorId = userSession.shopId,
                    status = status
                )
            )
        }.executeOnBackground()
        return@withContext response.id
    }
}