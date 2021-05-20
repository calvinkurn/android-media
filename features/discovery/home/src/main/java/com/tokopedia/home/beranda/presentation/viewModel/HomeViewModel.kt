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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.beranda.common.BaseCoRoutineScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.isSalamWidgetAvailable
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperRechargetoReminder
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperSalamtoReminder
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DisplayHeadlineAdsEntity
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.util.*
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
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
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class HomeViewModel @Inject constructor(
        private val homeUseCase: Lazy<HomeUseCase>,
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
        private val sendGeolocationInfoUseCase: Lazy<SendGeolocationInfoUseCase>,
        private val injectCouponTimeBasedUseCase: Lazy<InjectCouponTimeBasedUseCase>,
        private val getRechargeRecommendationUseCase: Lazy<GetRechargeRecommendationUseCase>,
        private val declineRechargeRecommendationUseCase: Lazy<DeclineRechargeRecommendationUseCase>,
        private val getSalamWidgetUseCase: Lazy<GetSalamWidgetUseCase>,
        private val declineSalamWidgetUseCase: Lazy<DeclineSalamWIdgetUseCase>,
        private val getRechargeBUWidgetUseCase: Lazy<GetRechargeBUWidgetUseCase>,
        private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
        private val bestSellerMapper: Lazy<BestSellerMapper>,
        private val homeDispatcher: Lazy<CoroutineDispatchers>,
        private val homeProcessor: Lazy<HomeCommandProcessor>,
        private val playWidgetTools: Lazy<PlayWidgetTools>
) : BaseCoRoutineScope(homeDispatcher.get().io), ResultCommandProcessor {

    companion object {
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITY = "quantity"
        const val POSITION = "position"
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        private val REQUEST_DELAY_SEND_GEOLOCATION = TimeUnit.HOURS.toMillis(1) // 1 hour
    }

    private var navRollanceType: String = ""
    private var popularKeywordRefreshCount = 1
    var currentTopAdsBannerToken: String = ""
    private val homeFlowData: Flow<HomeDataModel?> = homeUseCase.get().getHomeData().flowOn(homeDispatcher.get().io)

// ============================================================================================
// ================================ Live data UI Controller ===================================
// ============================================================================================

    val homeLiveData: LiveData<HomeDataModel>
        get() = _homeLiveData
    private val _homeLiveData: MutableLiveData<HomeDataModel> = MutableLiveData()
    private var homeVisitableListData: MutableList<Visitable<*>> = mutableListOf()

    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    private val _searchHint: MutableLiveData<SearchPlaceholder> = MutableLiveData()

    val injectCouponTimeBasedResult: LiveData<Result<InjectCouponTimeBased>>
        get() = _injectCouponTimeBasedResult
    private val _injectCouponTimeBasedResult: MutableLiveData<Result<InjectCouponTimeBased>> = MutableLiveData()

    val playWidgetReminderEvent: LiveData<Pair<String, PlayWidgetReminderType>>
        get() = _playWidgetReminderEvent
    private val _playWidgetReminderEvent = MutableLiveData<Pair<String, PlayWidgetReminderType>>()

    val playWidgetReminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _playWidgetReminderObservable
    private val _playWidgetReminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()

// ============================================================================================
// ==================================== Helper Live Data ======================================
// ================================= PLEASE SORT BY NAME A-Z ==================================
// ============================================================================================

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

    val errorEventLiveData: LiveData<Event<String>>
        get() = _errorEventLiveData
    private val _errorEventLiveData = MutableLiveData<Event<String>>()

    val isViewModelInitialized: LiveData<Event<Boolean>>
        get() = _isViewModelInitialized
    private val _isViewModelInitialized = MutableLiveData<Event<Boolean>>(null)

    val isRequestNetworkLiveData: LiveData<Event<Boolean>>
        get() = _isRequestNetworkLiveData
    private val _isRequestNetworkLiveData = MutableLiveData<Event<Boolean>>(null)

    val isStartToRenderDynamicChannel: LiveData<Event<Boolean>>
        get() = _isStartToRenderDynamicChannel
    private val _isStartToRenderDynamicChannel = MutableLiveData<Event<Boolean>>(Event(false))

    private val _salamWidgetLiveData = MutableLiveData<Event<SalamWidget>>()
    val salamWidgetLiveData: LiveData<Event<SalamWidget>> get() = _salamWidgetLiveData

    private val _rechargeRecommendationLiveData = MutableLiveData<Event<RechargeRecommendation>>()
    val rechargeRecommendationLiveData: LiveData<Event<RechargeRecommendation>> get() = _rechargeRecommendationLiveData

    private val _rechargeBUWidgetLiveData = MutableLiveData<Event<RechargePerso>>()
    val rechargeBUWidgetLiveData: LiveData<Event<RechargePerso>> get() = _rechargeBUWidgetLiveData

    private val _isNeedRefresh = MutableLiveData<Event<Boolean>>()
    val isNeedRefresh: LiveData<Event<Boolean>> get() = _isNeedRefresh

// ============================================================================================
// ==================================== Helper Local Job ======================================
// ================================= PLEASE SORT BY NAME A-Z ==================================
// ============================================================================================

    private var getHomeDataJob: Job? = null
    private var getSearchHintJob: Job? = null
    private var getPlayWidgetJob: Job? = null
    private var getTokopointJob: Job? = null
    private var getTokocashJob: Job? = null
    private var getSuggestedReviewJob: Job? = null
    private var getPendingCashBalanceJob: Job? = null
    private var dismissReviewJob: Job? = null
    private var getPopularKeywordJob: Job? = null
    private var buWidgetJob: Job? = null
    private var getRechargeRecommendationJob: Job? = null
    private var declineRechargeRecommendationJob: Job? = null
    private var getSalamWidgetJob: Job? = null
    private var declineSalamWidgetJob: Job? = null
    private var getRechargeBUWidgetJob: Job? = null
    private var injectCouponTimeBasedJob: Job? = null
    private var getTopAdsBannerDataJob: Job? = null
    private var getTabRecommendationJob: Job? = null

// ============================================================================================
// ===================================== LOCAL VARIABLE =======================================
// ================================= PLEASE SORT BY NAME A-Z ==================================
// ============================================================================================

    private var fetchFirstData = false
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var hasGeoLocationPermission = false
    private var isNeedShowGeoLocation = false
    private var headerDataModel: HeaderDataModel? = null

    private var homeFlowDataCancelled = false

    private val homeRateLimit = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)

    private var onRefreshState = true

    init {
        _isViewModelInitialized.value = Event(true)
        initFlow()
    }

    fun refresh(isFirstInstall: Boolean) {
        val needSendGeolocationRequest = lastRequestTimeHomeData + REQUEST_DELAY_SEND_GEOLOCATION < System.currentTimeMillis()
        if (!fetchFirstData && homeRateLimit.shouldFetch(HOME_LIMITER_KEY)) {
            refreshHomeData()
            _isNeedRefresh.value = Event(true)
        }
        if (needSendGeolocationRequest && hasGeoLocationPermission) {
            _sendLocationLiveData.postValue(Event(needSendGeolocationRequest))
        }
        getTokocashBalance()
        getTokopoint()
        getSearchHint(isFirstInstall)
    }

    fun getRecommendationWidget() {
        val data = homeVisitableListData
        data.withIndex().filter { it.value is BestSellerDataModel }.forEach {
            val bestSellerDataModel = it.value as BestSellerDataModel
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
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(
                            dataModel.copy(
                                    id = bestSellerDataModel.id,
                                    pageName = bestSellerDataModel.pageName,
                                    widgetParam = bestSellerDataModel.widgetParam
                            ),
                            it.index,
                            this@HomeViewModel
                    ))
                } else {
                    homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(bestSellerDataModel, it.index, this@HomeViewModel))
                }
            }) {
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(bestSellerDataModel, -1, this@HomeViewModel))
            }

        }
    }

    fun getRecommendationWidget(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel) {
        val data = _homeLiveData.value?.list?.toMutableList()
        data?.withIndex()?.find { it.value is BestSellerDataModel && (it.value as BestSellerDataModel).id == bestSellerDataModel.id }?.let {
            launchCatchError(coroutineContext, block = {
                val recomData = getRecommendationUseCase.get().getData(
                        GetRecommendationRequestParam(
                                pageName = bestSellerDataModel.pageName,
                                queryParam = if (filterChip.isActivated) filterChip.value else ""
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
                            filterChip = newBestSellerDataModel.filterChip.map {
                                it.copy(isActivated = filterChip.name == it.name
                                        && filterChip.isActivated)
                            }
                    )
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(
                            newModel,
                            it.index,
                            this@HomeViewModel
                    ))
                } else {
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(
                            bestSellerDataModel.copy(
                                    filterChip = bestSellerDataModel.filterChip.map {
                                        it.copy(isActivated = filterChip.name == it.name
                                                && !filterChip.isActivated)
                                    }
                            ),
                            it.index,
                            this@HomeViewModel
                    ))
                }
            }) { _ ->
                homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(
                        bestSellerDataModel.copy(
                                filterChip = bestSellerDataModel.filterChip.map {
                                    it.copy(isActivated = filterChip.name == it.name
                                            && !filterChip.isActivated)
                                }
                        ),
                        it.index,
                        this@HomeViewModel
                ))
            }
        }
    }

    fun sendGeolocationData() {
        sendGeolocationInfoUseCase.get().createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<String>>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(s: Response<String>?) {
                        lastRequestTimeSendGeolocation = System.currentTimeMillis()
                    }
                })
    }

    override fun onCleared() {
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
        super.onCleared()
    }

    fun onCloseChannel() {
        homeProcessor.get().onClear()
    }

    fun getHeaderData() {
        if (!userSession.get().isLoggedIn) return
        getTokocashBalance()
        getTokopoint()
    }

    fun updateBannerTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return
        val homeList = homeVisitableListData
        val playCard = homeList.withIndex().find { (_, visitable) ->
            (visitable is PlayCardDataModel && visitable.playCardHome?.channelId == channelId)
        } ?: return
        if (playCard.value is PlayCardDataModel && (playCard.value as PlayCardDataModel).playCardHome != null) {
            val newPlayCard = (playCard.value as PlayCardDataModel).copy(playCardHome = (playCard.value as PlayCardDataModel).playCardHome?.copy(totalView = totalView))
            homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newPlayCard, playCard.index, this))
        }
    }

    private fun updateHeaderViewModel(tokopointsDrawer: TokopointsDrawer? = null,
                                      tokopointsBBODrawer: TokopointsDrawer? = null,
                                      homeHeaderWalletAction: HomeHeaderWalletAction? = null,
                                      cashBackData: CashBackData? = null,
                                      isPendingTokocashChecked: Boolean? = null,
                                      isWalletDataError: Boolean? = null,
                                      isTokoPointDataError: Boolean? = null) {
        if (headerDataModel == null) {
            headerDataModel = (homeVisitableListData.find { visitable -> visitable is HeaderDataModel } as HeaderDataModel?)
        }

        val currentPosition = homeVisitableListData.withIndex().find { (_, model) -> model is HeaderDataModel }?.index
                ?: -1

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
            headerDataModel = headerDataModel?.copy(isUserLogin = userSession.get().isLoggedIn)
            homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(headerDataModel!!, currentPosition, this))
        }

    }

    private fun getReviewData() {
        if (isNeedShowGeoLocation) {
            onRemoveSuggestedReview()
        } else {
            getSuggestedReview()
        }
    }

    private fun getSuggestedReview() {
        if (getSuggestedReviewJob?.isActive == true) return
        if (!isNeedShowGeoLocation && userSession.get().isLoggedIn) {
            getSuggestedReviewJob = launchCatchError(coroutineContext, block = {
                val data = getHomeReviewSuggestedUseCase.get().executeOnBackground()
                insertSuggestedReview(data)
            }) {
                onRemoveSuggestedReview()
            }
        }
    }

    private fun evaluateAvailableComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let {
            var newHomeViewModel = homeDataModel
            newHomeViewModel = evaluateGeolocationComponent(newHomeViewModel)
            if (isNeedShowGeoLocation) newHomeViewModel = onRemoveSuggestedReview(it)
            newHomeViewModel = evaluatePlayWidget(newHomeViewModel)
            newHomeViewModel = evaluateBuWidgetData(newHomeViewModel)
            newHomeViewModel = evaluateRecommendationSection(newHomeViewModel)
            return newHomeViewModel
        }
        return homeDataModel
    }

    private fun evaluateGeolocationComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let {
            if (!isNeedShowGeoLocation) {
                val currentList = homeDataModel.list.toMutableList()
                currentList.let {
                    val mutableIterator = currentList.iterator()
                    for (e in mutableIterator) {
                        if (e is GeoLocationPromptDataModel) {
                            mutableIterator.remove()
                            break
                        }
                    }
                    return homeDataModel.copy(list = it)
                }
            }
        }
        return homeDataModel
    }

    fun getPlayBanner(position: Int) {
        val playBanner =
                if (position < homeVisitableListData.size
                        && homeVisitableListData.get(position) is PlayCardDataModel)
                    homeVisitableListData.getOrNull(position) as PlayCardDataModel
                else homeVisitableListData.find { it is PlayCardDataModel }
        playBanner?.let {
            getLoadPlayBannerFromNetwork(playBanner as PlayCardDataModel)
        }
    }

    // Logic detect play banner should load data from API
    private fun getPlayBanner() {
        // Check the current index is play card view model
        val playBanner = homeVisitableListData.find { it is PlayCardDataModel }
        if (playBanner != null && playBanner is PlayCardDataModel) {
            getLoadPlayBannerFromNetwork(playBanner)
        }
    }

    // If the image is valid it will be set play banner to UI
    fun setPlayBanner(playCardDataModel: PlayCardDataModel) {
        val newList = mutableListOf<Visitable<*>>()
        newList.addAll(homeVisitableListData)
        homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(playCardDataModel, -1, this))
    }

    // play widget it will be removed when load image is failed (deal from PO)
    // because don't let the banner blank
    fun clearPlayBanner() {
        homeVisitableListData.withIndex().find { (_, visitable) -> visitable is PlayCardDataModel }?.let {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(it.value, it.index, this))
        }
    }

    fun setNeedToShowGeolocationComponent(needToShowGeolocation: Boolean) {
        this.isNeedShowGeoLocation = needToShowGeolocation
    }

    fun setGeolocationPermission(hasGeolocationPermission: Boolean) {
        this.hasGeoLocationPermission = hasGeolocationPermission
    }

    fun hasGeolocationPermission() = hasGeoLocationPermission

