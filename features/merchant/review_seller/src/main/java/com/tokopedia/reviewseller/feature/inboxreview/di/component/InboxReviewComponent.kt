package com.tokopedia.reviewseller.feature.inboxreview.di.component

import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.feature.inboxreview.di.module.InboxReviewModule
import com.tokopedia.reviewseller.feature.inboxreview.di.scope.InboxReviewScope
import com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment.InboxReviewFragment
import dagger.Component

@InboxReviewScope
@Component(modules = [InboxReviewModule::class], dependencies = [ReviewSellerComponent::class])
interface InboxReviewComponent {
    fun inject(inboxReviewFragment: InboxReviewFragment)
}