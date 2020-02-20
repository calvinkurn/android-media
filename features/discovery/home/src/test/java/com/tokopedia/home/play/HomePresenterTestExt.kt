package com.tokopedia.home.play

import com.tokopedia.common_wallet.balance.domain.coroutine.GetCoroutineWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.coroutine.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import org.spekframework.spek2.dsl.TestBody

fun TestBody.createPresenter(): HomeViewModel{
    val userSessionInterface by memoized<UserSessionInterface>()
    val dismissHomeReviewUseCase by memoized<DismissHomeReviewUseCase>()
    val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase>()
    val getKeywordSearchUseCase by memoized<GetKeywordSearchUseCase>()
    val getFeedTabUseCase by memoized<GetFeedTabUseCase>()
    val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase>()
    val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase>()
    val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase>()
    val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase>()
    val getHomeUseCase by memoized<HomeUseCase>()
    val getSendGeolocationInfoUseCase by memoized<SendGeolocationInfoUseCase>()
    val getStickyLoginUseCase by memoized<StickyLoginUseCase>()
    val homeDataMapper by memoized<HomeDataMapper>()
    return HomeViewModel(
        dismissHomeReviewUseCase = dismissHomeReviewUseCase,
        getFeedTabUseCase = getFeedTabUseCase,
        getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase,
        getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
        getKeywordSearchUseCase = getKeywordSearchUseCase,
        getPendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
        getPlayCardHomeUseCase = getPlayLiveDynamicUseCase,
        getWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
        homeDataMapper = homeDataMapper,
        homeDispatcher = TestDispatcherProvider(),
        homeUseCase = getHomeUseCase,
        sendGeolocationInfoUseCase = getSendGeolocationInfoUseCase,
        stickyLoginUseCase = getStickyLoginUseCase,
        userSession = userSessionInterface
    )
}