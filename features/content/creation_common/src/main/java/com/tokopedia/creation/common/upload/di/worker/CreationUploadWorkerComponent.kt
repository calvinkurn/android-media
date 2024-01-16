package com.tokopedia.creation.common.upload.di.worker

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.upload.di.common.CreationUploadCommonModule
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
@Component(
    modules = [
        CreationUploadCommonModule::class,
        CreationUploadWorkerModule::class,
        MediaUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@CreationUploadWorkerScope
interface CreationUploadWorkerComponent {

    fun inject(worker: CreationUploaderWorker)
}
