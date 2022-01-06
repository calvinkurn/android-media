package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.home.beranda.common.BaseCoRoutineScope
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.usecase.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBusinessUnitUseCase
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeCoachmarkModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeNotifModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_CANCELLED_INIT_FLOW
import com.tokopedia.home.util.HomeServerLogger.TYPE_REVAMP_EMPTY_UPDATE
import com.tokopedia.home.util.HomeServerLogger.TYPE_REVAMP_ERROR_INIT_FLOW
import com.tokopedia.home.util.HomeServerLogger.TYPE_REVAMP_ERROR_REFRESH
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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
        private val homeBeautyFestUseCase: Lazy<HomeBeautyFestUseCase>) : BaseCoRoutineScope(homeDispatcher.get().io) {

    companion object {
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITY = "quantity"
        const val POSITION = "position"
    }

    var isFirstLoad = true

    val beautyFestLiveData: LiveData<Int>
        get() = _beautyFestLiveData
    private val _beautyFestLiveData : MutableLiveData<Int> = MutableLiveData()

    val homeLiveDynamicChannel: LiveData<HomeDynamicChannelModel>
        get() = _homeLiveDynamicChannel
    private val _homeLiveDynamicChannel: MutableLiveData<HomeDynamicChannelModel> = MutableLiveData()

    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    private val _searchHint: MutableLiveData<SearchPlaceholder> = MutableLiveData()

    val injectCouponTimeBasedResult : LiveData<Result<InjectCouponTimeBased>>
        get() = _injectCouponTimeBasedResult
    private val _injectCouponTimeBasedResult : MutableLiveData<Result<InjectCouponTimeBased>> = MutableLiveData()

    val playWidgetReminderEvent: LiveData<Pair<String, PlayWidgetReminderType>>
        get() = _playWidgetReminderEvent
    private val _playWidgetReminderEvent = MutableLiveData<Pair<String, PlayWidgetReminderType>>()

    val playWidgetReminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _playWidgetReminderObservable
    private val _playWidgetReminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()

    val popupIntroOvoLiveData: LiveData<Event<String>>
        get() = _popupIntroOvoLiveData
    private val _popupIntroOvoLiveData = MutableLiveData<Event<String>>()

    val oneClickCheckoutHomeComponent: LiveData<Event<Any>> get() = _oneClickCheckoutHomeComponent
    private val _oneClickCheckoutHomeComponent = MutableLiveData<Event<Any>>()

    val sendLocationLiveData: LiveData<Event<Any>>
        get() = _sendLocationLiveData
    private val _sendLocationLiveData = MutableLiveData<Event<Any>>()

    val trackingLiveData: LiveData<Event<List<HomeVisitable>>>
        get() = _trackingLiveData
    private val _trackingLiveData = MutableLiveData<Event<List<HomeVisitable>>>()

    val updateNetworkLiveData: LiveData<Result<Any>> get() = _updateNetworkLiveData
    private val _updateNetworkLiveData = MutableLiveData<Result<Any>>()

    val errorEventLiveData: LiveData<Event<Throwable>>
        get() = _errorEventLiveData
    private val _errorEventLiveData = MutableLiveData<Event<Throwable>>()

    val isViewModelInitialized: LiveData<Event<Boolean>>
        get() = _isViewModelInitialized
    private val _isViewModelInitialized = MutableLiveData<Event<Boolean>>(null)

    val isRequestNetworkLiveData: LiveData<Event<Boolean>>
        get() = _isRequestNetworkLiveData
    private val _isRequestNetworkLiveData = MutableLiveData<Event<Boolean>>(null)

    private val _isNeedRefresh = MutableLiveData<Event<Boolean>>()
    val isNeedRefresh: LiveData<Event<Boolean>> get() = _isNeedRefresh

    private val _resetNestedScrolling = MutableLiveData<Event<Boolean>>()
    val resetNestedScrolling: LiveData<Event<Boolean>> get() = _resetNestedScrolling

    private val _homeCoachmarkData = MutableLiveData<Event<HomeCoachmarkModel>>()
    val homeCoachmarkData: LiveData<Event<HomeCoachmarkModel>> get() = _homeCoachmarkData

    /**
     * Variable list
     */
    private val homeRateLimit = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)

    private var fetchFirstData = false
    private var homeFlowDataCancelled = false
    private var onRefreshState = true
    private var takeTicker = true
    private val homeFlowDynamicChannel: Flow<HomeDynamicChannelModel?> = homeUseCase.get().getHomeDataFlow().flowOn(homeDispatcher.get().io)
    private var popularKeywordRefreshCount = 1

    var currentTopAdsBannerToken: String = ""
    var homeDataModel = HomeDynamicChannelModel()
    var currentHeaderDataModel = HomeHeaderDataModel()

    /**
     * Job list
     */
    private var getHomeDataJob: Job? = null
    private var declineRechargeRecommendationJob: Job? = null
    private var declineSalamWidgetJob : Job? = null

    init {
        _isViewModelInitialized.value = Event(true)
        _isRequestNetworkLiveData.value = Event(true)
        initFlow()
        refreshHomeData()
    }

    /**
     * use this refresh mechanism only for conditional refresh (3 mins rule)
     */
    fun refresh(forceRefresh: Boolean = false, isFirstInstall: Boolean = false){
        if ((forceRefresh && getHomeDataJob?.isActive == false) || (!fetchFirstData && homeRateLimit.shouldFetch(HOME_LIMITER_KEY))) {
            refreshHomeData()
            _isNeedRefresh.value = Event(true)
        } else {
            getBalanceWidgetData()
        }
        getSearchHint(isFirstInstall)
    }

    fun getRecommendationWidget(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel, selectedChipsPosition: Int){
        findWidget<BestSellerDataModel> { currentDataModel, index ->
            launch {
                updateWidget(homeRecommendationUseCase.get().onHomeBestSellerFilterClick(
                        currentBestSellerDataModel = currentDataModel,
                        filterChip = filterChip,
                        selectedChipPosition = selectedChipsPosition
                ), index)
            }
        }
    }

    fun updateBannerTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return
        findWidget<PlayCardDataModel>(predicate = { it.playCardHome?.channelId == channelId}) { playCard, index ->
            val newPlayCard = playCard.copy(playCardHome = playCard.playCardHome?.copy(totalView = totalView))
            updateWidget(newPlayCard, index)
        }
    }

    fun onCloseBuyAgain(channelId: String, position: Int){
        findWidget<RecommendationListCarouselDataModel> {
            listCarouselModel, index ->
            launch {
                if (homeListCarouselUseCase.get().onClickCloseListCarousel(channelId)){
                    deleteWidget(listCarouselModel, position)
                } else {
                    _errorEventLiveData.postValue(Event(Throwable()))
                }
            }
        }
    }

    fun removeViewHolderAtPosition(position: Int) {
        if(position != -1 && position < homeDataModel.list.size) {
            val detectViewHolder = homeDataModel.list[position]
            detectViewHolder.let {
                deleteWidget(it, position)
            }
        }
    }

    fun onCloseTicker() {
        findWidget<TickerDataModel> { tickerModel, index -> deleteWidget(tickerModel, index) }
    }

    fun onRefreshTokoPoint() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderDataModel> { headerModel, index ->
            launch {
                currentHeaderDataModel = homeBalanceWidgetUseCase.get().onGetTokopointData(currentHeaderDataModel)
                val visitable = updateHeaderData(currentHeaderDataModel, index)
                visitable?.let { updateWidget(visitable, index) }
            }
        }
    }

    fun refreshHomeData() {
        if (homeFlowDataCancelled) {
            initFlow()
            homeFlowDataCancelled = false
        }

        onRefreshState = true
        if (getHomeDataJob?.isActive == true) return
        getHomeDataJob = launchCatchError(coroutineContext, block = {
            homeUseCase.get().updateHomeData().collect {
                _updateNetworkLiveData.postValue(it)
                if (it.status === Result.Status.ERROR_PAGINATION) {
                    removeDynamicChannelLoadingModel()
                }

                if (it.status === Result.Status.ERROR_ATF || it.status === Result.Status.ERROR_GENERAL ) {
                    _updateNetworkLiveData.postValue(it)
                }
            }
        }) {
            homeRateLimit.reset(HOME_LIMITER_KEY)
            _updateNetworkLiveData.postValue(Result.error(it, null))

            HomeServerLogger.logWarning(
                type = TYPE_REVAMP_ERROR_REFRESH,
                throwable = it,
                reason = (it.message ?: "").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = Log.getStackTraceString(it).take(ConstantKey.HomeTimber.MAX_LIMIT)
            )
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

    fun getBusinessUnitTabData(position: Int){
        findWidget<NewBusinessUnitWidgetDataModel> { buModel, _ ->
            launch{
                val buWidgetData = homeBusinessUnitUseCase.get().getBusinessUnitTab(buModel)
                updateWidget(buWidgetData, position)
            }
        }
    }

    fun getBusinessUnitData(tabId: Int, position: Int, tabName: String){
        findWidget<NewBusinessUnitWidgetDataModel> { buModel, index ->
            launch{
                val buData = homeBusinessUnitUseCase.get()
                    .getBusinessUnitData(tabId, position, tabName, homeDataModel, buModel, index)
                updateWidget(buData, index)
            }
        }
    }

    fun getDynamicChannelDataOnExpired(visitable: Visitable<*>, channelModel: ChannelModel, position: Int){
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(channelModel.groupId)

            if(visitableList.isEmpty()){
                deleteWidget(visitable, position)
            } else {
                val lastIndex = position
                deleteWidget(visitable, lastIndex)
                visitableList.reversed().forEach {
                    addWidget(it, lastIndex)
                }
                _trackingLiveData.postValue(Event(visitableList.filterIsInstance(HomeVisitable::class.java)))
            }
        }){
            deleteWidget(visitable, position)
        }
    }

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        findWidget<ReminderWidgetModel>({it.source == ReminderEnum.RECHARGE}) { rechargeModel, index ->
            deleteWidget(rechargeModel, index) }
        declineRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            homeRechargeRecommendationUseCase.get().onDeclineRechargeRecommendation(requestParams)
        }){}
    }

    fun declineSalamItem(requestParams: Map<String, Int>){
        findWidget<ReminderWidgetModel>({it.source == ReminderEnum.SALAM}) { rechargeModel, index ->
            deleteWidget(rechargeModel, index) }
        declineSalamWidgetJob = launchCatchError(coroutineContext, block = {
            homeSalamRecommendationUseCase.get().onDeclineSalamRecommendation(requestParams) }){}
    }

    fun getRechargeBUWidget(source: WidgetSource) {
        findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
            launchCatchError(coroutineContext, block = {
                val data = homeRechargeBuWidgetUseCase.get().onGetRechargeBuWidgetFromHolder(
                        widgetSource = source,
                        currentRechargeBuWidget = rechargeBuModel)
                updateWidget(data, index)
            }) {
                findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
                    deleteWidget(rechargeBuModel, index) }
            }
        }
    }

    fun getFeedTabData() { launch { homeUseCase.get().getFeedTabData(homeDataModel) } }

    fun getPopularKeywordOnRefresh() {
        findWidget<PopularKeywordListDataModel> { popularKeywordListDataModel, index ->
            launch {
                updateWidget(homePopularKeywordUseCase.get().onPopularKeywordRefresh(
                        popularKeywordRefreshCount, popularKeywordListDataModel
                ), index)
            }
            popularKeywordRefreshCount++
        }
    }

    fun getSearchHint(isFirstInstall: Boolean) {
        launch {
            _searchHint.postValue(homeSearchUseCase.get().onGetSearchHint(
                    isFirstInstall = isFirstInstall,
                    deviceId = userSession.get().deviceId,
                    userId = userSession.get().userId
            ))
        }
    }

    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int){
        launchCatchError(coroutineContext, block = {
            _oneClickCheckoutHomeComponent.postValue(Event(
                    homeListCarouselUseCase.get().onOneClickCheckOut(channel, grid, position, getUserId()))
            )
        }){
            _oneClickCheckoutHomeComponent.postValue(Event(it))
            it.printStackTrace()
        }
    }

    fun injectCouponTimeBased() {
        launch {
//            _injectCouponTimeBasedResult.value = homeBalanceWidgetUseCase.get().onGetInjectCouponTimeBased()
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

    fun getUserName() = userSession.get().name ?: ""

    fun getPlayWidgetWhenShouldRefresh() {
        findWidget<CarouselPlayWidgetDataModel> { playWidget, index ->
            launchCatchError(block = {
                updateWidget(playWidget.copy(widgetUiModel = homePlayUseCase.get().onGetPlayWidgetWhenShouldRefresh()), index)
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

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!userSession.get().isLoggedIn) _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        else {
            updateCarouselPlayWidget {
                it.copy(widgetUiModel = homePlayUseCase.get().onGetPlayWidgetUiModel(
                        it.widgetUiModel, channelId, reminderType
                ))
            }
            launch {
                when(homePlayUseCase.get().onUpdatePlayWidgetToggleReminder(channelId, reminderType)) {
                    true -> {
                        _playWidgetReminderObservable.postValue(Result.success(reminderType))
                    }
                    else -> {
                        updateCarouselPlayWidget {
                            it.copy(widgetUiModel = homePlayUseCase.get().onGetPlayWidgetUiModel(
                                    it.widgetUiModel, channelId, reminderType
                            ))
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
            updateHomeData(homeDataModel)
        }
    }

    fun getAddressData(): HomeChooseAddressData {
        return homeDataModel.homeChooseAddressData
    }

    //adjust unit test
    fun removeChooseAddressWidget() {
        findWidget<HomeHeaderDataModel> { homeHeaderModel, index ->
            homeHeaderModel.needToShowChooseAddress = false
            homeDataModel.homeChooseAddressData.isActive = false
            homeDataModel.updateWidgetModel(homeHeaderModel, homeHeaderModel, index){}
            updateHomeData(homeDataModel)
        }
    }

    private inline fun <reified T> findWidget(predicate: (T) -> Boolean = {true}, actionOnFound: (T, Int) -> Unit) {
        homeDataModel.list.withIndex().find { it.value is T && predicate.invoke(it.value as T) }.let {
            it?.let {
                if (it.value is T) {
                    actionOnFound.invoke(it.value as T, it. index)
                }
            }
        }
    }

    private fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        homeDataModel.addWidgetModel(visitable, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    private fun updateWidget(visitable: Visitable<*>, position: Int, visitableToChange: Visitable<*>? = null) {
        homeDataModel.updateWidgetModel(visitable, visitableToChange, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    private fun deleteWidget(visitable: Visitable<*>?, position: Int) {
        homeDataModel.deleteWidgetModel(visitable, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    private fun updateHomeData(homeNewDynamicChannelModel: HomeDynamicChannelModel) {
        this.homeDataModel = homeNewDynamicChannelModel
        this.homeDataModel.homeBalanceModel = currentHeaderDataModel?.headerDataModel?.homeBalanceModel?:HomeBalanceModel()


        _homeLiveDynamicChannel.postValue(homeDataModel)
        _resetNestedScrolling.postValue(Event(true))
    }

    private fun removeDynamicChannelLoadingModel() {
        findWidget<DynamicChannelLoadingModel> { _, index ->
            updateWidget(DynamicChannelRetryModel(false),index)
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

    private fun getBalanceWidgetData() {
        if (!userSession.get().isLoggedIn) return
        findWidget<HomeHeaderDataModel> { headerModel, index ->
            launch {
                val homeBalanceModel = currentHeaderDataModel.headerDataModel?.homeBalanceModel.apply {
                    this?.initBalanceModelByType()
                } ?: HomeBalanceModel()
                val headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                        homeBalanceModel = homeBalanceModel
                )
                val initialHeaderModel = currentHeaderDataModel.copy(
                        headerDataModel = headerDataModel
                )
                updateHeaderData(initialHeaderModel, index)
                currentHeaderDataModel = homeBalanceWidgetUseCase.get().onGetBalanceWidgetData(currentHeaderDataModel)
                updateHeaderData(currentHeaderDataModel, index)
                val visitable = updateHeaderData(currentHeaderDataModel, index)
                visitable?.let {
                    homeDataModel.updateWidgetModel(visitableToChange = visitable, visitable = currentHeaderDataModel, position = index) {
                        updateHomeData(homeDataModel)
                    }
                }
            }
        }
    }

    private fun updateHeaderData(visitable: Visitable<*>?, index: Int): Visitable<*>? {
        visitable.let {
            homeDataModel.updateWidgetModel(visitableToChange = visitable, visitable = currentHeaderDataModel, position = index) {
                updateHomeData(homeDataModel)
                if (it is HomeHeaderDataModel) {
                    this.currentHeaderDataModel = it
                }
            }
        }
        return visitable
    }

    fun initFlow() {
        launchCatchError(coroutineContext, block = {
            homeFlowDynamicChannel.collect { homeNewDataModel ->
                if (homeNewDataModel?.isCache == false) {
                    _isRequestNetworkLiveData.postValue(Event(false))
                    currentTopAdsBannerToken = homeNewDataModel.topadsNextPageToken
                    onRefreshState = false
                    if (homeNewDataModel.list.isEmpty()) {
                        val error = "type:" + "revamp_empty_update; " +
                                "reason:" + "Visitable is empty; " +
                                "isProcessingDynamicChannel:" + homeNewDataModel.isProcessingDynamicChannle.toString() + ";" +
                                "isProcessingAtf:" + homeNewDataModel.isProcessingAtf.toString() + ";" +
                                "isFirstPage:" + homeNewDataModel.isFirstPage.toString() + ";" +
                                "isCache:" + homeNewDataModel.isCache.toString()

                        HomeServerLogger.logWarning(
                            type = TYPE_REVAMP_EMPTY_UPDATE,
                            throwable = MessageErrorException(error),
                            reason = error,
                            data = error
                        )
                    }
                    updateHomeData(homeNewDataModel)
                    _trackingLiveData.postValue(Event(homeNewDataModel.list.filterIsInstance<HomeVisitable>()))
                } else if (onRefreshState) {
                    if (homeNewDataModel?.list?.size?:0 > 1) {
                        _isRequestNetworkLiveData.postValue(Event(false))
                        takeTicker = false
                    }
                    if (homeNewDataModel?.list?.size?:0 > 0) {
                        homeNewDataModel?.let { updateHomeData(it) }
                    }
                }
            }
        }) {
            _updateNetworkLiveData.postValue(Result.error(error = it, data = null))
            val stackTrace = Log.getStackTraceString(it)
            HomeServerLogger.logWarning(
                type = TYPE_REVAMP_ERROR_INIT_FLOW,
                throwable = it,
                reason = (it.message ?: "".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                data = stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
            )
        }.invokeOnCompletion {
            _updateNetworkLiveData.postValue(Result.error(error = Throwable(), data = null))
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
            HomeServerLogger.logWarning(
                type = TYPE_CANCELLED_INIT_FLOW,
                throwable = it,
                reason = (it?.message ?: "No error propagated").take(ConstantKey.HomeTimber.MAX_LIMIT),
                data = stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
            )
            homeFlowDataCancelled = true
        }
    }

    fun getBeautyFest(data: List<Visitable<*>>) {
        launch{
            _beautyFestLiveData.postValue(homeBeautyFestUseCase.get().getBeautyFest(data))
        }
    }

    fun deleteQuestWidget() {
        findWidget<QuestWidgetModel> { questWidgetModel, index ->
            deleteWidget(questWidgetModel, index)
        }
    }
}