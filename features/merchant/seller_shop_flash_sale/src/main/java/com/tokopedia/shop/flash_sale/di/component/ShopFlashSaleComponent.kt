package com.tokopedia.shop.flash_sale.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.flash_sale.di.module.ShopFlashSaleModule
import com.tokopedia.shop.flash_sale.di.module.ShopFlashSaleViewModelModule
import com.tokopedia.shop.flash_sale.di.scope.ShopFlashSaleScope
import com.tokopedia.shop.flash_sale.presentation.campaign_list.container.CampaignListActivity
import com.tokopedia.shop.flash_sale.presentation.campaign_list.container.CampaignListContainerFragment
import com.tokopedia.shop.flash_sale.presentation.campaign_list.list.CampaignListFragment
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.CampaignInformationActivity
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.CampaignInformationFragment
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_rule.bottomsheet.MerchantCampaignTNCBottomSheet
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
    fun inject(fragment: MerchantCampaignTNCBottomSheet)

    fun inject(activity: CampaignInformationActivity)
    fun inject(fragment: CampaignInformationFragment)

}