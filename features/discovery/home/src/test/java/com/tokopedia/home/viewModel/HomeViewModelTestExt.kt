package com.tokopedia.home.viewModel

import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

fun TestBody.createHomeViewModel(): HomeViewModel{
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
            homeUseCase = getHomeUseCase,
            userSession = userSessionInterface,
            getFeedTabUseCase = getFeedTabUseCase,
            sendGeolocationInfoUseCase = getSendGeolocationInfoUseCase,
            getWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getPendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
            getKeywordSearchUseCase = getKeywordSearchUseCase,
            stickyLoginUseCase = getStickyLoginUseCase,
            getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase,
            dismissHomeReviewUseCase = dismissHomeReviewUseCase,
            getPlayCardHomeUseCase = getPlayLiveDynamicUseCase,
            homeDispatcher = TestDispatcherProvider()
    )
}

fun FeatureBody.createHomeViewModelTestInstance() {
    val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
    val dismissHomeReviewUseCase by memoized<DismissHomeReviewUseCase> { mockk(relaxed = true) }
    val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase> { mockk(relaxed = true) }
    val getKeywordSearchUseCase by memoized<GetKeywordSearchUseCase> { mockk(relaxed = true) }
    val getFeedTabUseCase by memoized<GetFeedTabUseCase> { mockk(relaxed = true) }
    val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase> { mockk(relaxed = true) }
    val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase> { mockk(relaxed = true) }
    val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase> { mockk(relaxed = true) }
    val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase> { mockk(relaxed = true) }
    val getHomeUseCase by memoized<HomeUseCase> { mockk(relaxed = true) }
    val getSendGeolocationInfoUseCase by memoized<SendGeolocationInfoUseCase> { mockk(relaxed = true) }
    val getStickyLoginUseCase by memoized<StickyLoginUseCase> { mockk(relaxed = true) }
    val homeDataMapper by memoized<HomeDataMapper> { mockk(relaxed = true) }
}

fun GetPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(channel: PlayChannel) {
    setParams()
    coEvery { executeOnBackground() } returns PlayData(
            playChannels = listOf(channel)
    )
}
fun HomeUseCase.givenGetHomeDataReturn(homeDataModel: HomeDataModel) {
    coEvery { getHomeData() } returns flow{
        emit(homeDataModel)
    }
}