// =================================================================================
// ================================ View Controller ================================
// =================================================================================

    private fun insertSuggestedReview(suggestedProductReview: SuggestedProductReview) {
        val findReviewViewModel = homeVisitableListData.find { visitable -> visitable is ReviewDataModel }
        val indexOfReviewViewModel = homeVisitableListData.indexOf(findReviewViewModel)
        if (indexOfReviewViewModel != -1 && findReviewViewModel is ReviewDataModel) {
            val newFindReviewViewModel = findReviewViewModel.copy(
                    suggestedProductReview = suggestedProductReview
            )
            homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newFindReviewViewModel, indexOfReviewViewModel, this))
        }
    }

    private fun onRemoveSuggestedReview(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { it ->
            val findReviewViewModel =
                    it.list.find { visitable -> visitable is ReviewDataModel }
            val currentList = it.list.toMutableList()
            currentList.let { list ->
                if (findReviewViewModel is ReviewDataModel) {
                    val mutableIterator = list.iterator()
                    for (e in mutableIterator) {
                        if (e is ReviewDataModel) {
                            mutableIterator.remove()
                            break
                        }
                    }
                    return it.copy(
                            list = list
                    )
                }
            }
        }
        return homeDataModel
    }

    fun onRemoveSuggestedReview() {
        val findReviewViewModel =
                homeVisitableListData.find { visitable -> visitable is ReviewDataModel }
                        ?: return
        if (findReviewViewModel is ReviewDataModel) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(findReviewViewModel, -1, this))
        }
    }

    fun onCloseBuyAgain(channelId: String, position: Int) {
        val dynamicChannelDataModel = homeVisitableListData.find { visitable ->
            visitable is DynamicChannelDataModel && visitable.channel?.id == channelId
        }
        val recommendationListHomeComponentModel = homeVisitableListData.find { visitable ->
            visitable is RecommendationListCarouselDataModel && visitable.channelModel.id == channelId
        }

        if (dynamicChannelDataModel != null && dynamicChannelDataModel is DynamicChannelDataModel) {
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if (closeChannel.success) {
                    homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(dynamicChannelDataModel, position, this@HomeViewModel))
                } else {
                    _errorEventLiveData.postValue(Event(""))
                }
            }) {
                _errorEventLiveData.postValue(Event(it.message ?: ""))
                Timber.tag(this::class.java.simpleName).e(it)
            }
        }

        if (recommendationListHomeComponentModel != null && recommendationListHomeComponentModel is RecommendationListCarouselDataModel) {
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if (closeChannel.success) {
                    homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(recommendationListHomeComponentModel, position, this@HomeViewModel))
                } else {
                    _errorEventLiveData.postValue(Event(""))
                }
            }) {
                it.printStackTrace()
                _errorEventLiveData.postValue(Event(it.message ?: ""))
            }
        }
    }

    fun removeViewHolderAtPosition(position: Int) {
        if (position != -1 && position < homeVisitableListData.size) {
            val detectViewHolder = homeVisitableListData[position]
            detectViewHolder.let {
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(it, position, this))
            }
        }
    }

    fun onCloseGeolocation() {
        val detectGeolocation = homeVisitableListData.find { visitable -> visitable is GeoLocationPromptDataModel }
        (detectGeolocation as? GeoLocationPromptDataModel)?.let {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(it, -1, this))
        }
        setNeedToShowGeolocationComponent(false)
    }

    fun onCloseTicker() {
        val detectTicker = homeVisitableListData.find { visitable -> visitable is TickerDataModel }
        (detectTicker as? TickerDataModel)?.let {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(it, -1, this))
        }
    }

    fun onRefreshTokoPoint() {
        if (!userSession.get().isLoggedIn) return
        updateHeaderViewModel(
                tokopointsDrawer = null,
                tokopointsBBODrawer = null,
                isTokoPointDataError = false
        )
        getTokopoint()
    }

    fun onRefreshTokoCash() {
        if (!userSession.get().isLoggedIn) return
        updateHeaderViewModel(
                homeHeaderWalletAction = null,
                isWalletDataError = false
        )
        getTokocashBalance()
    }

    fun insertRechargeRecommendation(data: RechargeRecommendation) {
        if (data.recommendations.isNotEmpty()) {
            homeVisitableListData.apply {
                val findRechargeRecommendationViewModel = find { visitable ->
                    visitable is ReminderWidgetModel
                            && (visitable.source == ReminderEnum.RECHARGE)
                }
                val indexOfRechargeRecommendationViewModel = indexOf(findRechargeRecommendationViewModel)
                if (indexOfRechargeRecommendationViewModel > -1 && findRechargeRecommendationViewModel is ReminderWidgetModel) {
                    val newFindRechargeRecommendationViewModel = findRechargeRecommendationViewModel.copy(
                            data = mapperRechargetoReminder(data),
                            source = ReminderEnum.RECHARGE
                    )
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newFindRechargeRecommendationViewModel, indexOfRechargeRecommendationViewModel, this@HomeViewModel))
                }
            }
        } else {
            removeRechargeRecommendation()
        }
    }

    fun insertSalamWidget(data: SalamWidget) {
        if (isSalamWidgetAvailable(data.salamWidget)) {
            homeVisitableListData.let {
                val findSalamWidgetModel = it.find { visitable ->
                    visitable is ReminderWidgetModel
                            && (visitable.source == ReminderEnum.SALAM)
                }
                val indexOfSalamWidgetModel = it.indexOf(findSalamWidgetModel)
                if (indexOfSalamWidgetModel > -1 && findSalamWidgetModel is ReminderWidgetModel) {
                    val newFindSalamWidgetModel = findSalamWidgetModel.copy(
                            data = mapperSalamtoReminder(data),
                            source = ReminderEnum.SALAM
                    )
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newFindSalamWidgetModel, indexOfSalamWidgetModel, this@HomeViewModel))
                }
            }
        } else {
            removeSalamWidget()
        }
    }

    fun insertRechargeBUWidget(data: RechargePerso) {
        if (data.items.isNotEmpty()) {
            homeVisitableListData.let {
                val findRechargeBUWidget = it.find { visitable -> visitable is RechargeBUWidgetDataModel }
                val indexOfRechargeBUWidget = it.indexOf(findRechargeBUWidget)
                if (indexOfRechargeBUWidget > -1 && findRechargeBUWidget is RechargeBUWidgetDataModel) {
                    val newFindRechargeBUWidget = findRechargeBUWidget.copy(data = data)
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newFindRechargeBUWidget, indexOfRechargeBUWidget, this@HomeViewModel))
                }
            }
        } else {
            removeRechargeBUWidget()
        }
    }

    private fun removeRechargeRecommendation() {
        val findRechargeRecommendationViewModel =
                homeVisitableListData.find { visitable -> visitable is ReminderWidgetModel && (visitable.source == ReminderEnum.RECHARGE) }
                        ?: return
        if (findRechargeRecommendationViewModel is ReminderWidgetModel && findRechargeRecommendationViewModel.source == ReminderEnum.RECHARGE) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(findRechargeRecommendationViewModel, -1, this))
        }
    }

    private fun removeSalamWidget() {
        val findSalamWidgetModel =
                homeVisitableListData.find { visitable -> visitable is ReminderWidgetModel && (visitable.source == ReminderEnum.SALAM) }
                        ?: return
        if (findSalamWidgetModel is ReminderWidgetModel && findSalamWidgetModel.source == ReminderEnum.SALAM) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(findSalamWidgetModel, -1, this))
        }
    }

    private fun removeRechargeBUWidget() {
        val findRechargeBUWidgetViewModel =
                homeVisitableListData.find { visitable -> visitable is RechargeBUWidgetDataModel }
                        ?: return
        if (findRechargeBUWidgetViewModel is RechargeBUWidgetDataModel) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(findRechargeBUWidgetViewModel, -1, this))
        }
    }

