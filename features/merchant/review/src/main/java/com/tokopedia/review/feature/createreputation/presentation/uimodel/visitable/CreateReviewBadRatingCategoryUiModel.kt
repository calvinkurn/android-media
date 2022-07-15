package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewBadRatingCategoriesTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable.Companion.PAYLOAD_HIDING
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable.Companion.PAYLOAD_SHOWING
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

    override fun getChangePayload(other: Any?): List<String>? {
        return if (other is CreateReviewBadRatingCategoryUiModel) {
            arrayListOf<String>().apply {
                if (
                    uiState is CreateReviewBadRatingCategoryUiState.Showing &&
                    other.uiState is CreateReviewBadRatingCategoryUiState.Hidden
                ) {
                    add(PAYLOAD_HIDING)
                } else if (
                    uiState is CreateReviewBadRatingCategoryUiState.Hidden &&
                    other.uiState is CreateReviewBadRatingCategoryUiState.Showing
                ) {
                    add(PAYLOAD_SHOWING)
                }
            }
        } else null
    }

    override fun getSpanSize(): Int {
        return 1
    }

    override fun type(typeFactory: CreateReviewBadRatingCategoriesTypeFactory): Int {
        return typeFactory.type(this)
    }
}
