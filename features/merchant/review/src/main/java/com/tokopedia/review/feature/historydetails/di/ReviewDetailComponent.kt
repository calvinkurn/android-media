package com.tokopedia.review.feature.historydetails.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.historydetails.presentation.fragment.ReviewDetailFragment
import dagger.Component

@Component(modules = [ReviewDetailViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewDetailScope
interface ReviewDetailComponent {
    fun inject(reviewDetailFragment: ReviewDetailFragment)
}