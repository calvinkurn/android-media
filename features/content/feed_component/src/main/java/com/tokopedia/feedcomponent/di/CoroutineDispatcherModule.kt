package com.tokopedia.feedcomponent.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 2019-11-05
 */
@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcherModule(): CoroutineDispatchers = CoroutineDispatchersProvider
}