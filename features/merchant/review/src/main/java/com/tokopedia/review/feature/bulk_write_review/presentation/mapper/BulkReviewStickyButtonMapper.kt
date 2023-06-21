package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import javax.inject.Inject

class BulkReviewStickyButtonMapper @Inject constructor() {
    fun map(
        shouldSubmitReview: Boolean,
        submitBulkReviewRequestState: BulkReviewSubmitRequestState,
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        anonymous: Boolean
    ): BulkReviewStickyButtonUiState {
        return if (
            shouldSubmitReview &&
            submitBulkReviewRequestState is BulkReviewSubmitRequestState.Requesting
        ) {
            BulkReviewStickyButtonUiState.Submitting(
                text = StringRes(
                    R.string.btn_bulk_review_send,
                    listOf(bulkReviewVisitableList.count { it is BulkReviewItemUiModel }),
                ),
                anonymous = anonymous
            )
        } else {
            BulkReviewStickyButtonUiState.Showing(
                text = StringRes(
                    R.string.btn_bulk_review_send,
                    listOf(bulkReviewVisitableList.count { it is BulkReviewItemUiModel }),
                ),
                anonymous = anonymous
            )
        }
    }
}
