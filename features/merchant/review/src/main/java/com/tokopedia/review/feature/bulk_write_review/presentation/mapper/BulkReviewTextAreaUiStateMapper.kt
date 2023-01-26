package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemTestimonyUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel.BulkReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.reviewcommon.uimodel.StringRes
import javax.inject.Inject

class BulkReviewTextAreaUiStateMapper @Inject constructor() {
    fun map(
        getFormRequestState: BulkReviewGetFormRequestState,
        reviewItemsTestimony: List<BulkReviewItemTestimonyUiModel>,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>
    ): Map<String, BulkReviewTextAreaUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapTextAreaUiState(
                            reviewForm = reviewForm,
                            reviewItemTestimony = reviewItemsTestimony.find {
                                it.inboxID == reviewForm.inboxID
                            },
                            bulkReviewBadRatingCategoryUiState = bulkReviewBadRatingCategoryUiState,
                            bulkReviewRatingUiState = bulkReviewRatingUiState
                        )
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapTextAreaUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        reviewItemTestimony: BulkReviewItemTestimonyUiModel?,
        bulkReviewBadRatingCategoryUiState: Map<String, BulkReviewBadRatingCategoryUiState>,
        bulkReviewRatingUiState: Map<String, BulkReviewRatingUiState>
    ): BulkReviewTextAreaUiState {
        val inboxID = reviewForm.inboxID
        val badRatingCategoryUiState = bulkReviewBadRatingCategoryUiState[inboxID]
        val isOnlyBadRatingOtherCategorySelected =
            if (badRatingCategoryUiState is BulkReviewBadRatingCategoryUiState.Showing) {
                badRatingCategoryUiState.badRatingCategory.all {
                    (it.id != ReviewConstants.BAD_RATING_OTHER_ID && !it.selected) ||
                        (it.id == ReviewConstants.BAD_RATING_OTHER_ID && it.selected)
                }
            } else {
                false
            }
        val reviewItemRatingUiState = bulkReviewRatingUiState[inboxID]
        val rating = if (reviewItemRatingUiState is BulkReviewRatingUiState.Showing) {
            reviewItemRatingUiState.rating
        } else {
            BulkReviewRatingUiStateMapper.DEFAULT_PRODUCT_RATING
        }
        return if (reviewItemTestimony?.testimonyUiModel?.showTextArea == true) {
            BulkReviewTextAreaUiState.Showing(
                text = reviewItemTestimony.testimonyUiModel.text,
                hint = if (rating <= BulkReviewViewModel.BAD_RATING_CATEGORY_THRESHOLD) {
                    if (isOnlyBadRatingOtherCategorySelected) {
                        StringRes(R.string.review_form_bad_helper_must_fill)
                    } else {
                        StringRes(R.string.review_form_bad_helper)
                    }
                } else if (rating == CreateReviewFragment.RATING_3) {
                    StringRes(R.string.review_form_neutral_helper)
                } else {
                    StringRes(R.string.review_form_good_helper)
                },
                focused = reviewItemTestimony.testimonyUiModel.focused
            )
        } else {
            BulkReviewTextAreaUiState.Hidden
        }
    }
}
