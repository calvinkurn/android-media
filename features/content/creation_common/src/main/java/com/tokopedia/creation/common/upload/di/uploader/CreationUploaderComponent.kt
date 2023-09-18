package com.tokopedia.creation.common.upload.di.uploader

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.upload.activity.PlayShortsPostUploadActivity
import com.tokopedia.creation.common.upload.receiver.PlayShortsUploadReceiver
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Component(
    modules = [
        CreationUploaderModule::class,
        CreationUploaderInternalModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@CreationUploaderScope
interface CreationUploaderComponent {

    fun inject(receiver: PlayShortsUploadReceiver)

    fun inject(activity: PlayShortsPostUploadActivity)
}
