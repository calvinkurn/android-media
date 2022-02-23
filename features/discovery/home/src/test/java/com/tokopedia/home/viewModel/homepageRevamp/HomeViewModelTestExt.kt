package com.tokopedia.home.viewModel.homepageRevamp

import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBusinessUnitUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import net.bytebuddy.implementation.bytecode.Throw
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

@ExperimentalCoroutinesApi
fun createHomeViewModel(
        getHomeUseCase: HomeDynamicChannelUseCase = mockk(relaxed = true),
        userSessionInterface: UserSessionInterface = mockk(relaxed = true),
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
        homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase = mockk(relaxed = true),
        homeSuggestedReviewUseCase: HomeSuggestedReviewUseCase = mockk(relaxed = true),
        homeListCarouselUseCase: HomeListCarouselUseCase = mockk(relaxed = true),
        homePlayUseCase: HomePlayUseCase = mockk(relaxed = true),
        homePopularKeywordUseCase: HomePopularKeywordUseCase = mockk(relaxed = true),
        homeRechargeRecommendationUseCase: HomeRechargeRecommendationUseCase = mockk(relaxed = true),
        homeRechargeBuWidgetUseCase: HomeRechargeBuWidgetUseCase = mockk(relaxed = true),
        homeRecommendationUseCase: HomeRecommendationUseCase = mockk(relaxed = true),
        homeSalamRecommendationUseCase: HomeSalamRecommendationUseCase = mockk(relaxed = true),
        homeSearchUseCase: HomeSearchUseCase = mockk(relaxed = true),
        homeBusinessUnitUseCase: HomeBusinessUnitUseCase = mockk(relaxed = true),
        homeBeautyFestUseCase: HomeBeautyFestUseCase = mockk(relaxed = true),
        getCMHomeWidgetDataUseCase : GetCMHomeWidgetDataUseCase = mockk(relaxed = true),
        deleteCMHomeWidgetUseCase: DeleteCMHomeWidgetUseCase = mockk(relaxed = true)
): HomeRevampViewModel{
    homeBalanceWidgetUseCase.givenGetLoadingStateReturn()
    return HomeRevampViewModel(
            homeDispatcher = Lazy{ dispatchers },
            homeUseCase = Lazy{ getHomeUseCase },
            userSession = Lazy{userSessionInterface},
            homeBalanceWidgetUseCase = Lazy { homeBalanceWidgetUseCase },
            homeSuggestedReviewUseCase = Lazy { homeSuggestedReviewUseCase },
            homeListCarouselUseCase = Lazy { homeListCarouselUseCase },
            homePlayUseCase = Lazy { homePlayUseCase },
            homePopularKeywordUseCase = Lazy { homePopularKeywordUseCase },
            homeRechargeBuWidgetUseCase = Lazy { homeRechargeBuWidgetUseCase },
            homeRechargeRecommendationUseCase = Lazy { homeRechargeRecommendationUseCase },
            homeRecommendationUseCase = Lazy { homeRecommendationUseCase },
            homeSalamRecommendationUseCase = Lazy { homeSalamRecommendationUseCase },
            homeSearchUseCase = Lazy { homeSearchUseCase },
            homeBusinessUnitUseCase = Lazy { homeBusinessUnitUseCase },
            homeBeautyFestUseCase = Lazy { homeBeautyFestUseCase },
            getCMHomeWidgetDataUseCase = Lazy{ getCMHomeWidgetDataUseCase },
            deleteCMHomeWidgetUseCase = Lazy{ deleteCMHomeWidgetUseCase }
    )
}

