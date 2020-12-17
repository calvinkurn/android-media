package com.tokopedia.logisticaddaddress.di.shopeditaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.features.shopeditaddress.ShopEditAddressActivity
import com.tokopedia.logisticaddaddress.features.shopeditaddress.ShopEditAddressFragment
import dagger.Component

@ShopEditAddressScope
@Component(modules = [ShopEditAddressModule::class, ShopEditAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopEditAddressComponent{
    fun inject (shopEditAddressActivity: ShopEditAddressActivity)
    fun inject (shopEditAddressFragment: ShopEditAddressFragment)
}