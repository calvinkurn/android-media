package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.helper.DeviceScreenHelper
import com.tokopedia.home.beranda.presentation.view.helper.HomePrefController
import com.tokopedia.home.beranda.presentation.view.helper.HomeRemoteConfigController
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        HomeDataSourceModule::class,
        HomeDatabaseModule::class,
        HomeMapperModule::class,
        HomeUseCaseModule::class,
        HomeRateLimiterModule::class
    ]
)
class HomeModule {

    @HomeScope
    @Provides
    fun pagingHandler() = PagingHandler()

    @HomeScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @HomeScope
    @Provides
    fun provideExecutors(): SmartExecutors = SmartExecutors()

    @HomeScope
    @Provides
    fun provideUserSession(
        @ApplicationContext context: Context?
    ): UserSessionInterface = UserSession(context)

    @HomeScope
    @Provides
    fun provideHomePrefController(
        @ApplicationContext context: Context?
    ): HomePrefController = HomePrefController(context)

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @HomeScope
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()

    @Provides
    @HomeScope
    fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?, homePrefController: HomePrefController, remoteConfig: RemoteConfig): HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface!!, homePrefController, remoteConfig, HomeDefaultDataSource())

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
    fun provideHomeRemoteConfigController(
        remoteConfig: RemoteConfig
    ): HomeRemoteConfigController = HomeRemoteConfigController(remoteConfig)

    @HomeScope
    @Provides
    fun provideHomeThematicUtil(): HomeThematicUtil = HomeThematicUtil()

    @HomeScope
    @Provides
    fun provideDeviceScreenHelper(@ApplicationContext context: Context) = DeviceScreenHelper(context)
}
