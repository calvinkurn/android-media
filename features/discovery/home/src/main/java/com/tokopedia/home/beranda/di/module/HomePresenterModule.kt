package com.tokopedia.home.beranda.di.module

import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedPresenter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class HomePresenterModule {
    @Provides
    fun homeFeedPresenter(
            getHomeFeedUseCase: GetHomeFeedUseCase,
            userSessionInterface: UserSessionInterface
    ): HomeFeedPresenter = HomeFeedPresenter(userSessionInterface, getHomeFeedUseCase)
}