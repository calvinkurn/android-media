package com.tokopedia.review.feature.bulk_write_review.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.bulk_write_review.presentation.fragment.BulkReviewFragment

open class BulkReviewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return BulkReviewFragment()
    }
}
