package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemRatingUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import javax.inject.Inject

class BulkReviewRatingUiStateMapper @Inject constructor() {

    companion object {
        const val DEFAULT_PRODUCT_RATING = 5
    }

    fun map(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsRating: List<BulkReviewItemRatingUiModel>
    ): Map<String, BulkReviewRatingUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                mapOf(
                    *getFormRequestState.result.reviewForm?.map { reviewForm ->
                        mapRatingUiState(
                            reviewForm = reviewForm,
                            reviewItemRating = reviewItemsRating.find {
                                it.inboxID == reviewForm.inboxID
                            }
                        )
                    }?.toTypedArray().orEmpty()
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapRatingUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        reviewItemRating: BulkReviewItemRatingUiModel?
    ): Pair<String, BulkReviewRatingUiState> {
        return Pair(
            reviewForm.inboxID.orEmpty(),
            BulkReviewRatingUiState.Showing(
                rating = reviewItemRating?.rating ?: DEFAULT_PRODUCT_RATING,
                animate = reviewItemRating?.animate ?: true
            )
        )
    }
}
