package com.tokopedia.review.feature.reviewdetail.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reviewdetail.di.component.DaggerReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailActivity : BaseSimpleActivity(), HasComponent<ReviewProductDetailComponent> {

    override fun getNewFragment(): Fragment = SellerReviewDetailFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_seller_review_detail
    }

    override fun getParentViewResourceID(): Int {
        return R.id.seller_review_detail_parent_view
    }

    override fun getComponent(): ReviewProductDetailComponent {
        return DaggerReviewProductDetailComponent
                .builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .reviewProductDetailModule(ReviewProductDetailModule())
                .build()
    }


}