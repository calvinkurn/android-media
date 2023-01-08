package com.tokopedia.media.picker.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.di.module.PickerAnalyticsModule
import com.tokopedia.media.picker.di.module.PickerFragmentModule
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.di.module.PickerViewModelModule
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    PickerModule::class,
    PickerFragmentModule::class,
    PickerAnalyticsModule::class,
    PickerViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface PickerComponent {
    fun inject(activity: PickerActivity)
    fun inject(activity: AlbumActivity)
}