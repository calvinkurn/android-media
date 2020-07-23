package com.tokopedia.home.viewModel.homepage

import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
fun TestBody.createHomeViewModel(): HomeViewModel{
    val dismissHomeReviewUseCase by memoized<DismissHomeReviewUseCase>()
    val getAtcUseCase by memoized<AddToCartOccUseCase>()
    val getBusinessUnitDataUseCase by memoized<GetBusinessUnitDataUseCase>()
    val getBusinessWidgetTab by memoized<GetBusinessWidgetTab>()
    val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase>()
    val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase>()
    val getDynamicChannelsUseCase by memoized<GetDynamicChannelsUseCase>()
    val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase>()
    val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase>()
    val getHomeUseCase by memoized<HomeUseCase>()
    val getKeywordSearchUseCase by memoized<GetKeywordSearchUseCase>()
    val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase>()
    val getPopularKeywordUseCase by memoized<GetPopularKeywordUseCase>()
    val getRecommendationTabUseCase by memoized<GetRecommendationTabUseCase>()
    val getSendGeolocationInfoUseCase by memoized<SendGeolocationInfoUseCase>()
    val getStickyLoginUseCase by memoized<StickyLoginUseCase>()
    val userSessionInterface by memoized<UserSessionInterface>()
    val closeChannelUseCase by memoized<CloseChannelUseCase>()
    val injectCouponTimeBasedUseCase by memoized<InjectCouponTimeBasedUseCase>()
    val declineRechargeRecommendationUseCase by memoized<DeclineRechargeRecommendationUseCase>()
    val declineSalamWIdgetUseCase by memoized<DeclineSalamWIdgetUseCase>()
    val getSalamWidgetUseCase by memoized<GetSalamWidgetUseCase>()
    val getRechargeRecommendationUseCase by memoized<GetRechargeRecommendationUseCase>()
    val topAdsImageViewUseCase by memoized<TopAdsImageViewUseCase>()
    return HomeViewModel(
            dismissHomeReviewUseCase = Lazy{dismissHomeReviewUseCase},
            getBusinessUnitDataUseCase = Lazy{getBusinessUnitDataUseCase},
            getBusinessWidgetTab = Lazy{getBusinessWidgetTab},
            getDynamicChannelsUseCase = Lazy{getDynamicChannelsUseCase},
            getHomeReviewSuggestedUseCase = Lazy{getHomeReviewSuggestedUseCase},
            getHomeTokopointsDataUseCase = Lazy{getHomeTokopointsDataUseCase},
            getKeywordSearchUseCase = Lazy{getKeywordSearchUseCase},
            getPendingCashbackUseCase = Lazy{getCoroutinePendingCashbackUseCase},
            getPlayCardHomeUseCase = Lazy{getPlayLiveDynamicUseCase},
            getRecommendationTabUseCase = Lazy{getRecommendationTabUseCase},
            getWalletBalanceUseCase = Lazy{getCoroutineWalletBalanceUseCase},
            homeDispatcher = Lazy{TestDispatcherProvider()},
            homeUseCase = Lazy{getHomeUseCase},
            popularKeywordUseCase = Lazy{getPopularKeywordUseCase},
            sendGeolocationInfoUseCase = Lazy{getSendGeolocationInfoUseCase},
            stickyLoginUseCase = Lazy{getStickyLoginUseCase},
            getAtcUseCase = Lazy{getAtcUseCase},
            userSession = Lazy{userSessionInterface},
            closeChannelUseCase = Lazy{closeChannelUseCase},
            injectCouponTimeBasedUseCase = Lazy{injectCouponTimeBasedUseCase},
            declineSalamWIdgetUseCase = Lazy{declineSalamWIdgetUseCase},
            declineRechargeRecommendationUseCase = Lazy { declineRechargeRecommendationUseCase },
            getSalamWidgetUseCase = Lazy{getSalamWidgetUseCase},
            getRechargeRecommendationUseCase = Lazy{getRechargeRecommendationUseCase },
            topAdsImageViewUseCase = Lazy{topAdsImageViewUseCase}
    )
}

