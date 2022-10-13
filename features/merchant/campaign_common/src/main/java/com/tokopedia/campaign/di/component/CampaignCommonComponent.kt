package com.tokopedia.campaign.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.campaign.di.module.CampaignCommonModule
import com.tokopedia.campaign.di.module.CampaignCommonViewModelModule
import com.tokopedia.campaign.di.scope.CampaignCommonScope
import dagger.Component

@CampaignCommonScope
@Component(
    modules = [
        CampaignCommonModule::class,
        CampaignCommonViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CampaignCommonComponent {
    fun inject(bottomSheet: ProductBulkApplyBottomSheet)
}
