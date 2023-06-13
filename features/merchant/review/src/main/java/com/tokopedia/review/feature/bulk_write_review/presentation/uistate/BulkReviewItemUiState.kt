package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState

sealed interface BulkReviewItemUiState {
    val productCardUiState: BulkReviewProductInfoUiState
    val ratingUiState: BulkReviewRatingUiState
    val badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState
    val textAreaUiState: BulkReviewTextAreaUiState
    val mediaPickerUiState: CreateReviewMediaPickerUiState
    val miniActionsUiState: BulkReviewMiniActionsUiState

    data class Showing(
        override val productCardUiState: BulkReviewProductInfoUiState,
        override val ratingUiState: BulkReviewRatingUiState,
        override val badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState,
        override val textAreaUiState: BulkReviewTextAreaUiState,
        override val mediaPickerUiState: CreateReviewMediaPickerUiState,
        override val miniActionsUiState: BulkReviewMiniActionsUiState
    ) : BulkReviewItemUiState

    data class Dimmed(
        override val productCardUiState: BulkReviewProductInfoUiState,
        override val ratingUiState: BulkReviewRatingUiState,
        override val badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState,
        override val textAreaUiState: BulkReviewTextAreaUiState,
        override val mediaPickerUiState: CreateReviewMediaPickerUiState,
        override val miniActionsUiState: BulkReviewMiniActionsUiState
    ) : BulkReviewItemUiState

    data class Focused(
        override val productCardUiState: BulkReviewProductInfoUiState,
        override val ratingUiState: BulkReviewRatingUiState,
        override val badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState,
        override val textAreaUiState: BulkReviewTextAreaUiState,
        override val mediaPickerUiState: CreateReviewMediaPickerUiState,
        override val miniActionsUiState: BulkReviewMiniActionsUiState
    ) : BulkReviewItemUiState
}
