package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.home.beranda.common.BaseCoRoutineScope
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.isSalamWidgetAvailable
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperRechargetoReminder
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper.mapperSalamtoReminder
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.UpdateLiveDataModel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
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
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
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
        private val getDynamicChannelsUseCase: Lazy<GetDynamicChannelsUseCase>,
        private val getHomeReviewSuggestedUseCase: Lazy<GetHomeReviewSuggestedUseCase>,
        private val getHomeTokopointsDataUseCase: Lazy<GetHomeTokopointsDataUseCase>,
        private val getKeywordSearchUseCase: Lazy<GetKeywordSearchUseCase>,
        private val getPendingCashbackUseCase: Lazy<GetCoroutinePendingCashbackUseCase>,
        private val getPlayCardHomeUseCase: Lazy<GetPlayLiveDynamicUseCase>,
        private val getRecommendationTabUseCase: Lazy<GetRecommendationTabUseCase>,
        private val getWalletBalanceUseCase: Lazy<GetCoroutineWalletBalanceUseCase>,
        private val popularKeywordUseCase: Lazy<GetPopularKeywordUseCase>,
        private val sendGeolocationInfoUseCase: Lazy<SendGeolocationInfoUseCase>,
        private val stickyLoginUseCase: Lazy<StickyLoginUseCase>,
        private val injectCouponTimeBasedUseCase: Lazy<InjectCouponTimeBasedUseCase>,
        private val getRechargeRecommendationUseCase: Lazy<GetRechargeRecommendationUseCase>,
        private val declineRechargeRecommendationUseCase: Lazy<DeclineRechargeRecommendationUseCase>,
        private val getSalamWidgetUseCase: Lazy<GetSalamWidgetUseCase>,
        private val declineSalamWIdgetUseCase: Lazy<DeclineSalamWIdgetUseCase>,
        private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
        private val homeDispatcher: Lazy<HomeDispatcherProvider>
) : BaseCoRoutineScope(homeDispatcher.get().io()){

    companion object {
        private const val ACTION_ADD = 1
        private const val ACTION_DELETE = 2
        private const val ACTION_UPDATE = 3
        private const val ACTION_UPDATE_HOME_DATA = 4
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITIY = "quantity"
        const val POSITION = "position"
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        private val REQUEST_DELAY_SEND_GEOLOCATION = TimeUnit.HOURS.toMillis(1) // 1 hour
    }

    private val homeFlowData: Flow<HomeDataModel?> = homeUseCase.get().getHomeData().flowOn(homeDispatcher.get().io())

// ============================================================================================
// ================================ Live data UI Controller ===================================
// ============================================================================================

    val homeLiveData: LiveData<HomeDataModel>
        get() = _homeLiveData
    private val _homeLiveData: MutableLiveData<HomeDataModel> = MutableLiveData()

    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    private val _searchHint: MutableLiveData<SearchPlaceholder> = MutableLiveData()

    val stickyLogin: LiveData<Result<StickyLoginTickerPojo.TickerDetail>>
        get() = _stickyLogin
    private val _stickyLogin: MutableLiveData<Result<StickyLoginTickerPojo.TickerDetail>> = MutableLiveData()

    val injectCouponTimeBasedResult : LiveData<Result<InjectCouponTimeBased>>
        get() = _injectCouponTimeBasedResult
    private val _injectCouponTimeBasedResult : MutableLiveData<Result<InjectCouponTimeBased>> = MutableLiveData()

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

    val isViewModelInitalized: LiveData<Event<Boolean>>
        get() = _isViewModelInitalized
    private val _isViewModelInitalized = MutableLiveData<Event<Boolean>>(null)

    val isRequestNetworkLiveData: LiveData<Event<Boolean>>
        get() = _isRequestNetworkLiveData
    private val _isRequestNetworkLiveData = MutableLiveData<Event<Boolean>>(null)

    private val _salamWidgetLiveData = MutableLiveData<Event<SalamWidget>>()
    val salamWidgetLiveData: LiveData<Event<SalamWidget>> get() = _salamWidgetLiveData

    private val _rechargeRecommendationLiveData = MutableLiveData<Event<RechargeRecommendation>>()
    val rechargeRecommendationLiveData: LiveData<Event<RechargeRecommendation>> get() = _rechargeRecommendationLiveData

// ============================================================================================
// ==================================== Helper Local Job ======================================
// ================================= PLEASE SORT BY NAME A-Z ==================================
// ============================================================================================

    private var getHomeDataJob: Job? = null
    private var getStickyLoginJob: Job? = null
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
    private var declineSalamWidgetJob : Job? = null
    private var injectCouponTimeBasedJob: Job? = null
    private var getTopAdsBannerDataJob: Job? = null
    private var jobChannel: Job? = null
    private var channel : Channel<UpdateLiveDataModel>? = null

// ============================================================================================
// ===================================== LOCAL VARIABLE =======================================
// ================================= PLEASE SORT BY NAME A-Z ==================================
// ============================================================================================

    private var fetchFirstData = false
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var hasGeoLocationPermission = false
    private var isNeedShowGeoLocation = false
    private var headerDataModel: HeaderDataModel? = null

    private val homeRateLimit = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)

    init {
        _isViewModelInitalized.value = Event(true)
        initChannel()
        initFlow()
    }

    fun refresh(isFirstInstall: Boolean){
        val needSendGeolocationRequest = lastRequestTimeHomeData + REQUEST_DELAY_SEND_GEOLOCATION < System.currentTimeMillis()
        if (!fetchFirstData && homeRateLimit.shouldFetch(HOME_LIMITER_KEY)) {
            refreshHomeData()
        }
        if (needSendGeolocationRequest && hasGeoLocationPermission) {
            _sendLocationLiveData.postValue(Event(needSendGeolocationRequest))
        }
        getTokocashBalance()
        getTokopoint()
        getSearchHint(isFirstInstall)
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
        channel?.close()
        jobChannel?.cancelChildren()
        super.onCleared()
    }

    fun getHeaderData() {
        if (!userSession.get().isLoggedIn) return
        getTokocashBalance()
        getTokopoint()
    }

    fun updateBannerTotalView(totalView: String) {
        val homeList = _homeLiveData.value?.list ?: listOf()
        val playCard = _homeLiveData.value?.list?.firstOrNull { visitable -> visitable is PlayCardDataModel }
        val playIndex = homeList.indexOf(playCard)
        if(playCard != null && playCard is PlayCardDataModel && playCard.playCardHome != null) {
            val newPlayCard = playCard.copy(playCardHome = playCard.playCardHome.copy(totalView = totalView))
            launch(coroutineContext){
                updateWidget(UpdateLiveDataModel(ACTION_UPDATE, newPlayCard, playIndex))
            }
        }
    }

    private fun updateHeaderViewModel(tokopointsDrawer: TokopointsDrawer? = null,
                                      homeHeaderWalletAction: HomeHeaderWalletAction? = null,
                                      tokopointHomeDrawerData: TokopointHomeDrawerData? = null,
                                      cashBackData: CashBackData? = null,
                                      isPendingTokocashChecked: Boolean? = null,
                                      isWalletDataError: Boolean? = null,
                                      isTokoPointDataError: Boolean? = null) {
        if(headerDataModel == null){
            headerDataModel = (_homeLiveData.value?.list?.find { visitable-> visitable is HeaderDataModel } as HeaderDataModel?)
        }

        val currentPosition = _homeLiveData.value?.list?.withIndex()?.find { (_, model) ->  model is HeaderDataModel }?.index ?: -1

        headerDataModel?.let { headerViewModel ->
            tokopointsDrawer?.let {
                headerDataModel = headerDataModel?.copy(tokopointsDrawerHomeData = it)
            }
            homeHeaderWalletAction?.let {
                headerDataModel = headerDataModel?.copy(homeHeaderWalletActionData = it)
            }
            cashBackData?.let {
                headerDataModel = headerDataModel?.copy(cashBackData = it)
            }
            tokopointHomeDrawerData?.let {
                headerDataModel = headerDataModel?.copy(tokoPointDrawerData = it)
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
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, headerViewModel.copy(), currentPosition)) }
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
        if(getSuggestedReviewJob?.isActive == true) return
        if (!isNeedShowGeoLocation) {
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
            if(isNeedShowGeoLocation) newHomeViewModel = onRemoveSuggestedReview(it)
            newHomeViewModel = evaluatePlayWidget(newHomeViewModel)
            newHomeViewModel = evaluateBuWidgetData(newHomeViewModel)
            newHomeViewModel = evaluateRecommendationSection(newHomeViewModel)
            newHomeViewModel = evaluatePopularKeywordComponent(newHomeViewModel)
            newHomeViewModel = evaluateTopAdsBannerComponent(newHomeViewModel)
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
                        if(e is GeoLocationPromptDataModel){
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

    fun getPlayBanner(position: Int){
        val playBanner =
                if (position < _homeLiveData.value?.list?.size ?: -1
                        && _homeLiveData.value?.list?.get(position) is PlayCardDataModel)
                    _homeLiveData.value?.list?.getOrNull(position) as PlayCardDataModel
                else _homeLiveData.value?.list?.find { it is PlayCardDataModel }
        playBanner?.let {
            getLoadPlayBannerFromNetwork(playBanner as PlayCardDataModel)
        }
    }

    // Logic detect play banner should load data from API
    private fun getPlayBanner(){
        // Check the current index is play card view model
        val playBanner = _homeLiveData.value?.list?.find { it is PlayCardDataModel }
        if(playBanner != null && playBanner is PlayCardDataModel) {
            getLoadPlayBannerFromNetwork(playBanner)
        }
    }

    // If the image is valid it will be set play banner to UI
    fun setPlayBanner(playCardDataModel: PlayCardDataModel){
        val newList = mutableListOf<Visitable<*>>()
        newList.addAll(_homeLiveData.value?.list ?: listOf())
        val playIndex = newList.indexOfFirst { visitable -> visitable is PlayCardDataModel }
        if(playIndex != -1 && newList[playIndex] is PlayCardDataModel){
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, playCardDataModel, playIndex)) }
        }
    }

    // play widget it will be removed when load image is failed (deal from PO)
    // because don't let the banner blank
    fun clearPlayBanner(){
        val playIndex = _homeLiveData.value?.list.copy().indexOfFirst { visitable -> visitable is PlayCardDataModel }
        if(playIndex != -1) {
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_DELETE, null, playIndex )) }
        }
    }

    fun setNeedToShowGeolocationComponent(needToShowGeolocation: Boolean){
        this.isNeedShowGeoLocation = needToShowGeolocation
    }

    fun setGeolocationPermission(hasGeolocationPermission: Boolean){
        this.hasGeoLocationPermission = hasGeolocationPermission
    }

    fun hasGeolocationPermission() = hasGeoLocationPermission

