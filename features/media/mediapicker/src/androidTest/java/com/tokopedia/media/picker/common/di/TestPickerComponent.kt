package com.tokopedia.media.picker.common.di

import com.tokopedia.media.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.media.picker.common.di.module.TestPickerModule
import com.tokopedia.media.picker.di.PickerComponent
import com.tokopedia.media.picker.di.module.PickerViewModelModule
import com.tokopedia.media.picker.di.scope.PickerScope
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