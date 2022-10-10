package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewTemplateTypeFactory
import java.io.Serializable

data class CreateReviewTemplateItemUiModel(
    val data: CreateReviewTemplate
) : Serializable, BaseCreateReviewVisitable<CreateReviewTemplateTypeFactory> {
    override fun areItemsTheSame(other: Any?): Boolean {
        return other is CreateReviewTemplateItemUiModel && data.text == other.data.text
    }

    override fun areContentsTheSame(other: Any?): Boolean {
        return this == other
    }

    override fun type(typeFactory: CreateReviewTemplateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
