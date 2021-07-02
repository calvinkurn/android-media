package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play.domain.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play.domain.interactive.PostInteractiveTapUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PlayViewerInteractiveRepositoryImpl @Inject constructor(
        private val getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase,
        private val postInteractiveTapUseCase: PostInteractiveTapUseCase,
        private val getInteractiveLeaderboardUseCase: GetInteractiveLeaderboardUseCase,
        private val mapper: PlayUiModelMapper,
        private val dispatchers: CoroutineDispatchers,
        private val interactiveStorage: PlayInteractiveStorage
) : PlayViewerInteractiveRepository, PlayInteractiveStorage by interactiveStorage {

    override suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel = withContext(dispatchers.io) {
        val response = getCurrentInteractiveUseCase.apply {
            setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
        }.executeOnBackground()
        return@withContext mapper.mapInteractive(response.data.interactive)
    }

    override suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean = withContext(dispatchers.io) {
        val response = postInteractiveTapUseCase.apply {
            setRequestParams(PostInteractiveTapUseCase.createParams(channelId, interactiveId))
        }.executeOnBackground()
        return@withContext response.data.header.status == 200
    }

    override suspend fun getInteractiveLeaderboard(channelId: String): PlayLeaderboardInfoUiModel = withContext(dispatchers.io) {
        val response = getInteractiveLeaderboardUseCase.execute(channelId)
        return@withContext mapper.mapInteractiveLeaderboard(response)
    }
}