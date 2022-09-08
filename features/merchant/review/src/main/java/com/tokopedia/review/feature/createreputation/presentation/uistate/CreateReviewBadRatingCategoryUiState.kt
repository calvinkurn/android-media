package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.model.BadRatingCategory

sealed interface CreateReviewBadRatingCategoryUiState {
    val badRatingCategory: BadRatingCategory
    data class Hidden(override val badRatingCategory: BadRatingCategory): CreateReviewBadRatingCategoryUiState
    data class Showing(override val badRatingCategory: BadRatingCategory): CreateReviewBadRatingCategoryUiState
}