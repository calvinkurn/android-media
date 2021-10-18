package com.tokopedia.imagepicker_insta.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DispatcherModule {
    @Provides
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

}