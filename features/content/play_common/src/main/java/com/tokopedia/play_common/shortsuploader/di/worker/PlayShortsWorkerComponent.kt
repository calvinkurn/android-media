package com.tokopedia.play_common.shortsuploader.di.worker

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.play_common.shortsuploader.worker.PlayShortsUploadWorker
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
@PlayShortsWorkerScope
interface PlayShortsWorkerComponent {

    fun inject(worker: PlayShortsUploadWorker)
}
