package com.tokopedia.checkout.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.view.ShipmentFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@ActivityScope
@Component(modules = [CheckoutModule::class, CheckoutViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutComponent {
    fun inject(shipmentFragment: ShipmentFragment)
}
