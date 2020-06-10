package com.tokopedia.review.feature.inbox.history.di

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.inbox.history.presentation.fragment.ReviewHistoryFragment
import dagger.Component

@Component(modules = [ReviewHistoryViewModelModule::class], dependencies = [ReviewComponent::class])
@ReviewHistoryScope
interface ReviewHistoryComponent {
    fun inject(reviewHistoryFragment: ReviewHistoryFragment)
}