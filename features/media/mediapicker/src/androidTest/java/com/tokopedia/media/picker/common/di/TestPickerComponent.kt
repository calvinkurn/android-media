package com.tokopedia.media.picker.common.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.media.picker.common.di.module.TestFragmentModule
import com.tokopedia.media.picker.common.di.module.TestPickerModule
import com.tokopedia.media.picker.di.PickerComponent
import com.tokopedia.media.picker.di.module.PickerViewModelModule
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TestPickerModule::class,
        TestFragmentModule::class,
        PickerViewModelModule::class,
    ],
    dependencies = [
        TestBaseAppComponent::class
    ]
)
interface TestPickerComponent : PickerComponent {
    fun inject(test: TestPickerInterceptor)
}