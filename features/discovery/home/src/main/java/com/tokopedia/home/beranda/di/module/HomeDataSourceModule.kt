package com.tokopedia.home.beranda.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.common.HomeAceApi
import dagger.Module
import dagger.Provides

@Module
class HomeDataSourceModule {
    @HomeScope
    @Provides
    fun provideHomeRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider) = HomeRemoteDataSource(graphqlRepository, dispatcher)

    @Provides
    fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeCachedDataSource(homeDao)

    @HomeScope
    @Provides
    fun provideHomeDefaultDataSource() = HomeDefaultDataSource()

    @HomeScope
    @Provides
    fun provideGeolocationRemoteDataSource(homeAceApi: HomeAceApi) = GeolocationRemoteDataSource(homeAceApi)
}