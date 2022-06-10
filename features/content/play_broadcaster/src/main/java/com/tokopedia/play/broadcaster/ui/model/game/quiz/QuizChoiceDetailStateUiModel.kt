package com.tokopedia.play.broadcaster.ui.model.game.quiz

sealed class QuizChoiceDetailStateUiModel {
    object Loading : QuizChoiceDetailStateUiModel()
    data class Error(val choiceId: String, val index: Int) : QuizChoiceDetailStateUiModel()
    data class Success(val dataUiModel: QuizChoiceDetailUiModel) : QuizChoiceDetailStateUiModel()
    object Empty : QuizChoiceDetailStateUiModel()
}