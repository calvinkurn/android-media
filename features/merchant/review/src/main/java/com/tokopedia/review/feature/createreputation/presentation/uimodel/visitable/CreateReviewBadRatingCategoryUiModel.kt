package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewBadRatingCategoriesTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoryUiState

data class CreateReviewBadRatingCategoryUiModel(
    val uiState: CreateReviewBadRatingCategoryUiState
) : BaseCreateReviewVisitable<CreateReviewBadRatingCategoriesTypeFactory> {
    override fun areItemsTheSame(other: Any?): Boolean {
        return other is CreateReviewBadRatingCategoryUiModel && uiState.badRatingCategory.id == other.uiState.badRatingCategory.id
    }

    override fun areContentsTheSame(other: Any?): Boolean {
        return this == other
    }

    override fun type(typeFactory: CreateReviewBadRatingCategoriesTypeFactory): Int {
        return typeFactory.type(this)
    }
}
