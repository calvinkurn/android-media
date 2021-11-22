package com.tokopedia.mediauploader.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.MediaUploaderActivity
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.common.di.MediaUploaderNetworkModule
import com.tokopedia.mediauploader.common.di.NetworkModule
import com.tokopedia.mediauploader.services.UploaderWorker
import dagger.Component

@MediaUploaderTestScope
@Component(
    modules = [
        MediaUploaderTestModule::class,
        MediaUploaderModule::class,
        MediaUploaderNetworkModule::class,
        NetworkModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface MediaUploaderTestComponent {
    fun inject(activity: MediaUploaderActivity)
    fun inject(worker: UploaderWorker)
}