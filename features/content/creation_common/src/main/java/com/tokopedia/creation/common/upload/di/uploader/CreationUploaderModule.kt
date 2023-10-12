package com.tokopedia.creation.common.upload.di.uploader

import android.content.Context
import androidx.work.WorkManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalyticImpl
import com.tokopedia.creation.common.upload.di.common.CreationUploadDataModule
import com.tokopedia.creation.common.upload.di.common.CreationUploadServiceModule
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.creation.common.upload.uploader.CreationUploaderImpl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Module(
    includes = [
        CreationUploadDataModule::class,
        CreationUploadServiceModule::class,
    ]
)
class CreationUploaderModule {

    @Provides
    @CreationUploaderScope
    fun provideCreationUploader(
        @ApplicationContext appContext: Context,
        workManager: WorkManager,
        creationUploadQueueRepository: CreationUploadQueueRepository,
        gson: Gson,
    ): CreationUploader {
        return CreationUploaderImpl(
            appContext = appContext,
            workManager = workManager,
            creationUploadQueueRepository = creationUploadQueueRepository,
            gson = gson,
        )
    }

    @Provides
    @CreationUploaderScope
    fun providePlayShortsUploadAnalytic(userSession: UserSessionInterface): PlayShortsUploadAnalytic {
        return PlayShortsUploadAnalyticImpl(userSession)
    }
}
