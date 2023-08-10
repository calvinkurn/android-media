package com.tokopedia.home.viewModel.homepageRevamp

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.gopayhomewidget.domain.usecase.ClosePayLaterWidgetUseCase
import com.tokopedia.gopayhomewidget.domain.usecase.GetPayLaterWidgetUseCase
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineRechargeRecommendationRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineSalamWIdgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeDynamicChannelsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeHeadlineAdsRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeIconRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePageBannerRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePlayRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomePopularKeywordRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRechargeRecommendationRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationChipRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationFeedTabRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeRecommendationRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeReviewSuggestedRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeSalamWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTickerRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTodoWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTopadsImageRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeUserStatusRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBusinessUnitUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeListCarouselUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeMissionWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomePlayUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomePopularKeywordUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRechargeBuWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeSalamRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeSearchUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeSuggestedReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeTodoWidgetUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.usecase.todowidget.DismissTodoWidgetUseCase
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeoutException
import com.tokopedia.home.beranda.data.mapper.BestSellerMapper as BestSellerRevampMapper

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
    getCMHomeWidgetDataUseCase: GetCMHomeWidgetDataUseCase = mockk(relaxed = true),
    deleteCMHomeWidgetUseCase: DeleteCMHomeWidgetUseCase = mockk(relaxed = true),
    deletePayLaterWidgetUseCase: ClosePayLaterWidgetUseCase = mockk(relaxed = true),
    getPayLaterWidgetUseCase: GetPayLaterWidgetUseCase = mockk(relaxed = true),
    homeMissionWidgetUseCase: HomeMissionWidgetUseCase = mockk(relaxed = true),
    homeTodoWidgetUseCase: HomeTodoWidgetUseCase = mockk(relaxed = true),
    homeDismissTodoWidgetUseCase: DismissTodoWidgetUseCase = mockk(relaxed = true),
    homeRateLimit: RateLimiter<String> = mockk(relaxed = true)
): HomeRevampViewModel {
    homeBalanceWidgetUseCase.givenGetLoadingStateReturn()
    return spyk(
        HomeRevampViewModel(
            homeDispatcher = Lazy { dispatchers },
            homeUseCase = Lazy { getHomeUseCase },
            userSession = Lazy { userSessionInterface },
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
            getCMHomeWidgetDataUseCase = Lazy { getCMHomeWidgetDataUseCase },
            deleteCMHomeWidgetUseCase = Lazy { deleteCMHomeWidgetUseCase },
            deletePayLaterWidgetUseCase = Lazy { deletePayLaterWidgetUseCase },
            getPayLaterWidgetUseCase = Lazy { getPayLaterWidgetUseCase },
            homeMissionWidgetUseCase = Lazy { homeMissionWidgetUseCase },
            homeTodoWidgetUseCase = Lazy { homeTodoWidgetUseCase },
            homeDismissTodoWidgetUseCase = Lazy { homeDismissTodoWidgetUseCase },
            homeRateLimit = homeRateLimit
        ),
        recordPrivateCalls = true
    )
}