// =================================================================================
// ================================ View Controller ================================
// =================================================================================

    private fun insertSuggestedReview(suggestedProductReview: SuggestedProductReview) {
        val findReviewViewModel = _homeLiveData.value?.list?.find { visitable -> visitable is ReviewDataModel }
        val indexOfReviewViewModel = _homeLiveData.value?.list?.indexOf(findReviewViewModel) ?: -1
        if(indexOfReviewViewModel != -1 && findReviewViewModel is ReviewDataModel){
            val newFindReviewViewModel = findReviewViewModel.copy(
                    suggestedProductReview = suggestedProductReview
            )
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, newFindReviewViewModel, indexOfReviewViewModel)) }
        }
    }

    private fun onRemoveSuggestedReview(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { it->
            val findReviewViewModel =
                    it.list.find { visitable -> visitable is ReviewDataModel }
            val currentList = it.list.toMutableList()
            currentList.let { list->
                if (findReviewViewModel is ReviewDataModel) {
                    val mutableIterator = list.iterator()
                    for (e in mutableIterator) {
                        if(e is ReviewDataModel){
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
                _homeLiveData.value?.list?.find { visitable -> visitable is ReviewDataModel }
                        ?: return
        if (findReviewViewModel is ReviewDataModel) {
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_DELETE, findReviewViewModel)) }
        }
    }

    fun onCloseBuyAgain(channelId: String, position: Int){
        val dynamicChannelDataModel = _homeLiveData.value?.list?.find { visitable ->
            visitable is DynamicChannelDataModel && visitable.channel?.id == channelId }
        val recommendationListHomeComponentModel = _homeLiveData.value?.list?.find {
            visitable ->  visitable is RecommendationListCarouselDataModel && visitable.channelModel.id == channelId
        }

        if (dynamicChannelDataModel != null && dynamicChannelDataModel is DynamicChannelDataModel){
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if(closeChannel.success){
                    updateWidget(UpdateLiveDataModel(ACTION_DELETE, dynamicChannelDataModel, position))
                } else {
                    _errorEventLiveData.postValue(Event(""))
                }
            }){
                it.printStackTrace()
                _errorEventLiveData.postValue(Event(it.message ?: ""))
            }
        }

        if (recommendationListHomeComponentModel != null && recommendationListHomeComponentModel is RecommendationListCarouselDataModel){
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.get().setParams(channelId)
                val closeChannel = closeChannelUseCase.get().executeOnBackground()
                if(closeChannel.success){
                    updateWidget(UpdateLiveDataModel(ACTION_DELETE, recommendationListHomeComponentModel, position))
                } else {
                    _errorEventLiveData.postValue(Event(""))
                }
            }){
                it.printStackTrace()
                _errorEventLiveData.postValue(Event(it.message ?: ""))
            }
        }
    }

    fun removeViewHolderAtPosition(position: Int) {
        val homeViewModel = _homeLiveData.value
        val detectViewHolder = homeViewModel?.list?.get(position)
        detectViewHolder?.let {
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_DELETE, it)) }
        }
    }

    fun onCloseGeolocation() {
        val homeViewModel = _homeLiveData.value
        val detectGeolocation = homeViewModel?.list?.find { visitable -> visitable is GeoLocationPromptDataModel }
        (detectGeolocation as? GeoLocationPromptDataModel)?.let {
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_DELETE, it)) }
        }
        setNeedToShowGeolocationComponent(false)
    }

    fun onCloseTicker() {
        val homeViewModel = _homeLiveData.value
        val detectTicker = homeViewModel?.list?.find { visitable -> visitable is TickerDataModel }
        (detectTicker as? TickerDataModel)?.let {
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_DELETE, it)) }
        }
    }

    fun onRefreshTokoPoint() {
        if (!userSession.get().isLoggedIn) return
        updateHeaderViewModel(
                isTokoPointDataError = false,
                tokopointsDrawer = null,
                tokopointHomeDrawerData = null
        )
        getTokopoint()
    }

    fun onRefreshTokoCash() {
        if (!userSession.get().isLoggedIn) return
        updateHeaderViewModel(
                isWalletDataError = false,
                homeHeaderWalletAction = null
        )
        getTokocashBalance()
    }

   fun insertRechargeRecommendation(data: RechargeRecommendation) {
        if (data.recommendations.isNotEmpty()) {
            _homeLiveData.value?.list?.run {
                val findRechargeRecommendationViewModel = find { visitable -> visitable is ReminderWidgetModel
                        && (visitable.source == ReminderEnum.RECHARGE)}
                val indexOfRechargeRecommendationViewModel = indexOf(findRechargeRecommendationViewModel)
                if (indexOfRechargeRecommendationViewModel > -1 && findRechargeRecommendationViewModel is ReminderWidgetModel) {
                    val newFindRechargeRecommendationViewModel = findRechargeRecommendationViewModel.copy(
                           data = mapperRechargetoReminder(data),
                           source = ReminderEnum.RECHARGE
                    )
                    launch(coroutineContext){updateWidget(UpdateLiveDataModel(ACTION_UPDATE, newFindRechargeRecommendationViewModel, indexOfRechargeRecommendationViewModel))}
                }
            }
        } else {
            removeRechargeRecommendation()
        }
    }

    fun insertSalamWidget(data: SalamWidget) {
        if (isSalamWidgetAvailable(data.salamWidget)) {
            _homeLiveData.value?.list?.run {
                val findSalamWidgetModel = find { visitable -> visitable is ReminderWidgetModel
                        && (visitable.source == ReminderEnum.SALAM)}
                val indexOfSalamWidgetModel = indexOf(findSalamWidgetModel)
                if (indexOfSalamWidgetModel > -1 && findSalamWidgetModel is ReminderWidgetModel) {
                    val newFindSalamWidgetModel = findSalamWidgetModel.copy(
                            data = mapperSalamtoReminder(data),
                            source = ReminderEnum.SALAM
                    )
                    launch(coroutineContext){updateWidget(UpdateLiveDataModel(ACTION_UPDATE, newFindSalamWidgetModel, indexOfSalamWidgetModel))}
                }
            }
        } else {
            removeSalamWidget()
        }
    }

    private fun removeRechargeRecommendation() {
        val findRechargeRecommendationViewModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.RECHARGE)}
                        ?: return
        if (findRechargeRecommendationViewModel is ReminderWidgetModel && findRechargeRecommendationViewModel.source==ReminderEnum.RECHARGE) {
            launch { channel?.send(UpdateLiveDataModel(ACTION_DELETE, findRechargeRecommendationViewModel)) }
        }
    }

    private fun removeSalamWidget() {
        val findSalamWidgetModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is ReminderWidgetModel  && (visitable.source == ReminderEnum.SALAM)}
                        ?: return
        if (findSalamWidgetModel is ReminderWidgetModel && findSalamWidgetModel.source==ReminderEnum.SALAM) {
            launch { channel?.send(UpdateLiveDataModel(ACTION_DELETE, findSalamWidgetModel)) }
        }
    }

