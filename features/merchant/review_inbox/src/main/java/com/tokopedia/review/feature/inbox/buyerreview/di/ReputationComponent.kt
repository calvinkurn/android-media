package com.tokopedia.review.feature.inbox.buyerreview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationDetailFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFilterFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationReportFragment
import dagger.Component

/**
 * @author by nisie on 8/11/17.
 */
@ReputationScope
@Component(modules = [ReputationModule::class, ReputationViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ReputationComponent {
    fun inject(inboxReputationFragment: InboxReputationFragment)
    fun inject(inboxReputationDetailFragment: InboxReputationDetailFragment)
    fun inject(inboxReputationReportFragment: InboxReputationReportFragment)
    fun inject(inboxReputationFilterFragment: InboxReputationFilterFragment)
}