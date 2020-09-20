package com.tokopedia.home.viewModel.homepage

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
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
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
        getCoroutinePendingCashbackUseCase: GetCoroutinePendingCashbackUseCase = mockk(relaxed = true),
        getPlayLiveDynamicUseCase: GetPlayLiveDynamicUseCase = mockk(relaxed = true),
        getPlayBannerUseCase: GetPlayWidgetUseCase = mockk(relaxed = true),
        playToggleChannelReminderUseCase: PlayToggleChannelReminderUseCase = mockk(relaxed = true),
        getCoroutineWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase = mockk(relaxed = true),
        getSendGeolocationInfoUseCase: SendGeolocationInfoUseCase = mockk(relaxed = true),
        getStickyLoginUseCase: StickyLoginUseCase = mockk(relaxed = true),
        getPopularKeywordUseCase: GetPopularKeywordUseCase = mockk(relaxed = true),
        getDynamicChannelsUseCase: GetDynamicChannelsUseCase = mockk(relaxed = true),
        closeChannelUseCase: CloseChannelUseCase = mockk(relaxed = true),
        injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase = mockk(relaxed = true),
        getRechargeRecommendationUseCase: GetRechargeRecommendationUseCase = mockk(relaxed = true),
        getSalamWidgetUseCase: GetSalamWidgetUseCase = mockk(relaxed = true),
        declineSalamWidgetUseCase: DeclineSalamWIdgetUseCase = mockk{ mockk(relaxed = true)},
        declineRechargeRecommendationUseCase: DeclineRechargeRecommendationUseCase = mockk(relaxed = true),
        topadsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true),
        dispatcher: TestDispatcherProvider = TestDispatcherProvider()
): HomeViewModel{


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
            homeDispatcher = Lazy{ dispatcher },
            homeUseCase = Lazy{ getHomeUseCase },
            popularKeywordUseCase = Lazy{getPopularKeywordUseCase},
            sendGeolocationInfoUseCase = Lazy{getSendGeolocationInfoUseCase},
            stickyLoginUseCase = Lazy{getStickyLoginUseCase},
            getAtcUseCase = Lazy{getAtcUseCase},
            userSession = Lazy{userSessionInterface},
            closeChannelUseCase = Lazy{closeChannelUseCase},
            injectCouponTimeBasedUseCase = Lazy{injectCouponTimeBasedUseCase},
            declineSalamWidgetUseCase = Lazy{declineSalamWidgetUseCase},
            declineRechargeRecommendationUseCase = Lazy {declineRechargeRecommendationUseCase},
            getSalamWidgetUseCase = Lazy{getSalamWidgetUseCase},
            topAdsImageViewUseCase = Lazy{topadsImageViewUseCase},
            getPlayBannerUseCase = Lazy{getPlayBannerUseCase},
            playToggleChannelReminderUseCase = Lazy{playToggleChannelReminderUseCase},
            getRechargeRecommendationUseCase = Lazy{getRechargeRecommendationUseCase}
    )
}

fun GetPlayLiveDynamicUseCase.givenGetPlayLiveDynamicUseCaseReturn(channel: PlayChannel) {
    setParams()
    coEvery { executeOnBackground() } returns PlayData(
            playChannels = listOf(channel)
    )
}

fun GetPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturn(playBannerCarouselDataModel: PlayBannerCarouselDataModel) {
    coEvery { executeOnBackground() } returns playBannerCarouselDataModel
}

fun GetPlayWidgetUseCase.givenGetPlayCarouselUseCaseReturnError() {
    coEvery { executeOnBackground() } throws TimeoutException()
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

fun areEqualKeyValues(first: Map<String, Any>, second: Map<String,Any>): Boolean{
    first.forEach{
        if(it.value != second[it.key]) return false
    }
    return true
}
