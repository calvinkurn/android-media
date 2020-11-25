package com.tokopedia.review.feature.inboxreview.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.inboxreview.di.module.InboxReviewViewModelModule
import com.tokopedia.review.feature.inboxreview.di.scope.InboxReviewScope
import com.tokopedia.review.feature.inboxreview.presentation.fragment.InboxReviewFragment
import dagger.Component

@InboxReviewScope
@Component(modules = [InboxReviewViewModelModule::class], dependencies = [ReviewComponent::class])
interface InboxReviewComponent {
    fun inject(inboxReviewFragment: InboxReviewFragment)
}