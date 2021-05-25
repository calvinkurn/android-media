package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireAdapterFactory

/**
 * Created By @ilhamsuaib on 06/03/21
 */

sealed class QuestionnaireUiModel(
        open val type: String
) : Visitable<QuestionnaireAdapterFactory> {

    companion object {
        const val TYPE_RATE = "rate"
        const val TYPE_MULTIPLE_OPTION = "multi_answer_question"
    }

    open fun isNoAnswer(): Boolean = true

    data class QuestionnaireRatingUiModel(
            val question: String = "",
            var givenRating: Int = 0
    ) : QuestionnaireUiModel(TYPE_RATE) {

        override fun type(typeFactory: QuestionnaireAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun isNoAnswer(): Boolean = givenRating == 0
    }

    data class QuestionnaireMultipleOptionUiModel(
            val question: String = "",
            val options: List<QuestionnaireOptionUiModel>,
            val showItemDivider: Boolean = true
    ) : QuestionnaireUiModel(TYPE_MULTIPLE_OPTION) {

        override fun type(typeFactory: QuestionnaireAdapterFactory): Int {
            return typeFactory.type(this)
        }

        fun getAnswerList(): List<String> {
            return options.filter { it.isChecked }
                    .map { it.text }
        }

        override fun isNoAnswer(): Boolean = options.any { it.isChecked }.not()
    }
}