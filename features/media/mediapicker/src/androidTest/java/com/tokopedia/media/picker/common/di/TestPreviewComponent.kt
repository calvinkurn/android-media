package com.tokopedia.media.picker.common.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.media.picker.common.di.module.TestPreviewModule
import com.tokopedia.media.preview.di.PreviewComponent
import com.tokopedia.media.preview.di.module.PreviewViewModelModule
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TestPreviewModule::class,
        PreviewViewModelModule::class
    ],
    dependencies = [
        TestBaseAppComponent::class
    ]
)
interface TestPreviewComponent : PreviewComponent {
    fun inject(activity: TestPreviewInterceptor)
}