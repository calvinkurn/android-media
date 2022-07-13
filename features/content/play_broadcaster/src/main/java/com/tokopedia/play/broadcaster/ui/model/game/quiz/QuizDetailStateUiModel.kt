package com.tokopedia.play.broadcaster.ui.model.game.quiz

import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel

sealed class QuizDetailStateUiModel {
    object Loading : QuizDetailStateUiModel()
    data class Error(val allowChat: Boolean, val isQuizDetail: Boolean) : QuizDetailStateUiModel()
    data class Success(val leaderboardSlots: List<PlayLeaderboardUiModel>) : QuizDetailStateUiModel()
    object Empty : QuizDetailStateUiModel()

}