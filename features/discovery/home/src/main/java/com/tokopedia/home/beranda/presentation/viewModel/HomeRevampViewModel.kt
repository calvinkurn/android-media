package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.gopayhomewidget.domain.usecase.ClosePayLaterWidgetUseCase
import com.tokopedia.gopayhomewidget.domain.usecase.GetPayLaterWidgetUseCase
import com.tokopedia.home.beranda.common.BaseCoRoutineScope
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.interactor.usecase.*
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FlowPreview
@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class HomeRevampViewModel @Inject constructor(
    private val homeBalanceWidgetUseCase: Lazy<HomeBalanceWidgetUseCase>,
    private val homeUseCase: Lazy<HomeDynamicChannelUseCase>,
    private val homeSuggestedReviewUseCase: Lazy<HomeSuggestedReviewUseCase>,
    private val homeRecommendationUseCase: Lazy<HomeRecommendationUseCase>,
    private val homePlayUseCase: Lazy<HomePlayUseCase>,
    private val homePopularKeywordUseCase: Lazy<HomePopularKeywordUseCase>,
    private val homeListCarouselUseCase: Lazy<HomeListCarouselUseCase>,
    private val homeRechargeBuWidgetUseCase: Lazy<HomeRechargeBuWidgetUseCase>,
    private val homeSearchUseCase: Lazy<HomeSearchUseCase>,
    private val homeRechargeRecommendationUseCase: Lazy<HomeRechargeRecommendationUseCase>,
    private val homeSalamRecommendationUseCase: Lazy<HomeSalamRecommendationUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val homeBusinessUnitUseCase: Lazy<HomeBusinessUnitUseCase>,
    private val homeDispatcher: Lazy<CoroutineDispatchers>,
    private val getCMHomeWidgetDataUseCase: Lazy<GetCMHomeWidgetDataUseCase>,
    private val deleteCMHomeWidgetUseCase: Lazy<DeleteCMHomeWidgetUseCase>,
    private val deletePayLaterWidgetUseCase: Lazy<ClosePayLaterWidgetUseCase>,
    private val getPayLaterWidgetUseCase: Lazy<GetPayLaterWidgetUseCase>,
    private val homeMissionWidgetUseCase: Lazy<HomeMissionWidgetUseCase>,
    private val homeBalanceWidgetAtf1UseCase: Lazy<HomeBalanceWidgetAtf1UseCase>
) : BaseCoRoutineScope(homeDispatcher.get().io) {

    companion object {
        const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val POSITION = "position"
    }

    val homeLiveDynamicChannel: LiveData<HomeDynamicChannelModel>
        get() = _homeLiveDynamicChannel
    private val _homeLiveDynamicChannel: MutableLiveData<HomeDynamicChannelModel> =
        MutableLiveData()

    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    private val _searchHint: MutableLiveData<SearchPlaceholder> = MutableLiveData()

    val playWidgetReminderEvent: LiveData<Pair<String, PlayWidgetReminderType>>
        get() = _playWidgetReminderEvent
    private val _playWidgetReminderEvent = MutableLiveData<Pair<String, PlayWidgetReminderType>>()

    val playWidgetReminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _playWidgetReminderObservable
    private val _playWidgetReminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()

    val oneClickCheckoutHomeComponent: LiveData<Event<Any>> get() = _oneClickCheckoutHomeComponent
    private val _oneClickCheckoutHomeComponent = MutableLiveData<Event<Any>>()

    val trackingLiveData: LiveData<Event<List<HomeVisitable>>>
        get() = _trackingLiveData
    private val _trackingLiveData = MutableLiveData<Event<List<HomeVisitable>>>()

    val updateNetworkLiveData: LiveData<Result<Any>> get() = _updateNetworkLiveData
    private val _updateNetworkLiveData = MutableLiveData<Result<Any>>()

    val hideShowLoading: LiveData<Event<Boolean>> get() = _hideShowLoadingLiveData
    private val _hideShowLoadingLiveData = MutableLiveData<Event<Boolean>>()

    val errorEventLiveData: LiveData<Event<Throwable>>
        get() = _errorEventLiveData
    private val _errorEventLiveData = MutableLiveData<Event<Throwable>>()

    val isRequestNetworkLiveData: LiveData<Event<Boolean>>
        get() = _isRequestNetworkLiveData
    private val _isRequestNetworkLiveData = MutableLiveData<Event<Boolean>>(null)

    private val _isNeedRefresh = MutableLiveData<Event<Boolean>>()
    val isNeedRefresh: LiveData<Event<Boolean>> get() = _isNeedRefresh

    private val _resetNestedScrolling = MutableLiveData<Event<Boolean>>()
    val resetNestedScrolling: LiveData<Event<Boolean>> get() = _resetNestedScrolling

    val homeRateLimit = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)

    private var fetchFirstData = false
    private var homeFlowStarted = false
    private var homeFlowDataCancelled = false
    private var onRefreshState = true
    private var popularKeywordRefreshCount = 1

    var homeDataModel = HomeDynamicChannelModel()
    var currentTopAdsBannerPage: String = "1"
    var isFirstLoad = true
