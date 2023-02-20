package com.tokopedia.review.stub.bulk_write_review.presentation.fragment

import com.tokopedia.review.feature.bulk_write_review.presentation.fragment.BulkReviewFragment
import com.tokopedia.review.stub.bulk_write_review.di.component.DaggerBulkReviewComponentStub
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub

class BulkReviewFragmentStub: BulkReviewFragment() {
    override fun initInjector() {
        activity?.let {
            DaggerBulkReviewComponentStub.builder()
                .reviewComponentStub(ReviewInstanceStub.getComponent(it.application))
                .build()
                .inject(this)
        }
    }
}
