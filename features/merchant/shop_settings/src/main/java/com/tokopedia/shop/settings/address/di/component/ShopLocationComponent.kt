package com.tokopedia.shop.settings.address.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.settings.address.di.scope.ShopLocationScope
import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment
import com.tokopedia.shop.settings.address.view.ShopSettingAddressFragment
import dagger.Component

@ShopLocationScope
@Component(dependencies = arrayOf(BaseAppComponent::class))
interface ShopLocationComponent{
    fun inject(fragment: ShopSettingAddressFragment)
    fun inject(fragment: ShopSettingAddressAddEditFragment)
}