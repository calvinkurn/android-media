package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewAnnouncementUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewItemUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import javax.inject.Inject

class BulkReviewVisitableMapper @Inject constructor() {
    fun map(
        productRevGetBulkForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm,
        removedReviewItem: List<String>,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>
    ): List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>> {
        val hasFocusedReviewItem = bulkReviewTextAreaUiState.values.any { textAreaUiState ->
            textAreaUiState is BulkReviewTextAreaUiState.Showing && textAreaUiState.focused
        }
        return listOf(BulkReviewAnnouncementUiModel).plus(
            productRevGetBulkForm.reviewForm?.filter {
                it.inboxID !in removedReviewItem
            }?.mapNotNull { reviewForm ->
                mapReviewFormToBulkReviewVisitable(
                    reviewForm = reviewForm,
                    hasFocusedReviewItem = hasFocusedReviewItem,
                    bulkReviewProductInfoUiState = bulkReviewProductInfoUiState,
                    bulkReviewRatingUiState = bulkReviewRatingUiState,
                    bulkReviewBadRatingCategoryUiState = bulkReviewBadRatingCategoryUiState,
                    bulkReviewTextAreaUiState = bulkReviewTextAreaUiState,
                    bulkReviewMediaPickerUiState = bulkReviewMediaPickerUiState,
                    bulkReviewMiniActionsUiState = bulkReviewMiniActionsUiState
                )
            }.orEmpty()
        )
    }

    private fun mapReviewFormToBulkReviewVisitable(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        hasFocusedReviewItem: Boolean,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>
    ): BulkReviewItemUiModel? {
        val inboxID = reviewForm.inboxID.orEmpty()
        val productCardUiState = bulkReviewProductInfoUiState[inboxID]
        val ratingUiState = bulkReviewRatingUiState[inboxID]
        val badRatingCategoriesUiState = bulkReviewBadRatingCategoryUiState[inboxID]
        val textAreaUiState = bulkReviewTextAreaUiState[inboxID]
        val mediaPickerUiState = bulkReviewMediaPickerUiState[inboxID]
        val miniActionsUiState = bulkReviewMiniActionsUiState[inboxID]
        return if (productCardUiState != null && ratingUiState != null && badRatingCategoriesUiState != null && textAreaUiState != null && mediaPickerUiState != null && miniActionsUiState != null) {
            BulkReviewItemUiModel(
                inboxID = inboxID,
                uiState = if (textAreaUiState is BulkReviewTextAreaUiState.Showing && textAreaUiState.focused) {
                    BulkReviewItemUiState.Focused(
                        productCardUiState = productCardUiState,
                        ratingUiState = ratingUiState,
                        badRatingCategoriesUiState = badRatingCategoriesUiState,
                        textAreaUiState = textAreaUiState,
                        mediaPickerUiState = mediaPickerUiState,
                        miniActionsUiState = miniActionsUiState
                    )
                } else if (hasFocusedReviewItem) {
                    BulkReviewItemUiState.Dimmed(
                        productCardUiState = productCardUiState,
                        ratingUiState = ratingUiState,
                        badRatingCategoriesUiState = badRatingCategoriesUiState,
                        textAreaUiState = textAreaUiState,
                        mediaPickerUiState = mediaPickerUiState,
                        miniActionsUiState = miniActionsUiState
                    )
                } else {
                    BulkReviewItemUiState.Showing(
                        productCardUiState = productCardUiState,
                        ratingUiState = ratingUiState,
                        badRatingCategoriesUiState = badRatingCategoriesUiState,
                        textAreaUiState = textAreaUiState,
                        mediaPickerUiState = mediaPickerUiState,
                        miniActionsUiState = miniActionsUiState
                    )
                }
            )
        } else null
    }
}
