package com.tokopedia.reviewseller.feature.reviewlist.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.feature.reviewlist.di.component.DaggerReviewProductListComponent
import com.tokopedia.reviewseller.feature.reviewlist.di.component.ReviewProductListComponent
import com.tokopedia.reviewseller.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.reviewseller.feature.reviewlist.view.fragment.ReviewSellerListFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewListActivity : BaseSimpleActivity(), HasComponent<ReviewProductListComponent> {

    override fun getNewFragment(): Fragment = ReviewSellerListFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_list
    }

    override fun getComponent(): ReviewProductListComponent {
        return DaggerReviewProductListComponent
                .builder()
                .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
                .reviewProductListModule(ReviewProductListModule())
                .build()
    }
}