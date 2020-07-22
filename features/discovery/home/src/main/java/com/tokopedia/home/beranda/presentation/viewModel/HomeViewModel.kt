package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.UpdateLiveDataModel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
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
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play_common.domain.model.PlayToggleChannelReminder
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class HomeViewModel @Inject constructor(
        private val homeUseCase: Lazy<HomeUseCase>,
        private val userSession: UserSessionInterface,
        private val closeChannelUseCase: CloseChannelUseCase,
        private val dismissHomeReviewUseCase: DismissHomeReviewUseCase,
        private val getAtcUseCase: AddToCartOccUseCase,
        private val getBusinessUnitDataUseCase: GetBusinessUnitDataUseCase,
        private val getBusinessWidgetTab: GetBusinessWidgetTab,
        private val getDynamicChannelsUseCase: GetDynamicChannelsUseCase,
        private val getHomeReviewSuggestedUseCase: GetHomeReviewSuggestedUseCase,
        private val getHomeTokopointsDataUseCase: GetHomeTokopointsDataUseCase,
        private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
        private val getPendingCashbackUseCase: GetCoroutinePendingCashbackUseCase,
        private val getPlayBannerUseCase: GetPlayWidgetUseCase,
        private val getPlayCardHomeUseCase: GetPlayLiveDynamicUseCase,
        private val playToggleChannelReminderUseCase: PlayToggleChannelReminderUseCase,
        private val getRecommendationTabUseCase: GetRecommendationTabUseCase,
        private val getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase,
        private val popularKeywordUseCase: GetPopularKeywordUseCase,
        private val sendGeolocationInfoUseCase: Lazy<SendGeolocationInfoUseCase>,
        private val stickyLoginUseCase: StickyLoginUseCase,
        private val sendTopAdsUseCase: SendTopAdsUseCase,
        private val getRechargeRecommendationUseCase: GetRechargeRecommendationUseCase,
        private val declineRechargeRecommendationUseCase: DeclineRechargeRecommendationUseCase,
        private val homeDispatcher: HomeDispatcherProvider
) : BaseViewModel(homeDispatcher.io()){

    companion object {
        private const val ACTION_ADD = 1
        private const val ACTION_DELETE = 2
        private const val ACTION_UPDATE = 3
        private const val ACTION_UPDATE_HOME_DATA = 4
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

    private val homeFlowData: Flow<HomeDataModel?> = homeUseCase.get().getHomeData().flowOn(homeDispatcher.io())

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

    val reminderPlayLiveData: LiveData<Result<Boolean>> get() = _reminderPlayLiveData
    private val _reminderPlayLiveData = MutableLiveData<Result<Boolean>>()

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
    private var jobChannel: Job? = null
    private var channel : Channel<UpdateLiveDataModel> = Channel()

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
        channel.close()
        jobChannel?.cancelChildren()
        super.onCleared()
    }

    fun getHeaderData() {
        if (!userSession.isLoggedIn) return
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

    private fun getPlayBannerV2(playCarouselCardDataModel: PlayCarouselCardDataModel){
        launchCatchError(coroutineContext, block = {
            getPlayBannerUseCase.setParams(
                    widgetType = GetPlayWidgetUseCase.HOME_WIDGET_TYPE,
                    authorId = "",
                    authorType = GetPlayWidgetUseCase.HOME_AUTHOR_TYPE
            )
            val newPlayCarouselDataModel = getPlayBannerUseCase.executeOnBackground()
            logChannelUpdate("get play banner data: ${newPlayCarouselDataModel.title} - ${newPlayCarouselDataModel.channelList.size}")
            if(newPlayCarouselDataModel.channelList.isEmpty()){
                _homeLiveData.value?.list?.indexOfFirst { visitable -> visitable is PlayCarouselCardDataModel }?.let{ playIndex ->
                    logChannelUpdate("delete play banner data: ${newPlayCarouselDataModel.title}")
                    updateWidget(UpdateLiveDataModel(ACTION_DELETE, playCarouselCardDataModel, playIndex))
                }
            } else {
                val newList = mutableListOf<Visitable<*>>()
                newList.addAll(_homeLiveData.value?.list ?: listOf())
                val playIndex = newList.indexOfFirst { visitable -> visitable is PlayCarouselCardDataModel }
                logChannelUpdate("get update play banner data: ${newPlayCarouselDataModel.title} - index: $playIndex")
                if(playIndex != -1 && newList[playIndex] is PlayCarouselCardDataModel) {
                    updateWidget(UpdateLiveDataModel(ACTION_UPDATE, playCarouselCardDataModel.copy(
                            playBannerCarouselDataModel = newPlayCarouselDataModel
                    ), playIndex))
                }
            }
        }){
            logChannelUpdate("error get update play banner data: ${it.message}")
            if(playCarouselCardDataModel.playBannerCarouselDataModel.channelList.isEmpty()) {
                val newList = mutableListOf<Visitable<*>>()
                newList.addAll(_homeLiveData.value?.list ?: listOf())
                val playIndex = newList.indexOfFirst { visitable -> visitable is PlayCarouselCardDataModel }
                updateWidget(UpdateLiveDataModel(ACTION_DELETE, playCarouselCardDataModel, playIndex))
            }
        }
    }

    fun setToggleReminderPlayBanner(channelId: String, isSet: Boolean){
        launchCatchError(block = {
            playToggleChannelReminderUseCase.setParams(channelId, isSet)
            val reminder = playToggleChannelReminderUseCase.executeOnBackground()
            if(reminder.playToggleChannelReminder != null && reminder.playToggleChannelReminder?.header?.status == PlayToggleChannelReminder.SUCCESS_STATUS){
                _reminderPlayLiveData.postValue(Result.success(isSet))
            } else {
                rollbackRemindPlayBanner()
                _reminderPlayLiveData.postValue(Result.error(Throwable()))
            }
        }){
            rollbackRemindPlayBanner()
            it.printStackTrace()
            _reminderPlayLiveData.postValue(Result.error(Throwable()))
        }
    }

    private suspend fun rollbackRemindPlayBanner(){
        val pair = _homeLiveData.value?.list?.withIndex()?.find { (_, model) -> model is PlayCarouselCardDataModel }
        if(pair != null){
            updateWidget(UpdateLiveDataModel(
                    action = ACTION_UPDATE,
                    position = pair.index,
                    visitable = (pair.value as PlayCarouselCardDataModel).copy(
                            playBannerCarouselDataModel = (pair.value as PlayCarouselCardDataModel).playBannerCarouselDataModel.copy(
                                    channelList = (pair.value as PlayCarouselCardDataModel).playBannerCarouselDataModel.channelList.map {
                                        if(it is PlayBannerCarouselItemDataModel) it.copy(remindMe = it.remindMe) else it
                                    }
                            )
                    )
            ))
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
            headerDataModel = (_homeLiveData.value?.list?.find { visitable-> visitable is HeaderDataModel } as HeaderDataModel?)?.copy()
        }

        val currentPosition = _homeLiveData.value?.list?.withIndex()?.find { (_, model) ->  model is HeaderDataModel }?.index ?: -1

        headerDataModel?.let { headerViewModel ->
            tokopointsDrawer?.let {
                headerViewModel.tokopointsDrawerHomeData = it
            }
            homeHeaderWalletAction?.let {
                headerViewModel.homeHeaderWalletActionData = it
            }
            cashBackData?.let {
                headerViewModel.cashBackData = it
            }
            tokopointHomeDrawerData?.let {
                headerViewModel.tokoPointDrawerData = it
            }
            isPendingTokocashChecked?.let {
                headerViewModel.isPendingTokocashChecked = it
            }
            isWalletDataError?.let {
                headerViewModel.isWalletDataError = it
            }
            isTokoPointDataError?.let {
                headerViewModel.isTokoPointDataError = it
            }
            headerViewModel.isUserLogin = userSession.isLoggedIn
            launch(coroutineContext) { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, headerViewModel, currentPosition)) }
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
                val data = getHomeReviewSuggestedUseCase.executeOnBackground()
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
            newHomeViewModel = evaluatePlayCarouselWidget(newHomeViewModel)
            newHomeViewModel = evaluateBuWidgetData(newHomeViewModel)
            newHomeViewModel = evaluateRecommendationSection(newHomeViewModel)
            newHomeViewModel = evaluatePopularKeywordComponent(newHomeViewModel)
            return newHomeViewModel
        }
        return homeDataModel
    }

    private fun evaluateGeolocationComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let {
            if (!isNeedShowGeoLocation) {
                val findGeolocationModel =
                        homeDataModel.list.find { visitable -> visitable is GeoLocationPromptDataModel }
                val currentList = homeDataModel.list.toMutableList()

                currentList.let {
                    it.remove(findGeolocationModel)
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

    // Logic get play carousel and should load from API
    fun getPlayBannerCarousel(position: Int = 0) {
        if(position != -1 && position < _homeLiveData.value?.list?.size ?: 0) {
            val visitable = _homeLiveData.value?.list?.get(position)
            if (visitable != null && visitable is PlayCarouselCardDataModel) {
                getPlayBannerV2(visitable)
            } else {
                val playBannerCarousel = _homeLiveData.value?.list?.find { it is PlayCarouselCardDataModel }
                logChannelUpdate("find play banner ${playBannerCarousel?.javaClass?.simpleName}")
                if (playBannerCarousel != null && playBannerCarousel is PlayCarouselCardDataModel) {
                    getPlayBannerV2(playBannerCarousel)
                } else {
                    logChannelUpdate("Null find playbanner listnya: ${_homeLiveData.value?.list?.map { it.javaClass.simpleName }}")
                }
            }
        } else {
            val playBannerCarousel = _homeLiveData.value?.list?.find { it is PlayCarouselCardDataModel }
            logChannelUpdate("find play banner ${playBannerCarousel?.javaClass?.simpleName}")
            if (playBannerCarousel != null && playBannerCarousel is PlayCarouselCardDataModel) {
                getPlayBannerV2(playBannerCarousel)
            } else {
                logChannelUpdate("Null find playbanner listnya: ${_homeLiveData.value?.list?.map { it.javaClass.simpleName }}")
            }
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

    fun onBannerClicked(slidesModel: BannerSlidesModel) {
        if (slidesModel.redirectUrl.isNotEmpty()) {
            sendTopAdsUseCase.executeOnBackground(slidesModel.redirectUrl)
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
                    list.remove(findReviewViewModel)
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

    fun onCloseBuyAgain(channel: DynamicHomeChannel.Channels, position: Int){
        val dynamicChannelDataModel = _homeLiveData.value?.list?.find { visitable -> visitable is DynamicChannelDataModel && visitable.channel?.id == channel.id }
        if (dynamicChannelDataModel is DynamicChannelDataModel){
            launchCatchError(coroutineContext, block = {
                closeChannelUseCase.setParams(channel.id)
                val closeChannel = closeChannelUseCase.executeOnBackground()
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
        if (!userSession.isLoggedIn) return
        updateHeaderViewModel(
                isTokoPointDataError = false,
                tokopointsDrawer = null,
                tokopointHomeDrawerData = null
        )
        getTokopoint()
    }

    fun onRefreshTokoCash() {
        if (!userSession.isLoggedIn) return
        updateHeaderViewModel(
                isWalletDataError = false,
                homeHeaderWalletAction = null
        )
        getTokocashBalance()
    }

    private fun insertRechargeRecommendation(data: RechargeRecommendation) {
        if (data.recommendations.isNotEmpty()) {
            _homeLiveData.value?.list?.run {
                val findRechargeRecommendationViewModel = find { visitable -> visitable is RechargeRecommendationViewModel }
                val indexOfRechargeRecommendationViewModel = indexOf(findRechargeRecommendationViewModel)
                if (indexOfRechargeRecommendationViewModel > -1 && findRechargeRecommendationViewModel is RechargeRecommendationViewModel) {
                    val newFindRechargeRecommendationViewModel = findRechargeRecommendationViewModel.copy(
                            rechargeRecommendation = data
                    )
                    launch { channel.send(UpdateLiveDataModel(ACTION_UPDATE, newFindRechargeRecommendationViewModel, indexOfRechargeRecommendationViewModel)) }
                }
            }
        } else {
            removeRechargeRecommendation()
        }
    }

    private fun removeRechargeRecommendation() {
        val findRechargeRecommendationViewModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is RechargeRecommendationViewModel }
                        ?: return
        if (findRechargeRecommendationViewModel is RechargeRecommendationViewModel) {
            launch { channel.send(UpdateLiveDataModel(ACTION_DELETE, findRechargeRecommendationViewModel)) }
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

    private fun evaluatePlayCarouselWidget(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            // find the old data from current list
            val playWidget = _homeLiveData.value?.list?.find { visitable -> visitable is PlayCarouselCardDataModel}
            if(playWidget != null) {
                // Find the new play widget is still available or not
                val list = homeViewModel.list.toMutableList()
                val playIndex = list.indexOfFirst { visitable -> visitable is PlayCarouselCardDataModel }

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
                val findRetryModel = homeDataModel.list.find { visitable -> visitable is HomeRetryModel
                }
                visitableMutableList.remove(findRetryModel)
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
                    launch(homeDispatcher.ui()){
                        _homeLiveData.value = homeDataModel
                    }
                    delay(100)
                    getPlayBannerCarousel()
                    getHeaderData()
                    getReviewData()
                    getPlayBanner()
                    getPopularKeyword()
                    getRechargeRecommendation()
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
        logChannelUpdate("init channel")
        jobChannel?.cancelChildren()
        jobChannel = launch(homeDispatcher.ui()) {
            updateChannel(channel)
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
            dismissHomeReviewUseCase.executeOnBackground()
        }){}
    }

    fun getBusinessUnitTabData(position: Int){
        launchCatchError(coroutineContext, block = {
            val data = getBusinessWidgetTab.executeOnBackground()
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
            getBusinessUnitDataUseCase.setParams(tabId)
            val data = getBusinessUnitDataUseCase.executeOnBackground()
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
            getDynamicChannelsUseCase.setParams(dynamicChannelDataModel.channel?.groupId ?: "")
            val data = getDynamicChannelsUseCase.executeOnBackground()
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

    private fun getRechargeRecommendation() {
        if(getRechargeRecommendationJob?.isActive == true) return
        getRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            getRechargeRecommendationUseCase.setParams()
            val data = getRechargeRecommendationUseCase.executeOnBackground()
            insertRechargeRecommendation(data)
        }) {
            removeRechargeRecommendation()
        }
    }

    fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        removeRechargeRecommendation()
        if(declineRechargeRecommendationJob?.isActive == true) return
        declineRechargeRecommendationJob = launchCatchError(coroutineContext, block = {
            declineRechargeRecommendationUseCase.setParams(requestParams)
            declineRechargeRecommendationUseCase.executeOnBackground()
        }){}
    }

    fun getFeedTabData() {
        launchCatchError(coroutineContext, block={
            val homeRecommendationTabs = getRecommendationTabUseCase.executeOnBackground()
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
            getPlayCardHomeUseCase.setParams()
            val data = getPlayCardHomeUseCase.executeOnBackground()
            if (data.playChannels.isEmpty() || data.playChannels.first().coverUrl.isEmpty()) {
                clearPlayBanner()
            }
            _requestImageTestLiveData.postValue(Event(playBanner.copy(playCardHome = data.playChannels.first())))
        }){
            clearPlayBanner()
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
            popularKeywordUseCase.setParams()
            val results = popularKeywordUseCase.executeOnBackground()
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
            getKeywordSearchUseCase.params = getKeywordSearchUseCase.createParams(isFirstInstall)
            val data = getKeywordSearchUseCase.executeOnBackground()
            _searchHint.postValue(data.searchData)
        }){}
    }

    fun getStickyContent() {
        if(getStickyLoginJob?.isActive == true) return
        getStickyLoginJob = launchCatchError(coroutineContext, block = {
            stickyLoginUseCase.setParam(RequestParams.create().apply {
                putString(StickyLoginConstant.PARAMS_PAGE, StickyLoginConstant.Page.HOME.toString())
            })
            val response = stickyLoginUseCase.executeOnBackground()
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

    fun sendTopAds(url: String){
        sendTopAdsUseCase.executeOnBackground(url)
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
        getAtcUseCase.createObservable(requestParams)
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
                                                QUANTITY to quantity,
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

    private fun getTokocashBalance() {
        if(getTokocashJob?.isActive == true) return
        getTokocashJob = launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.executeOnBackground())
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
                val data = getPendingCashbackUseCase.executeOnBackground()
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
            val data = getHomeTokopointsDataUseCase.executeOnBackground()
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

    fun getUserId() = userSession.userId ?: ""

// ============================================================================================
// ================================ Live Data Controller ======================================
// ============================================================================================
    // make coffee channel
    private suspend fun updateChannel(channel: ReceiveChannel<UpdateLiveDataModel>){
        for(data in channel){
            if(data.action == ACTION_UPDATE_HOME_DATA){
                logChannelUpdate("Update channel: (Update all home data) data: ${data.homeData?.list?.map { it.javaClass.simpleName }}")
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
                                logChannelUpdate("Update channel: (Add widget ${homeVisitable.javaClass.simpleName})")
                                if(data.position == -1 || data.position > newList.size) newList.add(homeVisitable)
                                else newList.add(data.position, homeVisitable)
                            }
                            ACTION_UPDATE -> {
                                logChannelUpdate("Update channel: (Update widget ${homeVisitable.javaClass.simpleName})")
                                if (data.position != -1 && newList.isNotEmpty() && newList.size > data.position && newList[data.position]::class.java == homeVisitable::class.java) {
                                    newList[data.position] = homeVisitable
                                } else {
                                    newList.withIndex().find { it::class.java == homeVisitable::class.java && (it as HomeVisitable).visitableId() == homeVisitable.visitableId() }?.let {
                                        newList[it.index] = homeVisitable
                                    }
                                }
                            }
                            ACTION_DELETE -> {
                                logChannelUpdate("Update channel: (Remove widget ${homeVisitable.javaClass.simpleName} | ${homeVisitable.visitableId()})")
                                newList.find { it::class.java == homeVisitable::class.java
                                        && (it as HomeVisitable).visitableId() == homeVisitable.visitableId() }?.let {
                                    newList.remove(it)
                                }
                            }
                        }
                        _homeLiveData.value = _homeLiveData.value?.copy(list = newList)
                    }
                }
            }
        }
    }

    private suspend fun updateWidget(updateWidget: UpdateLiveDataModel){
        try {
            if(updateWidget.visitable != null) logChannelUpdate("Send Update Widget... (send = ${channel.isClosedForSend} | widget = ${updateWidget.visitable.javaClass.simpleName})")
            else logChannelUpdate("Send Widget Processing... (send = ${channel.isClosedForSend} | homeData = ${updateWidget.homeData?.list?.map{ it.javaClass.simpleName }}")
            if(channel.isClosedForSend){
                initChannel()
            }
            channel.send(updateWidget)
        }catch (e: ClosedSendChannelException){
            logChannelUpdate("Update Widget Error... (send = ${channel.isClosedForSend} | widget = $updateWidget)")
            initChannel()
            channel.send(updateWidget)
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
                isShowAnnouncement = walletBalanceModel.isShowAnnouncement
        )
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Log.e(this.javaClass.simpleName, message)
    }

}