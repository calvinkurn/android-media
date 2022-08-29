package com.tokopedia.campaign.components.bottomsheet.bulkapply.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.campaign.components.bottomsheet.bulkapply.view.ProductBulkApplyBottomSheet
import com.tokopedia.campaign.components.bottomsheet.bulkapply.di.module.CampaignManageProductBulkApplyBottomSheetModule
import com.tokopedia.campaign.components.bottomsheet.bulkapply.di.module.CampaignManageProductBulkApplyBottomSheetViewModelModule
import com.tokopedia.campaign.components.bottomsheet.bulkapply.di.scope.CampaignManageProductBulkApplyBottomSheetScope
import dagger.Component

@CampaignManageProductBulkApplyBottomSheetScope
@Component(
    modules = [
        CampaignManageProductBulkApplyBottomSheetModule::class,
        CampaignManageProductBulkApplyBottomSheetViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CampaignManageProductBulkApplyBottomSheetComponent {
    fun inject(bottomSheet: ProductBulkApplyBottomSheet)
}