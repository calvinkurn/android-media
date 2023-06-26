package com.tokopedia.shop.home.di.component

import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.shop.campaign.view.fragment.ShopPageCampaignFragment
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeBindModule
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeFlashSaleTncBottomSheet
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeNplCampaignTncBottomSheet
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageHomeScope
@Component(modules = [
    ShopPageHomeModule::class,
    ShopPageHomeBindModule::class,
    ContentCoachMarkSharedPrefModule::class,
 ], dependencies = [ShopComponent::class])
interface ShopPageHomeComponent {
    fun inject(fragment: ShopPageHomeFragment?)
    fun inject(fragment: ShopPageCampaignFragment?)
    fun inject(bottomSheet: ShopHomeNplCampaignTncBottomSheet?)
    fun inject(bottomSheet: ShopHomeFlashSaleTncBottomSheet?)
}
