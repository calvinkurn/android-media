package com.tokopedia.reviewseller.feature.reviewdetail.di.component

import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.reviewseller.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet.PopularTopicsBottomSheet
import com.tokopedia.reviewseller.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import dagger.Component

@ReviewDetailScope
@Component(modules = [ReviewProductDetailModule::class], dependencies = [ReviewSellerComponent::class])
interface ReviewProductDetailComponent {
    fun inject(sellerReviewDetailFragment: SellerReviewDetailFragment)
    fun inject(popularTopicsBottomSheet: PopularTopicsBottomSheet)
}