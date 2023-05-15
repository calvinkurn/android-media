package com.tokopedia.mediauploader.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.MediaUploaderActivity
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.services.UploaderReceiver
import com.tokopedia.mediauploader.services.UploaderWorker
import dagger.Component

@MediaUploaderTestScope
@Component(
    modules = [
        MediaUploaderModule::class,
        MediaUploaderDebugModule::class,
        MediaUploaderDebugViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface MediaUploaderTestComponent {
    fun inject(activity: MediaUploaderActivity)
    fun inject(broadcaster: UploaderReceiver)
    fun inject(worker: UploaderWorker)
}
