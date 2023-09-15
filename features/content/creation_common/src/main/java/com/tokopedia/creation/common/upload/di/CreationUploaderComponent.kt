package com.tokopedia.creation.common.upload.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Component(
    modules = [
        CreationUploaderModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@CreationUploaderScope
interface CreationUploaderComponent {

    fun inject(worker: CreationUploaderWorker)
}
