package com.tokopedia.picker.fake.di

import com.tokopedia.picker.di.PickerComponent
import com.tokopedia.picker.di.module.PickerViewModelModule
import com.tokopedia.picker.di.scope.PickerScope
import com.tokopedia.picker.fake.di.common.TestBaseAppComponent
import com.tokopedia.picker.fake.di.module.TestPickerModule
import com.tokopedia.picker.ui.PickerTest
import dagger.Component

@PickerScope
@Component(
    modules = [
        PickerViewModelModule::class,
        TestPickerModule::class
    ],
    dependencies = [
        TestBaseAppComponent::class
    ]
)
interface TestPickerComponent : PickerComponent {
    fun inject(test: PickerTest)
}