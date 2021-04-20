package com.tokopedia.editshipping.di.shopeditaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.editshipping.ui.shopeditaddress.ShopEditAddressActivity
import com.tokopedia.editshipping.ui.shopeditaddress.ShopEditAddressFragment
import dagger.Component

@ShopEditAddressScope
@Component(modules = [ShopEditAddressModule::class, ShopEditAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopEditAddressComponent{
    fun inject (shopEditAddressFragment: ShopEditAddressFragment)
}