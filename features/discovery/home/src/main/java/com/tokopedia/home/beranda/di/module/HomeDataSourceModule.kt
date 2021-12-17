package com.tokopedia.home.beranda.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.*
import dagger.Module
import dagger.Provides

@Module
class HomeDataSourceModule {
    @HomeScope
    @Provides
    fun provideHomeRemoteDataSource(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
            homeDataRepository: HomeDataRepository,
            homeAtfRepository: HomeAtfRepository,
            homeFlagRepository: HomeFlagRepository,
            homeTickerRepository: HomeTickerRepository,
            homeIconRepository: HomeIconRepository,
            homePageBannerRepository: HomePageBannerRepository
    )
            = HomeRemoteDataSource(
            dispatcher,
            homeDynamicChannelsRepository,
            homeDataRepository,
            homeAtfRepository,
            homeFlagRepository,
            homePageBannerRepository,
            homeIconRepository,
            homeTickerRepository
    )

    @Provides
    fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeRoomDataSource(homeDao)

    @HomeScope
    @Provides
    fun provideHomeDefaultDataSource() = HomeDefaultDataSource()
}