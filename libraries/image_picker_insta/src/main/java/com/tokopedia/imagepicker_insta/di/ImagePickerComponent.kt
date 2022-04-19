package com.tokopedia.imagepicker_insta.di

import com.tokopedia.imagepicker_insta.di.module.DispatcherModule
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import dagger.Component
import javax.inject.Scope

@ImagePickerScope
@Component(modules = [DispatcherModule::class])
interface ImagePickerComponent {
    fun inject(viewModel: PickerViewModel)
}

@Scope
@Retention
annotation class ImagePickerScope