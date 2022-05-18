package com.tokopedia.review.stub.reviewlist.view.fragment

import android.os.Bundle
import com.tokopedia.review.common.util.ReviewConstants
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs!!.edit()
            .putBoolean(ReviewConstants.HAS_FILTER_AND_SORT, true)
            .putBoolean(ReviewConstants.HAS_OVERALL_RATING_PRODUCT, true)
            .putBoolean(ReviewConstants.HAS_TAB_RATING_PRODUCT, true)
            .commit()
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