package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [
    MainNavUseCaseModule::class
])
class MainNavModule {

    @MainNavScope
    @Provides
    fun provideMainNavDispatcher(): NavDispatcherProvider {
        return MainNavDispatcherProviderImpl()
    }

}