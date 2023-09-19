package com.tokopedia.creation.common.upload.di.observer

import com.tokopedia.creation.common.upload.di.CreationUploadServiceModule
import com.tokopedia.creation.common.upload.observer.CreationUploadObserver
import com.tokopedia.creation.common.upload.observer.CreationUploadObserverImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 19, 2023
 */
@Module(
    includes = [
        CreationUploadServiceModule::class
    ]
)
abstract class CreationUploadObserverModule {

    @Binds
    abstract fun bindCreationUploadObserver(observer: CreationUploadObserverImpl): CreationUploadObserver
}