// =================================================================================
// ============================== Evaluate Controller ==============================
// =================================================================================

    private fun evaluatePlayWidget(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            // find the old data from current list
            val playWidget = _homeLiveData.value?.list?.find { visitable -> visitable is PlayCardDataModel}
            if(playWidget != null) {
                // Find the new play widget is still available or not
                val list = homeViewModel.list.toMutableList()
                val playIndex = list.indexOfFirst { visitable -> visitable is PlayCardDataModel }

                // if on new home available the data, it will be load new data
                if(playIndex != -1){
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
                    _homeLiveData.value?.list?.find { visitable -> visitable is NewBusinessUnitWidgetDataModel }
            findBuWidgetViewModel?.let { findBu->
                val currentList = homeViewModel.list.toMutableList()
                currentList.let {list ->
                    val buwidgetIndex = list.indexOfFirst { visitable -> visitable is NewBusinessUnitWidgetDataModel }
                    if(buwidgetIndex != -1) {
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
                    currentList.let {list ->
                        val buwidgetIndex = list.indexOfFirst { visitable -> visitable is NewBusinessUnitWidgetDataModel }
                        if(buwidgetIndex != -1) {
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
        val detectHomeRecom = _homeLiveData.value?.list?.find { visitable -> visitable is HomeRecommendationFeedDataModel }
        homeDataModel?.let {
            return if (detectHomeRecom != null) {
                val currentList = homeDataModel.list.toMutableList()
                currentList.add(detectHomeRecom)
                homeDataModel.copy(list = currentList)
            } else {
                val visitableMutableList: MutableList<Visitable<*>> = homeDataModel.list.toMutableList()
                val mutableIterator = visitableMutableList.iterator()
                for (e in mutableIterator) {
                    if(e is HomeRetryModel){
                        mutableIterator.remove()
                        break
                    }
                }
                if (!homeDataModel.isCache) {
                    visitableMutableList.add(HomeLoadingMoreModel())
                    getFeedTabData()
                }
                homeDataModel.copy(
                        list = visitableMutableList)
            }
        }
        return homeDataModel
    }

    private fun evaluatePopularKeywordComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            val popularWidget = _homeLiveData.value?.list?.find { visitable -> visitable is PopularKeywordListDataModel}
            if(popularWidget != null) {
                val list = homeViewModel.list.toMutableList()
                // find the old data from current list
                list.forEachIndexed { pos, data ->
                    run {
                        if (data is PopularKeywordListDataModel) {
                            list[pos] = data
                        }
                    }
                }
                homeViewModel.copy(list = list)
            }
        }
        return homeDataModel
    }

    private fun evaluateTopAdsBannerComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            val listData = _homeLiveData.value?.list?.filter {
                it is HomeTopAdsBannerDataModel
            }
            listData?.let {
                val data: List<HomeTopAdsBannerDataModel> = it as List<HomeTopAdsBannerDataModel>

                if (data.isNotEmpty()) {
                    val stack: Stack<HomeTopAdsBannerDataModel> = Stack<HomeTopAdsBannerDataModel>()
                    stack.addAll(data.toMutableList())
                    stack.reverse()

                    val list = homeViewModel.list.toMutableList()
                    // find the old data from current list
                    list.forEachIndexed { pos, data ->
                        run {
                            if (data is HomeTopAdsBannerDataModel) {
                                list[pos] = data
                            }
                        }
                    }
                    homeViewModel.copy(list = list)
                }
            }
        }
        return homeDataModel
    }

    fun getRecommendationFeedSectionPosition() = (_homeLiveData.value?.list?.size?:0)-1

// ===========================================================================================
// ===================================== API CONTROLLER ======================================
// ================================= PLEASE SORT BY NAME A-Z =================================
// ===========================================================================================

    private fun initFlow() {
        _isRequestNetworkLiveData.value = Event(true)
        launchCatchError(coroutineContext, block = {
            homeFlowData.collect { homeDataModel ->
                if (homeDataModel?.isCache == false) {
                    _isRequestNetworkLiveData.postValue(Event(false))
                    updateWidget(UpdateLiveDataModel(action = ACTION_UPDATE_HOME_DATA, homeData = homeDataModel))
                    getHeaderData()
                    getReviewData()
                    getPlayBanner()
                    getPopularKeyword()
                    getTopAdsBannerData()
                    _trackingLiveData.postValue(Event(_homeLiveData.value?.list?.filterIsInstance<HomeVisitable>() ?: listOf()))
                } else {
                    if (homeDataModel?.list?.size?:0 > 1) {
                        _isRequestNetworkLiveData.postValue(Event(false))
                    }
                    updateWidget(UpdateLiveDataModel(action = ACTION_UPDATE_HOME_DATA, homeData = homeDataModel))
                    refreshHomeData()
                }
            }
        }) {
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))
        }
    }

    private fun initChannel(){
        channel = Channel()
        if(channel != null) {
            jobChannel?.cancelChildren()
            jobChannel = launch(homeDispatcher.get().ui()) {
                updateChannel(channel!!)
            }
        }
    }

    fun refreshHomeData() {
        if (getHomeDataJob?.isActive == true) return
        getHomeDataJob = launchCatchError(coroutineContext, block = {
            homeUseCase.get().updateHomeData().collect {
                _updateNetworkLiveData.postValue(it)
            }
        }) {
            homeRateLimit.reset(HOME_LIMITER_KEY)
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))
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
            (_homeLiveData.value?.list?.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                val buWidgetData = buWidget.copy(
                        tabList = data.tabBusinessList,
                        backColor = data.widgetHeader.backColor,
                        contentsList = data.tabBusinessList.withIndex().map { BusinessUnitDataModel(tabName = it.value.name, tabPosition = it.index) })
                updateWidget(UpdateLiveDataModel(ACTION_UPDATE, buWidgetData, position))
            }
        }){
            (_homeLiveData.value?.list?.getOrNull(position) as? NewBusinessUnitWidgetDataModel)?.let{ buWidget ->
                updateWidget(UpdateLiveDataModel(ACTION_UPDATE, buWidget.copy(tabList = listOf()), position))
            }
        }
    }

    fun getBusinessUnitData(tabId: Int, position: Int){
        if(buWidgetJob?.isActive == true) return
        buWidgetJob = launchCatchError(coroutineContext, block = {
            getBusinessUnitDataUseCase.get().setParams(tabId)
            val data = getBusinessUnitDataUseCase.get().executeOnBackground()
            _homeLiveData.value?.list?.withIndex()?.find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = data)
                updateWidget(UpdateLiveDataModel(ACTION_UPDATE, oldBuData.copy(contentsList = newBuList), buModel.index))
            }
        }){
            // show error
            _homeLiveData.value?.list?.withIndex()?.find { it.value is NewBusinessUnitWidgetDataModel }?.let { buModel ->
                val oldBuData = buModel.value as NewBusinessUnitWidgetDataModel
                val newBuList = oldBuData.contentsList.copy().toMutableList()
                newBuList[position] = newBuList[position].copy(list = listOf())
                val newList = _homeLiveData.value?.list.copy().toMutableList()
                newList[buModel.index] = oldBuData.copy(contentsList = newBuList)
                updateWidget(UpdateLiveDataModel(ACTION_UPDATE, oldBuData.copy(contentsList = newBuList),buModel.index))
            }
        }
    }

    fun getDynamicChannelData(dynamicChannelDataModel: DynamicChannelDataModel, position: Int){
        launchCatchError(coroutineContext, block = {
            getDynamicChannelsUseCase.get().setParams(dynamicChannelDataModel.channel?.groupId ?: "")
            val data = getDynamicChannelsUseCase.get().executeOnBackground()
            if(data.isEmpty()){
                updateWidget(UpdateLiveDataModel(ACTION_DELETE, dynamicChannelDataModel, position))
            } else {
                var lastIndex = position
                val dynamicData = _homeLiveData.value?.list?.getOrNull(lastIndex)
                if(dynamicData !is DynamicChannelDataModel && dynamicData != dynamicChannelDataModel){
                    lastIndex = _homeLiveData.value?.list?.indexOf(dynamicChannelDataModel) ?: -1
                }
                updateWidget(UpdateLiveDataModel(ACTION_DELETE, dynamicChannelDataModel, lastIndex))
                data.reversed().forEach {
                    updateWidget(UpdateLiveDataModel(ACTION_ADD, it, lastIndex))
                }
                _trackingLiveData.postValue(Event(data))
            }
        }){
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, dynamicChannelDataModel, position))
        }
    }

    fun getDynamicChannelData(visitable: Visitable<*>, channelModel: ChannelModel, position: Int){
        launchCatchError(coroutineContext, block = {
            getDynamicChannelsUseCase.get().setParams(channelModel.groupId ?: "")
            val data = getDynamicChannelsUseCase.get().executeOnBackground()
            if(data.isEmpty()){
                updateWidget(UpdateLiveDataModel(ACTION_DELETE, visitable, position))
            } else {
                var lastIndex = position
                val dynamicData = _homeLiveData.value?.list?.getOrNull(lastIndex)
                if(dynamicData !is DynamicChannelDataModel && dynamicData != visitable){
                    lastIndex = _homeLiveData.value?.list?.indexOf(visitable) ?: -1
                }
                updateWidget(UpdateLiveDataModel(ACTION_DELETE, visitable, lastIndex))
                data.reversed().forEach {
                    updateWidget(UpdateLiveDataModel(ACTION_ADD, it, lastIndex))
                }
                _trackingLiveData.postValue(Event(data))
            }
        }){
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, visitable, position))
        }
    }

    fun getRechargeRecommendation() {
        if(getRechargeRecommendationJob?.isActive == true) return
        if(!isReminderWidgetAvailable()) return
        getRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            getRechargeRecommendationUseCase.get().setParams()
            val data = getRechargeRecommendationUseCase.get().executeOnBackground()
            _rechargeRecommendationLiveData.postValue(Event(data))
        }) {
            removeRechargeRecommendation()
        }
    }


    fun getSalamWidget(){
        if(getSalamWidgetJob?.isActive == true) return
        if(!isReminderWidgetAvailable()) return

        getSalamWidgetJob = launchCatchError(coroutineContext,  block = {
            val data = getSalamWidgetUseCase.get().executeOnBackground()
            _salamWidgetLiveData.postValue(Event(data))
        }){
            removeSalamWidget()
        }
    }


    private fun isReminderWidgetAvailable() : Boolean{
        return _homeLiveData.value?.list?.find {
            visitable -> visitable is ReminderWidgetModel
        } != null
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
            declineSalamWIdgetUseCase.get().setParams(requestParams)
            declineSalamWIdgetUseCase.get().executeOnBackground()
        }){}
    }

    fun getFeedTabData() {
        launchCatchError(coroutineContext, block={
            val homeRecommendationTabs = getRecommendationTabUseCase.get().executeOnBackground()
            val findRetryModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRetryModel
            }
            val findRecommendationModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRecommendationFeedDataModel
            }
            val findLoadingModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeLoadingMoreModel
            }

            if (findRecommendationModel != null) return@launchCatchError

            val homeRecommendationFeedViewModel = HomeRecommendationFeedDataModel()
            homeRecommendationFeedViewModel.recommendationTabDataModel = homeRecommendationTabs
            homeRecommendationFeedViewModel.isNewData = true

            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findLoadingModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findRetryModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_ADD, homeRecommendationFeedViewModel))

        }){
            val findRetryModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRetryModel
            }
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findRetryModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_ADD, HomeRetryModel()))
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
            }
            _requestImageTestLiveData.postValue(Event(playBanner.copy(playCardHome = data.playChannels.first())))
        }){
            clearPlayBanner()
        }
    }

    private fun getTopAdsBannerData() {
        val data: List<HomeTopAdsBannerDataModel> = _homeLiveData.value?.list?.filter {
            it is HomeTopAdsBannerDataModel
        } as List<HomeTopAdsBannerDataModel>

        if(data.isNotEmpty()) {
            if(getTopAdsBannerDataJob?.isActive == true) return
                getTopAdsBannerDataJob = launchCatchError(coroutineContext, {
                val results = topAdsImageViewUseCase.get().getImageData(
                        topAdsImageViewUseCase.get().getQueryMap(
                                "",
                                "1",
                                "",
                                data.size,
                                3,
                                ""
                        )
                )
                if (results.isNotEmpty()) {
                    val stack: Stack<TopAdsImageViewModel> = Stack<TopAdsImageViewModel>()
                    stack.addAll(results.toMutableList())
                    stack.reverse()

                    val newHomeData = _homeLiveData.value
                    val newList = newHomeData?.list?: listOf()
                    if (newList.isNotEmpty()) {
                        newList.mapIndexed { index, visitable ->
                            if (visitable is HomeTopAdsBannerDataModel) {
                                val topAdsImageViewModel = stack.pop()
                                launch(coroutineContext){
                                    val newTopAdsModel = visitable.copy(topAdsImageViewModel = topAdsImageViewModel)
                                    updateWidget(UpdateLiveDataModel(ACTION_UPDATE,
                                            newTopAdsModel, index))
                                }
                            }
                        }
                    }
                } else {
                    val newHomeData = _homeLiveData.value
                    val newList = newHomeData?.list?: listOf()
                    if (newList.isNotEmpty()) {
                        newList.mapIndexed { index, visitable ->
                            if (visitable is HomeTopAdsBannerDataModel) {
                                launch(coroutineContext){
                                    updateWidget(UpdateLiveDataModel(ACTION_DELETE,
                                            visitable, index))
                                }
                            }
                        }
                    }
                }
            }){
                it.printStackTrace()
            }
        }
    }

    private fun getPopularKeyword() {
        val data = _homeLiveData.value?.list?.find { it is PopularKeywordListDataModel }
        if(data != null && data is PopularKeywordListDataModel) {
            getPopularKeywordData()
        }
    }

    fun getPopularKeywordData() {
        if(getPopularKeywordJob?.isActive == true) return
        getPopularKeywordJob = launchCatchError(coroutineContext, {
            popularKeywordUseCase.get().setParams()
            val results = popularKeywordUseCase.get().executeOnBackground()
            if (results.data.keywords.isNotEmpty()) {
                val resultList = convertPopularKeywordDataList(results.data.keywords)
                _homeLiveData.value?.list?.withIndex()?.find { it.value is PopularKeywordListDataModel }?.let { indexedData ->
                    val oldData = indexedData.value
                    if (oldData is PopularKeywordListDataModel) {
                        updateWidget(UpdateLiveDataModel(ACTION_UPDATE, oldData.copy(popularKeywordList = resultList), indexedData.index))
                    }
                }
            }
        }){
            it.printStackTrace()
        }
    }

    fun getSearchHint(isFirstInstall: Boolean) {
        if(getSearchHintJob?.isActive == true) return
        getSearchHintJob = launchCatchError(coroutineContext, block={
            getKeywordSearchUseCase.get().params = getKeywordSearchUseCase.get().createParams(isFirstInstall)
            val data = getKeywordSearchUseCase.get().executeOnBackground()
            _searchHint.postValue(data.searchData)
        }){}
    }

    fun getStickyContent() {
        if(getStickyLoginJob?.isActive == true) return
        getStickyLoginJob = launchCatchError(coroutineContext, block = {
            stickyLoginUseCase.get().setParam(RequestParams.create().apply {
                putString(StickyLoginConstant.PARAMS_PAGE, StickyLoginConstant.Page.HOME.toString())
            })
            val response = stickyLoginUseCase.get().executeOnBackground()
            val data = response.response.tickers.find { it.layout == StickyLoginConstant.LAYOUT_FLOATING }
            if(data == null){
                _stickyLogin.postValue(Result.error(Exception()))
            } else {
                _stickyLogin.postValue(Result.success(data))
            }

        }){
            _stickyLogin.postValue(Result.error(it))
        }
    }

    fun getOneClickCheckout(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int){
        val requestParams = RequestParams()
        val quantity = if(grid.minOrder < 1) "1" else grid.minOrder.toString()
        requestParams.putObject(AddToCartOccUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartOccRequestParams(
                productId = grid.id,
                quantity = quantity,
                shopId = grid.shop.shopId,
                warehouseId = grid.warehouseId,
                productName = grid.name,
                price = grid.price
        ))
        getAtcUseCase.get().createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe (
                        {
                            if(it.status == STATUS_OK) {
                                _oneClickCheckout.postValue(Event(
                                        mapOf(
                                                ATC to it,
                                                CHANNEL to channel,
                                                GRID to grid,
                                                QUANTITIY to quantity,
                                                POSITION to position

                                        )
                                ))
                            }
                            else {
                                _oneClickCheckout.postValue(Event(Throwable()))
                            }
                        },
                        {
                            _oneClickCheckout.postValue(Event(it))
                        }
                )
    }

    fun getOneClickCheckoutHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int){
        val requestParams = RequestParams()
        val quantity = if(grid.minOrder < 1) "1" else grid.minOrder.toString()
        requestParams.putObject(AddToCartOccUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, AddToCartOccRequestParams(
                productId = grid.id,
                quantity = quantity,
                shopId = grid.shopId,
                warehouseId = grid.warehouseId,
                productName = grid.name,
                price = grid.price
        ))
        getAtcUseCase.get().createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe (
                        {
                            if(it.status == STATUS_OK) {
                                _oneClickCheckoutHomeComponent.postValue(Event(
                                        mapOf(
                                                ATC to it,
                                                CHANNEL to channel,
                                                GRID to grid,
                                                QUANTITIY to quantity,
                                                POSITION to position

                                        )
                                ))
                            }
                            else {
                                _oneClickCheckoutHomeComponent.postValue(Event(Throwable()))
                            }
                        },
                        {
                            _oneClickCheckoutHomeComponent.postValue(Event(it))
                        }
                )
    }

    private fun getTokocashBalance() {
        if(getTokocashJob?.isActive == true) return
        getTokocashJob = launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.get().executeOnBackground())
            updateHeaderViewModel(
                    isWalletDataError = false,
                    homeHeaderWalletAction = homeHeaderWalletAction
            )
            if (homeHeaderWalletAction?.isShowAnnouncement == true && homeHeaderWalletAction.appLinkActionButton.isNotEmpty()) {
                _popupIntroOvoLiveData.postValue(Event(homeHeaderWalletAction.appLinkActionButton))
            }
        }){
            updateHeaderViewModel(
                    isWalletDataError = true,
                    homeHeaderWalletAction = null
            )
        }
    }

    fun getTokocashPendingBalance(){
        if(getPendingCashBalanceJob?.isActive != true){
            getPendingCashBalanceJob = launchCatchError(coroutineContext, block={
                val data = getPendingCashbackUseCase.get().executeOnBackground()
                val cashBackData = CashBackData(data.amount, data.amountText)
                updateHeaderViewModel(
                        isWalletDataError = false,
                        cashBackData = cashBackData,
                        isPendingTokocashChecked = true
                )
            }){
                // do nothing if fail
            }
        }
    }

    private fun getTokopoint(){
        if(getTokopointJob?.isActive == true) return

        getTokopointJob = launchCatchError(coroutineContext, block={
            val data = getHomeTokopointsDataUseCase.get().executeOnBackground()
            updateHeaderViewModel(
                    isTokoPointDataError = false,
                    tokopointsDrawer = data.tokopointsDrawer
            )
        }){
            updateHeaderViewModel(
                    isTokoPointDataError = true,
                    tokopointsDrawer = null,
                    tokopointHomeDrawerData = null
            )
        }
    }

    fun injectCouponTimeBased() {
        if(injectCouponTimeBasedJob?.isActive == true) return
        injectCouponTimeBasedJob = launchCatchError(coroutineContext, {
            _injectCouponTimeBasedResult.value = Result.success(injectCouponTimeBasedUseCase.get().executeOnBackground().data)
        }){
            it.printStackTrace()
            _injectCouponTimeBasedResult.postValue(Result.error(it))
        }
    }

    fun getUserId() = userSession.get().userId ?: ""

