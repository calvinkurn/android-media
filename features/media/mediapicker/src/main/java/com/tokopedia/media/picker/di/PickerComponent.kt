package com.tokopedia.media.picker.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.di.module.*
import com.tokopedia.media.picker.ui.activity.album.AlbumActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import dagger.Component

@ActivityScope
@Component(modules = [
    PickerModule::class,
    PickerCommonModule::class,
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
