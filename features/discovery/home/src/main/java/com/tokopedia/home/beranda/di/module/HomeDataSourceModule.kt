package com.tokopedia.home.beranda.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.common.HomeAceApi
import dagger.Module
import dagger.Provides

@Module
class HomeDataSourceModule {
    @HomeScope
    @Provides
    fun provideHomeRemoteDataSource(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            getHomeDynamicChannelsRepository: GetHomeDynamicChannelsRepository,
            getHomeDataUseCase: GetHomeDataUseCase,
            getHomeAtfUseCase: GetHomeAtfUseCase,
            getHomeFlagUseCase: GetHomeFlagUseCase,
            getHomeTickerRepository: GetHomeTickerRepository,
            getHomeIconRepository: GetHomeIconRepository,
            getHomePageBannerUseCase: GetHomePageBannerUseCase
    )
            = HomeRemoteDataSource(
            dispatcher,
            getHomeDynamicChannelsRepository,
            getHomeDataUseCase,
            getHomeAtfUseCase,
            getHomeFlagUseCase,
            getHomePageBannerUseCase,
            getHomeIconRepository,
            getHomeTickerRepository
    )

    @Provides
    fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeCachedDataSource(homeDao)

    @HomeScope
    @Provides
    fun provideHomeDefaultDataSource() = HomeDefaultDataSource()

    @HomeScope
    @Provides
    fun provideGeolocationRemoteDataSource(homeAceApi: HomeAceApi) = GeolocationRemoteDataSource(homeAceApi)
}