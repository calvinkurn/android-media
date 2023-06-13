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
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapProductInfoUiState(reviewForm)
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapProductInfoUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm
    ): BulkReviewProductInfoUiState {
        return BulkReviewProductInfoUiState.Showing(
            productID = reviewForm.product.productID,
            productName = reviewForm.product.productName,
            productImageUrl = reviewForm.product.productImageURL,
            productVariantName = reviewForm.product.productVariant.variantName,
            productPurchaseDate = reviewForm.timestamp.createTimeFormatted
        )
    }
}
