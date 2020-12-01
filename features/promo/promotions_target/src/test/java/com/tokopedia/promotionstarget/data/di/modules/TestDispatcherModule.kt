package com.tokopedia.promotionstarget.data.di.modules

import com.tokopedia.promotionstarget.data.di.IO
import com.tokopedia.promotionstarget.data.di.MAIN
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import javax.inject.Named

@Module
class TestDispatcherModule {

    @Provides
    @Named(MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = TestCoroutineDispatcher()

    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = TestCoroutineDispatcher()

}