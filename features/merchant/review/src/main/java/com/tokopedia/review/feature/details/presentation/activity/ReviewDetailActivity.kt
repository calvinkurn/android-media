package com.tokopedia.review.feature.details.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.details.presentation.fragment.ReviewDetailFragment

class ReviewDetailActivity : BaseSimpleActivity() {

    private var feedbackId: Int = 0
    private var reputationId: Int = 0

    override fun getNewFragment(): Fragment? {
        return ReviewDetailFragment.createNewInstance(feedbackId, reputationId)
    }
}