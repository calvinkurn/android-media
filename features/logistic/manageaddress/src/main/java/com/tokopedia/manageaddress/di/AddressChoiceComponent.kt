package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.di.module.AddressChoiceModule
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@ActivityScope
@Component(modules = [AddressChoiceModule::class], dependencies = [BaseAppComponent::class])
interface AddressChoiceComponent {
    fun inject(shipmentAddressListFragment: ShipmentAddressListFragment)
}