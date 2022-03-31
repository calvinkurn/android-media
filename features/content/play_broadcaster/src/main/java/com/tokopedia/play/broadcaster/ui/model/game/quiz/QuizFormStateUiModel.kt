package com.tokopedia.play.broadcaster.ui.model.game.quiz

/**
 * Created By : Jonathan Darwin on March 31, 2022
 */
sealed class QuizFormStateUiModel {

    abstract fun prev(): QuizFormStateUiModel
    abstract fun next(): QuizFormStateUiModel

    object Nothing: QuizFormStateUiModel() {
        override fun prev() = Nothing
        override fun next() = Preparation
    }

    object Preparation: QuizFormStateUiModel() {
        override fun prev() = Nothing
        override fun next() = SetDuration
    }

    object SetDuration: QuizFormStateUiModel() {
        override fun prev() = Preparation
        override fun next() = Nothing
    }
}