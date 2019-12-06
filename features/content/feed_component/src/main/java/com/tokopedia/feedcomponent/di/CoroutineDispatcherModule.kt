package com.tokopedia.feedcomponent.di

import com.tokopedia.feedcomponent.util.coroutine.CommonCoroutineDispatcherProvider
import com.tokopedia.feedcomponent.util.coroutine.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 2019-11-05
 */
@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcherModule(): CoroutineDispatcherProvider = CommonCoroutineDispatcherProvider
}