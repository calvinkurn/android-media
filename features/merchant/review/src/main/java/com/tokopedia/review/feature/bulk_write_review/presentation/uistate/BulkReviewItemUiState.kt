package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface BulkReviewItemUiState {
    val productCardUiState: BulkReviewProductInfoUiState
    val ratingUiState: BulkReviewRatingUiState
    val badRatingCategoriesUiState: BulkReviewBadRatingCategoryUiState
    val textAreaUiState: BulkReviewTextAreaUiState
    val mediaPickerUiState: CreateReviewMediaPickerUiState
    val miniActionsUiState: BulkReviewMiniActionsUiState

    fun getReviewItemProductId(): String {
        return productCardUiState.let { productCardUiState ->
            if (productCardUiState is BulkReviewProductInfoUiState.Showing) {
                productCardUiState.productID
            } else {
                ""
            }
        }
    }

    fun getReviewItemRating(): Int {
        return ratingUiState.let { ratingUiState ->
            if (ratingUiState is BulkReviewRatingUiState.Showing) {
                ratingUiState.rating
            } else {
                Int.ZERO
            }
        }
    }

    fun isReviewItemHasEmptyReview(): Boolean {
        return textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text.isBlank()
            } else {
                true
            }
        }
    }

    fun getReviewItemReviewLength(): Int {
        return textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text.length
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemImageAttachmentCount(): Int {
        return mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState.getImageCount()
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemVideoAttachmentCount(): Int {
        return mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.HasMedia) {
                mediaPickerUiState.getVideoCount()
            } else {
                Int.ZERO
            }
        }
    }

    fun getReviewItemTextAreaHint(): StringRes {
        return textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.hint
            } else {
                StringRes(Int.ZERO)
            }
        }
    }

    fun getReviewItemTextAreaText(): String {
        return textAreaUiState.let { textAreaUiState ->
            if (textAreaUiState is BulkReviewTextAreaUiState.Showing) {
                textAreaUiState.text
            } else {
                ""
            }
        }
    }

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
