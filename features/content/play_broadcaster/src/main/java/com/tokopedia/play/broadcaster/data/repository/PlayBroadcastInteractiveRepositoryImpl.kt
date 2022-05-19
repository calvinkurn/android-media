package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastInteractiveRepository
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveConfigUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.PostInteractiveCreateSessionUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 09/12/21.
 */
class PlayBroadcastInteractiveRepositoryImpl @Inject constructor(
    private val getInteractiveConfigUseCase: GetInteractiveConfigUseCase,
    private val getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase,
    private val getInteractiveLeaderboardUseCase: GetInteractiveLeaderboardUseCase,
    private val createInteractiveSessionUseCase: PostInteractiveCreateSessionUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: PlayBroadcastMapper,
    private val interactiveMapper: PlayChannelInteractiveMapper,
    private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayBroadcastInteractiveRepository {

    override suspend fun getInteractiveConfig(): InteractiveConfigUiModel = withContext(dispatchers.io) {
        val response = getInteractiveConfigUseCase.apply {
            setRequestParams(GetInteractiveConfigUseCase.createParams(userSession.shopId))
        }.executeOnBackground()

        return@withContext mapper.mapInteractiveConfig(response)
    }

    override suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel = withContext(dispatchers.io) {
        val response = getCurrentInteractiveUseCase.apply {
            setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
        }.executeOnBackground()

        return@withContext interactiveMapper.mapInteractive(response.data.interactive)
    }

    override suspend fun getInteractiveLeaderboard(
        channelId: String,
        isChatAllowed: () -> Boolean
    ): PlayLeaderboardInfoUiModel {
        return interactiveLeaderboardMapper.mapLeaderboard(
            getInteractiveLeaderboardUseCase.execute(channelId),
            isChatAllowed
        )
    }

    override suspend fun createInteractiveSession(
        channelId: String,
        title: String,
        durationInMs: Long
    ): InteractiveSessionUiModel = withContext(dispatchers.io) {
        val response = createInteractiveSessionUseCase.execute(
            userSession.shopId,
            channelId,
            title,
            durationInMs
        )
        return@withContext mapper.mapInteractiveSession(response, title, durationInMs)
    }
}