package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.manageaddress.ui.ManageAddressActivity
import com.tokopedia.manageaddress.ui.ManageAddressFragment
import dagger.Component

@ManageAddressScope
@Component(modules = [ManageAddressModule::class, ManageAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ManageAddressComponent {
    fun inject(manageAddressActivity: ManageAddressActivity)
    fun inject(manageAddressFragment: ManageAddressFragment)
}