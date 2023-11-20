package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.di.module.ManageAddressModule
import com.tokopedia.manageaddress.di.module.ManageAddressViewModelModule
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationActivity
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationFragment
import dagger.Component

@ActivityScope
@Component(modules = [ManageAddressModule::class, ManageAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopLocationComponent {
    fun inject(shopLocationActivity: ShopLocationActivity)
    fun inject(shopLocationFragment: ShopLocationFragment)
}