@ExperimentalCoroutinesApi
fun createHomeDynamicChannelUseCase(
        homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase = mockk(relaxed = true),
        homeDataMapper: HomeDataMapper = mockk(relaxed = true),
        homeDynamicChannelsRepository: HomeDynamicChannelsRepository = mockk(relaxed = true),
        homeDataRepository: HomeDataRepository = mockk(relaxed = true),
        homeAtfRepository: HomeAtfRepository = mockk(relaxed = true),
        homeFlagRepository: HomeFlagRepository = mockk(relaxed = true),
        homePageBannerRepository: HomePageBannerRepository = mockk(relaxed = true),
        homeTickerRepository: HomeTickerRepository = mockk(relaxed = true),
        homeIconRepository: HomeIconRepository = mockk(relaxed = true),
        homeRoomDataSource: HomeRoomDataSource = mockk(relaxed = true),
        homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper = mockk(relaxed = true),
        applicationContext: Context = mockk(relaxed = true),
        remoteConfig: RemoteConfig = mockk(relaxed = true),
        homePlayRepository: HomePlayRepository = mockk(relaxed = true),
        homeReviewSuggestedRepository: HomeReviewSuggestedRepository = mockk(relaxed = true),
        homePlayLiveDynamicRepository: HomePlayLiveDynamicRepository = mockk(relaxed = true),
        homePopularKeywordRepository: HomePopularKeywordRepository = mockk(relaxed = true),
        homeHeadlineAdsRepository: HomeHeadlineAdsRepository = mockk(relaxed = true),
        homeRecommendationRepository: HomeRecommendationRepository = mockk(relaxed = true),
        homeRecommendationChipRepository: HomeRecommendationChipRepository = mockk(relaxed = true),
        bestSellerMapper: BestSellerMapper = mockk(relaxed = true),
        homeTopadsImageRepository: HomeTopadsImageRepository = mockk(relaxed = true),
        homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository = mockk(relaxed = true),
        homeSalamWidgetRepository: HomeSalamWidgetRepository = mockk(relaxed = true),
        homeRecommendationFeedTabRepository: HomeRecommendationFeedTabRepository = mockk(relaxed = true),
        homeChooseAddressRepository: HomeChooseAddressRepository = mockk(relaxed = true),
        homeUserSessionInterface: UserSessionInterface = mockk(relaxed = true)
): HomeDynamicChannelUseCase {
    return HomeDynamicChannelUseCase(
            homeBalanceWidgetUseCase = homeBalanceWidgetUseCase,
            homeDataMapper = homeDataMapper,
            homeDynamicChannelsRepository = homeDynamicChannelsRepository,
            homeDataRepository = homeDataRepository,
            atfDataRepository = homeAtfRepository,
            homeFlagRepository = homeFlagRepository,
            homePageBannerRepository = homePageBannerRepository,
            homeTickerRepository = homeTickerRepository,
            homeIconRepository = homeIconRepository,
            getHomeRoomDataSource = homeRoomDataSource,
            homeDynamicChannelDataMapper = homeDynamicChannelDataMapper,
            applicationContext = applicationContext,
            remoteConfig = remoteConfig,
            homePlayRepository = homePlayRepository,
            homeReviewSuggestedRepository = homeReviewSuggestedRepository,
            homePlayLiveDynamicRepository = homePlayLiveDynamicRepository,
            homePopularKeywordRepository = homePopularKeywordRepository,
            homeHeadlineAdsRepository = homeHeadlineAdsRepository,
            homeRecommendationRepository = homeRecommendationRepository,
            homeRecommendationChipRepository = homeRecommendationChipRepository,
            bestSellerMapper = bestSellerMapper,
            homeTopadsImageRepository = homeTopadsImageRepository,
            homeRechargeRecommendationRepository = homeRechargeRecommendationRepository,
            homeSalamWidgetRepository = homeSalamWidgetRepository,
            homeRecommendationFeedTabRepository = homeRecommendationFeedTabRepository,
            homeChooseAddressRepository = homeChooseAddressRepository,
            userSessionInterface = homeUserSessionInterface
    )
}

fun HomeRoomDataSource.givenGetHomeRoomDataReturn(homeData: HomeData) {
    coEvery { getCachedHomeData() } returns flow {
        homeData
    }
}

fun HomeSuggestedReviewUseCase.givenOnReviewDismissedReturn() {
    coEvery { onReviewDismissed() } answers ({})
}

fun HomeDynamicChannelUseCase.givenGetHomeDataReturn(homeDynamicChannelModel: HomeDynamicChannelModel? = createDefaultHomeDataModel()) {
    coEvery { getHomeDataFlow() } returns flow{
        emit(homeDynamicChannelModel)
    }
}

fun HomePlayUseCase.givenOnUpdatePlayToggleReminderReturn(returnValue: Boolean = true) {
    coEvery { onUpdatePlayWidgetToggleReminder(any(), any()) } returns returnValue
}

