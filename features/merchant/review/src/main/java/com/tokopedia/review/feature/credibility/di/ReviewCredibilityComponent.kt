package com.tokopedia.review.feature.credibility.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.credibility.presentation.fragment.ReviewCredibilityBottomSheet
import dagger.Component

@Component(
    modules = [ReviewCredibilityModule::class, ReviewCredibilityViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
@ReviewCredibilityScope
interface ReviewCredibilityComponent {
    fun inject(reviewCredibilityBottomSheet: ReviewCredibilityBottomSheet)
}