package com.tokopedia.review.feature.inbox.pending.di

import com.tokopedia.review.common.di.ReviewInboxComponent
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment
import dagger.Component

@Component(modules = [ReviewPendingViewModelModule::class], dependencies = [ReviewInboxComponent::class])
@ReviewPendingScope
interface ReviewPendingComponent {
    fun inject(reviewPendingFragment: ReviewPendingFragment)
}