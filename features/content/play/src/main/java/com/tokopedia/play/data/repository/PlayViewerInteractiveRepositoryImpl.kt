package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.interactive.AnswerQuizUseCase
import com.tokopedia.play.domain.interactive.PostInteractiveTapUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetViewerLeaderboardUseCase
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PlayViewerInteractiveRepositoryImpl @Inject constructor(
    private val getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase,
    private val postInteractiveTapUseCase: PostInteractiveTapUseCase,
    private val getInteractiveViewerLeaderboardUseCase: GetViewerLeaderboardUseCase,
    private val answerQuizUseCase: AnswerQuizUseCase,
    private val mapper: PlayUiModelMapper,
    private val dispatchers: CoroutineDispatchers,
    private val interactiveStorage: PlayInteractiveStorage,
) : PlayViewerInteractiveRepository, PlayInteractiveStorage by interactiveStorage {

    override suspend fun getCurrentInteractive(channelId: String): GameUiModel = withContext(dispatchers.io) {
        val response = getCurrentInteractiveUseCase.apply {
            setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
        }.executeOnBackground()
        return@withContext mapper.mapInteractive(response.data)
    }

    override suspend fun postGiveawayTap(channelId: String, interactiveId: String): Boolean = withContext(dispatchers.io) {
        return@withContext try {
            postInteractiveTapUseCase.apply {
                setRequestParams(PostInteractiveTapUseCase.createParams(channelId, interactiveId))
            }.executeOnBackground()
            true
        } catch (e: MessageErrorException) { false }
    }

    override suspend fun getInteractiveLeaderboard(channelId: String): List<LeaderboardGameUiModel> = withContext(dispatchers.io) {
        val response = getInteractiveViewerLeaderboardUseCase.apply {
            setRequestParams(getInteractiveViewerLeaderboardUseCase.createParams(channelId))
        }.executeOnBackground()
        return@withContext mapper.mapInteractiveLeaderboard(response)
    }

    override suspend fun answerQuiz(interactiveId: String, choiceId: String): String = withContext(dispatchers.io) {
        return@withContext answerQuizUseCase.apply {
                setRequestParams(answerQuizUseCase.createParam(interactiveId, choiceId))
        }.executeOnBackground().data.correctAnswerID
    }
}
