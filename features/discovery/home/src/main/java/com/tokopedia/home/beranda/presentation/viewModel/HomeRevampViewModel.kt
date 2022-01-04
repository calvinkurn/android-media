package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.VisibleForTesting
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
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
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
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class HomeRevampViewModel @Inject constructor(
    private val homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
    private val homeUseCase: Lazy<HomeDynamicChannelUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val closeChannelUseCase: Lazy<CloseChannelUseCase>,
    private val dismissHomeReviewUseCase: Lazy<DismissHomeReviewUseCase>,
    private val getAtcUseCase: Lazy<AddToCartOccMultiUseCase>,
    private val getKeywordSearchUseCase: Lazy<GetKeywordSearchUseCase>,
    private val homePlayCardHomeRepository: Lazy<HomePlayLiveDynamicRepository>,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val popularKeywordRepository: Lazy<HomePopularKeywordRepository>,
    private val injectCouponTimeBasedUseCase: Lazy<InjectCouponTimeBasedUseCase>,
    private val declineRechargeRecommendationUseCase: Lazy<DeclineRechargeRecommendationUseCase>,
    private val declineSalamWidgetUseCase: Lazy<DeclineSalamWIdgetUseCase>,
    private val getRechargeBUWidgetUseCase: Lazy<GetRechargeBUWidgetUseCase>,
    private val bestSellerMapper: Lazy<BestSellerMapper>,
    private val homeDispatcher: Lazy<CoroutineDispatchers>,
    private val playWidgetTools: Lazy<PlayWidgetTools>,
    private val homeBusinessUnitUseCase: Lazy<HomeBusinessUnitUseCase>) : BaseCoRoutineScope(homeDispatcher.get().io) {

    companion object {
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITY = "quantity"
        const val POSITION = "position"

        const val error_unable_to_parse_wallet = "Unable to parse wallet, wallet app list is empty"

        private const val TOP_ADS_BANNER_DIMEN_ID = 3
        private const val TOP_ADS_COUNT = 1
        private const val TOP_ADS_HOME_SOURCE = "1"
    }

    var isFirstLoad = true

    private var isGopayEligible: Boolean = false
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

    // Test cover banner url play widget is valid or not
    private val _requestImageTestLiveData = MutableLiveData<Event<PlayCardDataModel>>()
    val requestImageTestLiveData: LiveData<Event<PlayCardDataModel>> get() = _requestImageTestLiveData

    val oneClickCheckout: LiveData<Event<Any>> get() = _oneClickCheckout
    private val _oneClickCheckout = MutableLiveData<Event<Any>>()

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

    private val _rechargeBUWidgetLiveData = MutableLiveData<Event<RechargePerso>>()
    val rechargeBUWidgetLiveData: LiveData<Event<RechargePerso>> get() = _rechargeBUWidgetLiveData

    private val _homeNotifLiveData = MutableLiveData<HomeNotifModel>()
    val homeNotifLiveData: LiveData<HomeNotifModel> get() = _homeNotifLiveData

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
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var headerDataModel: HeaderDataModel? = null
    private var homeFlowDataCancelled = false
    private var onRefreshState = true
    private var takeTicker = true
    private var homeNotifModel = HomeNotifModel()
    private val homeFlowDynamicChannel: Flow<HomeDynamicChannelModel?> = homeUseCase.get().getHomeDataFlow().flowOn(homeDispatcher.get().io)
    private var popularKeywordRefreshCount = 1

    var currentTopAdsBannerToken: String = ""
    var homeDataModel = HomeDynamicChannelModel()
    var currentHeaderDataModel = HomeHeaderDataModel()

    /**
     * Job list
     */
    private var getHomeDataJob: Job? = null
    private var getSearchHintJob: Job? = null
    private var getPlayWidgetJob: Job? = null
    private var getSuggestedReviewJob: Job? = null
    private var dismissReviewJob: Job? = null
    private var getPopularKeywordJob: Job? = null
    private var buWidgetJob: Job? = null
    private var getRechargeRecommendationJob: Job? = null
    private var declineRechargeRecommendationJob: Job? = null
    private var getSalamWidgetJob: Job? = null
    private var declineSalamWidgetJob : Job? = null
    private var getRechargeBUWidgetJob: Job? = null
    private var injectCouponTimeBasedJob: Job? = null
    private var getTopAdsBannerDataJob: Job? = null
    private var getTabRecommendationJob: Job? = null

    init {
        _isViewModelInitialized.value = Event(true)
        _isRequestNetworkLiveData.value = Event(true)
        initFlow()
    }

    override fun onCleared() {
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
        super.onCleared()
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

    //TODO 2: Remove getRecommendationWidget -> Move to HomeRecommendationUseCase.onHomeBestSellerFilterClick()
    fun getRecommendationWidget(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel, selectedChipsPosition: Int){
        findWidget<BestSellerDataModel> { currentDataModel, index ->
            launchCatchError(coroutineContext, block = {
                val recomData = getRecommendationUseCase.get().getData(
                        GetRecommendationRequestParam(
                                pageName = bestSellerDataModel.pageName,
                                queryParam = if(filterChip.isActivated) filterChip.value else ""
                        )
                )
                if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                    val recomWidget = recomData.first().copy(
                            recommendationFilterChips = bestSellerDataModel.filterChip
                    )
                    val newBestSellerDataModel = bestSellerMapper.get().mappingRecommendationWidget(recomWidget)
                    val newModel = currentDataModel.copy(
                            seeMoreAppLink = newBestSellerDataModel.seeMoreAppLink,
                            recommendationItemList = newBestSellerDataModel.recommendationItemList,
                            productCardModelList = newBestSellerDataModel.productCardModelList,
                            height = newBestSellerDataModel.height,
                            filterChip = newBestSellerDataModel.filterChip.map{
                                it.copy(isActivated = filterChip.name == it.name
                                        && filterChip.isActivated)
                            },
                            dividerType = bestSellerDataModel.dividerType,
                            chipsPosition = (selectedChipsPosition+1)
                    )
                    updateWidget(newModel, index)
                } else {
                    updateWidget(bestSellerDataModel.copy(
                            filterChip = bestSellerDataModel.filterChip.map{
                                it.copy(isActivated = filterChip.name == it.name
                                        && !filterChip.isActivated)
                            }
                    ), index)
                }
            }){ _ ->
                updateWidget(bestSellerDataModel.copy(
                        filterChip = bestSellerDataModel.filterChip.map {
                            it.copy(isActivated = filterChip.name == it.name
                                    && !filterChip.isActivated)
                        }
                ), index)
            }
        }
    }

    //TODO 4.1: Remove updateBannerTotalView -> Move to HomePlayUseCase.onGetWalletData()
    fun updateBannerTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return

        val homeList = homeDataModel.list
        val playCard = homeList.withIndex().find {
            (_, visitable) -> (visitable is PlayCardDataModel && visitable.playCardHome?.channelId == channelId)
        } ?: return
        if(playCard.value is PlayCardDataModel && (playCard.value as PlayCardDataModel).playCardHome != null) {
            val newPlayCard = (playCard.value as PlayCardDataModel).copy(playCardHome = (playCard.value as PlayCardDataModel).playCardHome?.copy(totalView = totalView))
            updateWidget(newPlayCard, playCard.index)
        }
    }

    //TODO 4.4: Remove setPlayBanner -> Move to HomePlayLiveDynamicRepository,
    // integrate with HomeDynamicChannelUseCase to update data
    // If the image is valid it will be set play banner to UI
    fun setPlayBanner(playCardDataModel: PlayCardDataModel){
        updateWidget(playCardDataModel, -1)
    }

    //TODO 4.5: Remove setPlayBanner -> Move to HomePlayLiveDynamicRepository,
    // integrate with HomeDynamicChannelUseCase to update data
    // because don't let the banner blank
    fun clearPlayBanner(){
        homeDataModel.list.withIndex().find { (_, visitable) -> visitable is PlayCardDataModel }?.let{
            deleteWidget(it.value, it.index)
        }
    }

    //TODO 5.1: Remove onRemoveSuggestedReview -> Move to HomeSuggestedReviewUseCase.onSuggestedReviewDismissed
    fun onRemoveSuggestedReview() {
        findWidget<ReviewDataModel> { reviewWidget, index ->
            deleteWidget(reviewWidget, -1)
        }
    }

    //TODO 6: Remove onCloseBuyAgain -> Move to HomeListCarouselUseCase.onCloseListCarousel,
    fun onCloseBuyAgain(channelId: String, position: Int){
        val dynamicChannelDataModel = homeDataModel.list.find { visitable ->
            visitable is DynamicChannelDataModel && visitable.channel?.id == channelId }
        val recommendationListHomeComponentModel = homeDataModel.list.find {
            visitable ->  visitable is RecommendationListCarouselDataModel && visitable.channelModel.id == channelId
        }

        if (dynamicChannelDataModel != null && dynamicChannelDataModel is DynamicChannelDataModel){
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if(closeChannel.success){
                    deleteWidget(dynamicChannelDataModel, position)
                } else {
                    _errorEventLiveData.postValue(Event(Throwable()))
                }
            }){
                _errorEventLiveData.postValue(Event(it))
                Timber.tag(this::class.java.simpleName).e(it)
            }
        }

        if (recommendationListHomeComponentModel != null && recommendationListHomeComponentModel is RecommendationListCarouselDataModel){
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if(closeChannel.success){
                    deleteWidget(recommendationListHomeComponentModel, position)
                } else {
                    _errorEventLiveData.postValue(Event(Throwable()))
                }
            }){
                it.printStackTrace()
                _errorEventLiveData.postValue(Event(it))
            }
        }
    }

    //TODO 7: Remove removeViewHolderAtPosition -> Move to HomeDynamicChannelUseCase,
    fun removeViewHolderAtPosition(position: Int) {
        if(position != -1 && position < homeDataModel.list.size) {
            val detectViewHolder = homeDataModel.list[position]
            detectViewHolder.let {
                deleteWidget(it, position)
            }
        }
    }

    //TODO 8: Remove onCloseTicker -> Move to HomeTickerUseCase.onCloseTicker
    fun onCloseTicker() {
        val detectTicker = homeDataModel.list.find { visitable -> visitable is TickerDataModel }
        (detectTicker as? TickerDataModel)?.let {
            deleteWidget(it, -1)
        }
    }

    fun onRefreshTokoPoint() {
        if (!userSession.get().isLoggedIn) return
        launch {
            currentHeaderDataModel = homeBalanceWidgetUseCase.onGetTokopointData(currentHeaderDataModel)
            homeUseCase.get().updateHeaderData(currentHeaderDataModel, homeDataModel) {
                updateHomeData(it)
            }
        }
    }

    //TODO 9: Remove insertRechargeBUWidget -> Move to HomeDynamicChannelUseCase
    // integrate with HomeDynamicChannelUseCase to update data
    fun insertRechargeBUWidget(data: RechargePerso) {
        if (data.items.isNotEmpty()) {
            homeDataModel.list.let {
                val findRechargeBUWidget = it.find { visitable -> visitable is RechargeBUWidgetDataModel }
                val indexOfRechargeBUWidget = it.indexOf(findRechargeBUWidget)
                if (indexOfRechargeBUWidget > -1 && findRechargeBUWidget is RechargeBUWidgetDataModel) {
                    val newFindRechargeBUWidget = findRechargeBUWidget.copy(data = data)
                    updateWidget(newFindRechargeBUWidget, indexOfRechargeBUWidget)
                }
            }
        } else {
            removeRechargeBUWidget()
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

    //TODO 5.2: Remove onRemoveSuggestedReview -> Move to HomeSuggestedReviewUseCase.onSuggestedReviewDismissed
    fun dismissReview() {
        onRemoveSuggestedReview()
        if(dismissReviewJob?.isActive == true) return
        dismissReviewJob = launchCatchError(coroutineContext, block = {
            dismissHomeReviewUseCase.get().executeOnBackground()
        }){}
    }

    //TODO 11.1: Remove getBusinessUnitTabData -> Move to HomeDynamicChannelUseCase
    //Create BusinessUnitRepository
    fun getBusinessUnitTabData(position: Int){
        launch {
            val buWidgetData = homeBusinessUnitUseCase.get().getBusinessUnitTab(homeDataModel, position)
            updateWidget(buWidgetData, position)
        }
    }

    //TODO 11.2: Remove getBusinessUnitTabData -> Move to HomeBusinessUnitUseCase.onClickBusinessUnitTab
    //Create BusinessUnitRepository
    fun getBusinessUnitData(tabId: Int, position: Int, tabName: String){
        launch{
            val (buData, channelPosition) = homeBusinessUnitUseCase.get().getBusinessUnitData(tabId, position, tabName, homeDataModel)
            updateWidget(buData, channelPosition)

        }
    }

    //TODO 12.1: Remove getDynamicChannelData -> Move to HomeDynamicChannelUseCase
    fun getDynamicChannelData(dynamicChannelDataModel: DynamicChannelDataModel, position: Int){
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(
                    dynamicChannelDataModel.channel?.groupId?:"")

            if(visitableList.isEmpty()) {
                deleteWidget(dynamicChannelDataModel, position)
            } else {
                var lastIndex = position
                val dynamicData = homeDataModel.list.getOrNull(lastIndex)
                if(dynamicData !is DynamicChannelDataModel && dynamicData != dynamicChannelDataModel){
                    lastIndex = homeDataModel.list.indexOf(dynamicChannelDataModel)
                }
                deleteWidget(dynamicChannelDataModel, lastIndex)
                visitableList.reversed().forEach {
                    addWidget(it, lastIndex)
                }
                _trackingLiveData.postValue(Event(visitableList.filterIsInstance(HomeVisitable::class.java)))
            }
        }){
            deleteWidget(dynamicChannelDataModel, position)
        }
    }

    //TODO 12.2: Remove getDynamicChannelData -> Move to HomeDynamicChannelUseCase
    fun getDynamicChannelData(visitable: Visitable<*>, channelModel: ChannelModel, position: Int){
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(channelModel.groupId)

            if(visitableList.isEmpty()){
                deleteWidget(visitable, position)
            } else {
                var lastIndex = position
                val dynamicData = homeDataModel.list.getOrNull(lastIndex)
                if(dynamicData !is DynamicChannelDataModel && dynamicData != visitable){
                    lastIndex = homeDataModel.list.indexOf(visitable)
                }
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

    //TODO 13.2: Remove declineRechargeRecommendationItem -> Move to HomeRechargeRecommendationUseCase.onCloseHomeRechargeRecommendation
    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        removeRechargeRecommendation()
        if(declineRechargeRecommendationJob?.isActive == true) return
        declineRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            declineRechargeRecommendationUseCase.get().setParams(requestParams)
            declineRechargeRecommendationUseCase.get().executeOnBackground()
        }){}
    }

    //TODO 14.2: Remove declineSalamItem -> Move to HomeSalamRecommendationUseCase.onCloseHomeSalamRecommendation
    fun declineSalamItem(requestParams: Map<String, Int>){
        removeSalamWidget()
        if (declineSalamWidgetJob?.isActive==true) return
        declineSalamWidgetJob = launchCatchError(coroutineContext, block = {
            declineSalamWidgetUseCase.get().setParams(requestParams)
            declineSalamWidgetUseCase.get().executeOnBackground()
        }){}
    }

    //TODO 15: Remove getRechargeBUWidget -> Move to HomeDynamicChannelUseCase
    // integrate with HomeDynamicChannelUseCase to update data
    fun getRechargeBUWidget(source: WidgetSource) {
        if(getRechargeBUWidgetJob?.isActive == true) return
        getRechargeBUWidgetJob = launchCatchError(coroutineContext, block = {
            getRechargeBUWidgetUseCase.get().setParams(source)
            val data = getRechargeBUWidgetUseCase.get().executeOnBackground()
            _rechargeBUWidgetLiveData.postValue(Event(data))
        }){
            removeRechargeBUWidget()
        }
    }

    fun getFeedTabData() {
        launch {
            homeUseCase.get().getFeedTabData(homeDataModel)
        }
    }

    //TODO 4.6: Remove getLoadPlayBannerFromNetwork -> Move to HomePlayLiveDynamicRepository
    // integrate with HomeDynamicChannelUseCase to update data
    @VisibleForTesting
    fun getLoadPlayBannerFromNetwork(playBanner: PlayCardDataModel){
        if(getPlayWidgetJob?.isActive == true) return
        getPlayWidgetJob = launchCatchError(coroutineContext, block = {
            homePlayCardHomeRepository.get().setParams()
            val data = homePlayCardHomeRepository.get().executeOnBackground()
            if (data.playChannels.isEmpty() || data.playChannels.first().coverUrl.isEmpty()) {
                clearPlayBanner()
            } else{
                _requestImageTestLiveData.postValue(Event(playBanner.copy(playCardHome = data.playChannels.first())))
            }
        }){
            clearPlayBanner()
        }
    }

    //TODO 17.1: Remove getPopularKeyword -> Move to HomeDynamicChannelUseCase
    // integrate with HomeDynamicChannelUseCase to update data
    fun getPopularKeyword() {
        if(getPopularKeywordJob?.isActive == true) return
        findWidget<PopularKeywordListDataModel> { popularKeywordListDataModel, index ->
            getPopularKeywordJob = launchCatchError(coroutineContext, {
                popularKeywordRepository.get().setParams(page = popularKeywordRefreshCount)
                val results = popularKeywordRepository.get().executeOnBackground()
                if (results.data.keywords.isNotEmpty()) {
                    val resultList = convertPopularKeywordDataList(results.data)
                    updateWidget(popularKeywordListDataModel.copy(
                            title = results.data.title,
                            subTitle = results.data.subTitle,
                            popularKeywordList = resultList,
                            isErrorLoad = false
                    ), index)
                    popularKeywordRefreshCount++
                } else {
                    updateWidget(popularKeywordListDataModel.copy(isErrorLoad = true), index)
                }
            }){ throwable ->
                updateWidget(popularKeywordListDataModel.copy(isErrorLoad = true), index)
                throwable.printStackTrace()
            }
        }
    }

    //TODO 18: Remove getSearchHint -> Move to HomeSearchUseCase.onGetSearchHint
    fun getSearchHint(isFirstInstall: Boolean) {
        if(getSearchHintJob?.isActive == true) return
        getSearchHintJob = launchCatchError(coroutineContext, block={
            getKeywordSearchUseCase.get().params = getKeywordSearchUseCase.get().createParams(isFirstInstall, userSession.get().deviceId, userSession.get().userId)
            val data = getKeywordSearchUseCase.get().executeOnBackground()
            _searchHint.postValue(data.searchData)
        }){
            _searchHint.postValue(SearchPlaceholder())
        }
    }

    //TODO 19: Remove getOneClickCheckoutHomeComponent -> Move to HomeListCarouselUseCase.onClickOneClickCheckoutListCarousel
    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int){
        launchCatchError(coroutineContext, block = {
            val quantity = if(grid.minOrder < 1) "1" else grid.minOrder.toString()
            val addToCartResult = getAtcUseCase.get().setParams(AddToCartOccMultiRequestParams(
                    carts = listOf(
                            AddToCartOccMultiCartParam(
                                    productId = grid.id,
                                    quantity = quantity,
                                    shopId = grid.shopId,
                                    warehouseId = grid.warehouseId,
                                    productName = grid.name,
                                    price = grid.price
                            )
                    ),
                    userId = getUserId()
            )).executeOnBackground().mapToAddToCartDataModel()
            if(!addToCartResult.isStatusError()) {
                _oneClickCheckoutHomeComponent.postValue(Event(
                        mapOf(
                                ATC to addToCartResult,
                                CHANNEL to channel,
                                GRID to grid,
                                QUANTITY to quantity,
                                POSITION to position

                        )
                ))
            }
            else {
                _oneClickCheckoutHomeComponent.postValue(Event(Throwable()))
            }
        }){
            it.printStackTrace()
            _oneClickCheckoutHomeComponent.postValue(Event(it))
        }
    }

    //TODO 20: Remove injectCouponTimeBased -> Move to HomeCouponUseCase.onInjectCouponUseCase()
    //Create HomeWalletRepository
    fun injectCouponTimeBased() {
        if(injectCouponTimeBasedJob?.isActive == true) return
        injectCouponTimeBasedJob = launchCatchError(coroutineContext, {
            _injectCouponTimeBasedResult.value = Result.success(injectCouponTimeBasedUseCase.get().executeOnBackground().data)
        }){
            _injectCouponTimeBasedResult.postValue(Result.error(error = it))
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

    fun getUserName() = userSession.get().name ?: ""

    //TODO 4.6: Remove getPlayWidget -> Move to HomePlayLiveDynamicRepository,
    // integrate with HomeDynamicChannelUseCase to update data
    fun getPlayWidget() {
        findWidget<CarouselPlayWidgetDataModel> { playWidget, index ->
            launchCatchError(block = {
                val response = playWidgetTools.get().getWidgetFromNetwork(
                        PlayWidgetUseCase.WidgetType.Home,
                        homeDispatcher.get().io
                )
                val uiModel = playWidgetTools.get().mapWidgetToModel(response)
                updateWidget(playWidget.copy(widgetUiModel = uiModel), index)
            }) {
                deleteWidget(playWidget, index)
            }
        }
    }

    //TODO 4.7: Remove updatePlayWidgetTotalView -> Move to HomePlayUseCase.onTotalViewUpdate
    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        updateCarouselPlayWidget {
            it.copy(widgetUiModel = playWidgetTools.get().updateTotalView(it.widgetUiModel, channelId, totalView))
        }
    }

    //TODO 4.8: Remove updatePlayWidgetReminder -> Move to HomePlayUseCase.onWidgetReminder
    fun updatePlayWidgetReminder(channelId: String, isReminder: Boolean) {
        updateCarouselPlayWidget {
            val reminderType = if(isReminder) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
            it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType))
        }
    }

    //TODO 4.9: Remove shouldUpdatePlayWidgetToggleReminder -> Move to HomePlayUseCase.onShouldUpdatePlayWidgetToggleReminder
    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!userSession.get().isLoggedIn) _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        else updatePlayWidgetToggleReminder(channelId, reminderType)
    }

    //TODO 21: Remove, deleted because we already use centralized notification on nav toolbar
    fun setHomeNotif(notifCount: Int, messageCount: Int, cartCount: Int) {
        homeNotifModel = HomeNotifModel(
                notifCount = notifCount,
                messageCount = messageCount,
                cartCount = cartCount
        )
        _homeNotifLiveData.value = homeNotifModel
    }

    fun updateChooseAddressData(homeChooseAddressData: HomeChooseAddressData) {
        this.homeDataModel.setAndEvaluateHomeChooseAddressData(homeChooseAddressData)
        homeUseCase.get().updateHeaderData(currentHeaderDataModel, this.homeDataModel) {
            updateHomeData(it)
        }
    }

    fun getAddressData(): HomeChooseAddressData {
        return homeDataModel.homeChooseAddressData
    }

    fun removeChooseAddressWidget() {
        homeUseCase.get().removeChooseAddressData(homeDataModel) {
            updateHomeData(it)
        }
    }

    //TODO 29: Remove findWidget -> Move to HomeDynamicChannelUseCase
    private inline fun <reified T> findWidget(predicate: (T?) -> Boolean = {true}, actionOnFound: (T, Int) -> Unit) {
        homeDataModel.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
            it?.let {
                if (it.value is T) {
                    actionOnFound.invoke(it.value as T, it. index)
                }
            }
        }
    }

    //TODO 30: Remove addWidget -> Move to HomeDynamicChannelUseCase
    private fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        homeDataModel.addWidgetModel(visitable, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    //TODO 31: Remove updateWidget -> Move to HomeDynamicChannelUseCase
    private fun updateWidget(visitable: Visitable<*>, position: Int, visitableToChange: Visitable<*>? = null) {
        homeDataModel.updateWidgetModel(visitable, visitableToChange, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    //TODO 32: Remove deleteWidget -> Move to HomeDynamicChannelUseCase
    private fun deleteWidget(visitable: Visitable<*>?, position: Int) {
        homeDataModel.deleteWidgetModel(visitable, position) { _homeLiveDynamicChannel.postValue(homeDataModel) }
    }

    //TODO 33: Remove updateHomeData -> Move to HomeDynamicChannelUseCase
    private fun updateHomeData(homeNewDynamicChannelModel: HomeDynamicChannelModel) {
        this.homeDataModel = homeNewDynamicChannelModel
        this.homeDataModel.homeBalanceModel = currentHeaderDataModel?.headerDataModel?.homeBalanceModel?:HomeBalanceModel()


        _homeLiveDynamicChannel.postValue(homeDataModel)
        _resetNestedScrolling.postValue(Event(true))
    }

    //TODO 17.2: Remove convertPopularKeywordDataList -> Move to HomeDynamicChannelUseCase
    private fun convertPopularKeywordDataList(popularKeywordList: HomeWidget.PopularKeywordList): MutableList<PopularKeywordDataModel> {
        val keywordList = popularKeywordList.keywords
        val dataList: MutableList<PopularKeywordDataModel> = mutableListOf()
        for (pojo in keywordList) {
            dataList.add(
                    PopularKeywordDataModel(
                            recommendationType = popularKeywordList.recommendationType,
                            applink = pojo.url,
                            imageUrl = pojo.imageUrl,
                            title = pojo.keyword,
                            productCount = pojo.productCount)
            )
        }
        return dataList
    }

    //TODO 38: Remove removeDynamicChannelLoadingModel -> Move to HomeDynamicChannelUseCase
    private fun removeDynamicChannelLoadingModel() {
        findWidget<DynamicChannelLoadingModel> { _, index ->
            updateWidget(DynamicChannelRetryModel(false),index)
        }
        findWidget<DynamicChannelRetryModel> { retryWidget, index ->
            updateWidget(retryWidget, index)
            addWidget(DynamicChannelRetryModel(false), homeDataModel.list.size)
        }
    }

    //TODO 4.10: Remove updatePlayWidgetToggleReminder -> Move to HomePlayUseCase
    private fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        updateCarouselPlayWidget {
            it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType))
        }

        launchCatchError(block = {
            val response = playWidgetTools.get().updateToggleReminder(
                    channelId,
                    reminderType,
                    homeDispatcher.get().io
            )

            when (val success = playWidgetTools.get().mapWidgetToggleReminder(response)) {
                success -> {
                    _playWidgetReminderObservable.postValue(Result.success(reminderType))
                }
                else -> {
                    updateCarouselPlayWidget {
                        it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType.switch()))
                    }
                    _playWidgetReminderObservable.postValue(Result.error(error = Throwable()))
                }
            }
        }) { throwable ->
            updateCarouselPlayWidget {
                it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType.switch()))
            }
            _playWidgetReminderObservable.postValue(Result.error(error = throwable))
        }
    }

    //TODO 4.11: Remove updateCarouselPlayWidget -> Move to HomePlayUseCase
    private fun updateCarouselPlayWidget(onUpdate: (oldVal: CarouselPlayWidgetDataModel) -> CarouselPlayWidgetDataModel) {
        val dataModel = homeDataModel.list.find { it is CarouselPlayWidgetDataModel } ?: return
        if (dataModel !is CarouselPlayWidgetDataModel) return

        val index = homeDataModel.list.indexOfFirst { it is CarouselPlayWidgetDataModel }
        updateWidget(onUpdate(dataModel), index)
    }

    //TODO 3.14: Remove getBalanceWidgetData -> Move to HomeWalletUseCase
    private fun getBalanceWidgetData() {
        if (!userSession.get().isLoggedIn) return
        launch {
            homeUseCase.get().updateHeaderData(currentHeaderDataModel.copy(
                    headerDataModel = currentHeaderDataModel.headerDataModel?.copy(
                            homeBalanceModel = currentHeaderDataModel?.headerDataModel?.homeBalanceModel.apply {
                                this?.initBalanceModelByType()
                            }?: HomeBalanceModel()
                    )
            ), homeDataModel) {
                updateHomeData(it)
            }

            currentHeaderDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData(currentHeaderDataModel)
            homeUseCase.get().updateHeaderData(currentHeaderDataModel, homeDataModel) {
                updateHomeData(it)
            }
        }
    }

    //TODO 40: Remove removeRechargeRecommendation -> Move to HomeDynamicChannelUseCase
    private fun removeRechargeRecommendation() {
        val findRechargeRecommendationViewModel =
                homeDataModel.list.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.RECHARGE)}
                        ?: return
        if (findRechargeRecommendationViewModel is ReminderWidgetModel && findRechargeRecommendationViewModel.source==ReminderEnum.RECHARGE) {
            deleteWidget(findRechargeRecommendationViewModel, -1)
        }
    }

    //TODO 41: Remove removeSalamWidget -> Move to HomeDynamicChannelUseCase
    private fun removeSalamWidget() {
        val findSalamWidgetModel =
                homeDataModel.list.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.SALAM)}
                        ?: return
        if (findSalamWidgetModel is ReminderWidgetModel && findSalamWidgetModel.source==ReminderEnum.SALAM) {
            deleteWidget(findSalamWidgetModel, -1)
        }
    }

    //TODO 42: Remove removeRechargeBUWidget -> Move to HomeDynamicChannelUseCase
    private fun removeRechargeBUWidget() {
        findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
            deleteWidget(rechargeBuModel, index)
        }
    }

    private fun initFlow() {
        launchCatchError(coroutineContext, block = {
            homeFlowDynamicChannel.collect { homeNewDataModel ->
                if (homeNewDataModel?.isCache == false) {
                    _isRequestNetworkLiveData.postValue(Event(false))
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

                    /**
                     * Get external response when ATF is completed (first page = false)
                     */
                    if (homeNewDataModel.isProcessingAtf == false) {
                        getExternalApi()
                    }
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
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
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
            //TODO fix for unit test
//            homeFlowDataCancelled = true
        }

        refreshHomeData()
    }

    //TODO 44: Delete
    private fun getExternalApi() {
        getPopularKeyword()
    }

    //TODO 46: Remove getBeautyFest -> Move to HomeBeautyFestUseCase
    fun getBeautyFest(data: List<Visitable<*>>) {
        //beauty fest event will qualify if contains "isChannelBeautyFest":true
        launchCatchError(coroutineContext, {
            if (Gson().toJson(data).toString().contains("\"isChannelBeautyFest\":true"))
                _beautyFestLiveData.postValue(HomeRevampFragment.BEAUTY_FEST_TRUE)
            else
                _beautyFestLiveData.postValue(HomeRevampFragment.BEAUTY_FEST_FALSE)
        }, {
            it.printStackTrace()
            _beautyFestLiveData.postValue(HomeRevampFragment.BEAUTY_FEST_NOT_SET)
        })
    }

    //TODO 47: Remove deleteQuestWidget -> Move to HomeDynamicChannelUseCase
    fun deleteQuestWidget() {
        findWidget<QuestWidgetModel> { questWidgetModel, index ->
            deleteWidget(questWidgetModel, index)
        }
    }
}