fun HomePlayUseCase.givenOnGetPlayWidgetUiModelReturn(playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true)) {
    coEvery { onGetPlayWidgetUiModel(any(), any(), any()) } returns playWidgetState
}

fun HomePlayUseCase.givenOnUpdatePlayTotalViewReturn(carouselPlayWidgetDataModel: CarouselPlayWidgetDataModel = CarouselPlayWidgetDataModel(DynamicHomeChannel.Channels())) {
    coEvery { onUpdatePlayTotalView(any(), any(), any()) } returns carouselPlayWidgetDataModel
}

fun HomePlayUseCase.givenOnUpdateActionReminderReturn(carouselPlayWidgetDataModel: CarouselPlayWidgetDataModel = CarouselPlayWidgetDataModel(DynamicHomeChannel.Channels())) {
    coEvery { onUpdateActionReminder(any(), any(), any()) } returns carouselPlayWidgetDataModel
}

fun HomePlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshReturn(playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true)) {
    coEvery { onGetPlayWidgetWhenShouldRefresh() } returns playWidgetState
}

fun HomePlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshError() {
    coEvery { onGetPlayWidgetWhenShouldRefresh() } throws Exception()
}


fun HomeDynamicChannelUseCase.givenGetHomeDataError(t: Throwable = Throwable("Unit test simulate error")) {
    coEvery { getHomeDataFlow() } returns flow{
        throw t
    }
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataReturn(result: com.tokopedia.home.beranda.helper.Result<Any>) {
    coEvery { updateHomeData() } returns flow{
        emit(result)
    }
}

fun HomeBalanceWidgetUseCase.givenGetLoadingStateReturn() {
    coEvery { onGetBalanceWidgetLoadingState(any()) } returns HomeHeaderDataModel()
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataError(t: Throwable = Throwable("Unit test simulate error")) {
    mockkStatic(Log::class)
    every { Log.getStackTraceString(t) } returns ""
    coEvery { updateHomeData() } returns flow{
        throw t
    }
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataErrorNullMessage() {
    val throwable = Throwable()
    mockkStatic(Log::class)
    every { Log.getStackTraceString(throwable) } returns ""
    coEvery { updateHomeData() } returns flow{
        throw throwable
    }
}

fun HomeDynamicChannelUseCase.givenOnDynamicChannelExpiredReturns(visitableList: List<Visitable<*>> = listOf()) {
    coEvery { onDynamicChannelExpired(any()) } returns visitableList
}

fun HomeDynamicChannelUseCase.givenOnDynamicChannelExpiredError() {
    coEvery { onDynamicChannelExpired(any()) } throws Exception()
}

fun HomeListCarouselUseCase.givenOnOneClickCheckoutReturn(mapResponse: Map<String, Any> = mapOf()) {
    coEvery { onOneClickCheckOut(any(), any(), any(), any()) } returns mapResponse
}

fun HomeListCarouselUseCase.givenOnOneClickCheckoutError() {
    coEvery { onOneClickCheckOut(any(), any(), any(), any()) } throws Exception()
}

fun HomeListCarouselUseCase.givenOnClickCloseListCarouselReturn(value: Boolean) {
    coEvery { onClickCloseListCarousel(any()) } returns value
}

fun HomeRechargeRecommendationUseCase.givenOnDeclineRechargeRecommendationSuccess() {
    coEvery { onDeclineRechargeRecommendation(any()) } answers { }
}

fun HomeSalamRecommendationUseCase.givenOnDeclineSalamRecommendationSuccess() {
    coEvery { onDeclineSalamRecommendation(any()) } answers { }
}

fun HomeRechargeRecommendationUseCase.givenOnDeclineRechargeRecommendationError() {
    coEvery { onDeclineRechargeRecommendation(any()) } throws Exception()
}

fun HomeSalamRecommendationUseCase.givenOnDeclineSalamRecommendationError() {
    coEvery { onDeclineSalamRecommendation(any()) } throws Exception()
}


fun HomeRecommendationUseCase.givenOnHomeBestSellerFilterClickReturn(bestSellerDataModel: BestSellerDataModel = BestSellerDataModel()) {
    coEvery { onHomeBestSellerFilterClick(any(), any(), any()) } returns bestSellerDataModel
}

fun HomeRechargeBuWidgetUseCase.givenOnGetRechargeBuWidgetFromHolderReturn(rechargeBUWidgetDataModel: RechargeBUWidgetDataModel) {
    coEvery { onGetRechargeBuWidgetFromHolder(any(), any()) } returns rechargeBUWidgetDataModel
}

fun HomeRechargeBuWidgetUseCase.givenOnGetRechargeBuWidgetFromHolderError() {
    coEvery { onGetRechargeBuWidgetFromHolder(any(), any()) } throws Exception()
}

fun HomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetBalanceWidgetData(any()) } returns homeHeaderDataModel
}

fun HomeBalanceWidgetUseCase.givenGetTokopointDataReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetTokopointData(any()) } returns homeHeaderDataModel
}

