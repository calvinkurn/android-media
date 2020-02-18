package com.tokopedia.home.beranda.di.module

import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module
class HomePresenterModule {

    @HomeScope
    @Provides
    fun homePresenter(homeUseCase: HomeUseCase, homeDispatcher: HomeDispatcherProvider): HomePresenter = HomePresenter(homeUseCase, homeDispatcher)

    @Provides
    fun homeFeedPresenter(
            getHomeFeedUseCase: GetHomeFeedUseCase?,
            addWishListUseCase: AddWishListUseCase?,
            removeWishListUseCase: RemoveWishListUseCase?,
            topAdsWishlishedUseCase: TopAdsWishlishedUseCase?,
            userSessionInterface: UserSessionInterface?
    ): HomeFeedPresenter = HomeFeedPresenter(userSessionInterface!!, getHomeFeedUseCase!!, addWishListUseCase!!, removeWishListUseCase!!, topAdsWishlishedUseCase!!)

}