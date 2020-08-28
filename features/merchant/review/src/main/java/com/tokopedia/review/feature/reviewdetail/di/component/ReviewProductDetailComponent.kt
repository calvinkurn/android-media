package com.tokopedia.review.feature.reviewdetail.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.review.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.review.feature.reviewdetail.view.bottomsheet.PopularTopicsBottomSheet
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import dagger.Component

@ReviewDetailScope
@Component(modules = [ReviewProductDetailModule::class], dependencies = [ReviewComponent::class])
interface ReviewProductDetailComponent {
    fun inject(sellerReviewDetailFragment: SellerReviewDetailFragment)
    fun inject(popularTopicsBottomSheet: PopularTopicsBottomSheet)
}