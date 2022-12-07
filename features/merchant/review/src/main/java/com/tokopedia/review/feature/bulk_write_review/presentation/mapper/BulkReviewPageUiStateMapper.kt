package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.usecase.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import javax.inject.Inject

class BulkReviewPageUiStateMapper @Inject constructor() {
    fun map(
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        bulkReviewStickyButtonUiState: BulkReviewStickyButtonUiState,
        getFormRequestState: BulkReviewGetFormRequestState,
        getBadRatingCategoryRequestState: BulkReviewGetBadRatingCategoryRequestState
    ): BulkReviewPageUiState {
        return when (getFormRequestState) {
            is RequestState.Complete.Error -> mapOnBulkReviewPageError(getFormRequestState.throwable)
            is RequestState.Complete.Success -> mapOnGetFormSuccess(
                shouldSubmitReview,
                submitBulkReviewRequestState,
                bulkReviewVisitableList,
                bulkReviewStickyButtonUiState,
                getBadRatingCategoryRequestState
            )
            is RequestState.Requesting -> mapOnBulkReviewPageLoading()
        }
    }

    private fun mapOnGetFormSuccess(
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        bulkReviewStickyButtonUiState: BulkReviewStickyButtonUiState,
        badRatingCategoryRequestState: RequestState<BadRatingCategoriesResponse>
    ): BulkReviewPageUiState {
        return when (badRatingCategoryRequestState) {
            is RequestState.Complete.Error -> mapOnBulkReviewPageError(badRatingCategoryRequestState.throwable)
            is RequestState.Complete.Success -> mapOnBulkReviewPageSuccess(
                shouldSubmitReview,
                submitBulkReviewRequestState,
                bulkReviewVisitableList,
                bulkReviewStickyButtonUiState
            )
            is RequestState.Requesting -> mapOnBulkReviewPageLoading()
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
        return if (shouldSubmitReview && submitBulkReviewRequestState is RequestState.Requesting) {
            BulkReviewPageUiState.Submitting
        } else {
            BulkReviewPageUiState.Showing(
                items = bulkReviewVisitableList,
                stickyButtonUiState = bulkReviewStickyButtonUiState
            )
        }
    }
}
