package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BulkReviewPageUiStateMapper @Inject constructor(
    private val userSession: UserSessionInterface
) {
    fun map(
        shouldCancelBulkReview: Boolean,
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        bulkReviewStickyButtonUiState: BulkReviewStickyButtonUiState,
        getFormRequestState: BulkReviewGetFormRequestState,
        getBadRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState
    ): BulkReviewPageUiState {
        return if (shouldCancelBulkReview) {
            BulkReviewPageUiState.Cancelled
        } else {
            when (getFormRequestState) {
                is BulkReviewGetFormRequestState.Complete.Error -> mapOnBulkReviewPageError(
                    getFormRequestState.throwable
                )
                is BulkReviewGetFormRequestState.Complete.Success -> mapOnGetFormSuccess(
                    shouldSubmitReview,
                    submitBulkReviewRequestState,
                    bulkReviewVisitableList,
                    bulkReviewStickyButtonUiState,
                    getBadRatingCategoryRequestState
                )
                is BulkReviewGetFormRequestState.Requesting -> mapOnBulkReviewPageLoading()
            }
        }
    }

    private fun mapOnGetFormSuccess(
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        bulkReviewStickyButtonUiState: BulkReviewStickyButtonUiState,
        badRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState
    ): BulkReviewPageUiState {
        return when (badRatingCategoryRequestState) {
            is BulkReviewGetBadRatingCategoryRequestState.Complete.Error -> mapOnBulkReviewPageError(badRatingCategoryRequestState.throwable)
            is BulkReviewGetBadRatingCategoryRequestState.Complete.Success -> mapOnBulkReviewPageSuccess(
                shouldSubmitReview,
                submitBulkReviewRequestState,
                bulkReviewVisitableList,
                bulkReviewStickyButtonUiState
            )
            is BulkReviewGetBadRatingCategoryRequestState.Requesting -> mapOnBulkReviewPageLoading()
        }
    }

    private fun mapOnBulkReviewPageLoading(): BulkReviewPageUiState {
        return BulkReviewPageUiState.Loading
    }

    private fun mapOnBulkReviewPageError(throwable: Throwable?): BulkReviewPageUiState {
        return BulkReviewPageUiState.Error(throwable)
    }

    private fun mapOnBulkReviewPageSuccess(
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        bulkReviewStickyButtonUiState: BulkReviewStickyButtonUiState
    ): BulkReviewPageUiState {
        return if (
            shouldSubmitReview &&
            submitBulkReviewRequestState is BulkReviewSubmitRequestState.Requesting
        ) {
            BulkReviewPageUiState.Submitting
        } else if (
            submitBulkReviewRequestState is BulkReviewSubmitRequestState.Complete.Success && submitBulkReviewRequestState.result.success
        ) {
            BulkReviewPageUiState.Submitted(userName = userSession.name)
        } else {
            BulkReviewPageUiState.Showing(
                items = bulkReviewVisitableList,
                stickyButtonUiState = bulkReviewStickyButtonUiState
            )
        }
    }
}
