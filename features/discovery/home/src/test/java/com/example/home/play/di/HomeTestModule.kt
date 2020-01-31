package com.example.home.play.di

import android.content.Context
import com.example.home.rules.TestDispatcherProvider
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.datasource.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeFeedMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.module.HomeModule
import com.tokopedia.home.beranda.domain.interactor.GetFeedTabUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.domain.interactor.GetKeywordSearchUseCase
import com.tokopedia.home.beranda.domain.interactor.SendGeolocationInfoUseCase
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.home.common.HomeAceApi
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk

class HomeTestModule : HomeModule() {
    override fun provideHomeDispatcher(): HomeDispatcherProvider {
        return TestDispatcherProvider()
    }

    override fun homePresenter(): HomePresenter {
        return mockk(relaxed = true)
    }

    override fun pagingHandler(): PagingHandler {
        return mockk(relaxed = true)
    }

    override fun provideHomeDatabase(context: Context): HomeDatabase {
        return mockk(relaxed = true)
    }

    override fun provideHomeDao(homeDatabase: HomeDatabase): HomeDao {
        return mockk(relaxed = true)
    }

    override fun provideHomeRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider): HomeRemoteDataSource {
        return mockk(relaxed = true)
    }

    override fun providePlayRemoteDataSource(graphqlRepository: GraphqlRepository, dispatcher: HomeDispatcherProvider): PlayRemoteDataSource {
        return mockk(relaxed = true)
    }

    override fun provideHomeCachedDataSource(homeDao: HomeDao): HomeCachedDataSource {
        return mockk(relaxed = true)
    }

    override fun provideHomeDataSource(homeAceApi: HomeAceApi?): HomeDataSource {
        return mockk(relaxed = true)
    }

    override fun homeRepository(homeDataSource: HomeDataSource, homeRemoteDataSource: HomeRemoteDataSource, homeCachedDataSource: HomeCachedDataSource, playRemoteDataSource: PlayRemoteDataSource): HomeRepository {
        return mockk(relaxed = true)
    }

    override fun homeUsecase(homeRepository: HomeRepository): HomeUseCase {
        return mockk(relaxed = true)
    }

    override fun provideSendGeolocationInfoUseCase(homeRepository: HomeRepository?): SendGeolocationInfoUseCase {
        return mockk(relaxed = true)
    }

    override fun provideGetHomeFeedUseCase(context: Context?, graphqlUseCase: GraphqlUseCase?, homeFeedMapper: HomeFeedMapper?): GetHomeFeedUseCase {
        return mockk(relaxed = true)
    }

    override fun provideGetFeedTabUseCase(context: Context?, graphqlUseCase: GraphqlUseCase?, feedTabMapper: FeedTabMapper?): GetFeedTabUseCase {
        return mockk(relaxed = true)
    }

    override fun provideAddWishlistUseCase(context: Context?): AddWishListUseCase {
        return mockk(relaxed = true)
    }

    override fun provideRemoveWishListUseCase(context: Context?): RemoveWishListUseCase {
        return mockk(relaxed = true)
    }

    override fun feedTabMapper(): FeedTabMapper {
        return mockk(relaxed = true)
    }

    override fun homeFeedMapper(): HomeFeedMapper {
        return mockk(relaxed = true)
    }

    override fun graphqlUseCase(): GraphqlUseCase {
        return mockk(relaxed = true)
    }

    override fun provideUserSession(context: Context?): UserSessionInterface {
        return mockk(relaxed = true)
    }

    override fun getKeywordSearchUseCase(context: Context?): GetKeywordSearchUseCase {
        return mockk(relaxed = true)
    }

    override fun provideGraphqlRepository(): GraphqlRepository {
        return mockk(relaxed = true)
    }

    override fun provideItemTabBusinessViewModel(graphqlUseCase: GraphqlUseCase?): ItemTabBusinessViewModel {
        return mockk(relaxed = true)
    }

    override fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return mockk(relaxed = true)
    }

    override fun provideHomeVisitableFactory(userSessionInterface: UserSessionInterface?): HomeVisitableFactory {
        return mockk(relaxed = true)
    }

    override fun provideStickyLoginUseCase(context: Context, graphqlRepository: GraphqlRepository?): StickyLoginUseCase {
        return mockk(relaxed = true)
    }

    override fun provideRemoteConfig(context: Context?): RemoteConfig {
        return mockk(relaxed = true)
    }

    override fun homeFeedPresenter(getHomeFeedUseCase: GetHomeFeedUseCase?, addWishListUseCase: AddWishListUseCase?, removeWishListUseCase: RemoveWishListUseCase?, topAdsWishlishedUseCase: TopAdsWishlishedUseCase?, userSessionInterface: UserSessionInterface?): HomeFeedPresenter {
        return mockk(relaxed = true)
    }

    override fun equals(other: Any?): Boolean {
        return mockk(relaxed = true)
    }

    override fun hashCode(): Int {
        return mockk(relaxed = true)
    }

    override fun toString(): String {
        return mockk(relaxed = true)
    }
}