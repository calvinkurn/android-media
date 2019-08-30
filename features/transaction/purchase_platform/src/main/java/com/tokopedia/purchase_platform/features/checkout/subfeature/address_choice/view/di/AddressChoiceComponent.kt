package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.ShipmentAddressListFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@AddressChoiceScope
@Component(modules = [AddressChoiceModule::class], dependencies = [BaseAppComponent::class])
interface AddressChoiceComponent {
    fun inject(shipmentAddressListFragment: ShipmentAddressListFragment)
}