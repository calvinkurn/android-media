package com.tokopedia.creation.common.upload.di.uploader

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.creation.common.upload.uploader.activity.PlayShortsPostUploadActivity
import com.tokopedia.creation.common.upload.uploader.receiver.CreationUploadReceiver
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.videouploader.di.VideoUploaderModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Component(
    modules = [
        CreationUploaderModule::class,
        CreationUploaderInternalModule::class,
        VideoUploaderModule::class,
        MediaUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@CreationUploaderScope
interface CreationUploaderComponent {

    fun inject(receiver: CreationUploadReceiver)

    fun inject(activity: PlayShortsPostUploadActivity)
}
