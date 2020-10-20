package com.tokopedia.homenav.mainnav.di

import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.mainnav.data.factory.MainNavFactory
import com.tokopedia.homenav.mainnav.data.factory.MainNavFactoryImpl
import com.tokopedia.homenav.mainnav.data.repository.MainNavRepo
import com.tokopedia.homenav.mainnav.data.repository.MainNavRepositoryImpl
import com.tokopedia.homenav.mainnav.data.source.MainNavRemoteDataSource
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [
    MainNavUseCaseModule::class,
    MainNavMapperModule::class,
    MainNavDataSourceModule::class
])
class MainNavModule {

    @MainNavScope
    @Provides
    fun provideMainNavDispatcher(): NavDispatcherProvider {
        return MainNavDispatcherProviderImpl()
    }

    @MainNavScope
    @Provides
    fun provideMainNavFactory(userSession: UserSessionInterface): MainNavFactory = MainNavFactoryImpl(userSession)



    @MainNavScope
    @Provides
    fun provideMainNavRepository(mainNavRemoteDataSource: MainNavRemoteDataSource): MainNavRepo = MainNavRepositoryImpl(mainNavRemoteDataSource)
}