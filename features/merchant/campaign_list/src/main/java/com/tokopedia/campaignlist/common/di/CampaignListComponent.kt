package com.tokopedia.campaignlist.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.campaignlist.page.presentation.activity.CampaignListActivity
import dagger.Component

@CampaignListScope
@Component(
    modules = [CampaignListModule::class, CampaignListViewModelModule::class], dependencies = [BaseAppComponent::class]
)
interface CampaignListComponent {

    fun inject(campaignListActivity: CampaignListActivity)
}
