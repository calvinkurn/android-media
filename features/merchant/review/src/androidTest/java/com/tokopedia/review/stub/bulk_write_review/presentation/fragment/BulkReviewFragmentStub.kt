package com.tokopedia.review.stub.bulk_write_review.presentation.fragment

import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.fragment.BulkReviewFragment
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.review.stub.bulk_write_review.di.component.DaggerBulkReviewComponentStub
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub

class BulkReviewFragmentStub : BulkReviewFragment() {
    override fun initInjector() {
        activity?.let {
            DaggerBulkReviewComponentStub.builder()
                .reviewComponentStub(ReviewInstanceStub.getComponent(it.application))
                .build()
                .inject(this)
        }
    }

    // Since UnifyButton loading state is blocking the instrumentation test, we don't show the
    // loading state on the test
    override suspend fun onBulkReviewPageSubmitting(
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        stickyButtonUiState: BulkReviewStickyButtonUiState
    ) {
        super.onBulkReviewPageSubmitting(
            bulkReviewVisitableList,
            if (stickyButtonUiState is BulkReviewStickyButtonUiState.Submitting) {
                BulkReviewStickyButtonUiState.Showing(
                    stickyButtonUiState.text,
                    stickyButtonUiState.anonymous
                )
            } else {
                stickyButtonUiState
            }
        )
    }
}
