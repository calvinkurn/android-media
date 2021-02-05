package com.tokopedia.review.feature.inbox.common.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.inbox.common.di.module.InboxReputationViewModelModule
import com.tokopedia.review.feature.inbox.common.di.scope.InboxReputationScope
import com.tokopedia.review.feature.inbox.common.presentation.activity.InboxReputationActivity
import dagger.Component

@InboxReputationScope
@Component(modules = [InboxReputationViewModelModule::class], dependencies = [ReviewComponent::class])
interface InboxReputationComponent {
    fun inject(inboxReputationActivity: InboxReputationActivity)
}