package com.tokopedia.power_merchant.subscribe.domain.mapper

import com.tokopedia.power_merchant.subscribe.domain.model.Question
import com.tokopedia.power_merchant.subscribe.domain.model.QuestionnaireData
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/21
 */

class PMDeactivationQuestionnaireMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(result: QuestionnaireData): DeactivationQuestionnaireUiModel {
        return DeactivationQuestionnaireUiModel(generateQuestionsData(result))
    }

    private fun generateQuestionsData(
            questionnaireData: QuestionnaireData
    ): List<QuestionnaireUiModel> {
        val numOfQuestions = questionnaireData.data.questionList.size
        return questionnaireData.data.questionList.mapIndexed { index, question ->
             return@mapIndexed when (question.questionType) {
                QuestionnaireUiModel.TYPE_MULTIPLE_OPTION -> {
                    createMultipleOptionQuestion(question, index != numOfQuestions.minus(1))
                }
                else -> {
                    createSingleOptionQuestion(question)
                }
            }
        }
    }

    private fun createMultipleOptionQuestion(
            questionData: Question,
            showItemDivider: Boolean
    ): QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel {
        return QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel(
                question = questionData.question,
                options = questionData.option.map {
                    QuestionnaireOptionUiModel(it.value, imageURL = it.imageURL)
                },
                showItemDivider = showItemDivider
        )
    }

    private fun createSingleOptionQuestion(
        questionData: Question
    ): QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel {
        return QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel(
            question = questionData.question,
            options = questionData.option.map {
                QuestionnaireOptionUiModel(text = it.value)
            }
        )
    }
}