fun FeatureBody.createHomeViewModelTestInstance() {
    val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
    val dismissHomeReviewUseCase by memoized<DismissHomeReviewUseCase> { mockk(relaxed = true) }
    val getAtcUseCase by memoized<AddToCartOccUseCase>{ mockk(relaxed = true) }
    val getHomeReviewSuggestedUseCase by memoized<GetHomeReviewSuggestedUseCase> { mockk(relaxed = true) }
    val getKeywordSearchUseCase by memoized<GetKeywordSearchUseCase> { mockk(relaxed = true) }
    val getRecommendationTabUseCase by memoized<GetRecommendationTabUseCase> { mockk(relaxed = true) }
    val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase> { mockk(relaxed = true) }
    val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase> { mockk(relaxed = true) }
    val getPlayLiveDynamicUseCase by memoized<GetPlayLiveDynamicUseCase> { mockk(relaxed = true) }
    val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase> { mockk(relaxed = true) }
    val getHomeUseCase by memoized<HomeUseCase> { mockk(relaxed = true) }
    val getSendGeolocationInfoUseCase by memoized<SendGeolocationInfoUseCase> { mockk(relaxed = true) }
    val getStickyLoginUseCase by memoized<StickyLoginUseCase> { mockk(relaxed = true) }
    val getBusinessWidgetTab by memoized<GetBusinessWidgetTab> { mockk(relaxed = true) }
    val getBusinessUnitDataUseCase by memoized<GetBusinessUnitDataUseCase> { mockk(relaxed = true) }
    val getPopularKeywordUseCase by memoized<GetPopularKeywordUseCase> { mockk(relaxed = true) }
    val getDynamicChannelsUseCase by memoized<GetDynamicChannelsUseCase> { mockk(relaxed = true) }
    val closeChannelUseCase by memoized<CloseChannelUseCase> { mockk(relaxed = true) }
    val injectCouponTimeBasedUseCase by memoized<InjectCouponTimeBasedUseCase> { mockk(relaxed = true) }
    val homeDataMapper by memoized<HomeDataMapper> { mockk(relaxed = true) }
    val getRechargeRecommendationUseCase by memoized<GetRechargeRecommendationUseCase>{ mockk(relaxed = true)}
    val getSalamWidgetUseCase by memoized<GetSalamWidgetUseCase>{ mockk(relaxed = true)}
    val declineSalamWIdgetUseCase by memoized<DeclineSalamWIdgetUseCase>{ mockk(relaxed = true)}
    val declineRechargeRecommendationUseCase by memoized<DeclineRechargeRecommendationUseCase>{ mockk(relaxed = true)}
    val topAdsImageViewUseCase by  memoized<TopAdsImageViewUseCase>{mockk(relaxed = true)}
}

fun GetPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(channel: PlayChannel) {
    setParams()
    coEvery { executeOnBackground() } returns PlayData(
            playChannels = listOf(channel)
    )
}

fun GetBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget: HomeWidget) {
    coEvery { executeOnBackground() } returns homeWidget
}

fun GetDynamicChannelsUseCase.givenGetDynamicChannelsUseCase(dynamicChannelDataModels: List<DynamicChannelDataModel>) {
    coEvery { executeOnBackground() } returns dynamicChannelDataModels
}
fun GetDynamicChannelsUseCase.givenGetDynamicChannelsUseCaseThrowReturn() {
    coEvery { executeOnBackground() } throws TimeoutException()
}

fun GetBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseReturn(businessList: List<BusinessUnitItemDataModel>){
    coEvery{ executeOnBackground() } returns businessList
}
fun GetBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseThrowReturn(){
    coEvery{ executeOnBackground() } throws Exception()
}

fun GetRechargeRecommendationUseCase.givenGetRechargeRecommendationUseCase(rechargeRecommendation: RechargeRecommendation){
    coEvery { executeOnBackground() } returns rechargeRecommendation
}

fun GetRechargeRecommendationUseCase.givenGetRechargeRecommendationThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun DeclineRechargeRecommendationUseCase.givenDeclineRechargeRecommendationUseCase(declineRechargeRecommendation: DeclineRechargeRecommendation){
    coEvery { executeOnBackground() } returns declineRechargeRecommendation
}

fun GetSalamWidgetUseCase.givenGetSalamWidgetUseCase(salamWidget : SalamWidget){
    coEvery { executeOnBackground() } returns salamWidget
}

fun GetSalamWidgetUseCase.givenGetSalamWidgetThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun DeclineSalamWIdgetUseCase.givenDeclineSalamWidgetUseCase(salamWidget: SalamWidget){
    coEvery { executeOnBackground() } returns salamWidget
}

fun HomeUseCase.givenGetHomeDataReturn(homeDataModel: HomeDataModel) {
    coEvery { getHomeData() } returns flow{
        emit(homeDataModel)
    }
}
fun HomeUseCase.givenGetHomeDataReturn(homeDataModel: HomeDataModel, newHomeDataModel: HomeDataModel) {
    coEvery { getHomeData() } returns flow{
        emit(homeDataModel)
        emit(newHomeDataModel)
    }
}

fun InjectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(setInjectCouponTimeBased: SetInjectCouponTimeBased) {
    coEvery { executeOnBackground() } returns setInjectCouponTimeBased
}

fun InjectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseThrowReturn() {
    coEvery { executeOnBackground() } throws Exception()
}