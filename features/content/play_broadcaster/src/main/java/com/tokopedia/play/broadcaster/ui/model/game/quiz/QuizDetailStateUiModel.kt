package com.tokopedia.play.broadcaster.ui.model.game.quiz

import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel

sealed class QuizDetailStateUiModel {
    object Loading : QuizDetailStateUiModel()
    object Error : QuizDetailStateUiModel()
    data class Success(val leaderboardSlots: List<PlayLeaderboardUiModel>) : QuizDetailStateUiModel()
    object Unknown : QuizDetailStateUiModel()

}