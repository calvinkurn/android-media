package com.tokopedia.review.feature.inbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.inbox.buyerreview.di.ReputationModule
import com.tokopedia.review.feature.inbox.buyerreview.di.ReputationScope
import com.tokopedia.review.feature.inbox.presentation.InboxReputationActivity
import dagger.Component

@Component(modules = [ReputationModule::class], dependencies = [BaseAppComponent::class])
@ReputationScope
interface InboxReputationComponent {
    fun inject(inboxReputationActivity: InboxReputationActivity)
}