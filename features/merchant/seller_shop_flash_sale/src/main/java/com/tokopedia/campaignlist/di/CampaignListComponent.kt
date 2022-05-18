package com.tokopedia.campaignlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@CampaignListScope
@Component(modules = [CampaignListModule::class], dependencies = [BaseAppComponent::class])
interface CampaignListComponent {
}