// =================================================================================
// ============================== Evaluate Controller ==============================
// =================================================================================

    private fun evaluatePlayWidget(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            // find the old data from current list
            val playWidget = homeVisitableListData.find { visitable -> visitable is PlayCardDataModel }
            if (playWidget != null) {
                // Find the new play widget is still available or not
                val list = homeViewModel.list.toMutableList()
                val playIndex = list.indexOfFirst { visitable -> visitable is PlayCardDataModel }

                // if on new home available the data, it will be load new data
                if (playIndex != -1) {
                    list[playIndex] = playWidget
                    return homeViewModel.copy(list = list)
                }
            }
        }

        return homeDataModel
    }

    private fun evaluateBuWidgetData(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            val findBuWidgetViewModel =
                    homeVisitableListData.find { visitable -> visitable is NewBusinessUnitWidgetDataModel }
            findBuWidgetViewModel?.let { findBu ->
                val currentList = homeViewModel.list.toMutableList()
                currentList.let { list ->
                    val buwidgetIndex = list.indexOfFirst { visitable -> visitable is NewBusinessUnitWidgetDataModel }
                    if (buwidgetIndex != -1) {
                        list[buwidgetIndex] = findBu
                        return homeViewModel.copy(list = list)
                    }
                }
            }

            if (findBuWidgetViewModel == null) {
                val findCurrentBuWidgetViewModel =
                        homeViewModel.list.find { visitable -> visitable is NewBusinessUnitWidgetDataModel }
                findCurrentBuWidgetViewModel?.let {
                    val currentList = homeViewModel.list.toMutableList()
                    currentList.let { list ->
                        val buwidgetIndex = list.indexOfFirst { visitable -> visitable is NewBusinessUnitWidgetDataModel }
                        if (buwidgetIndex != -1) {
                            list[buwidgetIndex] = findCurrentBuWidgetViewModel
                            return homeViewModel.copy(list = list)
                        }
                    }
                }
            }
        }
        return homeDataModel
    }

    private fun evaluateRecommendationSection(homeDataModel: HomeDataModel?): HomeDataModel? {
        //check if still contains recom loading, then don't do anything
        if (homeVisitableListData.find { it::class.java == HomeLoadingMoreModel::class.java } != null)
            return homeDataModel

        homeDataModel?.let {
            //reuse the recommendation viewmodel
            val detectHomeRecom = homeVisitableListData.find { visitable -> visitable is HomeRecommendationFeedDataModel }
            return if (detectHomeRecom != null) {
                val currentList = homeDataModel.list.toMutableList()
                currentList.add(detectHomeRecom)
                homeDataModel.copy(list = currentList)
            } else {
                val visitableMutableList: MutableList<Visitable<*>> = homeDataModel.list.toMutableList()
                val mutableIterator = visitableMutableList.iterator()
                for (e in mutableIterator) {
                    if (e is HomeRetryModel) {
                        mutableIterator.remove()
                        break
                    }
                }
                if (!homeDataModel.isCache) {
                    if (visitableMutableList.find { it::class.java == HomeLoadingMoreModel::class.java } == null) visitableMutableList.add(HomeLoadingMoreModel())
                    getFeedTabData()
                }
                homeDataModel.copy(
                        list = visitableMutableList)
            }
        }
        return homeDataModel
    }

    fun getRecommendationFeedSectionPosition() = homeVisitableListData.size - 1

