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
                    *getFormRequestState.result.reviewForm?.map { reviewForm ->
                        mapProductInfoUiState(reviewForm)
                    }?.toTypedArray().orEmpty()
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapProductInfoUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm
    ): Pair<String, BulkReviewProductInfoUiState> {
        return Pair(
            reviewForm.inboxID.orEmpty(),
            BulkReviewProductInfoUiState.Showing(
                productID = reviewForm.product?.productID.orEmpty(),
                productName = reviewForm.product?.productName.orEmpty(),
                productImageUrl = reviewForm.product?.productImageURL.orEmpty(),
                productVariantName = reviewForm.product?.productVariant?.variantName.orEmpty(),
                productPurchaseDate = reviewForm.product?.timestamp?.createTimeFormatted.orEmpty()
            )
        )
    }
}
