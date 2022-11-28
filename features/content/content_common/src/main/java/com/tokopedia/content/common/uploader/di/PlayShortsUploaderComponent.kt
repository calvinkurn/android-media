package com.tokopedia.content.common.uploader.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.uploader.worker.PlayShortsUploadWorker
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
@Component(
    modules = [
        PlayShortsWorkerModule::class,
        MediaUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@PlayShortsUploaderScope
interface PlayShortsUploaderComponent {

    fun inject(worker: PlayShortsUploadWorker)
}
