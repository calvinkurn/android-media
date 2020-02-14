package com.tokopedia.reviewseller.feature.reviewlist.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.reviewseller.feature.reviewlist.view.fragment.SellerReviewListFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = SellerReviewListFragment()
}