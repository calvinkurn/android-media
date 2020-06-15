package com.tokopedia.reviewseller.feature.reviewdetail.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.DaggerReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.reviewseller.feature.reviewdetail.view.fragment.SellerReviewDetailFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailActivity : BaseSimpleActivity(), HasComponent<ReviewProductDetailComponent> {

    override fun getNewFragment(): Fragment = SellerReviewDetailFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_detail
    }

    override fun getComponent(): ReviewProductDetailComponent {
        return DaggerReviewProductDetailComponent
                .builder()
                .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
                .reviewProductDetailModule(ReviewProductDetailModule())
                .build()
    }


}