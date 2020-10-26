package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.NavCommandProcessor
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactoryImpl
import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
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

    @MainNavScope
    @Provides
    fun provideMainNavCommandProcessor(dispatcher: NavDispatcherProvider) = NavCommandProcessor(dispatcher.io())

    @MainNavScope
    @Provides
    fun provideMainNavDataFactory(userSession: UserSessionInterface): MainNavDataFactory = MainNavDataFactoryImpl(userSession)

    @MainNavScope
    @Provides
    fun provideMainNavMapper(mainNavDataFactory: MainNavDataFactory) = MainNavMapper(mainNavDataFactory)

}