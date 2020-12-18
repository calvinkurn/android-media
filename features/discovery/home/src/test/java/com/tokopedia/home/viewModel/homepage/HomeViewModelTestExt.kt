package com.tokopedia.home.viewModel.homepage

import android.app.Activity
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.TestDispatcherProvider
import com.tokopedia.home.util.HomeCommandProcessor
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

fun createHomeViewModel(
        getBusinessUnitDataUseCase: GetBusinessUnitDataUseCase = mockk(relaxed = true),
        getBusinessWidgetTab: GetBusinessWidgetTab = mockk(relaxed = true),
        getHomeUseCase: HomeUseCase = mockk(relaxed = true),
        userSessionInterface: UserSessionInterface = mockk(relaxed = true),
        dismissHomeReviewUseCase: DismissHomeReviewUseCase = mockk(relaxed = true),
        getAtcUseCase: AddToCartOccUseCase = mockk(relaxed = true),
        getHomeReviewSuggestedUseCase: GetHomeReviewSuggestedUseCase = mockk(relaxed = true),
        getKeywordSearchUseCase: GetKeywordSearchUseCase = mockk(relaxed = true),
        getRecommendationTabUseCase: GetRecommendationTabUseCase = mockk(relaxed = true),
        getHomeTokopointsDataUseCase: GetHomeTokopointsDataUseCase = mockk(relaxed = true),
        getHomeTokopointsListDataUseCase: GetHomeTokopointsListDataUseCase = mockk(relaxed = true),
        getCoroutinePendingCashbackUseCase: GetCoroutinePendingCashbackUseCase = mockk(relaxed = true),
        getPlayLiveDynamicUseCase: GetPlayLiveDynamicUseCase = mockk(relaxed = true),
        getCoroutineWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase = mockk(relaxed = true),
        getSendGeolocationInfoUseCase: SendGeolocationInfoUseCase = mockk(relaxed = true),
        getStickyLoginUseCase: StickyLoginUseCase = mockk(relaxed = true),
        getPopularKeywordUseCase: GetPopularKeywordUseCase = mockk(relaxed = true),
        getDynamicChannelsUseCase: GetDynamicChannelsUseCase = mockk(relaxed = true),
        getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true),
        getRecommendationFilterChips: GetRecommendationFilterChips = mockk(relaxed = true),
        closeChannelUseCase: CloseChannelUseCase = mockk(relaxed = true),
        injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase = mockk(relaxed = true),
        getRechargeRecommendationUseCase: GetRechargeRecommendationUseCase = mockk(relaxed = true),
        getSalamWidgetUseCase: GetSalamWidgetUseCase = mockk(relaxed = true),
        declineSalamWidgetUseCase: DeclineSalamWIdgetUseCase = mockk{ mockk(relaxed = true)},
        getRechargeBUWidgetUseCase: GetRechargeBUWidgetUseCase = mockk{ mockk(relaxed = true)},
        declineRechargeRecommendationUseCase: DeclineRechargeRecommendationUseCase = mockk(relaxed = true),
        topadsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true),
        getDisplayHeadlineAds: GetDisplayHeadlineAds = mockk(relaxed = true),
        playWidgetTools: PlayWidgetTools = mockk(relaxed = true),
        bestSellerMapper: BestSellerMapper = mockk(relaxed = true),
        dispatchers: TestDispatcherProvider = TestDispatcherProvider(),
        homeProcessor: HomeCommandProcessor = HomeCommandProcessor(Dispatchers.Unconfined)
): HomeViewModel{
    val context: Activity = mockk(relaxed = true)
    return HomeViewModel(
            homeUseCase = Lazy{ getHomeUseCase },
            userSession = Lazy{userSessionInterface},
            closeChannelUseCase = Lazy{closeChannelUseCase},
            dismissHomeReviewUseCase = Lazy{dismissHomeReviewUseCase},
            getAtcUseCase = Lazy{getAtcUseCase},
            getBusinessUnitDataUseCase = Lazy{getBusinessUnitDataUseCase},
            getBusinessWidgetTab = Lazy{getBusinessWidgetTab},
            getDisplayHeadlineAds = Lazy{ getDisplayHeadlineAds },
            getHomeReviewSuggestedUseCase = Lazy{getHomeReviewSuggestedUseCase},
            getHomeTokopointsListDataUseCase = Lazy{getHomeTokopointsListDataUseCase},
            getKeywordSearchUseCase = Lazy{getKeywordSearchUseCase},
            getPendingCashbackUseCase = Lazy{getCoroutinePendingCashbackUseCase},
            getPlayCardHomeUseCase = Lazy{getPlayLiveDynamicUseCase},
            getRecommendationTabUseCase = Lazy{getRecommendationTabUseCase},
            getRecommendationUseCase = Lazy{ getRecommendationUseCase},
            getRecommendationFilterChips = Lazy { getRecommendationFilterChips },
            getWalletBalanceUseCase = Lazy{getCoroutineWalletBalanceUseCase},
            popularKeywordUseCase = Lazy{getPopularKeywordUseCase},
            sendGeolocationInfoUseCase = Lazy{getSendGeolocationInfoUseCase},
            stickyLoginUseCase = Lazy{getStickyLoginUseCase},
            injectCouponTimeBasedUseCase = Lazy{injectCouponTimeBasedUseCase},
            getRechargeRecommendationUseCase = Lazy{getRechargeRecommendationUseCase},
            declineRechargeRecommendationUseCase = Lazy {declineRechargeRecommendationUseCase},
            getSalamWidgetUseCase = Lazy{getSalamWidgetUseCase},
            declineSalamWidgetUseCase = Lazy{declineSalamWidgetUseCase},
            getRechargeBUWidget = Lazy{getRechargeBUWidgetUseCase},
            topAdsImageViewUseCase = Lazy{topadsImageViewUseCase},
            bestSellerMapper = Lazy { bestSellerMapper },
            homeDispatcher = Lazy{ dispatchers },
            homeProcessor = Lazy{ homeProcessor },
            playWidgetTools = Lazy { playWidgetTools }
    )
}

fun GetPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(channel: PlayChannel) {
    setParams()
    coEvery { executeOnBackground() } returns PlayData(
            playChannels = listOf(channel)
    )
}

fun PlayWidgetTools.givenPlayWidgetToolsReturn(playWidget: PlayWidget, dispatchers: TestDispatcherProvider) {
    coEvery { getWidgetFromNetwork(PlayWidgetUseCase.WidgetType.Home, dispatchers.io()) } returns playWidget
}

fun GetBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget: HomeWidget) {
    coEvery { executeOnBackground() } returns homeWidget
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

fun HomeUseCase.givenGetDynamicChannelsUseCase(dynamicChannelDataModels: List<DynamicChannelDataModel>) {
    coEvery { onDynamicChannelExpired(any()) } returns dynamicChannelDataModels
}

fun InjectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(setInjectCouponTimeBased: SetInjectCouponTimeBased) {
    coEvery { executeOnBackground() } returns setInjectCouponTimeBased
}

fun InjectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseThrowReturn() {
    coEvery { executeOnBackground() } throws Exception()
}

fun areEqualKeyValues(first: Map<String, Any>, second: Map<String,Any>): Boolean{
    first.forEach{
        if(it.value != second[it.key]) return false
    }
    return true
}
