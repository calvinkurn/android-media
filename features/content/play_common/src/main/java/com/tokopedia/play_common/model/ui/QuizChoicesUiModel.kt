package com.tokopedia.play_common.model.ui

import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState

/**
 * @author by astidhiyaa on 08/04/22
 */
sealed class QuizChoicesUiModel {
    data class Complete(
        val id: String,
        val question: String, //should be choice
        val type: PlayQuizOptionState
    ): QuizChoicesUiModel()

    object Fail: QuizChoicesUiModel()
}

sealed class QuizInteractiveUiModel{
    data class Ongoing(
        val question: String,
        val reward: String = "", //in leaderboard it won't be empty (opt)
        val userPicks: String = "", //if user is participated won't be empty
        val listOfOption: List<QuizChoicesUiModel>
    )

    object Fail: QuizChoicesUiModel()
}
