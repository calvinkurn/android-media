package com.tokopedia.product.addedit.shipment.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import dagger.Component

@AddEditProductShipmentScope
@Component(modules = [AddEditProductShipmentModule::class], dependencies = [BaseAppComponent::class])
interface AddEditProductShipmentComponent {
    fun inject(fragment: AddEditProductShipmentFragment)
}