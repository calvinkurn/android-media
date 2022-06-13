package com.tokopedia.review.stub.reviewdetail.view.activity

import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub
import com.tokopedia.review.stub.reviewdetail.di.component.DaggerReviewProductDetailComponentStub

class SellerReviewDetailActivityStub : SellerReviewDetailActivity() {
    override fun getComponent(): ReviewProductDetailComponent {
        return DaggerReviewProductDetailComponentStub
            .builder()
            .reviewComponentStub(ReviewInstanceStub.getComponent(application))
            .reviewProductDetailModule(ReviewProductDetailModule())
            .build()
    }
}