package com.tokopedia.dropoff.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.dropoff.ui.autocomplete.AutoCompleteFragment
import com.tokopedia.dropoff.ui.dropoff_picker.DropoffPickerActivity
import com.tokopedia.logisticCommon.data.module.CoreModule
import dagger.Component

@DropoffPickerScope
@Component(modules = [
    DropoffPickerViewModelsModule::class,
    CoreModule::class
], dependencies = [BaseAppComponent::class])
interface DropoffPickerComponent {
    fun inject(activity: DropoffPickerActivity)
    fun inject(fragment: AutoCompleteFragment)
}