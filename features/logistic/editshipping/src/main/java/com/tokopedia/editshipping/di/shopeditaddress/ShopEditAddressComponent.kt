package com.tokopedia.editshipping.di.shopeditaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editshipping.ui.shopeditaddress.ShopEditAddressFragment
import dagger.Component

@ActivityScope
@Component(modules = [ShopEditAddressModule::class, ShopEditAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopEditAddressComponent {
    fun inject(shopEditAddressFragment: ShopEditAddressFragment)
}
