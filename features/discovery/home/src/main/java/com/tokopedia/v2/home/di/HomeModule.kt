package com.tokopedia.v2.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.R
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.data.datasource.local.HomeDatabase
import com.tokopedia.v2.home.data.datasource.local.dao.HomeDao
import com.tokopedia.v2.home.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.v2.home.data.repository.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named

@HomeScope
@Module
class HomeModule {
    @HomeScope
    @Provides
    fun provideHomeDatabase(@ApplicationContext context: Context): HomeDatabase = HomeDatabase.buildDatabase(context)

    @HomeScope
    @Provides
    fun provideHomeDao(homeDatabase: HomeDatabase) = homeDatabase.homeDao()

    @Provides
    @HomeScope
    @Named("homeQueryV2")
    fun provideHomeQueryV2(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.home_query_v2)

    @HomeScope
    @Provides
    fun provideHomeDataSource(graphqlRepository: GraphqlRepository, @Named("homeQueryV2") query: String) = HomeRemoteDataSource(graphqlRepository, query)

    @HomeScope
    @Provides
    fun provideHomeRepository(homeDao: HomeDao, homeRemoteDataSource: HomeRemoteDataSource): HomeRepository = HomeRepositoryImpl(homeDao, homeRemoteDataSource)
}