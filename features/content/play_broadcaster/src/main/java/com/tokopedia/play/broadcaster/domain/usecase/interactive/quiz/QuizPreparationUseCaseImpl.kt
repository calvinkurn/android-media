package com.tokopedia.play.broadcaster.domain.usecase.interactive.quiz

import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 07, 2022
 */
class QuizPreparationUseCaseImpl @Inject constructor(
    private val sharedPref: HydraSharedPreferences,
) : QuizPreparationUseCase {

    override fun updateQuizOption(
        options: List<QuizFormDataUiModel.Option>,
        order: Int,
        newText: String,
    ) : List<QuizFormDataUiModel.Option> {
        val isAutoSelectEligible = needToAutoSelectQuizOption(options, newText)

        val newOptions = options.map {
            it.copy(
                /** Auto Select */
                isSelected = if(isAutoSelectEligible) it.order == order else it.isSelected,
                text = if(it.order == order) newText else it.text,
                isFocus = it.order == order,
                isShowCoachmark = if(sharedPref.isFirstSelectQuizOption()) it.order == order else false,
            )
        }

        sharedPref.setNotFirstSelectQuizOption()

        return newOptions
    }

    override fun setupAutoSelectField(
        options: List<QuizFormDataUiModel.Option>,
        order: Int,
        newText: String,
    ) : Pair<List<QuizFormDataUiModel.Option>, Boolean> {
        val isAutoSelectEligible = needToAutoSelectQuizOption(options, newText)
        val newOptions = options.map {
            it.copy(
                isSelected = if(isAutoSelectEligible) it.order == order else it.isSelected,
            )
        }
        return Pair(newOptions, isAutoSelectEligible)
    }

    override fun setupAutoAddNewField(
        options: List<QuizFormDataUiModel.Option>,
        quizConfig: QuizConfigUiModel
    ) : List<QuizFormDataUiModel.Option> {
        val newOptions = options.toMutableList()
        val isAllOptionFilled = options.none { it.text.isEmpty() }
        val currentOption = options.size
        val isNeedAddNewField = isAllOptionFilled && currentOption < quizConfig.maxChoicesCount

        if(isNeedAddNewField) {
            newOptions.add(
                QuizFormDataUiModel.Option(
                order = currentOption,
                isMandatory = false,
            ))
        }

        return newOptions
    }

    override fun cleaningOptions(options: List<QuizFormDataUiModel.Option>): List<QuizFormDataUiModel.Option> {
        return options.filterNot { !it.isMandatory && it.text.isEmpty() }
    }

    override fun initQuizFormData(quizConfig: QuizConfigUiModel): QuizFormDataUiModel {
        val initialOptions = List(quizConfig.minChoicesCount) {
            QuizFormDataUiModel.Option(
                order = it,
                isMandatory = true,
            )
        }

        return QuizFormDataUiModel(options = initialOptions)
    }

    private fun needToAutoSelectQuizOption(options: List<QuizFormDataUiModel.Option>, text: String): Boolean {
        val noSelectedChoice = options.firstOrNull { it.isSelected } == null
        return noSelectedChoice && text.isNotEmpty()
    }
}