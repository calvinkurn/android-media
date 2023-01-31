package com.tokopedia.review.stub.bulk_write_review.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.review.feature.bulk_write_review.presentation.activity.BulkReviewActivity
import com.tokopedia.review.stub.bulk_write_review.presentation.fragment.BulkReviewFragmentStub

class BulkReviewActivityStub: BulkReviewActivity() {
    override fun getNewFragment(): Fragment? {
        return BulkReviewFragmentStub()
    }
}
