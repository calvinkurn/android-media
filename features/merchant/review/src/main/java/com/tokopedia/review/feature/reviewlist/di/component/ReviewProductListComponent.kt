package com.tokopedia.review.feature.reviewlist.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingViewModelModule
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListModule
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListViewModelModule
import com.tokopedia.review.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import dagger.Component

@ReviewProductListScope
@Component(modules = [ReviewProductListModule::class, ReviewProductListViewModelModule::class], dependencies = [ReviewComponent::class])
interface ReviewProductListComponent {
    fun inject(ratingProductFragment: RatingProductFragment)
}