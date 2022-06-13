package com.tokopedia.review.stub.inbox.presentation.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.review.feature.inbox.presentation.InboxReputationActivity
import com.tokopedia.review.stub.reviewlist.view.fragment.RatingProductFragmentStub

class InboxReputationActivityStub: InboxReputationActivity() {

    companion object {
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, InboxReputationActivityStub::class.java)
        }
    }

    override fun createRatingProductFragment() {
        reviewSellerFragment = RatingProductFragmentStub.createInstance()
    }
}