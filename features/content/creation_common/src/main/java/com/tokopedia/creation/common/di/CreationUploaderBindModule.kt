package com.tokopedia.creation.common.di

import com.tokopedia.creation.common.data.repository.CreationUploadQueueRepositoryImpl
import com.tokopedia.creation.common.domain.repository.CreationUploadQueueRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */

@Module
abstract class CreationUploaderBindModule {

    @Binds
    abstract fun bindCreationUploadQueueRepository(repository: CreationUploadQueueRepositoryImpl): CreationUploadQueueRepository
}
