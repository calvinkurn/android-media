package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.GeolocationRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.data.repository.HomeRevampRepositoryImpl
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.util.HomeCommandProcessor
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module(includes = [
    HomeDataSourceModule::class,
    HomeDatabaseModule::class,
    HomeMapperModule::class,
    HomeUseCaseModule::class
])
class HomeModule {

    @HomeScope
    @Provides
    fun provideHomeDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @HomeScope
    @Provides
    fun pagingHandler() = PagingHandler()

    @HomeScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @HomeScope
    @Provides
    fun homeRevampRepository(geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource>,
                       homeRemoteDataSource: HomeRemoteDataSource,
                       homeCachedDataSource: HomeCachedDataSource,
                       homeDefaultDataSource: HomeDefaultDataSource,
                       dynamicChannelDataMapper: HomeDynamicChannelDataMapper,
                       @ApplicationContext context: Context,
                       remoteConfig: RemoteConfig
    ): HomeRevampRepository = HomeRevampRepositoryImpl(
            homeCachedDataSource,
            homeRemoteDataSource,
            homeDefaultDataSource,
            geolocationRemoteDataSource,
            dynamicChannelDataMapper,
            context,
            remoteConfig)


    @HomeScope
    @Provides
    fun provideExecutors(): SmartExecutors = SmartExecutors()

    @HomeScope
    @Provides
    fun provideUserSession(
            @ApplicationContext context: Context?): UserSessionInterface = UserSession(context)

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @HomeScope
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()

    @Provides
    @HomeScope
    fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?, remoteConfig: RemoteConfig): HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface!!, remoteConfig, HomeDefaultDataSource())

    @Provides
    @HomeScope
    fun provideHomeDynamicChannelVisitableFactory(userSessionInterface: UserSessionInterface?, remoteConfig: RemoteConfig): HomeDynamicChannelVisitableFactory = HomeDynamicChannelVisitableFactoryImpl(userSessionInterface!!, remoteConfig, HomeDefaultDataSource())

    @HomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig = FirebaseRemoteConfigImpl(context)

    @HomeScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, CacheUtil.KEY_POPUP_INTRO_OVO_CACHE)
    }

    @HomeScope
    @Provides
    fun provideHomeProcessor(homeDispatcher: CoroutineDispatchers): HomeCommandProcessor = HomeCommandProcessor(homeDispatcher.io)

}
