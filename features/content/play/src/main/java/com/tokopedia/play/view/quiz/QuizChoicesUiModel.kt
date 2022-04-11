package com.tokopedia.play.view.quiz

import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState

/**
 * @author by astidhiyaa on 08/04/22
 */
sealed class QuizChoicesUiModel {
    data class Complete(
        val id: String,
        val question: String,
        val type: PlayQuizOptionState
    ): QuizChoicesUiModel()

    object Fail: QuizChoicesUiModel()
}
