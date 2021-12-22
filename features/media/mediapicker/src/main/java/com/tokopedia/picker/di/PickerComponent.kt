package com.tokopedia.picker.di

import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.di.module.PickerViewModelModule
import com.tokopedia.picker.di.scope.PickerScope
import com.tokopedia.picker.ui.activity.album.AlbumActivity
import com.tokopedia.picker.ui.fragment.gallery.GalleryFragment
import dagger.Component

@PickerScope
@Component(modules = [
    PickerModule::class,
    PickerViewModelModule::class
])
interface PickerComponent {
    fun inject(activity: AlbumActivity)
    fun inject(fragment: GalleryFragment)
}