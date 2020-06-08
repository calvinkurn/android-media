package com.tokopedia.review.feature.inbox.common.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment

class ReviewInboxActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return ReviewPendingFragment.createNewInstance()
    }
}