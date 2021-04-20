package com.tokopedia.home.test.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
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
    open val getBusinessWidgetTab = mockk<Lazy<GetBusinessWidgetTab>> (relaxed = true)
    open val getBusinessUnitDataUseCase = mockk<Lazy<GetBusinessUnitDataUseCase>> (relaxed = true)
    open val getPopularKeywordUseCase = mockk<Lazy<GetPopularKeywordUseCase>> (relaxed = true)
    open val getAtcUseCase = mockk<Lazy<AddToCartOccUseCase>>(relaxed = true)
    open val getRechargeRecommendationUseCase = mockk<Lazy<GetRechargeRecommendationUseCase>>(relaxed = true)
    open val declineRechargeRecommendationUseCase = mockk<Lazy<DeclineRechargeRecommendationUseCase>>(relaxed = true)
    open val getSalamWIdgetUseCase = mockk<Lazy<GetSalamWidgetUseCase>>(relaxed = true)
    open val declineSalamWIdgetUseCase = mockk<Lazy<DeclineSalamWIdgetUseCase>>(relaxed = true)
    open val getRechargeBUWidgetUseCase = mockk<Lazy<GetRechargeBUWidgetUseCase>>(relaxed = true)
    open val closeChannelUseCase = mockk<Lazy<CloseChannelUseCase>>(relaxed = true)
    open val topAdsImageViewUseCase = mockk<Lazy<TopAdsImageViewUseCase>>(relaxed = true)
    open val injectCouponTimeBasedUseCase = mockk<Lazy<InjectCouponTimeBasedUseCase>>(relaxed = true)
    open val remoteConfig = mockk<RemoteConfig>(relaxed = true)
    open val getDisplayHeadlineAds = mockk<Lazy<GetDisplayHeadlineAds>>(relaxed = true)
    open val homeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface.get(), remoteConfig, HomeDefaultDataSource())
    open val homeDynamicChannelVisitableFactory = HomeDynamicChannelVisitableFactoryImpl(userSessionInterface.get(), remoteConfig, HomeDefaultDataSource())
    open val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    open val homeDataMapper = HomeDataMapper(instrumentationContext, homeVisitableFactory, mockk(relaxed = true),
            HomeDynamicChannelDataMapper(instrumentationContext, homeDynamicChannelVisitableFactory, TrackingQueue(instrumentationContext)))
    open val playWidgetTools = mockk<Lazy<PlayWidgetTools>> (relaxed = true)
    open val bestSellerMapper = mockk<Lazy<BestSellerMapper>>(relaxed = true)
    open val getHomeTokopointsListDataUseCase = mockk<Lazy<GetHomeTokopointsListDataUseCase>>(relaxed = true)
    open val getRecommendationFilterChips = mockk<Lazy<GetRecommendationFilterChips>>(relaxed = true)
    open val getRecommendationUseCase = mockk<Lazy<GetRecommendationUseCase>>(relaxed = true)

    open fun reInitViewModel() = HomeViewModel(
            dismissHomeReviewUseCase = dismissHomeReviewUseCase,
            getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
            getBusinessWidgetTab = getBusinessWidgetTab,
            getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase,
            getKeywordSearchUseCase = getKeywordSearchUseCase,
            getPendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getPlayCardHomeUseCase = getPlayLiveDynamicUseCase,
            getRecommendationTabUseCase = getRecommendationTabUseCase,
            getWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            homeDispatcher = Lazy { CoroutineTestDispatchersProvider },
            homeUseCase = getHomeUseCase,
            popularKeywordUseCase = getPopularKeywordUseCase,
            sendGeolocationInfoUseCase = getSendGeolocationInfoUseCase,
            userSession = userSessionInterface,
            getAtcUseCase = getAtcUseCase,
            closeChannelUseCase = closeChannelUseCase,
            getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
            declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase,
            getSalamWidgetUseCase = getSalamWIdgetUseCase,
            declineSalamWidgetUseCase = declineSalamWIdgetUseCase,
            getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
            injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase,
            topAdsImageViewUseCase = topAdsImageViewUseCase,
            getDisplayHeadlineAds = getDisplayHeadlineAds,
            homeProcessor = mockk(relaxed = true),
            playWidgetTools = playWidgetTools,
            getRecommendationUseCase = getRecommendationUseCase,
            getRecommendationFilterChips = getRecommendationFilterChips,
            getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            bestSellerMapper = bestSellerMapper
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