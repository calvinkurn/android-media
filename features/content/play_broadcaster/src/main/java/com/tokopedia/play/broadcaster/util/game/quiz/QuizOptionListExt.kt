package com.tokopedia.play.broadcaster.util.game.quiz

import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel

/**
 * Created By : Jonathan Darwin on April 07, 2022
 */
object QuizOptionListExt {

    fun List<QuizFormDataUiModel.Option>.updateQuizOptionFlow(
        order: Int,
        newText: String,
        quizConfig: QuizConfigUiModel,
        isFirstSelectQuizOption: Boolean = false,
    ) : Pair<List<QuizFormDataUiModel.Option>, Boolean> {

        val isAutoSelect: Boolean
        val isAutoAdd: Boolean

        val newOptions = updateQuizOption(order, newText, isFirstSelectQuizOption)
                        .setupAutoSelectField(order, newText).also { isAutoSelect = it.second }.first
                        .setupAutoAddField(quizConfig).also { isAutoAdd = it.size > size }

        return Pair(newOptions, isAutoSelect || isAutoAdd)
    }

    private fun List<QuizFormDataUiModel.Option>.updateQuizOption(
        order: Int,
        newText: String,
        isFirstSelectQuizOption: Boolean = false,
    ): List<QuizFormDataUiModel.Option> {
        return map {
            it.copy(
                text = if(it.order == order) newText else it.text,
                isFocus = it.order == order,
                isShowCoachmark = if(isFirstSelectQuizOption) it.order == order else it.isShowCoachmark,
            )
        }
    }

    private fun List<QuizFormDataUiModel.Option>.setupAutoSelectField(
        order: Int,
        newText: String,
    ) : Pair<List<QuizFormDataUiModel.Option>, Boolean> {
        val noSelectedChoice = firstOrNull { it.isSelected } == null
        val isAutoSelectEligible = noSelectedChoice && newText.isNotEmpty()

        val newOptions = map {
            it.copy(
                isSelected = if(isAutoSelectEligible) it.order == order else it.isSelected,
            )
        }

        return Pair(newOptions, isAutoSelectEligible)
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
                    isMandatory = false,
                ))
        }

        return newOptions
    }

    fun List<QuizFormDataUiModel.Option>.setupEditable(
        isEditable: Boolean,
    ) : List<QuizFormDataUiModel.Option> {
        return map { it.copy(isEditable = isEditable, isFocus = false, isShowCoachmark = false) }
    }

    fun List<QuizFormDataUiModel.Option>.removeUnusedField(): List<QuizFormDataUiModel.Option> {
        return filterNot { !it.isMandatory && it.text.isEmpty() }
    }

    fun List<QuizFormDataUiModel.Option>.trim(): List<QuizFormDataUiModel.Option> {
        return map { it.copy(text = it.text.trim()) }
    }
}