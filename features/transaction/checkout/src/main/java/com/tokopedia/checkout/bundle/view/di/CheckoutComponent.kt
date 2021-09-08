package com.tokopedia.checkout.bundle.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.checkout.bundle.view.ShipmentFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@CheckoutScope
@Component(modules = [CheckoutModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutComponent {
    fun inject(shipmentFragment: ShipmentFragment)
}