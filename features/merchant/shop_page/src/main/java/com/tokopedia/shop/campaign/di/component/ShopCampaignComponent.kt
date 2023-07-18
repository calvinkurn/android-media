package com.tokopedia.shop.campaign.di.component

import com.tokopedia.shop.campaign.di.module.ShopCampaignModule
import com.tokopedia.shop.campaign.di.scope.ShopCampaignScope
import com.tokopedia.shop.campaign.view.bottomsheet.ExclusiveLaunchVoucherListBottomSheet
import com.tokopedia.shop.campaign.view.bottomsheet.VoucherDetailBottomSheet
import com.tokopedia.shop.common.di.component.ShopComponent
import dagger.Component

@ShopCampaignScope
@Component(
    modules = [ShopCampaignModule::class],
    dependencies = [ShopComponent::class]
)
interface ShopCampaignComponent {
    fun inject(exclusiveLaunchVoucherListBottomSheet: ExclusiveLaunchVoucherListBottomSheet?)
    fun inject(voucherDetailBottomSheet: VoucherDetailBottomSheet?)
}
