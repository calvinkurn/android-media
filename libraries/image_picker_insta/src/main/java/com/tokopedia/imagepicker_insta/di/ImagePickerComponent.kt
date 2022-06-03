package com.tokopedia.imagepicker_insta.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.imagepicker_insta.di.module.DispatcherModule
import com.tokopedia.imagepicker_insta.di.module.ImagePickerModule
import com.tokopedia.imagepicker_insta.fragment.ImagePickerInstaMainFragment
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import dagger.Component
import javax.inject.Scope

@ImagePickerScope
@Component(modules = [DispatcherModule::class, ImagePickerModule::class], dependencies = [BaseAppComponent::class])
interface ImagePickerComponent {
    fun inject(viewModel: PickerViewModel)

    fun inject(fragment: ImagePickerInstaMainFragment)
}

@Scope
@Retention
annotation class ImagePickerScope