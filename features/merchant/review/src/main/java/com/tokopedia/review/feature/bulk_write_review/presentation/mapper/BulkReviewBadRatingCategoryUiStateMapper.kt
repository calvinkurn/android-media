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
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapBulkReviewBadRatingCategoryUiState(
                            reviewForm = reviewForm,
                            reviewItemBadRatingCategory = reviewItemsBadRatingCategory.find {
                                it.inboxID == reviewForm.inboxID
                            }
                        )
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapBulkReviewBadRatingCategoryUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        reviewItemBadRatingCategory: BulkReviewItemBadRatingCategoryUiModel?
    ): BulkReviewBadRatingCategoryUiState {
        val badRatingCategory = reviewItemBadRatingCategory?.badRatingCategory
        return if (badRatingCategory.isNullOrEmpty()) {
            BulkReviewBadRatingCategoryUiState.Hidden
        } else {
            if (badRatingCategory.any { it.selected }) {
                BulkReviewBadRatingCategoryUiState.Showing(badRatingCategory = badRatingCategory)
            } else {
                BulkReviewBadRatingCategoryUiState.Hidden
            }
        }
    }
}
