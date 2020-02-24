package com.tokopedia.reviewseller.feature.reviewdetail.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.reviewseller.feature.reviewdetail.view.fragment.SellerReviewDetailFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = SellerReviewDetailFragment()

}