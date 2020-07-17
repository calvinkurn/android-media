package com.tokopedia.review.feature.historydetails.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.feature.historydetails.presentation.fragment.ReviewDetailFragment

class ReviewDetailActivity : BaseSimpleActivity() {

    private var feedbackId: Int = 0
    private var reviewDetailFragment: ReviewDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromApplink()
        reviewDetailFragment = ReviewDetailFragment.createNewInstance(feedbackId)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return reviewDetailFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewDetailFragment?.onBackPressed()
    }

    private fun getDataFromApplink() {
        val uri = intent.data ?: return
        feedbackId = uri.lastPathSegment.toIntOrZero()
    }
}