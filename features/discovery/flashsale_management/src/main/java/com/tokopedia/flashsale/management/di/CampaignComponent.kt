package com.tokopedia.flashsale.management.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.flashsale.management.view.fragment.BaseCampaignFragment
import dagger.Component

@CampaignScope
@Component(modules = [CampaignModule::class], dependencies = [BaseAppComponent::class])
interface CampaignComponent {
    fun inject(fragment: BaseCampaignFragment)
}