fun HomeBalanceWidgetUseCase.givenGetBalanceWidgetDataReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetWalletAppData(any()) } returns homeHeaderDataModel
}

fun HomeDynamicChannelUseCase.givenGetHomeDataReturn(homeDynamicChannelModel: HomeDynamicChannelModel, newHomeDynamicChannelModel: HomeDynamicChannelModel) {
    coEvery { getHomeDataFlow() } returns flow{
        emit(homeDynamicChannelModel)
        emit(newHomeDynamicChannelModel)
    }
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
    tabId: Int,
    resultBuModel: NewBusinessUnitWidgetDataModel,
    positionTab: Int,
    homeDataModel: HomeDynamicChannelModel,
    buModel: NewBusinessUnitWidgetDataModel,
    positionBuModelIndex: Int,
    tabName: String
) {
    coEvery { getBusinessUnitData(tabId, positionTab, tabName, homeDataModel, buModel, positionBuModelIndex) } returns resultBuModel
}

fun HomeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnTrue(
    data: HomeDynamicChannelModel
) {
    coEvery { getBeautyFest(data) } returns HomeRevampFragment.BEAUTY_FEST_TRUE
}

fun HomeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnFalse(
    data: HomeDynamicChannelModel
) {
    coEvery { getBeautyFest(data) } returns HomeRevampFragment.BEAUTY_FEST_FALSE
}

fun HomeBeautyFestUseCase.givenGetBeautyFestUseCaseReturnNotSet(
    data: HomeDynamicChannelModel
) {
    coEvery { getBeautyFest(data) } returns HomeRevampFragment.BEAUTY_FEST_NOT_SET
}

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationUseCase(rechargeRecommendation: RechargeRecommendation){
    coEvery { executeOnBackground() } returns rechargeRecommendation
}

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun HomeDeclineRechargeRecommendationRepository.givenDeclineRechargeRecommendationUseCase(declineRechargeRecommendation: DeclineRechargeRecommendation){
    coEvery { executeOnBackground() } returns declineRechargeRecommendation
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetUseCase(salamWidget : SalamWidget){
    coEvery { executeOnBackground() } returns salamWidget
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetThrowReturn(){
    coEvery { executeOnBackground() } throws Exception()
}

fun HomeDeclineSalamWIdgetRepository.givenDeclineSalamWidgetUseCase(salamWidget: SalamWidget){
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

fun HomePopularKeywordUseCase.givenOnPopularKeywordReturn(
    refreshCount: Int,
    currentPopularKeyword: PopularKeywordListDataModel,
    resultPopularKeyword: PopularKeywordListDataModel
) {
    coEvery { onPopularKeywordRefresh(refreshCount, currentPopularKeyword) } returns resultPopularKeyword
}

fun areEqualKeyValues(first: Map<String, Any>, second: Map<String,Any>): Boolean{
    first.forEach{
        if(it.value != second[it.key]) return false
    }
    return true
}

inline fun <reified T> HomeDynamicChannelModel.findWidget(predicate: (T?) -> Boolean = {true}, actionOnFound: (T, Int) -> Unit, actionOnNotFound: () -> Unit) {
    this.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
        it?.let {
            if (it.value is T) {
                actionOnFound.invoke(it.value as T, it. index)
                return
            }
        }
    }
    actionOnNotFound.invoke()
}

inline fun <reified T> HomeDynamicChannelModel.findWidgetList(actionOnFound: (List<T>) -> Unit) {
    this.list.filterIsInstance<T>().let {
        actionOnFound.invoke(it)
    }
}