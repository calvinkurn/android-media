package com.tokopedia.home.test.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

abstract class BaseWidgetUiTest{
    val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    val dismissHomeReviewUseCase = mockk<DismissHomeReviewUseCase>(relaxed = true)
    val getHomeReviewSuggestedUseCase = mockk<GetHomeReviewSuggestedUseCase>(relaxed = true)
    val getKeywordSearchUseCase = mockk<GetKeywordSearchUseCase>(relaxed = true)
    val getRecommendationTabUseCase = mockk<GetRecommendationTabUseCase>(relaxed = true)
    val getHomeTokopointsDataUseCase = mockk<GetHomeTokopointsDataUseCase>(relaxed = true)
    val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase> (relaxed = true)
    val getPlayLiveDynamicUseCase = mockk<GetPlayLiveDynamicUseCase> (relaxed = true)
    val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase> (relaxed = true)
    val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    val getSendGeolocationInfoUseCase = mockk<SendGeolocationInfoUseCase> (relaxed = true)
    val getStickyLoginUseCase = mockk<StickyLoginUseCase> (relaxed = true)
    val getBusinessWidgetTab = mockk<GetBusinessWidgetTab> (relaxed = true)
    val getBusinessUnitDataUseCase = mockk<GetBusinessUnitDataUseCase> (relaxed = true)
    val getPopularKeywordUseCase = mockk<GetPopularKeywordUseCase> (relaxed = true)
    val getDynamicChannelsUseCase = mockk<GetDynamicChannelsUseCase> (relaxed = true)
    val sendTopAdsUseCase = mockk<SendTopAdsUseCase>(relaxed = true)
    val getAtcUseCase = mockk<AddToCartOccUseCase>(relaxed = true)
    val homeDataMapper = HomeDataMapper(InstrumentationRegistry.getInstrumentation().context, HomeVisitableFactoryImpl(userSessionInterface), mockk(relaxed = true))

    fun reInitViewModel() = HomeViewModel(
            dismissHomeReviewUseCase = dismissHomeReviewUseCase,
            getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
            getBusinessWidgetTab = getBusinessWidgetTab,
            getDynamicChannelsUseCase = getDynamicChannelsUseCase,
            getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase,
            getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
            getKeywordSearchUseCase = getKeywordSearchUseCase,
            getPendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getPlayCardHomeUseCase = getPlayLiveDynamicUseCase,
            getRecommendationTabUseCase = getRecommendationTabUseCase,
            getWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            homeDispatcher = TestDispatcherProvider(),
            homeUseCase = getHomeUseCase,
            popularKeywordUseCase = getPopularKeywordUseCase,
            sendGeolocationInfoUseCase = getSendGeolocationInfoUseCase,
            stickyLoginUseCase = getStickyLoginUseCase,
            userSession = userSessionInterface,
            sendTopAdsUseCase = sendTopAdsUseCase,
            getAtcUseCase = getAtcUseCase
    )

    fun <T : ViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
                if (viewModelClass.isAssignableFrom(viewModel.javaClass)) {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                }
                throw IllegalArgumentException("Unknown view model class " + viewModelClass)
            }
        }
    }
}