@ExperimentalCoroutinesApi
fun createHomeDynamicChannelUseCase(
    homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase = mockk(relaxed = true),
    homeDataMapper: HomeDataMapper = mockk(relaxed = true),
    bestSellerRevampMapper: BestSellerRevampMapper = mockk(relaxed = true),
    homeDynamicChannelsRepository: HomeDynamicChannelsRepository = mockk(relaxed = true),
    homeAtfRepository: HomeAtfRepository = mockk(relaxed = true),
    homeUserStatusRepository: HomeUserStatusRepository = mockk(relaxed = true),
    homePageBannerRepository: HomePageBannerRepository = mockk(relaxed = true),
    homeTickerRepository: HomeTickerRepository = mockk(relaxed = true),
    homeIconRepository: HomeIconRepository = mockk(relaxed = true),
    homeRoomDataSource: HomeRoomDataSource = mockk(relaxed = true),
    homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper = mockk(relaxed = true),
    applicationContext: Context = mockk(relaxed = true),
    remoteConfig: RemoteConfig = mockk(relaxed = true),
    homePlayRepository: HomePlayRepository = mockk(relaxed = true),
    homeReviewSuggestedRepository: HomeReviewSuggestedRepository = mockk(relaxed = true),
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
    homeUserSessionInterface: UserSessionInterface = mockk(relaxed = true),
    homeMissionWidgetRepository: HomeMissionWidgetRepository = mockk(relaxed = true),
    homeTodoWidgetRepository: HomeTodoWidgetRepository = mockk(relaxed = true)
): HomeDynamicChannelUseCase {
    return HomeDynamicChannelUseCase(
        homeBalanceWidgetUseCase = homeBalanceWidgetUseCase,
        homeDataMapper = homeDataMapper,
        bestSellerRevampMapper = bestSellerRevampMapper,
        homeDynamicChannelsRepository = homeDynamicChannelsRepository,
        atfDataRepository = homeAtfRepository,
        homeUserStatusRepository = homeUserStatusRepository,
        homePageBannerRepository = homePageBannerRepository,
        homeTickerRepository = homeTickerRepository,
        homeIconRepository = homeIconRepository,
        getHomeRoomDataSource = homeRoomDataSource,
        homeDynamicChannelDataMapper = homeDynamicChannelDataMapper,
        applicationContext = applicationContext,
        remoteConfig = remoteConfig,
        homePlayRepository = homePlayRepository,
        homeReviewSuggestedRepository = homeReviewSuggestedRepository,
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
        userSessionInterface = homeUserSessionInterface,
        homeMissionWidgetRepository = homeMissionWidgetRepository,
        homeTodoWidgetRepository = homeTodoWidgetRepository
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
    coEvery { getHomeDataFlow() } returns flow {
        emit(homeDynamicChannelModel)
    }
}

fun HomePlayUseCase.givenOnUpdatePlayToggleReminderReturn(returnValue: Boolean = true) {
    coEvery { onUpdatePlayWidgetToggleReminder(any(), any()) } returns returnValue
}

fun HomePlayUseCase.givenOnGetPlayWidgetUiModelReturn(playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true)) {
    coEvery { onGetPlayWidgetUiModel(any(), any(), any()) } returns playWidgetState
}

fun HomePlayUseCase.givenOnUpdatePlayTotalViewReturn(carouselPlayWidgetDataModel: CarouselPlayWidgetDataModel = CarouselPlayWidgetDataModel(ChannelModel("", ""))) {
    coEvery { onUpdatePlayTotalView(any(), any(), any()) } returns carouselPlayWidgetDataModel
}

fun HomePlayUseCase.givenOnUpdateActionReminderReturn(carouselPlayWidgetDataModel: CarouselPlayWidgetDataModel = CarouselPlayWidgetDataModel(ChannelModel("", ""))) {
    coEvery { onUpdateActionReminder(any(), any(), any()) } returns carouselPlayWidgetDataModel
}

fun HomePlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshReturn(playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true)) {
    coEvery { onGetPlayWidgetWhenShouldRefresh(any()) } returns playWidgetState
}

fun HomePlayUseCase.givenOnGetPlayWidgetWhenShouldRefreshError() {
    coEvery { onGetPlayWidgetWhenShouldRefresh(any()) } throws Exception()
}

fun HomeDynamicChannelUseCase.givenGetHomeDataError(t: Throwable = Throwable("Unit test simulate error")) {
    coEvery { getHomeDataFlow() } returns flow {
        throw t
    }
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataReturn(result: com.tokopedia.home.beranda.helper.Result<Any>) {
    coEvery { updateHomeData() } returns flow {
        emit(result)
    }
}

fun HomeBalanceWidgetUseCase.givenGetLoadingStateReturn() {
    coEvery { onGetBalanceWidgetLoadingState(any()) } returns HomeHeaderDataModel()
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataError(t: Throwable = Throwable("Unit test simulate error")) {
    mockkStatic(Log::class)
    every { Log.getStackTraceString(t) } returns ""
    coEvery { updateHomeData() } returns flow {
        throw t
    }
}

fun HomeDynamicChannelUseCase.givenUpdateHomeDataErrorNullMessage() {
    val throwable = Throwable()
    mockkStatic(Log::class)
    every { Log.getStackTraceString(throwable) } returns ""
    coEvery { updateHomeData() } returns flow {
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
    coEvery { onHomeBestSellerFilterClick(any(), any(), any<Int>()) } returns bestSellerDataModel
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

fun HomeSearchUseCase.givenSearchPlaceHolderReturn(isFirstInstall: Boolean) {
    val searchPlaceHolder = SearchPlaceholder()
    searchPlaceHolder.Data().placeholders = arrayListOf<SearchPlaceholder.PlaceHolder>(SearchPlaceholder().PlaceHolder())
    coEvery { onGetSearchHint(isFirstInstall, any(), any()) } returns searchPlaceHolder
}

fun HomeBalanceWidgetUseCase.givenGetTokopointDataReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetTokopointData(any(), any(), any()) } returns homeHeaderDataModel
}

fun HomeBalanceWidgetUseCase.givenGetWalletDataReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetWalletAppData(any(), any(), any()) } returns homeHeaderDataModel
}

