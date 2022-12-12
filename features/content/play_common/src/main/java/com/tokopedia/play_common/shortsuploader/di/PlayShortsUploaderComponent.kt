package com.tokopedia.play_common.shortsuploader.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.play_common.shortsuploader.worker.PlayShortsUploadWorker
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.play_common.shortsuploader.receiver.PlayShortsUploadReceiver
import dagger.Component

/**
 * Created By : Jonathan Darwin on December 09, 2022
 */
@Component(
    modules = [
        PlayShortsUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@PlayShortsUploaderScope
interface PlayShortsUploaderComponent {

    fun inject(receiver: PlayShortsUploadReceiver)
}
