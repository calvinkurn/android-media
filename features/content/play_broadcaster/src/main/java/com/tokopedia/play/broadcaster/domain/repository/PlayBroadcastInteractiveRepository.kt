package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel

/**
 * Created by meyta.taliti on 09/12/21.
 */
interface PlayBroadcastInteractiveRepository {

    suspend fun getInteractiveConfig(authorId: String, authorType: String): InteractiveConfigUiModel

    suspend fun getCurrentInteractive(channelId: String): InteractiveUiModel

    suspend fun getInteractiveLeaderboard(channelId: String, isChatAllowed: () -> Boolean): PlayLeaderboardInfoUiModel

    suspend fun createGiveaway(channelId: String,
                               title: String,
                               durationInMs: Long): InteractiveSessionUiModel

    suspend fun createInteractiveQuiz(
        channelId: String,
        question: String,
        prize: String,
        runningTime: Long,
        choices: List<PostInteractiveCreateQuizUseCase.Choice>
    )

    suspend fun getInteractiveQuizDetail(interactiveId: String): QuizDetailDataUiModel

    suspend fun getInteractiveQuizChoiceDetail(
        choiceIndex: Int,
        choiceId: String,
        cursor: String,
        interactiveId: String,
        interactiveTitle: String,
    ): QuizChoiceDetailUiModel

    suspend fun getSellerLeaderboardWithSlot(channelId: String, allowChat: Boolean): List<PlayLeaderboardUiModel>
}