package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.common_wallet.di.CommonWalletModule
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.common.HomeDispatcherProviderImpl
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module(includes = [
    HomeDataSourceModule::class,
    HomeDatabaseModule::class,
    HomePresenterModule::class,
    HomeMapperModule::class,
    HomeUseCaseModule::class,
    CommonWalletModule::class,
    TopAdsWishlistModule::class
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
    fun homeRepository(geolocationRemoteDataSource: GeolocationRemoteDataSource,
                       homeRemoteDataSource: HomeRemoteDataSource,
                       homeCachedDataSource: HomeCachedDataSource,
                       playRemoteDataSource: PlayRemoteDataSource,
                       homeDefaultDataSource: HomeDefaultDataSource,
                       keywordSearchRemoteDataSource: KeywordSearchRemoteDataSource,
                       tokopointRemoteDataSource: TokopointRemoteDataSource
    ): HomeRepository = HomeRepositoryImpl(
            homeCachedDataSource,
            homeRemoteDataSource,
            playRemoteDataSource,
            homeDefaultDataSource,
            geolocationRemoteDataSource,
            keywordSearchRemoteDataSource,
            tokopointRemoteDataSource)

    @HomeScope
    @Provides
    fun provideUserSession(
            @ApplicationContext context: Context?): UserSessionInterface = UserSession(context)

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @HomeScope
    fun provideItemTabBusinessViewModel(graphqlUseCase: GraphqlUseCase?): ItemTabBusinessViewModel = ItemTabBusinessViewModel(graphqlUseCase!!)

    @Provides
    @HomeScope
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()

    @Provides
    @HomeScope
    fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?): HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface!!)

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig = FirebaseRemoteConfigImpl(context)

}
