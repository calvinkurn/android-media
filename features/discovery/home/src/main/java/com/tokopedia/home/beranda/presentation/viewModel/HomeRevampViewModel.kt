package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.beranda.common.BaseCoRoutineScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperRechargetoReminder
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperSalamtoReminder
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DisplayHeadlineAdsEntity
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeNotifModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.PendingCashbackModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeInitialShimmerDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
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
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.RequestParams
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
        private val homeUseCase: Lazy<HomeRevampUseCase>,
        private val userSession: Lazy<UserSessionInterface>,
        private val closeChannelUseCase: Lazy<CloseChannelUseCase>,
        private val dismissHomeReviewUseCase: Lazy<DismissHomeReviewUseCase>,
        private val getAtcUseCase: Lazy<AddToCartOccUseCase>,
        private val getBusinessUnitDataUseCase: Lazy<GetBusinessUnitDataUseCase>,
        private val getBusinessWidgetTab: Lazy<GetBusinessWidgetTab>,
        private val getDisplayHeadlineAds: Lazy<GetDisplayHeadlineAds>,
        private val getHomeReviewSuggestedUseCase: Lazy<GetHomeReviewSuggestedUseCase>,
        private val getHomeTokopointsDataUseCase: Lazy<GetHomeTokopointsDataUseCase>,
        private val getHomeTokopointsListDataUseCase: Lazy<GetHomeTokopointsListDataUseCase>,
        private val getKeywordSearchUseCase: Lazy<GetKeywordSearchUseCase>,
        private val getPendingCashbackUseCase: Lazy<GetCoroutinePendingCashbackUseCase>,
        private val getPlayCardHomeUseCase: Lazy<GetPlayLiveDynamicUseCase>,
        private val getRecommendationTabUseCase: Lazy<GetRecommendationTabUseCase>,
        private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
        private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>,
        private val getWalletBalanceUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val popularKeywordUseCase: Lazy<GetPopularKeywordUseCase>,
        private val injectCouponTimeBasedUseCase: Lazy<InjectCouponTimeBasedUseCase>,
        private val getRechargeRecommendationUseCase: Lazy<GetRechargeRecommendationUseCase>,
        private val declineRechargeRecommendationUseCase: Lazy<DeclineRechargeRecommendationUseCase>,
        private val getSalamWidgetUseCase: Lazy<GetSalamWidgetUseCase>,
        private val declineSalamWidgetUseCase: Lazy<DeclineSalamWIdgetUseCase>,
        private val getRechargeBUWidgetUseCase: Lazy<GetRechargeBUWidgetUseCase>,
        private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
        private val bestSellerMapper: Lazy<BestSellerMapper>,
        private val homeDispatcher: Lazy<CoroutineDispatchers>,
        private val playWidgetTools: Lazy<PlayWidgetTools>
) : BaseCoRoutineScope(homeDispatcher.get().io) {

    companion object {
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITY = "quantity"
        const val POSITION = "position"
    }

    val homeLiveData: LiveData<HomeDataModel>
        get() = _homeLiveData
    private val _homeLiveData: MutableLiveData<HomeDataModel> = MutableLiveData()

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
    private val homeFlowData: Flow<HomeDataModel?> = homeUseCase.get().getHomeData().flowOn(homeDispatcher.get().main)
    private var navRollanceType: String = ""
    private var useNewBalanceWidget: Boolean = true
    private var popularKeywordRefreshCount = 1

    var currentTopAdsBannerToken: String = ""
    var homeDataModel = HomeDataModel()

    /**
     * Job list
     */
    private var getHomeDataJob: Job? = null
    private var getSearchHintJob: Job? = null
    private var getPlayWidgetJob: Job? = null
    private var getTokopointJob: Job? = null
    private var getWalletBalanceJob: Job? = null
    private var getTokocashJob: Job? = null
    private var getSuggestedReviewJob: Job? = null
    private var getPendingCashBalanceJob: Job? = null
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
        initCacheData()
    }

    override fun onCleared() {
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
        super.onCleared()
    }

    fun refresh(isFirstInstall: Boolean, forceRefresh: Boolean = false){
        if ((forceRefresh && getHomeDataJob?.isActive == false) || (!fetchFirstData && homeRateLimit.shouldFetch(HOME_LIMITER_KEY))) {
            refreshHomeData()
            _isNeedRefresh.value = Event(true)
        }
        balanceRemoteConfigCondition(
                isNewBalanceWidget = {
                    getBalanceWidgetData()
                },
                isOldBalanceWidget = {
                    getTokocashBalance()
                    getTokopoint()
                }
        )
        getSearchHint(isFirstInstall)
    }

    fun getRecommendationWidget(){
        findWidget<BestSellerDataModel> { bestSellerDataModel, index ->
            launchCatchError(coroutineContext, block = {
                val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()

                getRecommendationFilterChips.get().setParams(
                        userId = if (userSession.get().userId.isEmpty()) 0 else userSession.get().userId.toInt(),
                        pageName = bestSellerDataModel.pageName,
                        queryParam = bestSellerDataModel.widgetParam
                )
                recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)

                val recomData = getRecommendationUseCase.get().getData(
                        GetRecommendationRequestParam(
                                pageName = bestSellerDataModel.pageName,
                                queryParam = bestSellerDataModel.widgetParam
                        )
                )

                if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                    val recomWidget = recomData.first().copy(
                            recommendationFilterChips = recomFilterList
                    )
                    val dataModel = bestSellerMapper.get().mappingRecommendationWidget(recomWidget)
                    updateWidget(dataModel.copy(
                            id = bestSellerDataModel.id,
                            pageName = bestSellerDataModel.pageName,
                            widgetParam = bestSellerDataModel.widgetParam
                    ), index)
                } else {
                    deleteWidget(bestSellerDataModel, index)
                }
            }){
                deleteWidget(bestSellerDataModel, index)
            }
        }
    }

    fun getRecommendationWidget(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel){
        val data = _homeLiveData.value?.list?.toMutableList()
        data?.withIndex()?.find { it.value is BestSellerDataModel && (it.value as BestSellerDataModel).id == bestSellerDataModel.id }?.let {
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
                    val newModel = (it.value as BestSellerDataModel).copy(
                            seeMoreAppLink = newBestSellerDataModel.seeMoreAppLink,
                            recommendationItemList = newBestSellerDataModel.recommendationItemList,
                            productCardModelList = newBestSellerDataModel.productCardModelList,
                            height = newBestSellerDataModel.height,
                            filterChip = newBestSellerDataModel.filterChip.map{
                                it.copy(isActivated = filterChip.name == it.name
                                        && filterChip.isActivated)
                            }
                    )
                    updateWidget(newModel, it.index)
                } else {
                    updateWidget(bestSellerDataModel.copy(
                            filterChip = bestSellerDataModel.filterChip.map{
                                it.copy(isActivated = filterChip.name == it.name
                                        && !filterChip.isActivated)
                            }
                    ), it.index)
                }
            }){ _ ->
                updateWidget(bestSellerDataModel.copy(
                        filterChip = bestSellerDataModel.filterChip.map {
                            it.copy(isActivated = filterChip.name == it.name
                                    && !filterChip.isActivated)
                        }
                ), it.index)
            }
        }
    }

    fun getHeaderData() {
        if (!userSession.get().isLoggedIn) return
        balanceRemoteConfigCondition(
                isNewBalanceWidget = {
                    getBalanceWidgetData()
                },
                isOldBalanceWidget = {
                    getTokocashBalance()
                    getTokopoint()
                }
        )
    }

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

    fun getPlayBanner(position: Int){
        val playBanner =
                if (position < homeDataModel.list.size
                        && homeDataModel.list.get(position) is PlayCardDataModel)
                    homeDataModel.list.getOrNull(position) as PlayCardDataModel
                else homeDataModel.list.find { it is PlayCardDataModel }
        playBanner?.let {
            getLoadPlayBannerFromNetwork(playBanner as PlayCardDataModel)
        }
    }

    // Logic detect play banner should load data from API
    private fun getPlayBanner(){
        // Check the current index is play card view model
        findWidget<PlayCardDataModel> { playCardModel, index ->
            getLoadPlayBannerFromNetwork(playCardModel)
        }
    }

    // If the image is valid it will be set play banner to UI
    fun setPlayBanner(playCardDataModel: PlayCardDataModel){
        updateWidget(playCardDataModel, -1)
    }

    // play widget it will be removed when load image is failed (deal from PO)
    // because don't let the banner blank
    fun clearPlayBanner(){
        homeDataModel.list.withIndex().find { (_, visitable) -> visitable is PlayCardDataModel }?.let{
            deleteWidget(it.value, it.index)
        }
    }

    fun onRemoveSuggestedReview() {
        findWidget<ReviewDataModel> { reviewWidget, index ->
            deleteWidget(reviewWidget, -1)
        }
    }

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

    fun removeViewHolderAtPosition(position: Int) {
        if(position != -1 && position < homeDataModel.list.size) {
            val detectViewHolder = homeDataModel.list[position]
            detectViewHolder.let {
                deleteWidget(it, position)
            }
        }
    }

    fun onCloseTicker() {
        val detectTicker = homeDataModel.list.find { visitable -> visitable is TickerDataModel }
        (detectTicker as? TickerDataModel)?.let {
            deleteWidget(it, -1)
        }
    }

    fun onRefreshTokoPoint() {
        if (!userSession.get().isLoggedIn) return
        balanceRemoteConfigCondition(
                isNewBalanceWidget = {
                    getTokopointDrawerListData()
                },
                isOldBalanceWidget = {
                    updateHeaderViewModel(
                            tokopointsDrawer = null,
                            tokopointsBBODrawer = null,
                            isTokoPointDataError = false
                    )
                    getTokopoint()
                }
        )
    }

    fun onRefreshTokoCash() {
        balanceRemoteConfigCondition(
                isNewBalanceWidget = {
                    getWalletBalanceData()
                },
                isOldBalanceWidget = {
                    if (!userSession.get().isLoggedIn) return@balanceRemoteConfigCondition
                    updateHeaderViewModel(
                            homeHeaderWalletAction = null,
                            isWalletDataError = false
                    )
                    getTokocashBalance()
                }
        )
    }

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

    fun getRecommendationFeedSectionPosition() = homeDataModel.list.size -1

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
            _updateNetworkLiveData.postValue(Result.error(it, null, ""))

            ServerLogger.log(Priority.P2, "HOME_STATUS",
                    mapOf("type" to "revamp_error_refresh",
                            "reason" to (it.message ?: "").take(ConstantKey.HomeTimber.MAX_LIMIT),
                            "data" to Log.getStackTraceString(it).take(ConstantKey.HomeTimber.MAX_LIMIT)
                    ))
        }
    }

    fun dismissReview() {
        onRemoveSuggestedReview()
        if(dismissReviewJob?.isActive == true) return
        dismissReviewJob = launchCatchError(coroutineContext, block = {
            dismissHomeReviewUseCase.get().executeOnBackground()
        }){}
    }

    fun getBusinessUnitTabData(position: Int){
        launchCatchError(coroutineContext, block = {
            val data = getBusinessWidgetTab.get().executeOnBackground()
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                val buWidgetData = buWidget.copy(
                        tabList = data.tabBusinessList,
                        backColor = data.widgetHeader.backColor,
                        contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
                updateWidget(buWidgetData, position)
            }
        }){
            (homeDataModel.list.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                updateWidget(buWidget.copy(tabList = listOf()), position)
            }
        }
    }

    fun getBusinessUnitData(tabId: Int, position: Int, tabName: String){
        if(buWidgetJob?.isActive == true) return
        buWidgetJob = launchCatchError(coroutineContext, block = {
            getBusinessUnitDataUseCase.get().setParams(tabId, position, tabName)
            val data = getBusinessUnitDataUseCase.get().executeOnBackground()
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = data)
                updateWidget(oldBuData.copy(contentsList = newBuList), buModel.index)
            }
        }){
            // show error
            homeDataModel.list.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = listOf())
                val newList = homeDataModel.list.copy().toMutableList()
                newList[buModel.index] = oldBuData.copy(contentsList = newBuList)
                updateWidget(oldBuData.copy(contentsList = newBuList),buModel.index)
            }
        }
    }

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

    fun getRechargeRecommendation() {
        if(getRechargeRecommendationJob?.isActive == true) return
        findWidget<ReminderWidgetModel>(
                actionOnFound = { reminderWidgetModel, i ->
                    getRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
                        getRechargeRecommendationUseCase.get().setParams()
                        val data = getRechargeRecommendationUseCase.get().executeOnBackground()
                        val newFindRechargeRecommendationViewModel = reminderWidgetModel.copy(
                                data = mapperRechargetoReminder(data),
                                source = ReminderEnum.RECHARGE
                        )
                        updateWidget(newFindRechargeRecommendationViewModel, i)
                    }) {
                        removeRechargeRecommendation()
                    }
                },
                predicate = {
                    it?.source == ReminderEnum.RECHARGE
                }
        )
    }

    fun getSalamWidget(){
        if(getSalamWidgetJob?.isActive == true) return
        findWidget<ReminderWidgetModel>(
                actionOnFound = { reminderWidgetModel, i ->
                    getSalamWidgetJob = launchCatchError(coroutineContext,  block = {
                        val data = getSalamWidgetUseCase.get().executeOnBackground()
                        val newFindRechargeRecommendationViewModel = reminderWidgetModel.copy(
                                data = mapperSalamtoReminder(data),
                                source = ReminderEnum.SALAM
                        )
                        updateWidget(newFindRechargeRecommendationViewModel, i)
                    }){
                        removeSalamWidget()
                    }
                },
                predicate = {
                    it?.source == ReminderEnum.SALAM
                }
        )
    }

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        removeRechargeRecommendation()
        if(declineRechargeRecommendationJob?.isActive == true) return
        declineRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            declineRechargeRecommendationUseCase.get().setParams(requestParams)
            declineRechargeRecommendationUseCase.get().executeOnBackground()
        }){}
    }

    fun declineSalamItem(requestParams: Map<String, Int>){
        removeSalamWidget()
        if (declineSalamWidgetJob?.isActive==true) return
        declineSalamWidgetJob = launchCatchError(coroutineContext, block = {
            declineSalamWidgetUseCase.get().setParams(requestParams)
            declineSalamWidgetUseCase.get().executeOnBackground()
        }){}
    }

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
        if (getTabRecommendationJob?.isActive == true) return
        addWidget(HomeLoadingMoreModel())
        getTabRecommendationJob = launchCatchError(coroutineContext, block={
            getRecommendationTabUseCase.get().setParams(getHomeLocationDataParam())
            val homeRecommendationTabs = getRecommendationTabUseCase.get().executeOnBackground()
            val findRetryModel = homeDataModel.list.withIndex().find { data -> data.value is HomeRetryModel
            }
            val findRecommendationModel = homeDataModel.list.find {
                data -> data is HomeRecommendationFeedDataModel
            }
            val findLoadingModel = homeDataModel.list.withIndex().find {
                data -> data.value is HomeLoadingMoreModel
            }

            if (findRecommendationModel != null) return@launchCatchError

            val homeRecommendationFeedViewModel = HomeRecommendationFeedDataModel(homeDataModel.homeChooseAddressData)
            homeRecommendationFeedViewModel.recommendationTabDataModel = homeRecommendationTabs
            homeRecommendationFeedViewModel.isNewData = true

            findLoadingModel?.value?.let { updateWidget(homeRecommendationFeedViewModel, findLoadingModel.index?:-1, it) }
            findRetryModel?.value?.let { updateWidget(homeRecommendationFeedViewModel, findLoadingModel?.index?:-1, it) }

        }){
            val findRetryModel = homeDataModel.list.withIndex().find { data -> data.value is HomeRetryModel
            }
            val findLoadingModel = homeDataModel.list.withIndex().find { data -> data.value is HomeLoadingMoreModel
            }
            addWidget(HomeRetryModel())
            deleteWidget(findLoadingModel?.value, findLoadingModel?.index ?: -1)
            deleteWidget(findRetryModel?.value, findRetryModel?.index ?: -1)
        }
    }

    @VisibleForTesting
    fun getLoadPlayBannerFromNetwork(playBanner: PlayCardDataModel){
        if(getPlayWidgetJob?.isActive == true) return
        getPlayWidgetJob = launchCatchError(coroutineContext, block = {
            getPlayCardHomeUseCase.get().setParams()
            val data = getPlayCardHomeUseCase.get().executeOnBackground()
            if (data.playChannels.isEmpty() || data.playChannels.first().coverUrl.isEmpty()) {
                clearPlayBanner()
            } else{
                _requestImageTestLiveData.postValue(Event(playBanner.copy(playCardHome = data.playChannels.first())))
            }
        }){
            clearPlayBanner()
        }
    }

    fun getPopularKeyword() {
        if(getPopularKeywordJob?.isActive == true) return
        findWidget<PopularKeywordListDataModel> { popularKeywordListDataModel, index ->
            getPopularKeywordJob = launchCatchError(coroutineContext, {
                popularKeywordUseCase.get().setParams(page = popularKeywordRefreshCount)
                val results = popularKeywordUseCase.get().executeOnBackground()
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

    fun getSearchHint(isFirstInstall: Boolean) {
        if(getSearchHintJob?.isActive == true) return
        getSearchHintJob = launchCatchError(coroutineContext, block={
            getKeywordSearchUseCase.get().params = getKeywordSearchUseCase.get().createParams(isFirstInstall, userSession.get().deviceId, userSession.get().userId)
            val data = getKeywordSearchUseCase.get().executeOnBackground()
            _searchHint.postValue(data.searchData)
        }){}
    }

    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int){
        launchCatchError(coroutineContext, block = {
            val requestParams = RequestParams()
            val quantity = if(grid.minOrder < 1) "1" else grid.minOrder.toString()
            requestParams.putObject(AddToCartOccUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartOccRequestParams(
                    productId = grid.id,
                    quantity = quantity,
                    shopId = grid.shopId,
                    warehouseId = grid.warehouseId,
                    productName = grid.name,
                    price = grid.price,
                    userId = getUserId()
            ))
            val addToCartResult = getAtcUseCase.get().createObservable(requestParams).toBlocking().first()
            if(addToCartResult.status == STATUS_OK) {
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

    fun getTokocashPendingBalance(){
        if(getPendingCashBalanceJob?.isActive != true){
            getPendingCashBalanceJob = launchCatchError(coroutineContext, block={
                val data = getPendingCashbackUseCase.get().executeOnBackground()
                val cashBackData = CashBackData(data.amount, data.amountText)
                updateHeaderViewModel(
                        cashBackData = cashBackData,
                        isPendingTokocashChecked = true,
                        isWalletDataError = false
                )
            }){
                // do nothing if fail
            }
        }
    }

    fun injectCouponTimeBased() {
        if(injectCouponTimeBasedJob?.isActive == true) return
        injectCouponTimeBasedJob = launchCatchError(coroutineContext, {
            _injectCouponTimeBasedResult.value = Result.success(injectCouponTimeBasedUseCase.get().executeOnBackground().data)
        }){
            _injectCouponTimeBasedResult.postValue(Result.error(error = it, messageString = ""))
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

    fun getUserName() = userSession.get().name ?: ""

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

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        updateCarouselPlayWidget {
            it.copy(widgetUiModel = playWidgetTools.get().updateTotalView(it.widgetUiModel, channelId, totalView))
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!userSession.get().isLoggedIn) _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        else updatePlayWidgetToggleReminder(channelId, reminderType)
    }

    fun setHomeNotif(notifCount: Int, messageCount: Int, cartCount: Int) {
        homeNotifModel = HomeNotifModel(
                notifCount = notifCount,
                messageCount = messageCount,
                cartCount = cartCount
        )
        _homeNotifLiveData.value = homeNotifModel
    }

    fun onDynamicChannelRetryClicked() {
        launch(coroutineContext) {
            refreshHomeData()
        }
    }

    fun setRollanceNavigationType(type: String) {
        navRollanceType = type
    }

    fun setNewBalanceWidget(useNewBalanceWidget: Boolean) {
        this.useNewBalanceWidget = useNewBalanceWidget
    }

    fun updateChooseAddressData(homeChooseAddressData: HomeChooseAddressData) {
        this.homeDataModel.setAndEvaluateHomeChooseAddressData(homeChooseAddressData)
        refresh(isFirstInstall = false, forceRefresh = true)
    }

    fun getAddressData(): HomeChooseAddressData {
        return homeDataModel.homeChooseAddressData
    }

    fun removeChooseAddressWidget() {
        val homeHeaderOvoDataModel = homeDataModel.list.withIndex().find {
            it.value is HomeHeaderOvoDataModel
        }
        (homeHeaderOvoDataModel?.value as? HomeHeaderOvoDataModel)?.needToShowChooseAddress = false
        homeHeaderOvoDataModel?.let {
            updateWidget(homeHeaderOvoDataModel.value, homeHeaderOvoDataModel.index)
        }
    }

    fun isAddressDataEmpty(): Boolean {
        return getAddressData().lat.isEmpty() &&
                getAddressData().long.isEmpty() &&
                getAddressData().districId.isEmpty() &&
                getAddressData().cityId.isEmpty() &&
                getAddressData().addressId.isEmpty() &&
                getAddressData().postCode.isEmpty()

    }

    private inline fun <reified T> findWidgetList(actionOnFound: (List<T>) -> Unit) {
        homeDataModel.list.withIndex().filterIsInstance<T>().let {
            actionOnFound.invoke(it)
        }
    }

    private inline fun <reified T> widgetIsAvailable(predicate: (T) -> Boolean = {true}): Boolean {
        homeDataModel.list.filterIsInstance<T>().let {
            return it.find { predicate.invoke(it) } != null
        }
    }

    private inline fun <reified T> findWidget(predicate: (T?) -> Boolean = {true}, actionOnFound: (T, Int) -> Unit) {
        homeDataModel.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
            it?.let {
                if (it.value is T) {
                    actionOnFound.invoke(it.value as T, it. index)
                }
            }
        }
    }

    private fun addWidget(visitable: Visitable<*>, position: Int? = null) {
        homeDataModel.addWidgetModel(visitable, position) { _homeLiveData.postValue(homeDataModel) }
    }

    private fun updateWidget(visitable: Visitable<*>, position: Int, visitableToChange: Visitable<*>? = null) {
        homeDataModel.updateWidgetModel(visitable, visitableToChange, position) { _homeLiveData.postValue(homeDataModel) }
    }

    private fun deleteWidget(visitable: Visitable<*>?, position: Int) {
        homeDataModel.deleteWidgetModel(visitable, position) { _homeLiveData.postValue(homeDataModel) }
    }

    private fun updateHomeData(homeNewDataModel: HomeDataModel) {
        logChannelUpdate("Update channel: (Update all home data) data: ${homeDataModel.list.map { it.javaClass.simpleName }}")
        this.homeDataModel = homeNewDataModel
        if (!homeNewDataModel.isProcessingDynamicChannle) {
            homeNewDataModel.evaluateHomeFlagData(
                    onNewBalanceWidgetSelected = { setNewBalanceWidget(it) },
                    onNeedToGetBalanceData = { getBalanceWidgetData() }
            )
            homeNewDataModel.copyStaticWidgetDataFrom(homeDataModel)
            homeNewDataModel.evaluateRecommendationSection(
                    onNeedTabLoad = { getFeedTabData() }
            )
        }
        _homeLiveData.postValue(homeDataModel)
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }

    private fun balanceRemoteConfigCondition(
            isNewBalanceWidget: () -> Unit,
            isOldBalanceWidget: () -> Unit
    ) {
        if (useNewBalanceWidget) {
            isNewBalanceWidget.invoke()
        } else {
            isOldBalanceWidget.invoke()
        }
    }

    private fun getHomeLocationDataParam() : String {
        return if (!isAddressDataEmpty()) {
            buildLocationParams(
                    getAddressData().lat,
                    getAddressData().long,
                    getAddressData().addressId,
                    getAddressData().cityId,
                    getAddressData().districId,
                    getAddressData().postCode)
        } else ""
    }

    private fun buildLocationParams(
            lat: String = "",
            long: String = "",
            addressId: String = "",
            cityId: String = "",
            districtId: String = "",
            postCode: String = ""): String {
        return "user_lat=" + lat +
                "&user_long=" + long +
                "&user_addressId=" + addressId +
                "&user_cityId=" + cityId +
                "&user_districtId=" + districtId +
                "&user_postCode=" + postCode
    }

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

    private fun mapToHomeHeaderWalletAction(walletBalanceModel: WalletBalanceModel): HomeHeaderWalletAction? {
        return HomeHeaderWalletAction(
                isLinked = walletBalanceModel.link,
                balance = walletBalanceModel.balance,
                labelTitle = walletBalanceModel.titleText,
                appLinkBalance = walletBalanceModel.applinks,
                labelActionButton = walletBalanceModel.actionBalanceModel?.labelAction ?: "",
                isVisibleActionButton = (walletBalanceModel.actionBalanceModel?.visibility == "1"),
                appLinkActionButton = walletBalanceModel.actionBalanceModel?.applinks ?: "",
                abTags = walletBalanceModel.abTags ?: listOf(),
                pointBalance = walletBalanceModel.pointBalance,
                rawPointBalance = walletBalanceModel.rawPointBalance,
                cashBalance = walletBalanceModel.cashBalance,
                rawCashBalance = walletBalanceModel.rawCashBalance,
                walletType = walletBalanceModel.walletType,
                isShowAnnouncement = walletBalanceModel.isShowAnnouncement,
                isShowTopup = walletBalanceModel.isShowTopup,
                topupUrl = walletBalanceModel.topupUrl,
                topupLimit = walletBalanceModel.topupLimit
        )
    }

    private fun removeDynamicChannelLoadingModel() {
        findWidget<DynamicChannelLoadingModel> { _, index ->
            updateWidget(DynamicChannelRetryModel(false),index)
        }
        findWidget<DynamicChannelRetryModel> { retryWidget, index ->
            updateWidget(retryWidget, index)
            addWidget(DynamicChannelRetryModel(false), homeDataModel.list.size)
        }
    }

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
                    _playWidgetReminderObservable.postValue(Result.error(error = Throwable(), messageString = ""))
                }
            }
        }) { throwable ->
            updateCarouselPlayWidget {
                it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType.switch()))
            }
            _playWidgetReminderObservable.postValue(Result.error(error = throwable, messageString = ""))
        }
    }

    private fun updateCarouselPlayWidget(onUpdate: (oldVal: CarouselPlayWidgetDataModel) -> CarouselPlayWidgetDataModel) {
        val dataModel = homeDataModel.list.find { it is CarouselPlayWidgetDataModel } ?: return
        if (dataModel !is CarouselPlayWidgetDataModel) return

        val index = homeDataModel.list.indexOfFirst { it is CarouselPlayWidgetDataModel }
        updateWidget(onUpdate(dataModel), index)
    }

    private fun List<TokopointsDrawer>.getDrawerListByType(type: String) : TokopointsDrawer? {
        return this.find { it.type == type }
    }

    private fun getTokopoint(){
        if(getTokopointJob?.isActive == true) return
        getTokopointJob = if (navRollanceType.equals(AbTestPlatform.NAVIGATION_VARIANT_REVAMP)) {
            launchCatchError(coroutineContext, block = {
                val data = getHomeTokopointsListDataUseCase.get().executeOnBackground()
                updateHeaderViewModel(
                        tokopointsDrawer = data.tokopointsDrawerList.drawerList.getDrawerListByType("Rewards")
                                ?: data.tokopointsDrawerList.drawerList.getDrawerListByType("Coupon"),
                        tokopointsBBODrawer = data.tokopointsDrawerList.drawerList.getDrawerListByType("BBO"),
                        isTokoPointDataError = false
                )
            }){
                updateHeaderViewModel(
                        tokopointsDrawer = null,
                        isTokoPointDataError = true
                )
            }
        } else {
            launchCatchError(coroutineContext, block = {
                getHomeTokopointsDataUseCase.get().setParams("2.0.0")
                val data = getHomeTokopointsDataUseCase.get().executeOnBackground()
                updateHeaderViewModel(
                        tokopointsDrawer = data.tokopointsDrawer,
                        isTokoPointDataError = false
                )
            }) {
                updateHeaderViewModel(
                        tokopointsDrawer = null,
                        isTokoPointDataError = true
                )
            }
        }
    }

    private fun getTokocashBalance() {
        if(getTokocashJob?.isActive == true) return
        getTokocashJob = launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.get().executeOnBackground())
            updateHeaderViewModel(
                    homeHeaderWalletAction = homeHeaderWalletAction,
                    isWalletDataError = false
            )
            if (homeHeaderWalletAction?.isShowAnnouncement == true && homeHeaderWalletAction.appLinkActionButton.isNotEmpty()) {
                _popupIntroOvoLiveData.postValue(Event(homeHeaderWalletAction.appLinkActionButton))
            }
        }){
            updateHeaderViewModel(
                    homeHeaderWalletAction = null,
                    isWalletDataError = true
            )
        }
    }

    private fun getBalanceWidgetData() {
        if (homeDataModel.homeBalanceModel.balanceDrawerItemModels.isEmpty()) {
            newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setWalletBalanceState(state = STATE_LOADING))
            newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setTokopointBalanceState(state = STATE_LOADING))
        }

        launchCatchError(coroutineContext, block = {

            var walletContent: HomeHeaderWalletAction? = null
            var tokopointContent: TokopointsDrawerListHomeData? = null
            var pendingCashback: PendingCashback? = null

            try {
                walletContent = getWalletBalanceContent()
            } catch (e: MessageErrorException) {
                homeDataModel.homeBalanceModel.isTokopointsOrOvoFailed = true
                homeDataModel.homeBalanceModel.mapErrorWallet()
                newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setWalletBalanceState(state = STATE_ERROR))
            }

            try {
                tokopointContent = getTokopointBalanceContent()
            } catch (e: MessageErrorException) {
                homeDataModel.homeBalanceModel.isTokopointsOrOvoFailed = true
                homeDataModel.homeBalanceModel.mapErrorTokopoints()
                newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setTokopointBalanceState(state = STATE_ERROR))
            }

            walletContent?.let {
                if (!walletContent.isLinked) {
                    try {
                        pendingCashback = getPendingTokoCashContent()
                        pendingCashback?.let { pendingData ->
                            homeDataModel.homeBalanceModel.mapBalanceData(
                                    tokopointDrawerListHomeData = tokopointContent,
                                    homeHeaderWalletAction = walletContent.copy(cashBalance = pendingData.amountText),
                                    pendingCashBackData = PendingCashbackModel(
                                            pendingCashback = pendingData,
                                            labelActionButton = walletContent.labelActionButton,
                                            labelTitle = walletContent.labelTitle,
                                            walletType = walletContent.walletType
                                    )
                            )
                        }
                    } catch (e: MessageErrorException) {
                        homeDataModel.homeBalanceModel.isTokopointsOrOvoFailed = true
                        newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setWalletBalanceState(state = STATE_ERROR))
                    }
                } else {
                    homeDataModel.homeBalanceModel.mapBalanceData(homeHeaderWalletAction = walletContent)
                }
            }

            tokopointContent?.let {
                homeDataModel.homeBalanceModel.mapBalanceData(tokopointDrawerListHomeData = getTokopointBalanceContent())
            }

            newUpdateHeaderViewModel(homeDataModel.homeBalanceModel)
        }) {

        }
    }

    private fun getTokopointDrawerListData() {
        //set loading to wallet item
        newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setTokopointBalanceState(state = STATE_LOADING))

        launchCatchError(coroutineContext, block = {
            val tokopointsDrawerListHome = getHomeTokopointsListDataUseCase.get().executeOnBackground()
            homeDataModel.homeBalanceModel.mapBalanceData(tokopointDrawerListHomeData = tokopointsDrawerListHome)
            newUpdateHeaderViewModel(homeBalanceModel = homeDataModel.homeBalanceModel)
        }) {
            homeDataModel.homeBalanceModel.mapErrorTokopoints()
            homeDataModel.homeBalanceModel.setTokopointBalanceState(state = STATE_ERROR)
            newUpdateHeaderViewModel(homeBalanceModel = homeDataModel.homeBalanceModel)
        }
    }

    private fun getWalletBalanceData() {
        //set loading to wallet item
        newUpdateHeaderViewModel(homeDataModel.homeBalanceModel.copy().setWalletBalanceState(state = STATE_LOADING))

        launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.get().executeOnBackground())
            homeDataModel.homeBalanceModel.mapBalanceData(homeHeaderWalletAction = homeHeaderWalletAction)
            newUpdateHeaderViewModel(homeBalanceModel = homeDataModel.homeBalanceModel)
            if (homeHeaderWalletAction?.isShowAnnouncement == true && homeHeaderWalletAction.appLinkActionButton.isNotEmpty()) {
                _popupIntroOvoLiveData.postValue(Event(homeHeaderWalletAction.appLinkActionButton))
            }
        }){
            homeDataModel.homeBalanceModel.mapErrorWallet()
            homeDataModel.homeBalanceModel.setWalletBalanceState(state = STATE_ERROR)
            newUpdateHeaderViewModel(homeBalanceModel = homeDataModel.homeBalanceModel)
        }
    }

    private suspend fun getTokopointBalanceContent(): TokopointsDrawerListHomeData? {
        val tokopointsDrawerListHome = getHomeTokopointsListDataUseCase.get().executeOnBackground()
        return tokopointsDrawerListHome
    }

    private suspend fun getWalletBalanceContent(): HomeHeaderWalletAction? {
        return mapToHomeHeaderWalletAction(getWalletBalanceUseCase.get().executeOnBackground())
    }

    private suspend fun getPendingTokoCashContent(): PendingCashback {
        return getPendingCashbackUseCase.get().executeOnBackground()
    }

    private fun getDisplayTopAdsHeader(){
        findWidget<FeaturedShopDataModel> { featuredShopDataModel, index ->
            launchCatchError(coroutineContext, block={
                getDisplayHeadlineAds.get().createParams(featuredShopDataModel.channelModel.widgetParam)
                val data = getDisplayHeadlineAds.get().executeOnBackground()
                if(data.isEmpty()){
                    deleteWidget(featuredShopDataModel, index)
                } else {
                    updateWidget(featuredShopDataModel.copy(
                            channelModel = featuredShopDataModel.channelModel.copy(
                                    channelGrids = mappingTopAdsHeaderToChannelGrid(data)
                            )), index)
                }
            }){
                deleteWidget(featuredShopDataModel, index)
            }
        }
    }

    private fun mappingTopAdsHeaderToChannelGrid(data: List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>): List<ChannelGrid>{
        return data.map {
            ChannelGrid(
                    id = it.id,
                    applink = it.applink,
                    shop = ChannelShop(
                            id = it.headline.shop.id,
                            shopName = it.headline.shop.name,
                            shopProfileUrl = it.headline.shop.imageShop.cover,
                            shopLocation = it.headline.shop.location,
                            shopBadgeUrl = it.headline.badges.firstOrNull()?.imageUrl ?: "",
                            isGoldMerchant = it.headline.shop.goldShop,
                            isOfficialStore = it.headline.shop.shopIsOfficialStore
                    ),
                    countReviewFormat = it.headline.shop.products.firstOrNull()?.review ?: "",
                    rating = it.headline.shop.products.firstOrNull()?.rating ?: 0,
                    impression = it.headline.image.url,
                    productClickUrl = it.adClickUrl,
                    imageUrl = it.headline.shop.products.firstOrNull()?.imageProduct?.imageUrl ?: ""
            )
        }
    }

    private fun newUpdateHeaderViewModel(homeBalanceModel: HomeBalanceModel) {
        val homeHeaderOvoDataModel = (homeDataModel.list.find { visitable-> visitable is HomeHeaderOvoDataModel } as HomeHeaderOvoDataModel?)
        homeHeaderOvoDataModel?.let {
            val currentPosition = -1
            if(headerDataModel == null){
                homeDataModel.list.withIndex().find { (_, model) ->  model is HomeHeaderOvoDataModel }?.index ?: -1
                headerDataModel = homeHeaderOvoDataModel.headerDataModel
            }

            homeHeaderOvoDataModel.headerDataModel = HeaderDataModel(
                    homeBalanceModel = homeBalanceModel,
                    isUserLogin = userSession.get().isLoggedIn
            )
            this.homeDataModel.homeBalanceModel = homeBalanceModel
            updateWidget(homeHeaderOvoDataModel as Visitable<*>, currentPosition)
        }
    }

    private fun updateHeaderViewModel(tokopointsDrawer: TokopointsDrawer? = null,
                                      tokopointsBBODrawer: TokopointsDrawer? = null,
                                      homeHeaderWalletAction: HomeHeaderWalletAction? = null,
                                      cashBackData: CashBackData? = null,
                                      isPendingTokocashChecked: Boolean? = null,
                                      isWalletDataError: Boolean? = null,
                                      isTokoPointDataError: Boolean? = null) {
        val homeHeaderOvoDataModel = (homeDataModel.list.find { visitable-> visitable is HomeHeaderOvoDataModel } as HomeHeaderOvoDataModel?)
        homeHeaderOvoDataModel?.let {
            val currentPosition = -1
            if(headerDataModel == null){
                homeDataModel.list.withIndex().find { (_, model) ->  model is HomeHeaderOvoDataModel }?.index ?: -1
                headerDataModel = homeHeaderOvoDataModel.headerDataModel
            }

            headerDataModel?.let {
                tokopointsDrawer?.let {
                    headerDataModel = headerDataModel?.copy(tokopointsDrawerHomeData = it)
                }
                tokopointsBBODrawer?.let {
                    headerDataModel = headerDataModel?.copy(tokopointsDrawerBBOHomeData = it)
                }
                homeHeaderWalletAction?.let {
                    headerDataModel = headerDataModel?.copy(homeHeaderWalletActionData = it)
                }
                cashBackData?.let {
                    headerDataModel = headerDataModel?.copy(cashBackData = it)
                }
                isPendingTokocashChecked?.let {
                    headerDataModel = headerDataModel?.copy(isPendingTokocashChecked = it)
                }
                isWalletDataError?.let {
                    headerDataModel = headerDataModel?.copy(isWalletDataError = it)
                }
                isTokoPointDataError?.let {
                    headerDataModel = headerDataModel?.copy(isTokoPointDataError = it)
                }
                headerDataModel = headerDataModel?.copy(
                        isUserLogin = userSession.get().isLoggedIn,
                        homeBalanceModel = homeDataModel.homeBalanceModel
                )
                homeHeaderOvoDataModel.headerDataModel = headerDataModel
                updateWidget(homeHeaderOvoDataModel as Visitable<*>, currentPosition)
            }
        }
    }

    private fun getReviewData() {
        if(getSuggestedReviewJob?.isActive == true) return
        if (userSession.get().isLoggedIn) {
            getSuggestedReviewJob = launchCatchError(coroutineContext, block = {
                findWidget<ReviewDataModel> { reviewWidget, index ->
                    val data = getHomeReviewSuggestedUseCase.get().executeOnBackground()
                    val newFindReviewViewModel = reviewWidget.copy(suggestedProductReview = data)
                    updateWidget(newFindReviewViewModel, index)
                }
            }) {
                onRemoveSuggestedReview()
            }
        } else {
            onRemoveSuggestedReview()
        }
    }

    private fun removeRechargeRecommendation() {
        val findRechargeRecommendationViewModel =
                homeDataModel.list.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.RECHARGE)}
                        ?: return
        if (findRechargeRecommendationViewModel is ReminderWidgetModel && findRechargeRecommendationViewModel.source==ReminderEnum.RECHARGE) {
            deleteWidget(findRechargeRecommendationViewModel, -1)
        }
    }

    private fun removeSalamWidget() {
        val findSalamWidgetModel =
                homeDataModel.list.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.SALAM)}
                        ?: return
        if (findSalamWidgetModel is ReminderWidgetModel && findSalamWidgetModel.source==ReminderEnum.SALAM) {
            deleteWidget(findSalamWidgetModel, -1)
        }
    }

    private fun removeRechargeBUWidget() {
        findWidget<RechargeBUWidgetDataModel> { rechargeBuModel, index ->
            deleteWidget(rechargeBuModel, index)
        }
    }

    private fun initCacheData() {
        _isRequestNetworkLiveData.value = Event(true)

        launch {
            val homeCacheData = homeUseCase.get().getHomeCachedData()
            homeCacheData?.let { homeDataModel ->
                homeDataModel.evaluateChooseAddressData()
                _homeLiveData.postValue(homeDataModel)

                if (homeDataModel.list.size > 1) {
                    _isRequestNetworkLiveData.postValue(Event(false))
                    takeTicker = false
                }
                if (homeDataModel.list.size == 1) {
                    val initialHomeDataModel = HomeDataModel(list = listOf(
                            HomeHeaderOvoDataModel(),
                            HomeInitialShimmerDataModel()
                    ))
                    updateHomeData(initialHomeDataModel)
                }
            }
            initFlow()
        }
    }

    private fun initFlow() {
        launchCatchError(coroutineContext, block = {
            homeFlowData.collect { homeNewDataModel ->
                if (homeNewDataModel?.isCache == false) {
                    _isRequestNetworkLiveData.postValue(Event(false))
                    onRefreshState = false
                    if (homeNewDataModel.list.isEmpty()) {
                        ServerLogger.log(Priority.P2, "HOME_STATUS",
                                mapOf("type" to "revamp_empty_update",
                                        "reason" to "Visitables is empty",
                                        "isProcessingDynamicChannel" to homeNewDataModel.isProcessingDynamicChannle.toString(),
                                        "isProcessingAtf" to homeNewDataModel.isProcessingAtf.toString(),
                                        "isFirstPage" to homeNewDataModel.isFirstPage.toString(),
                                        "isCache" to homeNewDataModel.isCache.toString()
                                ))
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
                    if (homeDataModel?.list?.size?:0 > 1) {
                        _isRequestNetworkLiveData.postValue(Event(false))
                        takeTicker = false
                    }
                    if (homeDataModel?.list?.size?:0 > 0) {
                        homeDataModel?.let { updateHomeData(it) }
                    }
                    refreshHomeData()
                }
            }
        }) {
            _updateNetworkLiveData.postValue(Result.error(error = it, data = null, messageString = ""))
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
            ServerLogger.log(Priority.P2, "HOME_STATUS",
                    mapOf("type" to "revamp_error_init_flow",
                            "reason" to (it.message ?: "".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                            "data" to stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
                    ))
        }.invokeOnCompletion {
            _updateNetworkLiveData.postValue(Result.error(error = Throwable(), data = null, messageString = ""))
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
            ServerLogger.log(Priority.P2, "HOME_STATUS",
                    mapOf("type" to "revamp_cancelled_init_flow",
                            "reason" to (it?.message ?: "No error propagated").take(ConstantKey.HomeTimber.MAX_LIMIT),
                            "data" to stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
                    ))
            homeFlowDataCancelled = true
        }
    }

    private fun getExternalApi() {
        getPlayWidget()
        getHeaderData()
        getReviewData()
        getPlayBanner()
        getPopularKeyword()
        getDisplayTopAdsHeader()
        getRecommendationWidget()
        getTopAdsBannerData()
        getRechargeRecommendation()
        getSalamWidget()
    }

    private fun getTopAdsBannerData() {
        if(getTopAdsBannerDataJob?.isActive == true) return
        findWidget<HomeTopAdsBannerDataModel> { topAdsModel, index ->
            getTopAdsBannerDataJob = launchCatchError(coroutineContext, {
                val results = topAdsImageViewUseCase.get().getImageData(
                        topAdsImageViewUseCase.get().getQueryMap(
                                "",
                                "1",
                                "",
                                1,
                                3,
                                "")
                )
                if (results.isNotEmpty()) {
                    val newTopAdsModel = topAdsModel.copy(topAdsImageViewModel = results[0])
                    updateWidget(newTopAdsModel, index)
                } else {
                    deleteWidget(topAdsModel, index)
                }
            }){
                it.printStackTrace()
                deleteWidget(topAdsModel, index)
            }
        }
    }
}