package com.tokopedia.creation.common.upload.di.uploader

import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalyticImpl
import com.tokopedia.creation.common.upload.di.common.CreationUploadDataModule
import com.tokopedia.creation.common.upload.di.common.CreationUploadServiceModule
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.creation.common.upload.uploader.CreationUploaderImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Module(
    includes = [
        CreationUploadDataModule::class,
        CreationUploadServiceModule::class,
    ]
)
abstract class CreationUploaderModule {

    @Binds
    @CreationUploaderScope
    abstract fun provideCreationUploader(creationUploader: CreationUploaderImpl): CreationUploader

    @Binds
    @CreationUploaderScope
    abstract fun providePlayShortsUploadAnalytic(analytic: PlayShortsUploadAnalyticImpl): PlayShortsUploadAnalytic
}
