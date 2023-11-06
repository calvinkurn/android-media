package com.tokopedia.creation.common.upload.di.uploader

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalytic
import com.tokopedia.creation.common.upload.analytic.PlayShortsUploadAnalyticImpl
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.data.repository.CreationUploadQueueRepositoryImpl
import com.tokopedia.creation.common.upload.di.CreationUploadDataModule
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.creation.common.upload.uploader.CreationUploaderImpl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.sync.Mutex

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Module(
    includes = [CreationUploadDataModule::class]
)
class CreationUploaderModule {

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideCreationUploader(
        workManager: WorkManager,
        creationUploadQueueRepository: CreationUploadQueueRepository,
    ): CreationUploader {
        return CreationUploaderImpl(
            workManager = workManager,
            creationUploadQueueRepository = creationUploadQueueRepository,
        )
    }

    @Provides
    fun providePlayShortsUploadAnalytic(userSession: UserSessionInterface): PlayShortsUploadAnalytic {
        return PlayShortsUploadAnalyticImpl(userSession)
    }
}
