package com.tokopedia.mvcwidget.di.module

import com.tokopedia.mvcwidget.IO
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class DispatcherModule {
    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

}