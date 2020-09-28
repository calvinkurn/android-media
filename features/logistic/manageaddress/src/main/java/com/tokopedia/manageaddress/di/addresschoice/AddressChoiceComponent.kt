package com.tokopedia.manageaddress.di.addresschoice

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@AddressChoiceScope
@Component(modules = [AddressChoiceModule::class], dependencies = [BaseAppComponent::class])
interface AddressChoiceComponent {
    fun inject(shipmentAddressListFragment: ShipmentAddressListFragment)
}