// ===========================================================================================
// ===================================== API CONTROLLER ======================================
// ================================= PLEASE SORT BY NAME A-Z =================================
// ===========================================================================================

    private fun initFlow() {
        _isRequestNetworkLiveData.value = Event(true)
        launchCatchError(coroutineContext, block = {
            homeFlowData.collect { homeDataModel ->
                if (homeDataModel?.isCache == false) {
                    onRefreshState = false
                    _isRequestNetworkLiveData.postValue(Event(false))
                    var homeData: HomeDataModel? = homeDataModel
                    homeData = evaluateGeolocationComponent(homeData)
                    homeData = evaluateAvailableComponent(homeData)
                    homeData?.let {
                        if (it.list.isEmpty()) {
                            ServerLogger.log(Priority.P2, "HOME_STATUS",
                                    mapOf("type" to "empty_update",
                                            "reason" to "Visitables is empty",
                                            "isFirstPage" to it.isFirstPage.toString(),
                                            "isCache" to it.isCache.toString()
                                    ))
                        }
                        homeProcessor.get().sendWithQueueMethod(UpdateHomeData(it, this@HomeViewModel))

                        //initialize master list data here
                        homeVisitableListData = it.list.toMutableList()
                    }
                    getPlayWidget()
                    getHeaderData()
                    getReviewData()
                    getPlayBanner()
                    getPopularKeyword()
                    getDisplayTopAdsHeader()
                    getRecommendationWidget()
                    getTopAdsBannerData()
                    _trackingLiveData.postValue(Event(homeVisitableListData.filterIsInstance<HomeVisitable>()))
                } else if (onRefreshState) {
                    if (homeDataModel?.list?.size ?: 0 > 1) {
                        _isRequestNetworkLiveData.postValue(Event(false))
                    }
                    homeDataModel?.let {
                        homeProcessor.get().sendWithQueueMethod(UpdateHomeData(homeDataModel, this@HomeViewModel))
                    }
                    refreshHomeData()
                }
            }
        }) {
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
            ServerLogger.log(Priority.P2, "HOME_STATUS", mapOf(
                    "type" to "error_init_flow",
                    "reason" to (it.message ?: "".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                    "data" to stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
            ))
        }.invokeOnCompletion {
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))
            val stackTrace = if (it != null) Log.getStackTraceString(it) else ""
            ServerLogger.log(Priority.P2, "HOME_STATUS", mapOf(
                    "type" to "cancelled_init_flow",
                    "reason" to (it?.message
                            ?: "No error propagated".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                    "data" to stackTrace.take(ConstantKey.HomeTimber.MAX_LIMIT)
            ))
            homeFlowDataCancelled = true
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
            }
        }) {
            homeRateLimit.reset(HOME_LIMITER_KEY)
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))

            ServerLogger.log(Priority.P2, "HOME_STATUS", mapOf(
                    "type" to "error_refresh",
                    "reason" to (it.message ?: "".take(ConstantKey.HomeTimber.MAX_LIMIT)),
                    "data" to Log.getStackTraceString(it)
                            .take(ConstantKey.HomeTimber.MAX_LIMIT)
            ))
        }
    }

    fun dismissReview() {
        onRemoveSuggestedReview()
        if (dismissReviewJob?.isActive == true) return
        dismissReviewJob = launchCatchError(coroutineContext, block = {
            dismissHomeReviewUseCase.get().executeOnBackground()
        }) {}
    }

    fun getBusinessUnitTabData(position: Int) {
        launchCatchError(coroutineContext, block = {
            val data = getBusinessWidgetTab.get().executeOnBackground()
            (homeVisitableListData.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let { buWidget ->
                val buWidgetData = buWidget.copy(
                        tabList = data.tabBusinessList,
                        backColor = data.widgetHeader.backColor,
                        contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
                homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(buWidgetData, position, this@HomeViewModel))
            }
        }) {
            (homeVisitableListData.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let { buWidget ->
                homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(buWidget.copy(tabList = listOf()), position, this@HomeViewModel))
            }
        }
    }

    fun getBusinessUnitData(tabId: Int, position: Int, tabName: String) {
        if (buWidgetJob?.isActive == true) return
        buWidgetJob = launchCatchError(coroutineContext, block = {
            getBusinessUnitDataUseCase.get().setParams(tabId, position, tabName)
            val data = getBusinessUnitDataUseCase.get().executeOnBackground()
            homeVisitableListData.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = data)
                homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(oldBuData.copy(contentsList = newBuList), buModel.index, this@HomeViewModel))
            }
        }) {
            // show error
            homeVisitableListData.withIndex().find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = listOf())
                val newList = homeVisitableListData.copy().toMutableList()
                newList[buModel.index] = oldBuData.copy(contentsList = newBuList)
                homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(oldBuData.copy(contentsList = newBuList), buModel.index, this))
            }
        }
    }

    fun getDynamicChannelData(dynamicChannelDataModel: DynamicChannelDataModel, position: Int) {
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(
                    dynamicChannelDataModel.channel?.groupId ?: "")

            if (visitableList.isEmpty()) {
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(dynamicChannelDataModel, position, this@HomeViewModel))
            } else {
                var lastIndex = position
                val dynamicData = homeVisitableListData.getOrNull(lastIndex)
                if (dynamicData !is DynamicChannelDataModel && dynamicData != dynamicChannelDataModel) {
                    lastIndex = homeVisitableListData.indexOf(dynamicChannelDataModel)
                }
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(dynamicChannelDataModel, lastIndex, this@HomeViewModel))
                visitableList.reversed().forEach {
                    homeProcessor.get().sendWithQueueMethod(AddWidgetCommand(it, lastIndex, this@HomeViewModel))
                }
                _trackingLiveData.postValue(Event(visitableList.filterIsInstance(HomeVisitable::class.java)))
            }
        }) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(dynamicChannelDataModel, position, this))
        }
    }

    fun getDynamicChannelData(visitable: Visitable<*>, channelModel: ChannelModel, position: Int) {
        launchCatchError(coroutineContext, block = {
            val visitableList = homeUseCase.get().onDynamicChannelExpired(channelModel.groupId)

            if (visitableList.isEmpty()) {
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(visitable, position, this@HomeViewModel))
            } else {
                var lastIndex = position
                val dynamicData = homeVisitableListData.getOrNull(lastIndex)
                if (dynamicData !is DynamicChannelDataModel && dynamicData != visitable) {
                    lastIndex = homeVisitableListData.indexOf(visitable)
                }
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(visitable, lastIndex, this@HomeViewModel))
                visitableList.reversed().forEach {
                    homeProcessor.get().sendWithQueueMethod(AddWidgetCommand(it, lastIndex, this@HomeViewModel))
                }
                _trackingLiveData.postValue(Event(visitableList.filterIsInstance(HomeVisitable::class.java)))
            }
        }) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(visitable, position, this))
        }
    }

    fun getRechargeRecommendation() {
        if (getRechargeRecommendationJob?.isActive == true) return
        if (!isReminderWidgetAvailable()) return
        getRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            getRechargeRecommendationUseCase.get().setParams()
            val data = getRechargeRecommendationUseCase.get().executeOnBackground()
            _rechargeRecommendationLiveData.postValue(Event(data))
        }) {
            removeRechargeRecommendation()
        }
    }


    fun getSalamWidget() {
        if (getSalamWidgetJob?.isActive == true) return
        if (!isReminderWidgetAvailable()) return

        getSalamWidgetJob = launchCatchError(coroutineContext, block = {
            val data = getSalamWidgetUseCase.get().executeOnBackground()
            _salamWidgetLiveData.postValue(Event(data))
        }) {
            removeSalamWidget()
        }
    }


    private fun isReminderWidgetAvailable(): Boolean {
        return homeVisitableListData.find { visitable ->
            visitable is ReminderWidgetModel
        } != null
    }

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        removeRechargeRecommendation()
        if (declineRechargeRecommendationJob?.isActive == true) return
        declineRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            declineRechargeRecommendationUseCase.get().setParams(requestParams)
            declineRechargeRecommendationUseCase.get().executeOnBackground()
        }) {}
    }

    fun declineSalamItem(requestParams: Map<String, Int>) {
        removeSalamWidget()
        if (declineSalamWidgetJob?.isActive == true) return
        declineSalamWidgetJob = launchCatchError(coroutineContext, block = {
            declineSalamWidgetUseCase.get().setParams(requestParams)
            declineSalamWidgetUseCase.get().executeOnBackground()
        }) {}
    }

    fun getRechargeBUWidget(source: WidgetSource) {
        if (getRechargeBUWidgetJob?.isActive == true) return
        getRechargeBUWidgetJob = launchCatchError(coroutineContext, block = {
            getRechargeBUWidgetUseCase.get().setParams(source)
            val data = getRechargeBUWidgetUseCase.get().executeOnBackground()
            _rechargeBUWidgetLiveData.postValue(Event(data))
        }) {
            removeRechargeBUWidget()
        }
    }

    fun getFeedTabData() {
        if (getTabRecommendationJob != null) return
        getTabRecommendationJob = launchCatchError(coroutineContext, block = {
            val homeRecommendationTabs = getRecommendationTabUseCase.get().executeOnBackground()
            val findRetryModel = homeVisitableListData.withIndex().find { data ->
                data.value is HomeRetryModel
            }
            val findRecommendationModel = homeVisitableListData.find { data ->
                data is HomeRecommendationFeedDataModel
            }
            val findLoadingModel = homeVisitableListData.withIndex().find { data ->
                data.value is HomeLoadingMoreModel
            }

            if (findRecommendationModel != null) return@launchCatchError

            val homeRecommendationFeedViewModel = HomeRecommendationFeedDataModel()
            homeRecommendationFeedViewModel.recommendationTabDataModel = homeRecommendationTabs
            homeRecommendationFeedViewModel.isNewData = true

            homeProcessor.get().sendWithQueueMethod(listOf(
                    AddWidgetCommand(homeRecommendationFeedViewModel, -1, this@HomeViewModel),
                    DeleteWidgetCommand(findLoadingModel?.value, findLoadingModel?.index
                            ?: -1, this@HomeViewModel),
                    DeleteWidgetCommand(findRetryModel?.value, findRetryModel?.index
                            ?: -1, this@HomeViewModel)
            ))

        }) {
            val findRetryModel = homeVisitableListData.withIndex().find { data ->
                data.value is HomeRetryModel
            }
            val findLoadingModel = homeVisitableListData.withIndex().find { data ->
                data.value is HomeLoadingMoreModel
            }
            homeProcessor.get().sendWithQueueMethod(listOf(
                    AddWidgetCommand(HomeRetryModel(), -1, this@HomeViewModel),
                    DeleteWidgetCommand(findLoadingModel?.value, findLoadingModel?.index
                            ?: -1, this@HomeViewModel),
                    DeleteWidgetCommand(findRetryModel?.value, findRetryModel?.index
                            ?: -1, this@HomeViewModel)
            ))
        }
    }

    @VisibleForTesting
    fun getLoadPlayBannerFromNetwork(playBanner: PlayCardDataModel) {
        if (getPlayWidgetJob?.isActive == true) return
        getPlayWidgetJob = launchCatchError(coroutineContext, block = {
            getPlayCardHomeUseCase.get().setParams()
            val data = getPlayCardHomeUseCase.get().executeOnBackground()
            if (data.playChannels.isEmpty() || data.playChannels.first().coverUrl.isEmpty()) {
                clearPlayBanner()
            } else {
                _requestImageTestLiveData.postValue(Event(playBanner.copy(playCardHome = data.playChannels.first())))
            }
        }) {
            clearPlayBanner()
        }
    }

    private fun getTopAdsBannerData() {
        if (getTopAdsBannerDataJob?.isActive == true) return
        homeVisitableListData.withIndex().find {
            it.value is HomeTopAdsBannerDataModel
        }?.let { visitable ->
            getTopAdsBannerDataJob = launchCatchError(coroutineContext, {
                val results = topAdsImageViewUseCase.get().getImageData(
                        topAdsImageViewUseCase.get().getQueryMap(
                                "",
                                "1",
                                "",
                                1,
                                3,
                                ""
                        )
                )
                if (results.isNotEmpty()) {
                    (visitable.value as? HomeTopAdsBannerDataModel)?.let { topAdsModel ->
                        launch(coroutineContext) {
                            val newTopAdsModel = topAdsModel.copy(topAdsImageViewModel = results[0])
                            homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(newTopAdsModel, visitable.index, this@HomeViewModel))
                        }
                    }
                } else {
                    removeAllTopAdsData()
                }
            }) {
                it.printStackTrace()
                removeAllTopAdsData()
            }
        }
    }

    private fun removeAllTopAdsData() {
        val topadsData = homeVisitableListData.withIndex().filter {
            it.value is HomeTopAdsBannerDataModel
        }
        topadsData.map {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(it.value, it.index, this@HomeViewModel))
        }
    }

    private fun getPopularKeyword() {
        val data = homeVisitableListData.find { it is PopularKeywordListDataModel }
        if (data != null && data is PopularKeywordListDataModel) {
            getPopularKeywordData()
        }
    }

    fun getPopularKeywordData() {
        if (getPopularKeywordJob?.isActive == true) return
        getPopularKeywordJob = launchCatchError(coroutineContext, {
            popularKeywordUseCase.get().setParams(page = popularKeywordRefreshCount)
            val results = popularKeywordUseCase.get().executeOnBackground()
            if (results.data.keywords.isNotEmpty()) {
                val resultList = convertPopularKeywordDataList(results.data)
                homeVisitableListData.withIndex().find { it.value is PopularKeywordListDataModel }?.let { indexedData ->
                    val oldData = indexedData.value
                    if (oldData is PopularKeywordListDataModel) {
                        homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(oldData.copy(
                                title = results.data.title,
                                subTitle = results.data.subTitle,
                                popularKeywordList = resultList), indexedData.index, this@HomeViewModel))
                    }
                }
                popularKeywordRefreshCount++
            } else {
                homeVisitableListData.withIndex().find { it.value is PopularKeywordListDataModel }?.let { indexedData ->
                    val oldData = indexedData.value
                    if (oldData is PopularKeywordListDataModel) {
                        homeProcessor.get().sendWithQueueMethod(
                                DeleteWidgetCommand(oldData, indexedData.index, this@HomeViewModel))
                    }
                }
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getSearchHint(isFirstInstall: Boolean) {
        if (getSearchHintJob?.isActive == true) return
        getSearchHintJob = launchCatchError(coroutineContext, block = {
            getKeywordSearchUseCase.get().params = getKeywordSearchUseCase.get().createParams(isFirstInstall, userSession.get().deviceId, userSession.get().userId)
            val data = getKeywordSearchUseCase.get().executeOnBackground()
            _searchHint.postValue(data.searchData)
        }) {}
    }

    private fun getDisplayTopAdsHeader() {
        homeVisitableListData.withIndex().filter { it.value is FeaturedShopDataModel }.forEach { indexed ->
            val featuredShopDataModel = indexed.value as FeaturedShopDataModel
            launchCatchError(coroutineContext, block = {
                getDisplayHeadlineAds.get().createParams(featuredShopDataModel.channelModel.widgetParam)
                val data = getDisplayHeadlineAds.get().executeOnBackground()
                if (data.isEmpty()) {
                    homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(featuredShopDataModel, indexed.index, this@HomeViewModel))
                } else {
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(featuredShopDataModel.copy(
                            channelModel = featuredShopDataModel.channelModel.copy(
                                    channelGrids = mappingTopAdsHeaderToChannelGrid(data)
                            )), indexed.index, this@HomeViewModel))
                }
            }) {
                homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(featuredShopDataModel, indexed.index, this))
            }
        }
    }

    private fun mappingTopAdsHeaderToChannelGrid(data: List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>): List<ChannelGrid> {
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

    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int) {
        launchCatchError(coroutineContext, block = {
            val requestParams = RequestParams()
            val quantity = if (grid.minOrder < 1) "1" else grid.minOrder.toString()
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
            if (addToCartResult.status == STATUS_OK) {
                _oneClickCheckoutHomeComponent.postValue(Event(
                        mapOf(
                                ATC to addToCartResult,
                                CHANNEL to channel,
                                GRID to grid,
                                QUANTITY to quantity,
                                POSITION to position

                        )
                ))
            } else {
                _oneClickCheckoutHomeComponent.postValue(Event(Throwable()))
            }
        }) {
            it.printStackTrace()
            _oneClickCheckoutHomeComponent.postValue(Event(it))
        }
    }

    private fun getTokocashBalance() {
        if (getTokocashJob?.isActive == true) return
        getTokocashJob = launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.get().executeOnBackground())
            updateHeaderViewModel(
                    homeHeaderWalletAction = homeHeaderWalletAction,
                    isWalletDataError = false
            )
            if (homeHeaderWalletAction?.isShowAnnouncement == true && homeHeaderWalletAction.appLinkActionButton.isNotEmpty()) {
                _popupIntroOvoLiveData.postValue(Event(homeHeaderWalletAction.appLinkActionButton))
            }
        }) {
            updateHeaderViewModel(
                    homeHeaderWalletAction = null,
                    isWalletDataError = true
            )
        }
    }

    fun getTokocashPendingBalance() {
        if (getPendingCashBalanceJob?.isActive != true) {
            getPendingCashBalanceJob = launchCatchError(coroutineContext, block = {
                val data = getPendingCashbackUseCase.get().executeOnBackground()
                val cashBackData = CashBackData(data.amount, data.amountText)
                updateHeaderViewModel(
                        cashBackData = cashBackData,
                        isPendingTokocashChecked = true,
                        isWalletDataError = false
                )
            }) {
                // do nothing if fail
            }
        }
    }

    private fun List<TokopointsDrawer>.getDrawerListByType(type: String): TokopointsDrawer? {
        return this.find { it.type == type }
    }

    private fun getTokopoint() {
        if (getTokopointJob?.isActive == true) return
        getTokopointJob = if (navRollanceType.equals(AbTestPlatform.NAVIGATION_VARIANT_REVAMP)) {
            launchCatchError(coroutineContext, block = {
                val data = getHomeTokopointsListDataUseCase.get().executeOnBackground()
                updateHeaderViewModel(
                        tokopointsDrawer = data.tokopointsDrawerList.drawerList.getDrawerListByType("Rewards")
                                ?: data.tokopointsDrawerList.drawerList.getDrawerListByType("Coupon"),
                        tokopointsBBODrawer = data.tokopointsDrawerList.drawerList.getDrawerListByType("BBO"),
                        isTokoPointDataError = false
                )
            }) {
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

    fun injectCouponTimeBased() {
        if (injectCouponTimeBasedJob?.isActive == true) return
        injectCouponTimeBasedJob = launchCatchError(coroutineContext, {
            _injectCouponTimeBasedResult.value = Result.success(injectCouponTimeBasedUseCase.get().executeOnBackground().data)
        }) {
            _injectCouponTimeBasedResult.postValue(Result.error(it))
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

    fun getUserName() = userSession.get().name ?: ""

    // ============================================================================================
// ================================ Live Data Controller ======================================
// ============================================================================================
    override suspend fun updateWidget(visitable: Visitable<*>, position: Int) {
        // should create another list cause homeVisitableListData can run find at other place and throw java.util.ConcurrentModificationException
        val newList = homeVisitableListData.toMutableList()
        logChannelUpdate("Update channel: (Update widget ${visitable.javaClass.simpleName})")
        if (position != -1 && newList.isNotEmpty() && newList.size > position && newList[position]::class.java == visitable::class.java) {
            newList[position] = visitable
        } else {
            newList.withIndex().find {
                it.value::class.java == visitable::class.java && getVisitableId(it.value) == getVisitableId(visitable)
            }?.let {
                newList[it.index] = visitable
            }
        }
        homeVisitableListData = newList
        _homeLiveData.postValue(_homeLiveData.value?.copy(list = homeVisitableListData.copy()))
    }

    override suspend fun addWidget(visitable: Visitable<*>, position: Int) {
        // should create another list cause homeVisitableListData can run find at other place
        val newList = homeVisitableListData.toMutableList()
        logChannelUpdate("Update channel: (Add widget ${visitable.javaClass.simpleName})")
        if (newList.find { getVisitableId(it) == getVisitableId(visitable) && it::class.java == visitable::class.java } != null) return

        //prevent widget becomes the last instead of recom
        if (homeVisitableListData.find { it is HomeRecommendationFeedDataModel } != null &&
                visitable is HomeRecommendationFeedDataModel) {
            return
        }

        if ((position == -1 || position > newList.size)) newList.add(visitable)
        else newList.add(position, visitable)
        homeVisitableListData = newList
        _homeLiveData.postValue(_homeLiveData.value?.copy(list = homeVisitableListData.copy()))
    }

    override suspend fun deleteWidget(visitable: Visitable<*>, position: Int) {
        val newList = homeVisitableListData.toMutableList()
        logChannelUpdate("Update channel: (Remove widget ${visitable.javaClass.simpleName} | ${getVisitableId(visitable)})")
        newList.find { getVisitableId(it) == getVisitableId(visitable) }?.let {
            newList.remove(it)
        }
        homeVisitableListData = newList
        _homeLiveData.postValue(_homeLiveData.value?.copy(list = homeVisitableListData.copy()))
    }

    override suspend fun updateHomeData(homeDataModel: HomeDataModel) {
        logChannelUpdate("Update channel: (Update all home data) data: ${homeDataModel.list.map { it.javaClass.simpleName }}")
        homeVisitableListData = homeDataModel.list.toMutableList()
        _homeLiveData.postValue(homeDataModel)
    }

    private fun getVisitableId(visitable: Visitable<*>): Any? {
        return when (visitable) {
            is HomeVisitable -> visitable.visitableId()
            is HomeComponentVisitable -> visitable.visitableId()
            is RecommendationVisitable -> visitable.visitableId()
            else -> null
        }
    }

    /**
     * Play Widget
     */
    fun getPlayWidget() {
        val dataModel = homeVisitableListData.find { it is CarouselPlayWidgetDataModel } ?: return
        if (dataModel !is CarouselPlayWidgetDataModel) return

        val index = homeVisitableListData.indexOfFirst { it is CarouselPlayWidgetDataModel }

        launchCatchError(block = {
            val response = playWidgetTools.get().getWidgetFromNetwork(
                    PlayWidgetUseCase.WidgetType.Home,
                    homeDispatcher.get().io
            )
            val uiModel = playWidgetTools.get().mapWidgetToModel(response)
            homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(dataModel.copy(
                    widgetUiModel = uiModel
            ), index, this@HomeViewModel))

        }) {
            homeProcessor.get().sendWithQueueMethod(DeleteWidgetCommand(dataModel, index, this@HomeViewModel))
        }
    }

    fun updatePlayWidgetTotalView(channelId: String, totalView: String) {
        updateWidget {
            it.copy(widgetUiModel = playWidgetTools.get().updateTotalView(it.widgetUiModel, channelId, totalView))
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!userSession.get().isLoggedIn) _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        else updatePlayWidgetToggleReminder(channelId, reminderType)
    }

    private fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        updateWidget {
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
                    updateWidget {
                        it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType.switch()))
                    }
                    _playWidgetReminderObservable.postValue(Result.error(Throwable()))
                }
            }
        }) { throwable ->
            updateWidget {
                it.copy(widgetUiModel = playWidgetTools.get().updateActionReminder(it.widgetUiModel, channelId, reminderType.switch()))
            }
            _playWidgetReminderObservable.postValue(Result.error(throwable))
        }
    }

    private fun updateWidget(onUpdate: (oldVal: CarouselPlayWidgetDataModel) -> CarouselPlayWidgetDataModel) {
        val dataModel = homeVisitableListData.find { it is CarouselPlayWidgetDataModel } ?: return
        if (dataModel !is CarouselPlayWidgetDataModel) return

        val index = homeVisitableListData.indexOfFirst { it is CarouselPlayWidgetDataModel }
        homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(onUpdate(dataModel), index, this@HomeViewModel))
    }

