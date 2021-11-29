package com.tokopedia.homecredit.di.module

import com.tokopedia.homecredit.di.qualifier.CoroutineMainDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class HomeCreditModule {
    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}