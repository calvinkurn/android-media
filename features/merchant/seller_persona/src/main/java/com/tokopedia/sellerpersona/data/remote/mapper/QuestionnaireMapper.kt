package com.tokopedia.sellerpersona.data.remote.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireModel
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class QuestionnaireMapper @Inject constructor() {

    fun mapToUiModel(items: List<QuestionnaireModel>?): List<QuestionnairePagerUiModel> {
        return items?.map {
            QuestionnairePagerUiModel(
                id = it.id,
                questionTitle = it.question?.title.orEmpty(),
                questionSubtitle = it.question?.subtitle.orEmpty(),
                type = if (it.type == Int.ONE) {
                    QuestionnairePagerUiModel.QuestionnaireType.SINGLE_ANSWER
                } else {
                    QuestionnairePagerUiModel.QuestionnaireType.MULTIPLE_ANSWER
                },
                options = getOptions(it.type, it.options)
            )
        }.orEmpty()
    }

    private fun getOptions(
        type: Int,
        options: List<QuestionnaireModel.OptionModel>?
    ): List<BaseOptionUiModel>? {
        return options?.map {
            if (type == Int.ONE) {
                BaseOptionUiModel.QuestionOptionSingleUiModel(
                    value = it.value,
                    title = it.title
                )
            } else {
                BaseOptionUiModel.QuestionOptionMultipleUiModel(
                    value = it.value,
                    title = it.title
                )
            }
        }
    }
}