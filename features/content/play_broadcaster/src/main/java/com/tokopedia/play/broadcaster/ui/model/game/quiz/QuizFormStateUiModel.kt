package com.tokopedia.play.broadcaster.ui.model.game.quiz

/**
 * Created By : Jonathan Darwin on March 31, 2022
 */
sealed class QuizFormStateUiModel {

    object Nothing: QuizFormStateUiModel()
    object Preparation: QuizFormStateUiModel()
    object SetDuration: QuizFormStateUiModel()
}