// ============================================================================================
// ================================== Mapper Function =========================================
// ============================================================================================

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

    private fun logChannelUpdate(message: String) {
        if (GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }

    private fun removeDynamicChannelLoadingModel() {
        val currentList = homeVisitableListData
        currentList.let {
            val detectLoadingModel = homeVisitableListData.find { visitable -> visitable is DynamicChannelLoadingModel }
            val detectRetryModel = homeVisitableListData.find { visitable -> visitable is DynamicChannelRetryModel }

            (detectRetryModel as? DynamicChannelRetryModel)?.let {
                launch(Dispatchers.Main) {
                    homeVisitableListData.let { list ->
                        updateWidget(DynamicChannelRetryModel(false), list.indexOf(it))
                    }
                }
            }

            (detectLoadingModel as? DynamicChannelLoadingModel)?.let {
                launch(Dispatchers.Main) {
                    homeProcessor.get().sendWithQueueMethod(UpdateWidgetCommand(it, currentList.indexOf(it), this@HomeViewModel))
                    addWidget(DynamicChannelRetryModel(false), currentList.size)
                }
            }
        }
    }

    fun onDynamicChannelRetryClicked() {
        launch(coroutineContext) {
            refreshHomeData()
        }
    }

    fun setRollanceNavigationType(type: String) {
        navRollanceType = type
    }
}