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
import com.tokopedia.home.beranda.data.datasource.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named


@Module(includes = [TopAdsWishlistModule::class, PlayCardModule::class])
open class HomeModule {

    @HomeScope
    @Provides
    open fun provideHomeDispatcher(): HomeDispatcherProvider = HomeDispatcherProviderImpl()

    @HomeScope
    @Provides
    open fun pagingHandler(): PagingHandler {
        return PagingHandler()
    }

    @HomeScope
    @Provides
    open fun provideHomeDatabase(@ApplicationContext context: Context): HomeDatabase = HomeDatabase.buildDatabase(context)

    @HomeScope
    @Provides
    open fun provideHomeDao(homeDatabase: HomeDatabase) = homeDatabase.homeDao()

    @HomeScope
    @Provides
    open fun provideHomeRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider) = HomeRemoteDataSource(graphqlRepository, dispatcher)

    @HomeScope
    @Provides
    open fun providePlayRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider) = PlayRemoteDataSource(graphqlRepository, dispatcher)

    @HomeScope
    @Provides
    open fun provideHomeCachedDataSource(homeDao: HomeDao) = HomeCachedDataSource(homeDao)

    @HomeScope
    @Provides
    open fun provideHomeDataSource(homeAceApi: HomeAceApi?): HomeDataSource {
        return HomeDataSource(homeAceApi)
    }

    @HomeScope
    @Provides
    open fun homeRepository(homeDataSource: HomeDataSource,
                       homeRemoteDataSource: HomeRemoteDataSource,
                       homeCachedDataSource: HomeCachedDataSource,
                       playRemoteDataSource: PlayRemoteDataSource): HomeRepository {
        return HomeRepositoryImpl(homeDataSource, homeCachedDataSource, homeRemoteDataSource, playRemoteDataSource)
    }

    @HomeScope
    @Provides
    open fun homeUsecase(homeRepository: HomeRepository) = HomeUseCase(homeRepository)

    @Provides
    open fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository?): SendGeolocationInfoUseCase {
        return SendGeolocationInfoUseCase(homeRepository)
    }

    @Provides
    open fun provideGetHomeFeedUseCase(@ApplicationContext context: Context?,
                                            graphqlUseCase: GraphqlUseCase?,
                                            homeFeedMapper: HomeFeedMapper?): GetHomeFeedUseCase {
        return GetHomeFeedUseCase(context, graphqlUseCase, homeFeedMapper)
    }

    @Provides
    open fun provideGetFeedTabUseCase(@ApplicationContext context: Context?,
                                           graphqlUseCase: GraphqlUseCase?,
                                           feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return GetFeedTabUseCase(context, graphqlUseCase, feedTabMapper)
    }

    @Provides
    open fun provideAddWishlistUseCase(@ApplicationContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    open fun provideRemoveWishListUseCase(@ApplicationContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    open fun feedTabMapper(): FeedTabMapper {
        return FeedTabMapper()
    }

    @Provides
    open fun homeFeedMapper(): HomeFeedMapper {
        return HomeFeedMapper()
    }

    @Provides
    open fun graphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    open fun provideUserSession(
            @ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @HomeScope
    @Provides
    open fun getKeywordSearchUseCase(@ApplicationContext context: Context?): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(context!!)
    }

    @Provides
    open fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @HomeScope
    open fun provideItemTabBusinessViewModel(graphqlUseCase: GraphqlUseCase?): ItemTabBusinessViewModel {
        return ItemTabBusinessViewModel(graphqlUseCase!!)
    }

    @Provides
    @HomeScope
    open fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @Provides
    @HomeScope
    open fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?): HomeVisitableFactory {
        return HomeVisitableFactoryImpl(userSessionInterface!!)
    }

    @Provides
    @HomeScope
    open fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository?): StickyLoginUseCase {
        return StickyLoginUseCase(context.resources, graphqlRepository!!)
    }

    @Provides
    open fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @HomeScope
    @Provides
    open fun homePresenter(): HomePresenter {
        return HomePresenter()
    }

    @Provides
    open fun homeFeedPresenter(
            getHomeFeedUseCase: GetHomeFeedUseCase?,
            addWishListUseCase: AddWishListUseCase?,
            removeWishListUseCase: RemoveWishListUseCase?,
            topAdsWishlishedUseCase: TopAdsWishlishedUseCase?,
            userSessionInterface: UserSessionInterface?
    ): HomeFeedPresenter {
        return HomeFeedPresenter(userSessionInterface!!, getHomeFeedUseCase!!, addWishListUseCase!!, removeWishListUseCase!!, topAdsWishlishedUseCase!!)
    }
}
