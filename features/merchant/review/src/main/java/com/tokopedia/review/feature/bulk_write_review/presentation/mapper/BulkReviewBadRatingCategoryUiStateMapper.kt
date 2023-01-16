package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemBadRatingCategoryUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import javax.inject.Inject

class BulkReviewBadRatingCategoryUiStateMapper @Inject constructor() {
    fun map(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsBadRatingCategory: List<BulkReviewItemBadRatingCategoryUiModel>
    ): Map<String, BulkReviewBadRatingCategoryUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                mapOf(
                    *getFormRequestState.result.reviewForm.map { reviewForm ->
                        mapBulkReviewBadRatingCategoryUiState(
                            reviewForm = reviewForm,
                            reviewItemBadRatingCategory = reviewItemsBadRatingCategory.find {
                                it.inboxID == reviewForm.inboxID
                            }
                        )
                    }.toTypedArray()
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapBulkReviewBadRatingCategoryUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevBulkSubmitProductReview.ReviewForm,
        reviewItemBadRatingCategory: BulkReviewItemBadRatingCategoryUiModel?
    ): Pair<String, BulkReviewBadRatingCategoryUiState> {
        val badRatingCategory = reviewItemBadRatingCategory?.badRatingCategory
        return if (badRatingCategory.isNullOrEmpty()) {
            Pair(reviewForm.inboxID, BulkReviewBadRatingCategoryUiState.Hidden)
        } else {
            if (badRatingCategory.any { it.selected }) {
                Pair(
                    reviewForm.inboxID,
                    BulkReviewBadRatingCategoryUiState.Showing(badRatingCategory = badRatingCategory)
                )
            } else {
                Pair(reviewForm.inboxID, BulkReviewBadRatingCategoryUiState.Hidden)
            }
        }
    }
}
