package com.tokopedia.shop.setting.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.setting.di.module.ShopPageSettingModule
import com.tokopedia.shop.setting.di.scope.ShopPageSettingScope
import com.tokopedia.shop.setting.view.fragment.ShopPageSettingFragment
import dagger.Component

@ShopPageSettingScope
@Component(dependencies = [ShopComponent::class], modules = [ShopPageSettingModule::class])
interface ShopPageSettingComponent {
    fun inject(fragment: ShopPageSettingFragment)
}