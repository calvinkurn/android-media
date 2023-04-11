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
        val questionList = mutableListOf<QuestionnaireUiModel>()

        questionnaireData.data.questionList.forEachIndexed { index, question ->
            when (question.questionType) {
                QuestionnaireUiModel.TYPE_MULTIPLE_OPTION -> {
                    questionList.add(
                        createMultipleOptionQuestion(
                            question,
                            index != numOfQuestions.minus(1)
                        )
                    )
                }
                QuestionnaireUiModel.TYPE_SINGLE_OPTION -> {
                    questionList.add(createSingleOptionQuestion(question))
                }
                QuestionnaireUiModel.TYPE_RATE -> {
                    createRatingOptionQuestion(question)
                }
                else -> {
                    return@forEachIndexed
                }
            }
        }
        return questionList.toList()
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
    ): QuestionnaireUiModel.QuestionnaireSingleOptionUiModel {
        return QuestionnaireUiModel.QuestionnaireSingleOptionUiModel(
            question = questionData.question,
            options = questionData.option.map {
                QuestionnaireOptionUiModel(text = it.value)
            }
        )
    }

    private fun createRatingOptionQuestion(
        questionData: Question
    ): QuestionnaireUiModel.QuestionnaireRatingUiModel {
        return QuestionnaireUiModel.QuestionnaireRatingUiModel(questionData.question)
    }
}
