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
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.mockk

abstract class BaseWidgetUiTest{
    open val userSessionInterface: Lazy<UserSessionInterface> = mockk<Lazy<UserSessionInterface>>(relaxed = true)
    open val dismissHomeReviewUseCase = mockk<Lazy<DismissHomeReviewUseCase>>(relaxed = true)
    open val getHomeReviewSuggestedUseCase = mockk<Lazy<GetHomeReviewSuggestedUseCase>>(relaxed = true)
    open val getKeywordSearchUseCase = mockk<Lazy<GetKeywordSearchUseCase>>(relaxed = true)
    open val getRecommendationTabUseCase = mockk<Lazy<GetRecommendationTabUseCase>>(relaxed = true)
    open val getHomeTokopointsDataUseCase = mockk<Lazy<GetHomeTokopointsDataUseCase>>(relaxed = true)
    open val getCoroutinePendingCashbackUseCase = mockk<Lazy<GetCoroutinePendingCashbackUseCase>> (relaxed = true)
    open val getPlayLiveDynamicUseCase = mockk<Lazy<GetPlayLiveDynamicUseCase>> (relaxed = true)
    open val getCoroutineWalletBalanceUseCase = mockk<Lazy<GetCoroutineWalletBalanceUseCase>> (relaxed = true)
    open val getHomeUseCase = mockk<dagger.Lazy<HomeUseCase>> (relaxed = true)
    open val getSendGeolocationInfoUseCase = mockk<dagger.Lazy<SendGeolocationInfoUseCase>> (relaxed = true)
    open val getStickyLoginUseCase = mockk<Lazy<StickyLoginUseCase>> (relaxed = true)
    open val getBusinessWidgetTab = mockk<Lazy<GetBusinessWidgetTab>> (relaxed = true)
    open val getBusinessUnitDataUseCase = mockk<Lazy<GetBusinessUnitDataUseCase>> (relaxed = true)
    open val getPopularKeywordUseCase = mockk<Lazy<GetPopularKeywordUseCase>> (relaxed = true)
    open val getDynamicChannelsUseCase = mockk<Lazy<GetDynamicChannelsUseCase>> (relaxed = true)
    open val getAtcUseCase = mockk<Lazy<AddToCartOccUseCase>>(relaxed = true)
    open val getRechargeRecommendationUseCase = mockk<Lazy<GetRechargeRecommendationUseCase>>(relaxed = true)
    open val declineRechargeRecommendationUseCase = mockk<Lazy<DeclineRechargeRecommendationUseCase>>(relaxed = true)
    open val getSalamWIdgetUseCase = mockk<Lazy<GetSalamWidgetUseCase>>(relaxed = true)
    open val declineSalamWIdgetUseCase = mockk<Lazy<DeclineSalamWIdgetUseCase>>(relaxed = true)
    open val closeChannelUseCase = mockk<Lazy<CloseChannelUseCase>>(relaxed = true)
    open val topAdsImageViewUseCase = mockk<Lazy<TopAdsImageViewUseCase>>(relaxed = true)
    open val injectCouponTimeBasedUseCase = mockk<Lazy<InjectCouponTimeBasedUseCase>>(relaxed = true)
    open val getPlayBannerUseCase = mockk<Lazy<GetPlayWidgetUseCase>>(relaxed = true)
    open val playToggleChannelReminderUseCase = mockk<Lazy<PlayToggleChannelReminderUseCase>>(relaxed = true)
    open val remoteConfig = mockk<RemoteConfig>(relaxed = true)
    open val homeDataMapper = HomeDataMapper(InstrumentationRegistry.getInstrumentation().context, HomeVisitableFactoryImpl(userSessionInterface.get(), remoteConfig), mockk(relaxed = true))

    open fun reInitViewModel() = HomeViewModel(
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
            getAtcUseCase = getAtcUseCase,
            closeChannelUseCase = closeChannelUseCase,
            getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
            declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase,
            getSalamWidgetUseCase = getSalamWIdgetUseCase,
            declineSalamWidgetUseCase = declineSalamWIdgetUseCase,
            injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase,
            topAdsImageViewUseCase = topAdsImageViewUseCase,
            getPlayBannerUseCase = getPlayBannerUseCase,
            playToggleChannelReminderUseCase = playToggleChannelReminderUseCase
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