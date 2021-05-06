package com.tokopedia.thankyou_native.recommendation.di.module

import com.tokopedia.thankyou_native.recommendation.di.qualifier.IODispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class DispatcherModule {

    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IODispatcher
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

}