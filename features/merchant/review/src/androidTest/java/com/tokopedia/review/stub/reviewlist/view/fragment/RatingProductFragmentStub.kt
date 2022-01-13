package com.tokopedia.review.stub.reviewlist.view.fragment

import com.tokopedia.review.feature.reviewlist.di.component.ReviewProductListComponent
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub
import com.tokopedia.review.stub.reviewlist.di.component.DaggerReviewProductListComponentStub

class RatingProductFragmentStub : RatingProductFragment() {

    companion object {
        fun createInstance(): RatingProductFragment {
            return RatingProductFragmentStub()
        }
    }

    override fun getComponent(): ReviewProductListComponent? {
        return activity?.run {
            DaggerReviewProductListComponentStub
                .builder()
                .reviewComponentStub(ReviewInstanceStub.getComponent(application))
                .reviewProductListModule(ReviewProductListModule())
                .build()
        }
    }
}