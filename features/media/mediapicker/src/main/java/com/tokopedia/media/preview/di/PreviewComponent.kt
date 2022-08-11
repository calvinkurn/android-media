package com.tokopedia.media.preview.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.preview.di.module.PreviewModule
import com.tokopedia.media.preview.di.module.PreviewViewModelModule
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import dagger.Component

@ActivityScope
@Component(modules = [
    PreviewModule::class,
    PreviewViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface PreviewComponent {
    fun inject(activity: PickerPreviewActivity)
}