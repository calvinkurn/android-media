package com.tokopedia.home.viewModel.homepageRevamp

import android.app.Activity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBusinessUnitUseCase
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

fun createHomeViewModel(
        getHomeUseCase: HomeDynamicChannelUseCase = mockk(relaxed = true),
        userSessionInterface: UserSessionInterface = mockk(relaxed = true),
        dismissHomeReviewUseCase: DismissHomeReviewUseCase = mockk(relaxed = true),
        getAtcUseCase: AddToCartOccMultiUseCase = mockk(relaxed = true),
        homeReviewSuggestedRepository: HomeReviewSuggestedRepository = mockk(relaxed = true),
        getKeywordSearchUseCase: GetKeywordSearchUseCase = mockk(relaxed = true),
        getRecommendationTabUseCase: GetRecommendationTabUseCase = mockk(relaxed = true),
        getHomeTokopointsListDataUseCase: GetHomeTokopointsListDataUseCase = mockk(relaxed = true),
        getCoroutinePendingCashbackUseCase: GetCoroutinePendingCashbackUseCase = mockk(relaxed = true),
        homePlayLiveDynamicRepository: HomePlayLiveDynamicRepository = mockk(relaxed = true),
        getCoroutineWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase = mockk(relaxed = true),
        homePopularKeywordRepository: HomePopularKeywordRepository = mockk(relaxed = true),
        getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true),
        getRecommendationFilterChips: GetRecommendationFilterChips = mockk(relaxed = true),
        closeChannelUseCase: CloseChannelUseCase = mockk(relaxed = true),
        injectCouponTimeBasedUseCase: InjectCouponTimeBasedUseCase = mockk(relaxed = true),
        homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository = mockk(relaxed = true),
        homeSalamWidgetRepository: HomeSalamWidgetRepository = mockk(relaxed = true),
        declineSalamWidgetUseCase: DeclineSalamWIdgetUseCase = mockk{ mockk(relaxed = true)},
        getRechargeBUWidgetUseCase: GetRechargeBUWidgetUseCase = mockk{ mockk(relaxed = true)},
        declineRechargeRecommendationUseCase: DeclineRechargeRecommendationUseCase = mockk(relaxed = true),
        topadsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true),
        getDisplayHeadlineAds: GetDisplayHeadlineAds = mockk(relaxed = true),
        playWidgetTools: PlayWidgetTools = mockk(relaxed = true),
        bestSellerMapper: BestSellerMapper = mockk(relaxed = true),
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
        getWalletAppBalanceUseCase: GetWalletAppBalanceUseCase = mockk(relaxed = true),
        getWalletEligibilityUseCase: GetWalletEligibilityUseCase = mockk(relaxed = true),
        homeBusinessWidgetUseCase: HomeBusinessUnitUseCase = mockk(relaxed = true),
        homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase = mockk(relaxed = true)
): HomeRevampViewModel{
    val context: Activity = mockk(relaxed = true)
    return HomeRevampViewModel(
            dismissHomeReviewUseCase = Lazy{dismissHomeReviewUseCase},
            //TODO fix this for unit test
//            getHomeReviewSuggestedUseCase = Lazy{homeReviewSuggestedRepository},
            getKeywordSearchUseCase = Lazy{getKeywordSearchUseCase},
            //TODO fix this for unit test
//            getPlayCardHomeUseCase = Lazy{homePlayLiveDynamicRepository},
            homeDispatcher = Lazy{ dispatchers },
            homeUseCase = Lazy{ getHomeUseCase },
            //TODO fix this for unit test
//            popularKeywordUseCase = Lazy{homePopularKeywordRepository},
            injectCouponTimeBasedUseCase = Lazy{injectCouponTimeBasedUseCase},
            getAtcUseCase = Lazy{getAtcUseCase},
            userSession = Lazy{userSessionInterface},
            closeChannelUseCase = Lazy{closeChannelUseCase},
            declineSalamWidgetUseCase = Lazy{declineSalamWidgetUseCase},
            declineRechargeRecommendationUseCase = Lazy {declineRechargeRecommendationUseCase},
            //TODO fix this for unit test
//            getSalamWidgetUseCase = Lazy{homeSalamWidgetRepository},
            getRechargeBUWidgetUseCase = Lazy{getRechargeBUWidgetUseCase},
            getRecommendationUseCase = Lazy{ getRecommendationUseCase},
            //TODO fix this for unit test
//            getRechargeRecommendationUseCase = Lazy{homeRechargeRecommendationRepository},
            playWidgetTools = Lazy { playWidgetTools },
            bestSellerMapper = Lazy { bestSellerMapper },
            homeBusinessUnitUseCase = Lazy { homeBusinessWidgetUseCase },
            homeBalanceWidgetUseCase = homeBalanceWidgetUseCase,
            homePlayCardHomeRepository = Lazy { homePlayLiveDynamicRepository },
            popularKeywordRepository = Lazy { homePopularKeywordRepository }
    )
}

