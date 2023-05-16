package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class QuestionnairePagerUiModel(
    val id: String = String.EMPTY,
    val questionTitle: String = String.EMPTY,
    val questionSubtitle: String = String.EMPTY,
    val type: QuestionnaireType = QuestionnaireType.SINGLE_ANSWER,
    val options: List<BaseOptionUiModel>? = null
) {

    enum class QuestionnaireType {
        SINGLE_ANSWER, MULTIPLE_ANSWER
    }
}