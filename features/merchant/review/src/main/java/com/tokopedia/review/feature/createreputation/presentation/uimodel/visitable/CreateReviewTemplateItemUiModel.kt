package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewTemplateTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateItemUiState
import java.io.Serializable

data class CreateReviewTemplateItemUiModel(
    val uiState: CreateReviewTemplateItemUiState
) : Serializable, BaseCreateReviewVisitable<CreateReviewTemplateTypeFactory> {
    override fun areItemsTheSame(other: Any?): Boolean {
        return other is CreateReviewTemplateItemUiModel && uiState.areItemsTheSame(other.uiState)
    }

    override fun areContentsTheSame(other: Any?): Boolean {
        return other is CreateReviewTemplateItemUiModel && uiState.areContentsTheSame(other.uiState)
    }

    override fun getChangePayload(other: Any?): List<String>? {
        return null
    }

    override fun getSpanSize(): Int {
        return 1
    }

    override fun type(typeFactory: CreateReviewTemplateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
