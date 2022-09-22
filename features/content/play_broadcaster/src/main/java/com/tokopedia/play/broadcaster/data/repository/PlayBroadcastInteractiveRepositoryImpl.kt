package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastInteractiveRepository
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveConfigUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.PostInteractiveCreateSessionUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.GetInteractiveQuizChoiceDetailsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.GetInteractiveQuizDetailsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
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
    private val getSellerLeaderboardUseCase: GetSellerLeaderboardUseCase,
    private val getInteractiveQuizDetailsUseCase: GetInteractiveQuizDetailsUseCase,
    private val getInteractiveQuizChoiceDetailsUseCase: GetInteractiveQuizChoiceDetailsUseCase,
    private val createInteractiveSessionUseCase: PostInteractiveCreateSessionUseCase,
    private val createInteractiveQuizUseCase: PostInteractiveCreateQuizUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: PlayBroadcastMapper,
    private val interactiveMapper: PlayInteractiveMapper,
    private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayBroadcastInteractiveRepository {

    override suspend fun getInteractiveConfig(authorId: String, authorType: String): InteractiveConfigUiModel =
        withContext(dispatchers.io) {
            val response = getInteractiveConfigUseCase.apply {
                //gonna change this with authorId after be team confirms
                setRequestParams(GetInteractiveConfigUseCase.createParams(userSession.shopId))
            }.executeOnBackground()

            return@withContext mapper.mapInteractiveConfig(authorType, response)
        }

    override suspend fun getCurrentInteractive(channelId: String): InteractiveUiModel =
        withContext(dispatchers.io) {
            val response = getCurrentInteractiveUseCase.apply {
                setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
            }.executeOnBackground()
            return@withContext interactiveMapper.mapInteractive(response.data)
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

    override suspend fun createGiveaway(
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

    override suspend fun createInteractiveQuiz(
        channelId: String,
        question: String,
        prize: String,
        runningTime: Long,
        choices: List<PostInteractiveCreateQuizUseCase.Choice>
    ) {
        createInteractiveQuizUseCase.execute(
            channelId = channelId,
            question = question,
            prize = prize,
            runningTime = runningTime,
            choices = choices,
        )
    }

    override suspend fun getInteractiveQuizDetail(
        interactiveId: String
    ): QuizDetailDataUiModel =
        withContext(dispatchers.io) {
            val response = getInteractiveQuizDetailsUseCase.apply {
                setRequestParams(GetInteractiveQuizDetailsUseCase.createParams(interactiveId))
            }.executeOnBackground()

            return@withContext mapper.mapQuizDetail(response, interactiveId)
        }

    override suspend fun getInteractiveQuizChoiceDetail(
        choiceIndex: Int,
        choiceId: String,
        cursor: String,
        interactiveId: String,
        interactiveTitle: String,
    ): QuizChoiceDetailUiModel =
        withContext(dispatchers.io) {
            val response = getInteractiveQuizChoiceDetailsUseCase.apply {
                setRequestParams(
                    GetInteractiveQuizChoiceDetailsUseCase.createParams(
                        choiceId,
                        cursor,
                    )
                )
            }.executeOnBackground()

            return@withContext mapper.mapChoiceDetail(
                response,
                choiceIndex,
                interactiveId,
                interactiveTitle,
            )
        }

    override suspend fun getSellerLeaderboardWithSlot(
        channelId: String, allowChat: Boolean
    ): List<PlayLeaderboardUiModel> = withContext(dispatchers.io) {
        val response = getSellerLeaderboardUseCase.apply {
            setRequestParams(GetSellerLeaderboardUseCase.createParams(channelId))
        }.executeOnBackground()

        return@withContext mapper.mapLeaderBoardWithSlot(response, allowChat)
    }
}