package com.tokopedia.play.broadcaster.ui.model.game.quiz

sealed class QuizChoiceDetailStateUiModel {
    object Loading : QuizChoiceDetailStateUiModel()
    object Error : QuizChoiceDetailStateUiModel()
    data class Success(val dataUiModel: QuizChoiceDetailUiModel) : QuizChoiceDetailStateUiModel()
    data class LoadMoreParticipant(val dataUiModel: QuizChoiceDetailUiModel) : QuizChoiceDetailStateUiModel()
    object Unknown : QuizChoiceDetailStateUiModel()
}