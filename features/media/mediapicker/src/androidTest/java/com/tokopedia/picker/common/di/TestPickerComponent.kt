package com.tokopedia.picker.common.di

import com.tokopedia.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.picker.common.di.module.TestPickerModule
import com.tokopedia.picker.di.PickerComponent
import com.tokopedia.picker.di.module.PickerViewModelModule
import com.tokopedia.picker.di.scope.PickerScope
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
    fun inject(test: TestPickerInterceptor)
}