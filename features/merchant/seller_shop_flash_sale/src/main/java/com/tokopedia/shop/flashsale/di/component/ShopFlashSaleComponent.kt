package com.tokopedia.shop.flashsale.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.flashsale.di.module.ShopFlashSaleModule
import com.tokopedia.shop.flashsale.di.module.ShopFlashSaleViewModelModule
import com.tokopedia.shop.flashsale.di.scope.ShopFlashSaleScope
import com.tokopedia.shop.flashsale.presentation.campaign_list.container.CampaignListActivity
import com.tokopedia.shop.flashsale.presentation.campaign_list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.campaign_list.list.CampaignListFragment
import com.tokopedia.shop.flashsale.presentation.creation.campaign_information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.campaign_information.CampaignInformationFragment
import com.tokopedia.shop.flashsale.presentation.creation.campaign_information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.shop.flashsale.presentation.draft.bottomsheet.DraftDeleteBottomSheet
import dagger.Component

@ShopFlashSaleScope
@Component(
    modules = [ShopFlashSaleModule::class, ShopFlashSaleViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShopFlashSaleComponent {
    fun inject(activity: CampaignListActivity)
    fun inject(fragment: CampaignListContainerFragment)
    fun inject(fragment: CampaignListFragment)

    fun inject(activity: CampaignInformationActivity)
    fun inject(fragment: CampaignInformationFragment)

    fun inject(bottomSheet: DraftDeleteBottomSheet)
    fun inject(bottomSheet: CampaignDatePickerBottomSheet)
}