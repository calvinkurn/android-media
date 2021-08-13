package com.tokopedia.product.addedit.shipment.di

import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import dagger.Component

@AddEditProductShipmentScope
@Component(modules = [AddEditProductShipmentModule::class, AddEditProductShipmentViewModelModule::class],
        dependencies = [AddEditProductComponent::class])
interface AddEditProductShipmentComponent {
    fun inject(fragment: AddEditProductShipmentFragment)
}