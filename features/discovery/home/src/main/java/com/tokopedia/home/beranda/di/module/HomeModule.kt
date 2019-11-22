package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.mapper.HomeMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.home.common.HomeAceApi
import com.tokopedia.home.common.HomeDataApi
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
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


@Module(includes = [TopAdsWishlistModule::class])
class HomeModule {

    @HomeScope
    @Named("Main")
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @HomeScope
    @Provides
    fun providehomeMapper(@ApplicationContext context: Context?,
                                    homeVisitableFactory: HomeVisitableFactory?): HomeMapper {
        return HomeMapper(context, homeVisitableFactory)
    }

    @HomeScope
    @Provides
    fun homePresenter(userSession: UserSessionInterface,
                                getShopInfoByDomainUseCase: GetShopInfoByDomainUseCase,
                      @Named("Main") coroutineDispatcher: CoroutineDispatcher): HomePresenter {
        return realHomePresenter(userSession, getShopInfoByDomainUseCase, coroutineDispatcher)
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

    private fun realHomePresenter(userSession: UserSessionInterface,
                                    getShopInfoByDomainUseCase: GetShopInfoByDomainUseCase,
                                    coroutineDispatcher: CoroutineDispatcher): HomePresenter {
        return HomePresenter(userSession, getShopInfoByDomainUseCase, coroutineDispatcher)
    }

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
    fun provideHomeRemoteDataSource(graphqlRepository: GraphqlRepository) = HomeRemoteDataSource(graphqlRepository)

    @HomeScope
    @Provides
    fun homeRepository(homeDataSource: HomeDataSource, homeDao: HomeDao, homeRemoteDataSource: HomeRemoteDataSource, homeMapper: HomeMapper): HomeRepository {
        return HomeRepositoryImpl(homeDataSource, homeDao, homeRemoteDataSource, homeMapper)
    }

    @Provides
    fun provideHomeDataSource(homeDataApi: HomeDataApi?,
                                        homeAceApi: HomeAceApi?,
                                        homeMapper: HomeMapper?,
                                        @ApplicationContext context: Context?,
                                        cacheManager: CacheManager?,
                                        gson: Gson?): HomeDataSource {
        return HomeDataSource(homeDataApi, homeAceApi, homeMapper, context, cacheManager, gson)
    }

    @Provides
    fun provideGetHomeDataUseCase(homeRepository: HomeRepository?): GetHomeDataUseCase {
        return GetHomeDataUseCase(homeRepository)
    }

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
    fun getLocalHomeDataUseCase(repository: HomeRepository?): GetLocalHomeDataUseCase {
        return GetLocalHomeDataUseCase(repository)
    }

    @HomeScope
    @Provides
    fun getKeywordSearchUseCase(@ApplicationContext context: Context?): GetKeywordSearchUseCase {
        return GetKeywordSearchUseCase(context!!)
    }

    @HomeScope
    @Provides
    fun provideStatusMapper(): StatusMapper {
        return StatusMapper()
    }

    @HomeScope
    @Provides
    fun provideCategoryMapper(): CategoryMapper {
        return CategoryMapper()
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
}