fun HomeBalanceWidgetUseCase.givenGetBalanceWidgetDataReturn(homeHeaderDataModel: HomeHeaderDataModel) {
    coEvery { onGetBalanceWidgetData() } returns homeHeaderDataModel
}

fun HomeBalanceWidgetUseCase.givenGetBalanceWidgetFailed() {
    coEvery { onGetBalanceWidgetData() } throws NetworkErrorException()
}

fun HomeDynamicChannelUseCase.givenGetHomeDataReturn(homeDynamicChannelModel: HomeDynamicChannelModel, newHomeDynamicChannelModel: HomeDynamicChannelModel) {
    coEvery { getHomeDataFlow() } returns flow {
        emit(homeDynamicChannelModel)
        emit(newHomeDynamicChannelModel)
    }
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

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationUseCase(rechargeRecommendation: RechargeRecommendation) {
    coEvery { executeOnBackground() } returns rechargeRecommendation
}

fun HomeRechargeRecommendationRepository.givenGetRechargeRecommendationThrowReturn() {
    coEvery { executeOnBackground() } throws Exception()
}

fun HomeDeclineRechargeRecommendationRepository.givenDeclineRechargeRecommendationUseCase(declineRechargeRecommendation: DeclineRechargeRecommendation) {
    coEvery { executeOnBackground() } returns declineRechargeRecommendation
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetUseCase(salamWidget: SalamWidget) {
    coEvery { executeOnBackground() } returns salamWidget
}

fun HomeSalamWidgetRepository.givenGetSalamWidgetThrowReturn() {
    coEvery { executeOnBackground() } throws Exception()
}

fun HomeDeclineSalamWIdgetRepository.givenDeclineSalamWidgetUseCase(salamWidget: SalamWidget) {
    coEvery { executeOnBackground() } returns salamWidget
}

fun GetRechargeBUWidgetUseCase.givenGetRechargeBUWidgetUseCase(rechargePerso: RechargePerso) {
    coEvery { executeOnBackground() } returns rechargePerso
}

fun GetRechargeBUWidgetUseCase.givenGetRechargeBUWidgetThrowReturn() {
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

fun HomeMissionWidgetUseCase.givenOnMissionWidgetReturn(
    currentMissionWidgetListDataModel: MissionWidgetListDataModel,
    resultMissionWidgetListDataModel: MissionWidgetListDataModel
) {
    coEvery { onMissionWidgetRefresh(currentMissionWidgetListDataModel) } returns resultMissionWidgetListDataModel
}

fun HomeTodoWidgetUseCase.givenOnTodoWidgetReturn(
    currentTodoWidgetListDataModel: TodoWidgetListDataModel,
    resultTodoWidgetListDataModel: TodoWidgetListDataModel
) {
    coEvery { onTodoWidgetRefresh(currentTodoWidgetListDataModel) } returns resultTodoWidgetListDataModel
}

fun areEqualKeyValues(first: Map<String, Any>, second: Map<String, Any>): Boolean {
    first.forEach {
        if (it.value != second[it.key]) return false
    }
    return true
}

inline fun <reified T> HomeDynamicChannelModel.findWidget(predicate: (T?) -> Boolean = { true }, actionOnFound: (T, Int) -> Unit, actionOnNotFound: () -> Unit) {
    this.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
        it?.let {
            if (it.value is T) {
                actionOnFound.invoke(it.value as T, it.index)
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
