package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz.PostInteractiveCreateQuizUseCase
import com.tokopedia.play.broadcaster.ui.model.interactive.GameConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by meyta.taliti on 09/12/21.
 */
interface PlayBroadcastInteractiveRepository {

    suspend fun getInteractiveConfig(): GameConfigUiModel

    suspend fun getCurrentInteractive(channelId: String): InteractiveUiModel

    suspend fun getInteractiveLeaderboard(channelId: String, isChatAllowed: () -> Boolean): PlayLeaderboardInfoUiModel

    suspend fun createInteractiveGiveaway(channelId: String,
                                          title: String,
                                          durationInMs: Long): InteractiveSessionUiModel

    suspend fun createInteractiveQuiz(
        channelId: String,
        question: String,
        prize: String,
        runningTime: Long,
        choices: List<PostInteractiveCreateQuizUseCase.Choice>
    )
}