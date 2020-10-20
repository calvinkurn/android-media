package com.tokopedia.review.feature.inbox.container.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.inbox.container.presentation.fragment.ReviewInboxContainerFragment
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingScope
import dagger.Component

@Component(modules = [ReviewInboxContainerViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewPendingScope
interface ReviewInboxContainerComponent {
    fun inject(reviewInboxContainerFragment: ReviewInboxContainerFragment)
}