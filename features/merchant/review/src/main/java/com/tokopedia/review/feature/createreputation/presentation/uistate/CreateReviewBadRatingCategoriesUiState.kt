package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewBadRatingCategoryUiModel

sealed interface CreateReviewBadRatingCategoriesUiState {
    val badRatingCategories: List<CreateReviewBadRatingCategoryUiModel>

    object Loading : CreateReviewBadRatingCategoriesUiState {
        override val badRatingCategories: List<CreateReviewBadRatingCategoryUiModel> = emptyList()
    }

    data class Hidden(
        override val badRatingCategories: List<CreateReviewBadRatingCategoryUiModel>,
        val trackerData: Showing.TrackerData
    ) : CreateReviewBadRatingCategoriesUiState

    data class Showing(
        override val badRatingCategories: List<CreateReviewBadRatingCategoryUiModel>,
        val trackerData: TrackerData
    ) : CreateReviewBadRatingCategoriesUiState {
        data class TrackerData(
            val orderId: String,
            val productId: String,
            val userId: String
        )
    }
}