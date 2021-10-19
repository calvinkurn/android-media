package com.tokopedia.review.feature.credibility.di

import com.tokopedia.review.common.di.ReviewInboxComponent
import com.tokopedia.review.feature.credibility.presentation.fragment.ReviewCredibilityBottomSheet
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingScope
import dagger.Component

@Component(modules = [ReviewCredibilityViewModelModule::class], dependencies = [ReviewInboxComponent::class])
@ReviewPendingScope
interface ReviewCredibilityComponent {
    fun inject(reviewCredibilityBottomSheet: ReviewCredibilityBottomSheet)
}