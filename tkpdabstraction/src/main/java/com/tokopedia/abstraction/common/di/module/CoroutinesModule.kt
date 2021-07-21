package com.tokopedia.abstraction.common.di.module

import com.tokopedia.graphql.di.DefaultDispatcher
import com.tokopedia.graphql.di.IoDispatcher
import com.tokopedia.graphql.di.MainDispatcher
import com.tokopedia.graphql.di.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CoroutinesModule {

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @MainImmediateDispatcher
    fun provideMainImmediateDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }

}