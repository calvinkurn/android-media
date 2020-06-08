package com.tokopedia.review.feature.inbox.common.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment

class ReviewInboxActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return ReviewPendingFragment.createNewInstance()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = ReviewInboxConstants.NO_SHADOW_ELEVATION
    }
}