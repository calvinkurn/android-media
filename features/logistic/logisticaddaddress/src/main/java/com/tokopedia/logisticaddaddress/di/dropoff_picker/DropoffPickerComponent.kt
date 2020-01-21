package com.tokopedia.logisticaddaddress.di.dropoff_picker

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.di.CoreModule
import com.tokopedia.logisticaddaddress.features.autocomplete.AutoCompleteFragment
import com.tokopedia.logisticaddaddress.features.dropoff_picker.DropoffPickerActivity
import dagger.Component

@DropoffPickerScope
@Component(modules = [
    DropoffPickerViewModelsModule::class,
    DropoffPickerGraphqlModule::class,
    CoreModule::class
], dependencies = [BaseAppComponent::class])
interface DropoffPickerComponent {
    fun inject(activity: DropoffPickerActivity)

    fun inject(fragment: AutoCompleteFragment)
}