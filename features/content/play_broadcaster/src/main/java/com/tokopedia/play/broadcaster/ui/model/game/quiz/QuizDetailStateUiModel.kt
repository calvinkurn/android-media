package com.tokopedia.play.broadcaster.ui.model.game.quiz

sealed class QuizDetailStateUiModel {
    object Loading : QuizDetailStateUiModel()
    object Error : QuizDetailStateUiModel()
    data class Success(val dataUiModel: QuizDetailDataUiModel) : QuizDetailStateUiModel()
    object Unknown : QuizDetailStateUiModel()

}