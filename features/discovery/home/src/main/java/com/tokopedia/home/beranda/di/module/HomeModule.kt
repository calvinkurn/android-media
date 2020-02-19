package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.dynamicbanner.di.PlayCardModule
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.common.HomeDispatcherProviderImpl
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.GetFeedTabUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.domain.interactor.SendGeolocationInfoUseCase
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.home.common.HomeAceApi
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides


@Module(includes = [TopAdsWishlistModule::class, PlayCardModule::class])
class HomeModule {

    @HomeScope
    @Provides
    fun provideHomeDispatcher(): HomeDispatcherProvider = HomeDispatcherProviderImpl()

    @HomeScope
    @Provides
    fun pagingHandler(): PagingHandler {
        return PagingHandler()
    }

    @HomeScope
    @Provides
    fun provideHomeDatabase(@ApplicationContext context: Context): HomeDatabase = HomeDatabase.buildDatabase(context)

    @HomeScope
    @Provides
    fun provideHomeDao(homeDatabase: HomeDatabase) = homeDatabase.homeDao()

    @HomeScope
    @Provides
    fun provideHomeRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider) = HomeRemoteDataSource(graphqlRepository, dispatcher)

    @HomeScope
    @Provides
    fun providePlayRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider) = PlayRemoteDataSource(graphqlRepository, dispatcher)

    @HomeScope
    @Provides
    fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeCachedDataSource(homeDao)

    @HomeScope
    @Provides
    fun provideHomeDataSource(homeAceApi: HomeAceApi?): HomeDataSource {
        return HomeDataSource(homeAceApi)
    }

    @HomeScope
    @Provides
    fun provideHomeDafaultDataSource(): HomeDefaultDataSource {
        return HomeDefaultDataSource()
    }

    @HomeScope
    @Provides
    fun homeRepository(homeDataSource: HomeDataSource,
                       homeRemoteDataSource: HomeRemoteDataSource,
                       homeCachedDataSource: HomeCachedDataSource,
                       playRemoteDataSource: PlayRemoteDataSource,
                       homeDefaultDataSource: HomeDefaultDataSource): HomeRepository {
        return HomeRepositoryImpl(homeDataSource, homeCachedDataSource, homeRemoteDataSource, playRemoteDataSource, homeDefaultDataSource)
    }

    @HomeScope
    @Provides
    fun homeUsecase(homeRepository: HomeRepository) = HomeUseCase(homeRepository)

    @Provides
    fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository?): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    fun provideGetHomeFeedUseCase(@ApplicationContext context: Context?,
                                            graphqlUseCase: GraphqlUseCase?,
                                            homeFeedMapper: HomeFeedMapper?): GetHomeFeedUseCase {
        return GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper)
    }

    @Provides
    fun provideGetFeedTabUseCase(@ApplicationContext context: Context?,
                                           graphqlUseCase: GraphqlUseCase?,
                                           feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper)
    }

    @Provides
    fun provideAddWishlistUseCase(@ApplicationContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    fun provideRemoveWishListUseCase(@ApplicationContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    fun feedTabMapper(): FeedTabMapper {
        return FeedTabMapper()
    }

    @Provides
    fun homeFeedMapper(): HomeFeedMapper {
        return HomeFeedMapper()
    }

    @Provides
    fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    fun provideUserSession(
            @ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(@ApplicationContext context: Context?): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(context!!)
    }

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @HomeScope
    fun provideItemTabBusinessViewModel(graphqlUseCase: GraphqlUseCase?): ItemTabBusinessViewModel {
        return ItemTabBusinessViewModel(graphqlUseCase!!)
    }

    @Provides
    @HomeScope
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @Provides
    @HomeScope
    fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?): HomeVisitableFactory {
        return HomeVisitableFactoryImpl(userSessionInterface!!)
    }

    @Provides
    @HomeScope
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository?): StickyLoginUseCase {
        return StickyLoginUseCase(context.resources, graphqlRepository!!)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @HomeScope
    @Provides
    fun homePresenter(homeUseCase: HomeUseCase, homeDispatcher: HomeDispatcherProvider): HomePresenter {
        return HomePresenter(homeUseCase, homeDispatcher)
    }

    @Provides
    fun homeFeedPresenter(
            getHomeFeedUseCase: GetHomeFeedUseCase?,
            addWishListUseCase: AddWishListUseCase?,
            removeWishListUseCase: RemoveWishListUseCase?,
            topAdsWishlishedUseCase: TopAdsWishlishedUseCase?,
            userSessionInterface: UserSessionInterface?
    ): HomeFeedPresenter {
        return HomeFeedPresenter(userSessionInterface!!, getHomeFeedUseCase!!, addWishListUseCase!!, removeWishListUseCase!!, topAdsWishlishedUseCase!!)
    }
}
