package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.di.module.ManageAddressModule
import com.tokopedia.manageaddress.di.module.ManageAddressViewModelModule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressFragment
import dagger.Component

@ActivityScope
@Component(modules = [ManageAddressModule::class, ManageAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ManageAddressComponent {
    fun inject(manageAddressActivity: ManageAddressActivity)
    fun inject(manageAddressFragment: ManageAddressFragment)
}