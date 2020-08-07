package com.tokopedia.reviewseller.feature.reviewlist.di.component

import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.reviewseller.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.reviewseller.feature.reviewlist.view.fragment.RatingProductFragment
import dagger.Component

@ReviewProductListScope
@Component(modules = [ReviewProductListModule::class], dependencies = [ReviewSellerComponent::class])
interface ReviewProductListComponent {
    fun inject(ratingProductFragment: RatingProductFragment)
}