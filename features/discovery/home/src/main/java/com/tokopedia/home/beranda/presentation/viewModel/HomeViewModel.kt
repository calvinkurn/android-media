package com.tokopedia.home.beranda.presentation.viewModel

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.UpdateLiveDataModel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.copy
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.listener.ImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import rx.Observable
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
        private val homeUseCase: HomeUseCase,
        private val userSession: UserSessionInterface,
        private val getRecommendationTabUseCase: GetRecommendationTabUseCase,
        private val sendGeolocationInfoUseCase: SendGeolocationInfoUseCase,
        private val getWalletBalanceUseCase: GetCoroutineWalletBalanceUseCase,
        private val getPendingCashbackUseCase: GetCoroutinePendingCashbackUseCase,
        private val getHomeTokopointsDataUseCase: GetHomeTokopointsDataUseCase,
        private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
        private val stickyLoginUseCase: StickyLoginUseCase,
        private val getHomeReviewSuggestedUseCase: GetHomeReviewSuggestedUseCase,
        private val dismissHomeReviewUseCase: DismissHomeReviewUseCase,
        private val getPlayCardHomeUseCase: GetPlayLiveDynamicUseCase,
        private val popularKeywordUseCase: GetPopularKeywordUseCase,
        private val getBusinessWidgetTab: GetBusinessWidgetTab,
        private val getBusinessUnitDataUseCase: GetBusinessUnitDataUseCase,
        private val homeDispatcher: HomeDispatcherProvider
) : BaseViewModel(homeDispatcher.io()){

    companion object {
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        private const val ACTION_ADD = 1
        private const val ACTION_DELETE = 2
        private const val ACTION_UPDATE = 3
        private const val ACTION_UPDATE_HOME_DATA = 4
        private const val HOME_LIMITER_KEY = "HOME_LIMITER_KEY"
        private val REQUEST_DELAY_SEND_GEOLOCATION = TimeUnit.HOURS.toMillis(1) // 1 hour
    }

    private val homeFlowData: Flow<HomeDataModel?> = homeUseCase.getHomeData().flowOn(homeDispatcher.io())

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

// ============================================================================================
// ================================= Helper Live Data =========================================
// ============================================================================================

    val trackingLiveData: LiveData<Event<List<HomeVisitable>>>
        get() = _trackingLiveData
    private val _trackingLiveData = MutableLiveData<Event<List<HomeVisitable>>>()

    val popupIntroOvoLiveData: LiveData<Event<String>>
        get() = _popupIntroOvoLiveData
    private val _popupIntroOvoLiveData = MutableLiveData<Event<String>>()

    val sendLocationLiveData: LiveData<Event<Any>>
        get() = _sendLocationLiveData
    private val _sendLocationLiveData = MutableLiveData<Event<Any>>()

    val updateNetworkLiveData: LiveData<Result<Any>> get() = _updateNetworkLiveData
    private val _updateNetworkLiveData = MutableLiveData<Result<Any>>()

    // Test cover banner url play widget is valid or not
    private val _requestImageTestLiveData = MutableLiveData<Event<PlayCardViewModel>>()
    val requestImageTestLiveData: LiveData<Event<PlayCardViewModel>> get() = _requestImageTestLiveData

// ============================================================================================
// ================================= Helper Local Job =========================================
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
    private var jobChannel: Job? = null
    private var channel : Channel<UpdateLiveDataModel>? = null

// ============================================================================================
// ================================== Local variable ==========================================
// ============================================================================================

    private var fetchFirstData = false
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var hasGeoLocationPermission = false
    private var isNeedShowGeoLocation = false
    private var headerViewModel: HeaderViewModel? = null


    private val homeRateLimit = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)
    init {
        initChannel()
        initFlow()
    }

    fun refresh(){
        val needSendGeolocationRequest = lastRequestTimeHomeData + REQUEST_DELAY_SEND_GEOLOCATION < System.currentTimeMillis()
        if (!fetchFirstData && homeRateLimit.shouldFetch(HOME_LIMITER_KEY)) {
            refreshHomeData()
        }
        if (needSendGeolocationRequest && hasGeoLocationPermission) {
            _sendLocationLiveData.postValue(Event(needSendGeolocationRequest))
        }
        getTokocashBalance()
        getTokopoint()
        searchHint()
    }

    fun hitBannerImpression(slidesModel: BannerSlidesModel) {
        if (!slidesModel.isImpressed && slidesModel.topadsViewUrl.isNotEmpty()) {
            compositeSubscription.add(Observable.just(ImpresionTask(object : ImpressionListener {
                        override fun onSuccess() {
                            slidesModel.isImpressed = true
                        }

                        override fun onFailed() {
                            slidesModel.isImpressed = false
                        }
                    }).execute(slidesModel.topadsViewUrl))
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

    fun sendGeolocationData() {
        sendGeolocationInfoUseCase.createObservable(RequestParams.EMPTY)
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
        if (!userSession.isLoggedIn) return
        getTokocashBalance()
        getTokopoint()
    }

    fun updateBannerTotalView(totalView: String) {
        val newList = mutableListOf<Visitable<*>>()
        newList.addAll(_homeLiveData.value?.list ?: listOf())

        val playCard = newList.firstOrNull { visitable -> visitable is PlayCardViewModel }
        val playIndex = newList.indexOf(playCard)
        if(playCard != null && playCard is PlayCardViewModel && playCard.playCardHome != null) {
            val newPlayCard = playCard.copy(playCardHome = playCard.playCardHome.copy(totalView = totalView))
            newList[playIndex] = newPlayCard
            _homeLiveData.postValue(_homeLiveData.value?.copy(
                    list = newList
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
        if(headerViewModel == null){
            headerViewModel = _homeLiveData.value?.list?.find { visitable-> visitable is HeaderViewModel } as HeaderViewModel?
        }

        val currentPosition = _homeLiveData.value?.list?.withIndex()?.find { (_, model) ->  model is HeaderViewModel }?.index ?: -1

        headerViewModel?.let { headerViewModel ->
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
            launch { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, headerViewModel, currentPosition)) }
        }

    }

    private fun getReviewData() {
        if (isNeedShowGeoLocation) {
            removeSuggestedReview()
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
                removeSuggestedReview()
            }
        }
    }

    private fun evaluateAvailableComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let {
            var newHomeViewModel = homeDataModel
            if(isNeedShowGeoLocation) newHomeViewModel = removeSuggestedReview(it)
            newHomeViewModel = evaluatePlayWidget(newHomeViewModel)
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
                        homeDataModel.list.find { visitable -> visitable is GeolocationPromptViewModel }
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
                        && _homeLiveData.value?.list?.get(position) is PlayCardViewModel)
                    _homeLiveData.value?.list?.getOrNull(position) as PlayCardViewModel
                else _homeLiveData.value?.list?.find { it is PlayCardViewModel }
        playBanner?.let {
            loadPlayBannerFromNetwork(playBanner as PlayCardViewModel)
        }
    }

    // Logic detect play banner should load data from API
    private fun getPlayBanner(){
        // Check the current index is play card view model
        val playBanner = _homeLiveData.value?.list?.find { it is PlayCardViewModel }
        if(playBanner != null && playBanner is PlayCardViewModel) {
            loadPlayBannerFromNetwork(playBanner)
        }
    }

    // If the image is valid it will be set play banner to UI
    fun setPlayBanner(playCardViewModel: PlayCardViewModel){
        val newList = mutableListOf<Visitable<*>>()
        newList.addAll(_homeLiveData.value?.list ?: listOf())
        val playIndex = newList.indexOfFirst { visitable -> visitable is PlayCardViewModel }
        if(playIndex != -1 && newList[playIndex] is PlayCardViewModel){
            launch { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, playCardViewModel, playIndex)) }
        }
    }

    // play widget it will be removed when load image is failed (deal from PO)
    // because don't let the banner blank
    fun clearPlayBanner(){
        val playIndex = _homeLiveData.value?.list.copy().indexOfFirst { visitable -> visitable is PlayCardViewModel }
        if(playIndex != -1) {
            launch { updateWidget(UpdateLiveDataModel(ACTION_DELETE, null, playIndex )) }
        }
    }

    fun onBannerClicked(slidesModel: BannerSlidesModel) {
        if (slidesModel.redirectUrl.isNotEmpty()) {
            ImpresionTask().execute(slidesModel.redirectUrl)
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
        val findReviewViewModel = _homeLiveData.value?.list?.find { visitable -> visitable is ReviewViewModel }
        val indexOfReviewViewModel = _homeLiveData.value?.list?.indexOf(findReviewViewModel) ?: -1
        if(indexOfReviewViewModel != -1 && findReviewViewModel is ReviewViewModel){
            val newFindReviewViewModel = findReviewViewModel.copy(
                    suggestedProductReview = suggestedProductReview
            )
            launch { updateWidget(UpdateLiveDataModel(ACTION_UPDATE, newFindReviewViewModel, indexOfReviewViewModel)) }
        }
    }

    private fun removeSuggestedReview(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { it->
            val findReviewViewModel =
                    it.list.find { visitable -> visitable is ReviewViewModel }
            val currentList = it.list.toMutableList()
            currentList.let { list->
                if (findReviewViewModel is ReviewViewModel) {
                    list.remove(findReviewViewModel)
                    return it.copy(
                            list = list
                    )
                }
            }
        }
        return homeDataModel
    }

    fun removeSuggestedReview() {
        val findReviewViewModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is ReviewViewModel }
                        ?: return
        if (findReviewViewModel is ReviewViewModel) {
            launch { updateWidget(UpdateLiveDataModel(ACTION_DELETE, findReviewViewModel)) }
        }
    }

    fun onCloseGeolocation() {
        val homeViewModel = _homeLiveData.value
        val detectGeolocation = homeViewModel?.list?.find { visitable -> visitable is GeolocationPromptViewModel }
        (detectGeolocation as? GeolocationPromptViewModel)?.let {
            launch { updateWidget(UpdateLiveDataModel(ACTION_DELETE, it)) }
        }
    }

    fun onCloseTicker() {
        val homeViewModel = _homeLiveData.value
        val detectTicker = homeViewModel?.list?.find { visitable -> visitable is TickerViewModel }
        (detectTicker as? TickerViewModel)?.let {
            launch { updateWidget(UpdateLiveDataModel(ACTION_DELETE, it)) }
        }
    }

    fun onRefreshTokoPoint() {
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

// =================================================================================
// ============================== Evaluate Controller ==============================
// =================================================================================

    private fun evaluatePlayWidget(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            // find the old data from current list
            val playWidget = _homeLiveData.value?.list?.find { visitable -> visitable is PlayCardViewModel}
            if(playWidget != null) {
                // Find the new play widget is still available or not
                val list = homeViewModel.list.toMutableList()
                val playIndex = list.indexOfFirst { visitable -> visitable is PlayCardViewModel }

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
        val detectHomeRecom = _homeLiveData.value?.list?.find { visitable -> visitable is HomeRecommendationFeedViewModel }
        homeDataModel?.let {
            if (detectHomeRecom != null) {
                val currentList = homeDataModel.list.toMutableList()
                currentList.add(detectHomeRecom)
                return homeDataModel.copy(list = currentList)
            } else {
                val visitableMutableList: MutableList<Visitable<*>> = homeDataModel.list.toMutableList()
                val findRetryModel = homeDataModel.list.find {
                    visitable -> visitable is HomeRetryModel
                }
                val findLoadingModel = homeDataModel.list.find {
                    visitable -> visitable is HomeLoadingMoreModel
                }
                visitableMutableList.remove(findLoadingModel)
                visitableMutableList.remove(findRetryModel)
                visitableMutableList.add(HomeLoadingMoreModel())
                val newHomeViewModel = homeDataModel.copy(
                        list = visitableMutableList)
                getFeedTabData()
                return newHomeViewModel
            }
        }
        return homeDataModel
    }

    private fun evaluatePopularKeywordComponent(homeDataModel: HomeDataModel?): HomeDataModel? {
        homeDataModel?.let { homeViewModel ->
            val popularWidget = _homeLiveData.value?.list?.find { visitable -> visitable is PopularKeywordListViewModel}
            if(popularWidget != null) {
                val list = homeViewModel.list.toMutableList()
                // find the old data from current list
                list.forEachIndexed { pos, data ->
                    run {
                        if (data is PopularKeywordListViewModel) {
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

// =================================================================================
// ================================ API Controller ================================
// =================================================================================

    private fun initFlow() {
        launchCatchError(coroutineContext, block = {
            homeFlowData.collect { homeDataModel ->
                if (homeDataModel?.isCache == false) {
                    updateWidget(UpdateLiveDataModel(action = ACTION_UPDATE_HOME_DATA, homeData = homeDataModel))
                    getHeaderData()
                    getReviewData()
                    getPlayBanner()
                    getPopularKeyword()
                    _trackingLiveData.postValue(Event(_homeLiveData.value?.list?.filterIsInstance<HomeVisitable>() ?: listOf()))
                } else {
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
            jobChannel = launch {
                updateChannel(channel!!)
            }
        }
    }

    fun refreshHomeData() {
        if (getHomeDataJob?.isActive == true) return
        getHomeDataJob = launchCatchError(coroutineContext, block = {
            homeUseCase.updateHomeData().collect {
                _updateNetworkLiveData.postValue(it)
            }
        }) {
            homeRateLimit.reset(HOME_LIMITER_KEY)
            _updateNetworkLiveData.postValue(Result.error(Throwable(), null))
        }
    }

    fun dismissReview() {
        removeSuggestedReview()
        if(dismissReviewJob?.isActive == true) return
        dismissReviewJob = launchCatchError(coroutineContext, block = {
            dismissHomeReviewUseCase.executeOnBackground()
        }){}
    }

    @VisibleForTesting
    fun loadPlayBannerFromNetwork(playBanner: PlayCardViewModel){
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

    private fun getTokocashBalance() {
        if(getTokocashJob?.isActive == true) return
        getTokocashJob = launchCatchError(coroutineContext, block = {
            val homeHeaderWalletAction = mapToHomeHeaderWalletAction(getWalletBalanceUseCase.executeOnBackground())
            updateHeaderViewModel(
                    isWalletDataError = false,
                    homeHeaderWalletAction = homeHeaderWalletAction
            )
            if (homeHeaderWalletAction?.isShowAnnouncement == true && homeHeaderWalletAction.appLinkActionButton != null && homeHeaderWalletAction.appLinkActionButton?.isNotEmpty() == true) {
                _popupIntroOvoLiveData.postValue(Event(homeHeaderWalletAction.appLinkActionButton ?: ""))
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
                val cashBackData = CashBackData()
                cashBackData.amount = data.amount
                cashBackData.amountText = data.amountText
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

    fun searchHint(){
        if(getSearchHintJob?.isActive == true) return
        getSearchHintJob = launchCatchError(coroutineContext, block={
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

    private fun getPopularKeyword() {
        val data = _homeLiveData.value?.list?.find { it is PopularKeywordListViewModel }
        if(data != null && data is PopularKeywordListViewModel) {
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
                _homeLiveData.value?.list?.withIndex()?.find { it.value is PopularKeywordListViewModel }?.let { indexedData ->
                    val oldData = indexedData.value
                    if (oldData is PopularKeywordListViewModel) {
                        updateWidget(UpdateLiveDataModel(ACTION_UPDATE, oldData.copy(popularKeywordList = resultList), indexedData.index))
                    }
                }
            }
        }){
            it.printStackTrace()
        }
    }

    fun getFeedTabData() {
        launchCatchError(coroutineContext, block={
            val homeRecommendationTabs = getRecommendationTabUseCase.executeOnBackground()
            val findLoadingModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeLoadingMoreModel
            }
            val findRetryModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRetryModel
            }
            val findRecommendationModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRecommendationFeedViewModel
            }

            if (findRecommendationModel != null) return@launchCatchError

            val homeRecommendationFeedViewModel = HomeRecommendationFeedViewModel()
            homeRecommendationFeedViewModel.feedTabModel = homeRecommendationTabs
            homeRecommendationFeedViewModel.isNewData = true
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findLoadingModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findRetryModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_ADD, homeRecommendationFeedViewModel))

        }){
            val visitableMutableList: MutableList<Visitable<*>> = mutableListOf()

            val findLoadingModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeLoadingMoreModel
            }
            val findRetryModel = _homeLiveData.value?.list?.find {
                visitable -> visitable is HomeRetryModel
            }
            visitableMutableList.add(HomeRetryModel())

            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findLoadingModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_DELETE, findRetryModel as HomeVisitable?))
            updateWidget(UpdateLiveDataModel(ACTION_ADD, HomeRetryModel()))
        }
    }

// ============================================================================================
// ================================ Live Data Controller ======================================
// ============================================================================================

    private suspend fun updateChannel(channel: Channel<UpdateLiveDataModel>){
        for(data in channel){
            val newList = _homeLiveData.value?.list?.toMutableList()
            data.visitable?.let { homeVisitable ->
                if(newList != null && newList.size >  data.position) {
                    when (data.action) {
                        ACTION_ADD -> newList.add(homeVisitable)
                        ACTION_UPDATE -> {
                            if (data.position != -1 && newList.isNotEmpty() && newList.size > data.position && newList[data.position]::class.java == homeVisitable::class.java) {
                                newList[data.position] = homeVisitable
                            } else {
                                newList.withIndex().find { it::class.java == homeVisitable::class.java }?.let {
                                    newList[it.index] = homeVisitable
                                }
                            }
                        }
                        ACTION_DELETE -> newList.remove(homeVisitable)
                    }
                    withContext(homeDispatcher.ui()) {
                        _homeLiveData.value = _homeLiveData.value?.copy(list = newList)
                    }
                }
            }
            data.homeData?.let { homeData ->
                if(data.action == ACTION_UPDATE_HOME_DATA){
                    var homeDataModel = evaluateGeolocationComponent(homeData)
                    homeDataModel = evaluateAvailableComponent(homeDataModel)

                    withContext(homeDispatcher.ui()) {
                        _homeLiveData.value = homeDataModel
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

    private fun mapToHomeHeaderWalletAction(walletBalanceModel: WalletBalanceModel): HomeHeaderWalletAction? {
        val data = HomeHeaderWalletAction()
        data.isLinked = walletBalanceModel.link
        data.balance = walletBalanceModel.balance
        data.labelTitle = walletBalanceModel.titleText
        data.appLinkBalance = walletBalanceModel.applinks
        data.labelActionButton = walletBalanceModel.actionBalanceModel?.labelAction ?: ""
        data.isVisibleActionButton = (walletBalanceModel.actionBalanceModel?.visibility == "1")
        data.appLinkActionButton = walletBalanceModel.actionBalanceModel?.applinks ?: ""
        data.abTags = if (walletBalanceModel.abTags == null) ArrayList() else walletBalanceModel.abTags
        data.pointBalance = walletBalanceModel.pointBalance
        data.rawPointBalance = walletBalanceModel.rawPointBalance
        data.cashBalance = walletBalanceModel.cashBalance
        data.rawCashBalance = walletBalanceModel.rawCashBalance
        data.walletType = walletBalanceModel.walletType
        data.isShowAnnouncement = walletBalanceModel.isShowAnnouncement
        return data
    }

    private fun convertPopularKeywordDataList(list: List<HomeWidget.PopularKeyword>): MutableList<PopularKeywordViewModel> {
        val dataList: MutableList<PopularKeywordViewModel> = mutableListOf()
        for (pojo in list) {
            dataList.add(PopularKeywordViewModel(pojo.url, pojo.imageUrl, pojo.keyword, pojo.productCount))
        }
        return dataList
    }

}