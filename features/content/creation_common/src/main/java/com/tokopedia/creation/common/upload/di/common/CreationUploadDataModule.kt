package com.tokopedia.creation.common.upload.di.common

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.data.repository.CreationUploadQueueRepositoryImpl
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.util.CreationUploadMutex
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.sync.Mutex

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
@Module
class CreationUploadDataModule {

    @Provides
    fun provideCreationUploadQueueDatabase(@ApplicationContext context: Context): CreationUploadQueueDatabase {
        return CreationUploadQueueDatabase.getInstance(context)
    }

    @Provides
    fun provideMutex(): Mutex {
        return CreationUploadMutex.get()
    }

    @Provides
    fun provideCreationUploadQueueRepository(
        dispatchers: CoroutineDispatchers,
        mutex: Mutex,
        gson: Gson,
        creationUploadQueueDatabase: CreationUploadQueueDatabase,
    ): CreationUploadQueueRepository {
        return CreationUploadQueueRepositoryImpl(
            dispatchers = dispatchers,
            mutex = mutex,
            gson = gson,
            creationUploadQueueDatabase = creationUploadQueueDatabase,
        )
    }
}
