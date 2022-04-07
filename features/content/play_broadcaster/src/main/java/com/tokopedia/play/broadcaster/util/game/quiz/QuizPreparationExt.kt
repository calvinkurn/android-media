package com.tokopedia.play.broadcaster.util.game.quiz

import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel

/**
 * Created By : Jonathan Darwin on April 07, 2022
 */
object QuizPreparationExt {

    fun List<QuizFormDataUiModel.Option>.updateQuizOption(
        order: Int,
        newText: String,
        isFirstSelectQuizOption: Boolean = false,
    ): List<QuizFormDataUiModel.Option> {
        return map {
            it.copy(
                text = if(it.order == order) newText else it.text,
                isFocus = it.order == order,
                isShowCoachmark = if(isFirstSelectQuizOption) it.order == order else false,
            )
        }
    }

    fun List<QuizFormDataUiModel.Option>.setupAutoSelectField(
        order: Int,
        newText: String,
    ) : List<QuizFormDataUiModel.Option> {
        val noSelectedChoice = firstOrNull { it.isSelected } == null
        val isAutoSelectEligible = noSelectedChoice && newText.isNotEmpty()

        return map {
            it.copy(
                isSelected = if(isAutoSelectEligible) it.order == order else it.isSelected,
            )
        }
    }

    fun List<QuizFormDataUiModel.Option>.setupAutoAddField(
        quizConfig: QuizConfigUiModel,
    ) : List<QuizFormDataUiModel.Option> {
        val newOptions = toMutableList()
        val isAllOptionFilled = none { it.text.isEmpty() }
        val isNeedAddNewField = isAllOptionFilled && size < quizConfig.maxChoicesCount

        if(isNeedAddNewField) {
            newOptions.add(
                QuizFormDataUiModel.Option(
                    order = size,
                    maxLength = quizConfig.maxChoiceLength,
                    isMandatory = false,
                ))
        }

        return newOptions
    }
}