// ============================================================================================
// ================================ Live Data Controller ======================================
// ============================================================================================

    private suspend fun updateChannel(channel: Channel<UpdateLiveDataModel>){
        for(data in channel){
            if(data.action == ACTION_UPDATE_HOME_DATA){
                data.homeData?.let { homeData ->
                    var homeDataModel = evaluateGeolocationComponent(homeData)
                    homeDataModel = evaluateAvailableComponent(homeDataModel)
                        _homeLiveData.value = homeDataModel
                }
            } else {
                val newList = _homeLiveData.value?.list?.toMutableList()
                data.visitable?.let { homeVisitable ->
                    if(newList != null && newList.size >  data.position) {
                        when (data.action) {
                            ACTION_ADD -> {
                                if(data.position == -1 || data.position > newList.size) newList.add(homeVisitable)
                                else newList.add(data.position, homeVisitable)
                            }
                            ACTION_UPDATE -> {
                                if (data.position != -1 && newList.isNotEmpty() && newList.size > data.position && newList[data.position]::class.java == homeVisitable::class.java) {
                                    newList[data.position] = homeVisitable
                                } else {
                                    newList.withIndex().find {
                                        it::class.java == Visitable::class.java }?.let {
                                        newList[it.index] = homeVisitable
                                    }
                                }
                            }
                            ACTION_DELETE -> newList.remove(homeVisitable)
                        }
                        _homeLiveData.value = _homeLiveData.value?.copy(list = newList)
                    }
                }
            }
        }
    }

    private suspend fun updateWidget(updateWidget: UpdateLiveDataModel){
        try {
            if(channel?.isClosedForSend == true){
                initChannel()
            }
            channel?.send(updateWidget)
        }catch (e: ClosedSendChannelException){
            initChannel()
            channel?.send(updateWidget)
        }
    }

// ============================================================================================
// ================================== Mapper Function =========================================
// ============================================================================================

    private fun convertPopularKeywordDataList(list: List<HomeWidget.PopularKeyword>): MutableList<PopularKeywordDataModel> {
        val dataList: MutableList<PopularKeywordDataModel> = mutableListOf()
        for (pojo in list) {
            dataList.add(PopularKeywordDataModel(pojo.url, pojo.imageUrl, pojo.keyword, pojo.productCount))
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
}