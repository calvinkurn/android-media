package com.tokopedia.review.feature.createreputation.presentation.uimodel

import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import java.io.Serializable

data class CreateReviewTextAreaTextUiModel(
    val text: String = "",
    val source: Source = Source.SAVED_INSTANCE_STATE,
    val appendedTemplates: List<CreateReviewTemplate> = emptyList()
) : Serializable {
    enum class Source {
        CREATE_REVIEW_TEXT_AREA,
        CREATE_REVIEW_EXPANDED_TEXT_AREA,
        CREATE_REVIEW_TEMPLATE,
        SAVED_INSTANCE_STATE
    }
}
