package com.tokopedia.reviewseller.feature.reviewlist.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.fragment.RatingProductFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewListActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = RatingProductFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_list
    }
}