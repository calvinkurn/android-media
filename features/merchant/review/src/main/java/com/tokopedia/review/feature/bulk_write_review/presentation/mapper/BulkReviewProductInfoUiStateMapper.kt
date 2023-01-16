package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import javax.inject.Inject

class BulkReviewProductInfoUiStateMapper @Inject constructor() {
    fun map(
        getFormRequestState: BulkReviewGetFormRequestState
    ): Map<String, BulkReviewProductInfoUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                mapOf(
                    *getFormRequestState.result.reviewForm.map { reviewForm ->
                        mapProductInfoUiState(reviewForm)
                    }.toTypedArray()
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapProductInfoUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm
    ): Pair<String, BulkReviewProductInfoUiState> {
        return Pair(
            reviewForm.inboxID,
            BulkReviewProductInfoUiState.Showing(
                productID = reviewForm.product.productID,
                productName = reviewForm.product.productName,
                productImageUrl = reviewForm.product.productImageURL,
                productVariantName = reviewForm.product.productVariant.variantName,
                productPurchaseDate = reviewForm.timestamp.createTimeFormatted
            )
        )
    }
}