fun HomeDynamicChannelUseCase.givenGetHomeDataReturn(homeDynamicChannelModel: HomeDynamicChannelModel? = createDefaultHomeDataModel()) {
//    coEvery { getHomeData() } returns flow{
//        emit(homeDynamicChannelModel)
//    }
}

fun HomeDynamicChannelUseCase.givenGetHomeDataReturn(homeDynamicChannelModel: HomeDynamicChannelModel, newHomeDynamicChannelModel: HomeDynamicChannelModel) {
//    coEvery { getHomeData() } returns flow{
//        emit(homeDynamicChannelModel)
//        emit(newHomeDynamicChannelModel)
//    }
}

fun HomeDynamicChannelUseCase.givenGetDynamicChannelsUseCase(dynamicChannelDataModels: List<DynamicChannelDataModel>) {
    coEvery { onDynamicChannelExpired(any()) } returns dynamicChannelDataModels
}

fun createDefaultHomeDataModel(): HomeDynamicChannelModel {
    return HomeDynamicChannelModel(
            list = listOf<Visitable<*>>(
                    DynamicLegoBannerDataModel(ChannelModel(id = "1", groupId = "1")),
                    DynamicLegoBannerDataModel(ChannelModel(id = "2", groupId = "1")),
                    DynamicLegoBannerDataModel(ChannelModel(id = "3", groupId = "1")),
                    DynamicLegoBannerDataModel(ChannelModel(id = "4", groupId = "1")),
                    DynamicLegoBannerDataModel(ChannelModel(id = "5", groupId = "1"))
            )
    )
}

fun HomePlayLiveDynamicRepository.givenGetPlayLiveDynamicUseCaseReturn(channel: PlayChannel) {
    setParams()
    coEvery { executeOnBackground() } returns PlayData(
            playChannels = listOf(channel)
    )
}

fun PlayWidgetTools.givenPlayWidgetToolsReturn(playWidget: PlayWidget, dispatchers: CoroutineDispatchers) {
    coEvery { getWidgetFromNetwork(PlayWidgetUseCase.WidgetType.Home, dispatchers.io) } returns playWidget
}

fun HomeBusinessUnitUseCase.givenGetBusinessWidgetTabUseCaseReturn(
    newBusinessUnitWidgetDataModel: NewBusinessUnitWidgetDataModel
) {
    coEvery { getBusinessUnitTab(newBusinessUnitWidgetDataModel) } returns newBusinessUnitWidgetDataModel
}

fun GetDynamicChannelsUseCase.givenGetDynamicChannelsUseCaseThrowReturn() {
    coEvery { executeOnBackground() } throws TimeoutException()
}

fun HomeBusinessUnitUseCase.givenGetBusinessUnitDataUseCaseReturn(
    resultBuModel: NewBusinessUnitWidgetDataModel,
    positionTab: Int,
    homeDataModel: HomeDynamicChannelModel,
    buModel: NewBusinessUnitWidgetDataModel,
    positionBuModelIndex: Int,
    tabName: String
) {
    coEvery { getBusinessUnitData(0, positionTab, tabName, homeDataModel, buModel, positionBuModelIndex) } returns resultBuModel
}

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationUseCase(rechargeRecommendation: RechargeRecommendation){
    coEvery { executeOnBackground() } returns rechargeRecommendation
}

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun DeclineRechargeRecommendationUseCase.givenDeclineRechargeRecommendationUseCase(declineRechargeRecommendation: DeclineRechargeRecommendation){
    coEvery { executeOnBackground() } returns declineRechargeRecommendation
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetUseCase(salamWidget : SalamWidget){
    coEvery { executeOnBackground() } returns salamWidget
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun DeclineSalamWIdgetUseCase.givenDeclineSalamWidgetUseCase(salamWidget: SalamWidget){
    coEvery { executeOnBackground() } returns salamWidget
}

fun GetRechargeBUWidgetUseCase.givenGetRechargeBUWidgetUseCase(rechargePerso: RechargePerso){
    coEvery { executeOnBackground() } returns rechargePerso
}

fun GetRechargeBUWidgetUseCase.givenGetRechargeBUWidgetThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
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

inline fun <reified T> HomeDynamicChannelModel.findWidget(predicate: (T?) -> Boolean = {true}, actionOnFound: (T, Int) -> Unit) {
    this.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
        it?.let {
            if (it.value is T) {
                actionOnFound.invoke(it.value as T, it. index)
            }
        }
    }
}

inline fun <reified T> HomeDynamicChannelModel.findWidgetList(actionOnFound: (List<T>) -> Unit) {
    this.list.filterIsInstance<T>().let {
        actionOnFound.invoke(it)
    }
}