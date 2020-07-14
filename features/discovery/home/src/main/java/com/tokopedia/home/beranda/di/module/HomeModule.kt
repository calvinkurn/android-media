package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.common.HomeDispatcherProviderImpl
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.GeolocationRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.permissionchecker.PermissionCheckerHelper
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
    fun provideHomeDispatcher(): HomeDispatcherProvider = HomeDispatcherProviderImpl()

    @HomeScope
    @Provides
    fun pagingHandler() = PagingHandler()

    @HomeScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @HomeScope
    @Provides
    fun homeRepository(geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource>,
                       homeRemoteDataSource: HomeRemoteDataSource,
                       homeCachedDataSource: HomeCachedDataSource,
                       homeDefaultDataSource: HomeDefaultDataSource
    ): HomeRepository = HomeRepositoryImpl(
            homeCachedDataSource,
            homeRemoteDataSource,
            homeDefaultDataSource,
            geolocationRemoteDataSource)

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
    fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?): HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface!!)

    @HomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig = FirebaseRemoteConfigImpl(context)

    @HomeScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, CacheUtil.KEY_POPUP_INTRO_OVO_CACHE)
    }

}
