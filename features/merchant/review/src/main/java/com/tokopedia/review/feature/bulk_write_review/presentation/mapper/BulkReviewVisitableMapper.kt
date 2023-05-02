package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.kotlin.model.ImpressHolder
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
        removedReviewItem: Set<String>,
        impressedReviewItems: Set<String>,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>
    ): List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>> {
        return arrayListOf<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>().apply {
            includeAnnouncementWidget(productRevGetBulkForm.themeCopy)
            includeReviewItems(
                productRevGetBulkForm.reviewForm,
                removedReviewItem,
                impressedReviewItems,
                bulkReviewProductInfoUiState,
                bulkReviewRatingUiState,
                bulkReviewBadRatingCategoryUiState,
                bulkReviewMediaPickerUiState,
                bulkReviewTextAreaUiState,
                bulkReviewMiniActionsUiState
            )
        }
    }

    private fun ArrayList<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>.includeAnnouncementWidget(
        themeCopy: String
    ) {
        if (themeCopy.isNotBlank()) add(BulkReviewAnnouncementUiModel(themeCopy))
    }

    private fun ArrayList<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>.includeReviewItems(
        reviewForms: List<BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm>,
        removedReviewItem: Set<String>,
        impressedReviewItems: Set<String>,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>
    ) {
        val hasFocusedReviewItem = bulkReviewTextAreaUiState.values.any { textAreaUiState ->
            textAreaUiState is BulkReviewTextAreaUiState.Showing && textAreaUiState.focused
        }
        reviewForms.forEachIndexed { index, reviewForm ->
            if (reviewForm.inboxID !in removedReviewItem) {
                mapReviewFormToBulkReviewVisitable(
                    index = index,
                    reviewForm = reviewForm,
                    hasFocusedReviewItem = hasFocusedReviewItem,
                    bulkReviewProductInfoUiState = bulkReviewProductInfoUiState,
                    bulkReviewRatingUiState = bulkReviewRatingUiState,
                    bulkReviewBadRatingCategoryUiState = bulkReviewBadRatingCategoryUiState,
                    bulkReviewTextAreaUiState = bulkReviewTextAreaUiState,
                    bulkReviewMediaPickerUiState = bulkReviewMediaPickerUiState,
                    bulkReviewMiniActionsUiState = bulkReviewMiniActionsUiState,
                    reviewItemImpressed = reviewForm.inboxID in impressedReviewItems
                )?.let { add(it) }
            }
        }
    }

    private fun mapReviewFormToBulkReviewVisitable(
        index: Int,
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        hasFocusedReviewItem: Boolean,
        bulkReviewProductInfoUiState: Map<String, BulkReviewProductInfoUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>,
        bulkReviewMiniActionsUiState: Map<String, BulkReviewMiniActionsUiState>,
        reviewItemImpressed: Boolean
    ): BulkReviewItemUiModel? {
        val inboxID = reviewForm.inboxID
        val reputationID = reviewForm.reputationID
        val orderID = reviewForm.orderID
        val shopID = reviewForm.shopID
        val productCardUiState = bulkReviewProductInfoUiState[inboxID]
        val ratingUiState = bulkReviewRatingUiState[inboxID]
        val badRatingCategoriesUiState = bulkReviewBadRatingCategoryUiState[inboxID]
        val textAreaUiState = bulkReviewTextAreaUiState[inboxID]
        val mediaPickerUiState = bulkReviewMediaPickerUiState[inboxID]
        val miniActionsUiState = bulkReviewMiniActionsUiState[inboxID]
        return if (productCardUiState != null && ratingUiState != null && badRatingCategoriesUiState != null && textAreaUiState != null && mediaPickerUiState != null && miniActionsUiState != null) {
            BulkReviewItemUiModel(
                position = index,
                inboxID = inboxID,
                reputationID = reputationID,
                orderID = orderID,
                shopID = shopID,
                impressHolder = if (reviewItemImpressed) {
                    ImpressHolder().apply { invoke() }
                } else {
                    ImpressHolder()
                },
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
        } else {
            null
        }
    }
}
