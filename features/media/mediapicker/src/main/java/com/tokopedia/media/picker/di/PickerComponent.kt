package com.tokopedia.media.picker.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.di.module.PickerViewModelModule
import com.tokopedia.media.picker.di.scope.PickerScope
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.main.PickerActivity
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import dagger.Component

@PickerScope
@Component(modules = [
    PickerModule::class,
    PickerViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface PickerComponent {
    fun inject(activity: PickerActivity)
    fun inject(activity: AlbumActivity)
    fun inject(fragment: CameraFragment)
    fun inject(fragment: GalleryFragment)
}