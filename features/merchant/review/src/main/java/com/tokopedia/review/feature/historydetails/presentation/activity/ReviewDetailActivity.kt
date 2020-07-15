package com.tokopedia.review.feature.historydetails.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.historydetails.presentation.fragment.ReviewDetailFragment

class ReviewDetailActivity : BaseSimpleActivity() {

    private var feedbackId: Int = 0
    private var reputationId: Int = 0
    private val reviewDetailFragment = ReviewDetailFragment.createNewInstance(feedbackId, reputationId)

    override fun getNewFragment(): Fragment? {
        return reviewDetailFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewDetailFragment.onBackPressed()
    }
}