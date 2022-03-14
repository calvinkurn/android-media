package com.tokopedia.media.preview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import dagger.Component

@PreviewScope
@Component(modules = [
    PickerModule::class,
], dependencies = [
    BaseAppComponent::class
])
interface PreviewComponent {
    fun inject(activity: PickerPreviewActivity)
}