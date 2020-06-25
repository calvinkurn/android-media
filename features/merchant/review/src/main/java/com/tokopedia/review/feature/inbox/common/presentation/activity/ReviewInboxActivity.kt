package com.tokopedia.review.feature.inbox.common.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.container.presentation.fragment.ReviewInboxContainerFragment

class ReviewInboxActivity : BaseSimpleActivity() {

    private val reviewInboxContainerFragment = ReviewInboxContainerFragment.createNewInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return reviewInboxContainerFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reviewInboxContainerFragment.onBackPressed()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = ReviewInboxConstants.NO_SHADOW_ELEVATION
    }
}