//    private var atfStyle = ""

    //    @FlowPreview
    private fun homeFlowDynamicChannel(): Flow<HomeDynamicChannelModel?> {
        return homeUseCase.get().getHomeDataFlow(HomeRollenceController.rollenceAtfValue).flowOn(homeDispatcher.get().io)
    }

    var getHomeDataJob: Job? = null

    init {
        _isRequestNetworkLiveData.value = Event(true)
        HomeRollenceController.fetchAtfRollenceValue()
        injectCouponTimeBased()
        homeRateLimit.reset(HOME_LIMITER_KEY)
    }

    private inline fun <reified T> findWidget(
        predicate: (T) -> Boolean = { true },
        actionOnFound: (T, Int) -> Unit
    ) {
        homeDataModel.list.withIndex().find { it.value is T && predicate.invoke(it.value as T) }
            .let {
                it?.let {
                    if (it.value is T) {
                        actionOnFound.invoke(it.value as T, it.index)
                    }
                }
            }
    }

    private fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        homeDataModel.addWidgetModel(visitable, position) {
            _homeLiveDynamicChannel.postValue(
                homeDataModel
            )
        }
    }

    private fun updateWidget(
        visitable: Visitable<*>,
        position: Int,
        visitableToChange: Visitable<*>? = null
    ) {
        homeDataModel.updateWidgetModel(
            visitable,
            visitableToChange,
            position
        ) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    private fun deleteWidget(visitable: Visitable<*>?, position: Int) {
        homeDataModel.deleteWidgetModel(visitable, position) {
            _homeLiveDynamicChannel.postValue(
                homeDataModel
            )
        }
    }

    private fun updateHomeData(homeNewDynamicChannelModel: HomeDynamicChannelModel) {
        this.homeDataModel = homeNewDynamicChannelModel
        _homeLiveDynamicChannel.postValue(homeDataModel)
        _resetNestedScrolling.postValue(Event(true))
    }

    private fun injectCouponTimeBased() {
        launch {
            if (userSession.get().isLoggedIn) {
                homeBalanceWidgetUseCase.get()
                    .onGetInjectCouponTimeBased()
            }
        }
    }

    private fun removeDynamicChannelLoadingModel() {
        findWidget<DynamicChannelLoadingModel> { _, index ->
            updateWidget(DynamicChannelRetryModel(false), index)
        }
        findWidget<DynamicChannelRetryModel> { retryWidget, index ->
            updateWidget(DynamicChannelRetryModel(false), index)
        }
    }

    private fun updateCarouselPlayWidget(onUpdate: (oldVal: CarouselPlayWidgetDataModel) -> CarouselPlayWidgetDataModel) {
        findWidget<CarouselPlayWidgetDataModel> { carouselPlayModel, index ->
            updateWidget(onUpdate(carouselPlayModel), index)
        }
    }

    private fun getBalanceWidgetLoadingState() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderDataModel> { headerModel, index ->
            launch {
                val visitable = updateHeaderData(
                    homeBalanceWidgetUseCase.get().onGetBalanceWidgetLoadingState(headerModel)
                )
                visitable?.let { updateWidget(visitable, index) }
            }
        }
    }

    private fun getBalanceWidgetAtf1LoadingState() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderAtf1DataModel> { headerModel, index ->
            launch {
                val visitable = updateHeaderData(
                    homeBalanceWidgetAtf1UseCase.get().onGetBalanceWidgetLoadingState(headerModel)
                )
                visitable?.let { updateWidget(visitable, index) }
            }
        }
    }

    fun getBalanceWidgetData() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderDataModel> { headerModel, index ->
            launch {
                if (headerModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) {
                    val homeBalanceModel = headerModel.headerDataModel?.homeBalanceModel.apply {
                        this?.status = HomeBalanceModel.STATUS_LOADING
                    } ?: HomeBalanceModel()
                    val headerDataModel = headerModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel
                    )
                    val initialHeaderModel = headerModel.copy(
                        headerDataModel = headerDataModel
                    )
                    updateHeaderData(initialHeaderModel, index)
                    val currentHeaderDataModel =
                        homeBalanceWidgetUseCase.get().onGetBalanceWidgetData()
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let {
                        homeDataModel.updateWidgetModel(
                            visitableToChange = visitable,
                            visitable = currentHeaderDataModel,
                            position = index
                        ) {
                            updateHomeData(homeDataModel)
                        }
                    }
                } else {
                    val currentHeaderDataModel =
                        homeBalanceWidgetUseCase.get().onGetBalanceWidgetData(headerModel)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let {
                        homeDataModel.updateWidgetModel(
                            visitableToChange = visitable,
                            visitable = currentHeaderDataModel,
                            position = index
                        ) {
                            updateHomeData(homeDataModel)
                        }
                    }
                }
            }
        }
    }

    fun getBalanceWidgetAtf1Data() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderAtf1DataModel> { headerModel, index ->
            launch {
                if (headerModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) {
                    val homeBalanceModel = headerModel.headerDataModel?.homeBalanceModel.apply {
                        this?.status = HomeBalanceModel.STATUS_LOADING
                    } ?: HomeBalanceModel()
                    val headerDataModel = headerModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel
                    )
                    val initialHeaderModel = headerModel.copy(
                        headerDataModel = headerDataModel
                    )
                    updateHeaderData(initialHeaderModel, index)
                    val currentHeaderDataModel =
                        homeBalanceWidgetUseCase.get().onGetBalanceWidgetData()
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let {
                        homeDataModel.updateWidgetModel(
                            visitableToChange = visitable,
                            visitable = currentHeaderDataModel,
                            position = index
                        ) {
                            updateHomeData(homeDataModel)
                        }
                    }
                } else {
                    val currentHeaderDataModel =
                        homeBalanceWidgetAtf1UseCase.get().onGetBalanceWidgetData(headerModel)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let {
                        homeDataModel.updateWidgetModel(
                            visitableToChange = visitable,
                            visitable = currentHeaderDataModel,
                            position = index
                        ) {
                            updateHomeData(homeDataModel)
                        }
                    }
                }
            }
        }
    }

    private fun updateHeaderData(visitable: Visitable<*>?, index: Int = 0): Visitable<*>? {
        visitable?.let {
            findWidget<HomeHeaderDataModel> { model, index ->
                updateWidget(
                    it,
                    index
                )
            }
        }
        return visitable
    }

    @FlowPreview
    fun initFlow() {
        homeFlowStarted = true
//        Log.d("dhabalog", "initFlow $atfStyle")
        launchCatchError(coroutineContext, block = {
            Log.d("dhabalog", "initFlowlaunchcatcherror")
            homeFlowDynamicChannel().collect { homeNewDataModel ->
                if (homeNewDataModel?.isCache == false) {
                    _isRequestNetworkLiveData.postValue(Event(false))
                    currentTopAdsBannerPage = homeNewDataModel.topadsPage
                    onRefreshState = false
                    if (homeNewDataModel.list.isEmpty()) {
                        HomeServerLogger.warning_empty_channel_update(homeNewDataModel)
                    }
                    updateHomeData(homeNewDataModel)
                    _trackingLiveData.postValue(Event(homeNewDataModel.list.filterIsInstance<HomeVisitable>()))
                } else if (homeNewDataModel?.list?.size ?: 0 > 0) {
                    homeNewDataModel?.let {
                        updateHomeData(it)
                    }
                }
            }
        }) {
            _updateNetworkLiveData.postValue(Result.error(error = it, data = null))
            HomeServerLogger.warning_error_flow(it)
        }.invokeOnCompletion {
            _updateNetworkLiveData.postValue(Result.error(error = Throwable(), data = null))
            HomeServerLogger.warning_error_cancelled_flow(it)
            homeFlowDataCancelled = true
        }
    }

    @FlowPreview
    fun refreshHomeData() {
        if (getHomeDataJob?.isActive == true) {
            _hideShowLoadingLiveData.postValue(Event(true))
            return
        }
        if (!homeDataModel.flowCompleted) {
            _hideShowLoadingLiveData.postValue(Event(true))
            return
        }
        homeRateLimit.shouldFetch(HOME_LIMITER_KEY)
        onRefreshState = true
        if (HomeRollenceController.isUsingAtf1Variant()) {
            getBalanceWidgetAtf1LoadingState()
        } else {
            getBalanceWidgetLoadingState()
        }

        if (homeFlowDataCancelled || !homeFlowStarted) {
            initFlow()
            homeFlowDataCancelled = false
        }

        getHomeDataJob = launchCatchError(coroutineContext, block = {
            homeUseCase.get().updateHomeData().collect {
                _updateNetworkLiveData.postValue(it)
                if (it.status === Result.Status.ERROR_PAGINATION) {
                    removeDynamicChannelLoadingModel()
                }
                if (it.status === Result.Status.ERROR_ATF || it.status === Result.Status.ERROR_GENERAL) {
                    _updateNetworkLiveData.postValue(it)
                }
            }
        }) {
            homeRateLimit.reset(HOME_LIMITER_KEY)
            _updateNetworkLiveData.postValue(Result.error(it, null))
            HomeServerLogger.warning_error_refresh(it)
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

    fun getUserName() = userSession.get().name ?: ""

    fun refreshWithThreeMinsRules(forceRefresh: Boolean = false, isFirstInstall: Boolean = false) {
        if ((forceRefresh && getHomeDataJob?.isActive == false) || (
            !fetchFirstData && homeRateLimit.shouldFetch(
                    HOME_LIMITER_KEY
                )
            )
        ) {
            refreshHomeData()
            _isNeedRefresh.value = Event(true)
        } else {
            if (HomeRollenceController.isUsingAtf1Variant()) {
                getBalanceWidgetAtf1Data()
            } else {
                getBalanceWidgetData()
            }
        }
        getSearchHint(isFirstInstall)
    }

    fun removeViewHolderAtPosition(position: Int) {
        if (position != -1 && position < homeDataModel.list.size) {
            val detectViewHolder = homeDataModel.list[position]
            detectViewHolder.let {
                deleteWidget(it, position)
            }
        }
    }

    fun getRecommendationWidget(
        filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip,
        bestSellerDataModel: BestSellerDataModel,
        selectedChipsPosition: Int
    ) {
        findWidget<BestSellerDataModel> { currentDataModel, index ->
            launch {
                updateWidget(
                    homeRecommendationUseCase.get().onHomeBestSellerFilterClick(
                        currentBestSellerDataModel = currentDataModel,
                        filterChip = filterChip,
                        selectedChipPosition = selectedChipsPosition
                    ),
                    index
                )
            }
        }
    }

    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int) {
        launchCatchError(coroutineContext, block = {
            _oneClickCheckoutHomeComponent.postValue(
                Event(
                    homeListCarouselUseCase.get()
                        .onOneClickCheckOut(channel, grid, position, getUserId())
                )
            )
        }) {
            _oneClickCheckoutHomeComponent.postValue(Event(it))
            it.printStackTrace()
        }
    }

    fun onCloseBuyAgain(channelId: String, position: Int) {
        findWidget<RecommendationListCarouselDataModel> { listCarouselModel, index ->
            launch {
                if (homeListCarouselUseCase.get().onClickCloseListCarousel(channelId)) {
                    deleteWidget(listCarouselModel, position)
                } else {
                    _errorEventLiveData.postValue(Event(Throwable()))
                }
            }
        }
    }

    fun updateBannerTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return
        findWidget<PlayCardDataModel>(predicate = { it.playCardHome?.channelId == channelId }) { playCard, index ->
            val newPlayCard =
                playCard.copy(playCardHome = playCard.playCardHome?.copy(totalView = totalView))
            updateWidget(newPlayCard, index)
        }
    }

    fun onCloseTicker() {
        findWidget<TickerDataModel> { tickerModel, index -> deleteWidget(tickerModel, index) }
    }

    fun onRefreshMembership(position: Int, headerTitle: String) {
        if (!userSession.get().isLoggedIn) return
        if (HomeRollenceController.isUsingAtf1Variant()) {
            findWidget<HomeHeaderAtf1DataModel> { headerModel, index ->
                launch {
                    val currentHeaderDataModel = homeBalanceWidgetAtf1UseCase.get()
                        .onGetTokopointData(headerModel, position, headerTitle)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let { updateWidget(visitable, index) }
                }
            }
        } else {
            findWidget<HomeHeaderDataModel> { headerModel, index ->
                launch {
                    val currentHeaderDataModel = homeBalanceWidgetUseCase.get()
                        .onGetTokopointData(headerModel, position, headerTitle)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let { updateWidget(visitable, index) }
                }
            }
        }
    }

    fun onRefreshWalletApp(position: Int, headerTitle: String) {
        if (!userSession.get().isLoggedIn) return
        if (HomeRollenceController.isUsingAtf1Variant()) {
            findWidget<HomeHeaderAtf1DataModel> { headerModel, index ->
                launch {
                    val currentHeaderDataModel = homeBalanceWidgetAtf1UseCase.get()
                        .onGetWalletAppData(headerModel, position, headerTitle)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let { updateWidget(visitable, index) }
                }
            }
        } else {
            findWidget<HomeHeaderDataModel> { headerModel, index ->
                launch {
                    val currentHeaderDataModel = homeBalanceWidgetUseCase.get()
                        .onGetWalletAppData(headerModel, position, headerTitle)
                    val visitable = updateHeaderData(currentHeaderDataModel, index)
                    visitable?.let { updateWidget(visitable, index) }
                }
            }
        }
    }

    fun dismissReview() {
        onRemoveSuggestedReview()
        launch { homeSuggestedReviewUseCase.get().onReviewDismissed() }
    }

    fun onRemoveSuggestedReview() {
        findWidget<ReviewDataModel> { reviewWidget, index ->
            deleteWidget(reviewWidget, -1)
        }
    }

    fun getBusinessUnitTabData(position: Int) {
        findWidget<NewBusinessUnitWidgetDataModel> { buModel, _ ->
            launch {
                val buWidgetData = homeBusinessUnitUseCase.get().getBusinessUnitTab(buModel)
                updateWidget(buWidgetData, position)
            }
        }
    }

    fun getBusinessUnitData(tabId: Int, position: Int, tabName: String) {
        findWidget<NewBusinessUnitWidgetDataModel> { buModel, index ->
            launch {
                val buData = homeBusinessUnitUseCase.get()
                    .getBusinessUnitData(tabId, position, tabName, homeDataModel, buModel, index)
                updateWidget(buData, index)
            }
        }
    }

    fun getDynamicChannelDataOnExpired(
        visitable: Visitable<*>,
        channelModel: ChannelModel,
        position: Int
    ) {
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(channelModel.groupId)

            if (visitableList.isEmpty()) {
                deleteWidget(visitable, position)
            } else {
                val lastIndex = position
                deleteWidget(visitable, lastIndex)
                visitableList.reversed().forEach {
                    addWidget(it, lastIndex)
                }
                _trackingLiveData.postValue(Event(visitableList.filterIsInstance(HomeVisitable::class.java)))
            }
        }) {
            deleteWidget(visitable, position)
        }
    }

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        findWidget<ReminderWidgetModel>({ it.source == ReminderEnum.RECHARGE }) { rechargeModel, index ->
            deleteWidget(rechargeModel, index)
        }
        launch {
            homeRechargeRecommendationUseCase.get().onDeclineRechargeRecommendation(requestParams)
        }
    }

    fun declineSalamItem(requestParams: Map<String, Int>) {
        findWidget<ReminderWidgetModel>({ it.source == ReminderEnum.SALAM }) { rechargeModel, index ->
            deleteWidget(rechargeModel, index)
        }
        launch {
            homeSalamRecommendationUseCase.get().onDeclineSalamRecommendation(requestParams)
        }
    }

    fun getRechargeBUWidget(source: WidgetSource) {
        findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
            launchCatchError(coroutineContext, block = {
                val data = homeRechargeBuWidgetUseCase.get().onGetRechargeBuWidgetFromHolder(
                    widgetSource = source,
                    currentRechargeBuWidget = rechargeBuModel
                )
                updateWidget(data, index)
            }) {
                findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
                    deleteWidget(rechargeBuModel, index)
                }
            }
        }
    }

    fun getFeedTabData() {
        launch { homeUseCase.get().getFeedTabData(homeDataModel) }
    }

    fun getPopularKeywordOnRefresh() {
        findWidget<PopularKeywordListDataModel> { popularKeywordListDataModel, index ->
            launch {
                updateWidget(
                    homePopularKeywordUseCase.get().onPopularKeywordRefresh(
                        popularKeywordRefreshCount,
                        popularKeywordListDataModel
                    ),
                    index
                )
            }
            popularKeywordRefreshCount++
        }
    }

    fun getMissionWidgetRefresh() {
        findWidget<MissionWidgetListDataModel> { missionWidgetListDataModel, position ->
            launch {
                updateWidget(
                    missionWidgetListDataModel.copy(status = MissionWidgetListDataModel.STATUS_LOADING),
                    position
                )
                updateWidget(
                    homeMissionWidgetUseCase.get()
                        .onMissionWidgetRefresh(missionWidgetListDataModel),
                    position
                )
            }
        }
    }

    fun getSearchHint(isFirstInstall: Boolean) {
        launch {
            _searchHint.postValue(
                homeSearchUseCase.get().onGetSearchHint(
                    isFirstInstall = isFirstInstall,
                    deviceId = userSession.get().deviceId,
                    userId = userSession.get().userId
                )
            )
        }
    }

    fun getPlayWidgetWhenShouldRefresh() {
        findWidget<CarouselPlayWidgetDataModel> { playWidget, index ->
            launchCatchError(block = {
                updateWidget(
                    playWidget.copy(
                        widgetState = homePlayUseCase.get().onGetPlayWidgetWhenShouldRefresh()
                    ),
                    index
                )
            }) {
                deleteWidget(playWidget, index)
            }
        }
    }

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        updateCarouselPlayWidget {
            homePlayUseCase.get().onUpdatePlayTotalView(it, channelId, totalView)
        }
    }

    fun updatePlayWidgetReminder(channelId: String, isReminder: Boolean) {
        updateCarouselPlayWidget {
            homePlayUseCase.get().onUpdateActionReminder(it, channelId, isReminder)
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(
        channelId: String,
        reminderType: PlayWidgetReminderType
    ) {
        if (!userSession.get().isLoggedIn) {
            _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        } else {
            updateCarouselPlayWidget {
                it.copy(
                    widgetState = homePlayUseCase.get().onGetPlayWidgetUiModel(
                        it.widgetState,
                        channelId,
                        reminderType
                    )
                )
            }
            launch {
                when (
                    homePlayUseCase.get()
                        .onUpdatePlayWidgetToggleReminder(channelId, reminderType)
                ) {
                    true -> {
                        _playWidgetReminderObservable.postValue(Result.success(reminderType))
                    }
                    else -> {
                        updateCarouselPlayWidget {
                            it.copy(
                                widgetState = homePlayUseCase.get().onGetPlayWidgetUiModel(
                                    it.widgetState,
                                    channelId,
                                    reminderType
                                )
                            )
                        }
                        _playWidgetReminderObservable.postValue(Result.error(error = Throwable()))
                    }
                }
            }
        }
    }

    fun updateChooseAddressData(homeChooseAddressData: HomeChooseAddressData) {
        this.homeDataModel.setAndEvaluateHomeChooseAddressData(homeChooseAddressData)
        findWidget<HomeHeaderDataModel> { headerModel, index ->
            updateHeaderData(headerModel, index)
        }
    }

    fun getAddressData(): HomeChooseAddressData {
        return homeDataModel.homeChooseAddressData
    }

    fun removeChooseAddressWidget() {
        findWidget<HomeHeaderDataModel> { homeHeaderModel, index ->
            homeHeaderModel.needToShowChooseAddress = false
            homeDataModel.homeChooseAddressData.isActive = false
            homeDataModel.updateWidgetModel(homeHeaderModel, homeHeaderModel, index) {}
            updateHomeData(homeDataModel)
        }
    }

    fun deleteQuestWidget() {
        findWidget<QuestWidgetModel> { questWidgetModel, index ->
            deleteWidget(questWidgetModel, index)
        }
    }

    fun getCMHomeWidgetData(isForceRefresh: Boolean = true) {
        findWidget<CMHomeWidgetDataModel> { cmHomeWidgetDataModel, index ->
            launchCatchError(coroutineContext, {
                getCMHomeWidgetDataUseCase.get().getCMHomeWidgetData(
                    {
                        val newCMHomeWidgetDataModel =
                            cmHomeWidgetDataModel.copy(cmHomeWidgetData = it.cmHomeWidgetData)
                        updateWidget(newCMHomeWidgetDataModel, index)
                    },
                    {
                        deleteWidget(cmHomeWidgetDataModel, index)
                    },
                    isForceRefresh
                )
            }) {
                deleteWidget(cmHomeWidgetDataModel, index)
            }
        }
    }

    fun deleteCMHomeWidget() {
        findWidget<CMHomeWidgetDataModel> { cmHomeWidgetDataModel, index ->
            launchCatchError(coroutineContext, {
                cmHomeWidgetDataModel.cmHomeWidgetData?.let { it ->
                    deleteCMHomeWidgetUseCase.get().deleteCMHomeWidgetData(
                        {
                            deleteWidget(cmHomeWidgetDataModel, index)
                        },
                        {
                            updateWidget(cmHomeWidgetDataModel.copy(), index)
                        },
                        it.parentId,
                        it.campaignId
                    )
                }
            }) {
                updateWidget(cmHomeWidgetDataModel.copy(), index)
            }
        }
    }

    fun deleteCMHomeWidgetLocally() {
        findWidget<CMHomeWidgetDataModel> { cmHomeWidgetDataModel, index ->
            deleteWidget(cmHomeWidgetDataModel, index)
        }
    }

    /**
     * Calling fintech api to get detail for the fintech widget
     * @author minion-yoda
     */

    fun getPayLaterWidgetData() {
        findWidget<HomePayLaterWidgetDataModel> { homePayLaterWidgetDataModel, index ->
            launchCatchError(coroutineContext, {
                getPayLaterWidgetUseCase.get().getPayLaterWidgetData(
                    {
                        val newPaylaterHomeWidgetDataModel =
                            homePayLaterWidgetDataModel.copy(payLaterWidgetData = it)
                        updateWidget(newPaylaterHomeWidgetDataModel, index)
                    },
                    {
                        deleteWidget(homePayLaterWidgetDataModel, index)
                    }
                )
            }) {
                deleteWidget(homePayLaterWidgetDataModel, index)
            }
        }
    }

    /**
     * Calling fintech api delete the widget
     * @author minion-yoda
     */
    fun deletePayLaterWidget() {
        findWidget<HomePayLaterWidgetDataModel> { homePayLaterWidgetDataModel, index ->
            deleteWidget(homePayLaterWidgetDataModel, index)
            launchCatchError(coroutineContext, {
                deletePayLaterWidgetUseCase.get().getPayLaterWidgetCloseData({
                    deleteWidget(homePayLaterWidgetDataModel, index)
                }, { deleteWidget(homePayLaterWidgetDataModel, index) })
            }) {
                deleteWidget(homePayLaterWidgetDataModel, index)
            }
        }
    }
}
