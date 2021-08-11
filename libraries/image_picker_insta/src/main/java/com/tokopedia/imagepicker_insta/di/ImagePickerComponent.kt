package com.tokopedia.imagepicker_insta.di

import com.tokopedia.imagepicker_insta.di.module.AppModule
import com.tokopedia.imagepicker_insta.di.module.DispatcherModule
import com.tokopedia.imagepicker_insta.di.module.ViewModelModule
import com.tokopedia.imagepicker_insta.fragment.MainFragment
import dagger.Component
import javax.inject.Scope

@ImagePickerScope
@Component(modules = [AppModule::class,ViewModelModule::class, DispatcherModule::class])
interface ImagePickerComponent {
    fun inject(fragment: MainFragment)
}

@Scope
@Retention
annotation class ImagePickerScope