package com.tokopedia.tokomember.di

import com.tokopedia.tokomember.util.IO
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class TokomemberDispatcherModule {
    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

}