package com.tokopedia.creation.common.upload.di.worker

import com.tokopedia.creation.common.upload.di.CreationUploadDataModule
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
@Module(
    includes = [CreationUploadDataModule::class]
)
class CreationUploadWorkerModule {
}
