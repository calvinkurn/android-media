package com.tokopedia.manageaddress.di.shoplocation

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationActivity
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationFragment
import com.tokopedia.shop.settings.address.view.ShopSettingAddressAddEditFragment
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.ShopSettingAddressFragment
import dagger.Component

@ShopLocationScope
@Component(modules = [ShopLocationModule::class, ShopLocationViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopLocationComponent {
    fun inject(shopLocationActivity: ShopLocationActivity)
    fun inject(shopLocationFragment: ShopLocationFragment)
    fun inject(fragment: ShopSettingAddressFragment)
    fun inject(fragment: ShopSettingAddressAddEditFragment)
}