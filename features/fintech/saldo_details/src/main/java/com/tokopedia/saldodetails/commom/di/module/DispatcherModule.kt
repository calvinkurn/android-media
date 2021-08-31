package com.tokopedia.saldodetails.commom.di.module

import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class DispatcherModule {

    @SaldoDetailsScope
    @Provides
    @Named(MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SaldoDetailsScope
    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

    companion object {
        const val MAIN = "MAIN"
        const val IO = "IO"
    }

}