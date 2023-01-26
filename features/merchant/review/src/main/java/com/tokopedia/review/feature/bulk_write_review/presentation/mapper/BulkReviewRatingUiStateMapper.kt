package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
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
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapRatingUiState(
                            reviewItemRating = reviewItemsRating.find {
                                it.inboxID == reviewForm.inboxID
                            }
                        )
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapRatingUiState(
        reviewItemRating: BulkReviewItemRatingUiModel?
    ): BulkReviewRatingUiState {
        return BulkReviewRatingUiState.Showing(
            rating = reviewItemRating?.rating ?: DEFAULT_PRODUCT_RATING,
            animate = reviewItemRating?.animate ?: true
        )
    }
}
