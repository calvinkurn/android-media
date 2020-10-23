package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import dagger.Module
import dagger.Provides

@Module
class MainNavModule {

    @MainNavScope
    @Provides
    fun provideMainNavDispatcher(): NavDispatcherProvider {
        return MainNavDispatcherProviderImpl()
    }
}