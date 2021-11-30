package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 01/10/21
 */
class PlayBroadcastChannelRepositoryImpl @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
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
}