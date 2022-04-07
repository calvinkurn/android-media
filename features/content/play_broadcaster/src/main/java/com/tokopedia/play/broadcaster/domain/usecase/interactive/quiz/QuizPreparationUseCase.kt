package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 07, 2022
 */
interface QuizPreparationUseCase {

    fun updateQuizOption(
        options: List<QuizFormDataUiModel.Option>,
        order: Int,
        newText: String,
    ) : List<QuizFormDataUiModel.Option>

    fun setupAutoSelectField(
        options: List<QuizFormDataUiModel.Option>,
        order: Int,
        newText: String,
    ) : Pair<List<QuizFormDataUiModel.Option>, Boolean>

    fun setupAutoAddNewField(
        options: List<QuizFormDataUiModel.Option>,
        quizConfig: QuizConfigUiModel
    ) : List<QuizFormDataUiModel.Option>

    fun cleaningOptions(
        options: List<QuizFormDataUiModel.Option>
    ) : List<QuizFormDataUiModel.Option>

    fun initQuizFormData(
        quizConfig: QuizConfigUiModel,
    ) : QuizFormDataUiModel
}