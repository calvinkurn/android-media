package com.tokopedia.home.beranda.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.dynamicbanner.domain.PlayCardHomeUseCase
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.presentation.view.HomeContract
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel
import com.tokopedia.home.beranda.presentation.view.subscriber.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.listener.ImpressionListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import retrofit2.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomePresenter(private val userSession: UserSessionInterface,
                    private val getShopInfoByDomainUseCase: GetShopInfoByDomainUseCase,
                    private val coroutineDispatcher: CoroutineDispatcher,
                    private val homeUseCase: HomeUseCase,
                    private val homeDataMapper: HomeDataMapper) :
        BaseDaggerPresenter<HomeContract.View?>(), HomeContract.Presenter, CoroutineScope {

    protected var compositeSubscription: CompositeSubscription
    protected var subscription: Subscription?
    private val masterJob = SupervisorJob()

    @Inject
    lateinit var getFeedTabUseCase: GetFeedTabUseCase

    @Inject
    lateinit var sendGeolocationInfoUseCase: SendGeolocationInfoUseCase

    @Inject
    lateinit var getWalletBalanceUseCase: GetWalletBalanceUseCase

    @Inject
    lateinit var getPendingCasbackUseCase: GetPendingCasbackUseCase

    @Inject
    lateinit var getHomeTokopointsDataUseCaseLazy: Lazy<GetHomeTokopointsDataUseCase>

    @Inject
    lateinit var getKeywordSearchUseCaseLazy: Lazy<GetKeywordSearchUseCase>

    @Inject
    lateinit var stickyLoginUseCase: StickyLoginUseCase

    @Inject
    lateinit var getHomeReviewSuggestedUseCase: GetHomeReviewSuggestedUseCase

    @Inject
    lateinit var dismissHomeReviewUseCase: DismissHomeReviewUseCase

    @Inject
    lateinit var playCardHomeUseCase: PlayCardHomeUseCase

    val homeFlowData: Flow<HomeViewModel?> = homeUseCase.getHomeData()

    val homeLiveData: LiveData<HomeViewModel>
    get() = _homeLiveData

    val _homeLiveData: MutableLiveData<HomeViewModel> = MutableLiveData()

    private val _updateNetworkLiveData = MutableLiveData<Resource<Any>>()
    val updateNetworkLiveData: LiveData<Resource<Any>> get() = _updateNetworkLiveData

    private var currentCursor = ""
    private var fetchFirstData = false
    private val REQUEST_DELAY_HOME_DATA: Long = TimeUnit.MINUTES.toMillis(10) // 10 minutes
    private val REQUEST_DELAY_SEND_GEOLOCATION = TimeUnit.HOURS.toMillis(1) // 1 hour

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcher + masterJob

    override fun detachView() {
        super.detachView()
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
    }

    override fun onFirstLaunch() {
        fetchFirstData = true
    }

    override fun onResume() {
        val needRefresh = lastRequestTimeHomeData + REQUEST_DELAY_HOME_DATA < System.currentTimeMillis()
        val needSendGeolocationRequest = lastRequestTimeHomeData + REQUEST_DELAY_SEND_GEOLOCATION < System.currentTimeMillis()
        if (isViewAttached && !fetchFirstData && needRefresh) {
            refreshHomeData()
        }
        if (needSendGeolocationRequest && view?.hasGeolocationPermission() == true) {
            view?.detectAndSendLocation()
        }
        getTokocashBalance()
        getTokopoint()
        searchHint()
        getStickyContent()
    }

    fun sendGeolocationData() {
        sendGeolocationInfoUseCase?.createObservable(RequestParams.EMPTY)
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

    override fun dismissReview() {
        view?.onSuccessDismissReview()

        dismissHomeReviewUseCase.execute(RequestParams.EMPTY, object: Subscriber<String>(){
            override fun onNext(t: String?) {}

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {}

        })
    }

    override fun getSuggestedReview() {
        getHomeReviewSuggestedUseCase.execute(RequestParams.EMPTY, object: Subscriber<SuggestedProductReview>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                removeSuggestedReview()
            }

            override fun onNext(suggestedProductReview: SuggestedProductReview?) {
                suggestedProductReview?.let {
                    if (!viewNeedToShowGeolocationComponent()) {
                        insertSuggestedReview(it)
                    }
                }
            }
        })
    }

    override fun refreshHomeData() {
        initHeaderViewModelData()

        lastRequestTimeHomeData = System.currentTimeMillis()
        launchCatchError(coroutineContext, block = {
            val resource = homeUseCase.updateHomeData()
            _updateNetworkLiveData.value = resource
        }){
            Timber.tag(HomePresenter::class.java.name).e(it)
            _updateNetworkLiveData.value = Resource.error(Throwable(), null)
        }
    }

    private fun initHeaderViewModelData() {
        //check if headerviewmodel is exist
        val headerViewModel = _homeLiveData.value?.list?.find {
            visitable-> visitable is HeaderViewModel }

        headerViewModel?.let {
            if (headerViewModel is HeaderViewModel) {
                updateHeaderViewModel(
                        null,
                        null,
                        null,
                        null,
                        false,
                        null,
                        null
                )
                return
            }
        }
    }

    override fun updateHeaderTokoCashData(homeHeaderWalletAction: HomeHeaderWalletAction) {
        updateHeaderViewModel(
                isWalletDataError = false,
                homeHeaderWalletAction = homeHeaderWalletAction
        )
    }

    override fun showPopUpIntroWalletOvo(applinkActivation: String) {
        view?.showPopupIntroOvo(applinkActivation)
    }

    override fun onHeaderTokocashError() {
        updateHeaderViewModel(
                isWalletDataError = true,
                homeHeaderWalletAction = null
        )
    }

    override fun updateHeaderTokoCashPendingData(cashBackData: CashBackData) {
        updateHeaderViewModel(
                isWalletDataError = false,
                cashBackData = cashBackData,
                isPendingTokocashChecked = true
        )
    }

    override fun onHeaderTokopointError() {
        updateHeaderViewModel(
                isTokoPointDataError = true,
                tokopointsDrawer = null,
                tokopointHomeDrawerData = null
        )
    }

    override fun onRefreshTokoPoint() {
        updateHeaderViewModel(
                isTokoPointDataError = false,
                tokopointsDrawer = null,
                tokopointHomeDrawerData = null
        )
        getTokopoint()
    }

    override fun onRefreshTokoCash() {
        if (!userSession.isLoggedIn) return
        updateHeaderViewModel(
                isWalletDataError = false,
                homeHeaderWalletAction = null
        )
        getTokocashBalance()
    }

    override fun getHeaderData() {
        if (!userSession.isLoggedIn) return
        getTokocashBalance()
        getTokopoint()
    }

    fun hasNextPageFeed(): Boolean {
        return CURSOR_NO_NEXT_PAGE_FEED != currentCursor
    }

    private fun updateHeaderViewModel(tokopointsDrawer: TokopointsDrawer? = null,
                                      homeHeaderWalletAction: HomeHeaderWalletAction? = null,
                                      tokopointHomeDrawerData: TokopointHomeDrawerData? = null,
                                      cashBackData: CashBackData? = null,
                                      isPendingTokocashChecked: Boolean? = null,
                                      isWalletDataError: Boolean? = null,
                                      isTokoPointDataError: Boolean? = null) {

        val currentHeaderViewModel = _homeLiveData.value?.list?.find {
            visitable-> visitable is HeaderViewModel }

        if (currentHeaderViewModel is HeaderViewModel) {
            val headerViewModel = currentHeaderViewModel.copy()

            val currentPosition = _homeLiveData.value?.list?.indexOf(headerViewModel)?:-1

            headerViewModel?.let {
                tokopointsDrawer?.let { headerViewModel.tokopointsDrawerHomeData = it }
                homeHeaderWalletAction?.let { headerViewModel.homeHeaderWalletActionData = it }
                cashBackData?.let { headerViewModel.cashBackData = it }
                tokopointHomeDrawerData?.let { headerViewModel.tokoPointDrawerData = it }
                isPendingTokocashChecked?.let { headerViewModel.isPendingTokocashChecked = it }
                isWalletDataError?.let { headerViewModel.isWalletDataError = it }
                isTokoPointDataError?.let { headerViewModel.isTokoPointDataError = it }
                headerViewModel.isUserLogin = userSession.isLoggedIn

                val homeListWithNewHeader = _homeLiveData.value?.list?.toMutableList()
                homeListWithNewHeader?.let {
                    it[currentPosition] = headerViewModel
                    val newHomeViewModel = _homeLiveData.value?.copy(
                            list = it
                    )
                    _homeLiveData.value = newHomeViewModel
                }
            }
        }
    }

    override fun onDestroy() {
        unsubscribeAllUseCase()
    }

    private fun unsubscribeAllUseCase() {
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
        if (getFeedTabUseCase != null) {
            getFeedTabUseCase?.unsubscribe()
        }
        if (subscription != null) {
            subscription?.unsubscribe()
        }
        if (stickyLoginUseCase != null) {
            stickyLoginUseCase?.cancelJobs()
        }
    }

    /**
     * Tokocash & Tokopoint
     */
    private fun getTokocashBalance() {
            compositeSubscription.add(getWalletBalanceUseCase?.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(TokocashHomeSubscriber(this)))
        }

    fun getTokocashPendingBalance(){
        compositeSubscription.add(getPendingCasbackUseCase?.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(PendingCashbackHomeSubscriber(this)))
    }

    fun getTokopoint(){
        val graphqlResponseObservable = tokopointsObservable
        if (graphqlResponseObservable != null) {
            compositeSubscription.add(graphqlResponseObservable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(TokopointHomeSubscriber(this)))
        }
    }

    fun searchHint(){
            val graphqlResponseObservable = keywordSearchObservable
            if (graphqlResponseObservable != null) {
                compositeSubscription.add(graphqlResponseObservable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(KeywordSearchHomeSubscriber(this)))
            }
        }

    private val tokopointsObservable: Observable<GraphqlResponse>?
        private get() {
            if (getHomeTokopointsDataUseCaseLazy != null) {
                val getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCaseLazy.get()
                getHomeTokopointsDataUseCase.clearRequest()
                getHomeTokopointsDataUseCase.addRequest(getHomeTokopointsDataUseCase.request)
                return getHomeTokopointsDataUseCase.getExecuteObservable(RequestParams.EMPTY)
            }
            return null
        }

    private val keywordSearchObservable: Observable<GraphqlResponse>?
        private get() {
            if (getKeywordSearchUseCaseLazy != null) {
                val getKeywordSearchUseCase = getKeywordSearchUseCaseLazy?.get()
                getKeywordSearchUseCase.clearRequest()
                getKeywordSearchUseCase.addRequest(getKeywordSearchUseCase.getRequest())
                return getKeywordSearchUseCase.getExecuteObservable(RequestParams.EMPTY)
            }
            return null
        }

    override fun hitBannerImpression(slidesModel: BannerSlidesModel) {
        if (!slidesModel.isImpressed
                && slidesModel.topadsViewUrl != null && !slidesModel.topadsViewUrl.isEmpty()) {
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

    override fun onBannerClicked(slidesModel: BannerSlidesModel) {
        if (slidesModel.redirectUrl != null && !slidesModel.redirectUrl.isEmpty()) {
            ImpresionTask().execute(slidesModel.redirectUrl)
        }
    }

    override fun updateHeaderTokoPointData(tokopointsDrawerHomeData: TokopointsDrawerHomeData) {
        updateHeaderViewModel(
                isTokoPointDataError = false,
                tokopointsDrawer = tokopointsDrawerHomeData.tokopointsDrawer
        )
    }

    override fun updateKeywordSearch(keywordSearchData: KeywordSearchData) {
        view?.setHint(keywordSearchData.searchData)
    }

    override fun getFeedTabData() {
        val currentData = _homeLiveData.value
        val visitableMutableList: MutableList<Visitable<*>> = currentData?.list?.toMutableList()?: mutableListOf()
        val findRetryModel = _homeLiveData.value?.list?.find {
            visitable -> visitable is HomeRetryModel
        }
        val findLoadingModel = _homeLiveData.value?.list?.find {
            visitable -> visitable is HomeLoadingMoreModel
        }
        visitableMutableList.remove(findLoadingModel)
        visitableMutableList.remove(findRetryModel)
        visitableMutableList.add(HomeLoadingMoreModel())
        val newHomeViewModel = currentData?.copy(
                list = visitableMutableList)

        _homeLiveData.value = newHomeViewModel

        getFeedTabUseCase.execute(object: Subscriber<List<FeedTabModel>>() {
            override fun onNext(t: List<FeedTabModel>?) {
                val currentData = _homeLiveData.value
                val visitableMutableList: MutableList<Visitable<*>> = currentData?.list?.toMutableList()?: mutableListOf()
                val findLoadingModel = _homeLiveData.value?.list?.find {
                    visitable -> visitable is HomeLoadingMoreModel
                }
                val findRetryModel = _homeLiveData.value?.list?.find {
                    visitable -> visitable is HomeRetryModel
                }

                visitableMutableList.remove(findLoadingModel)
                visitableMutableList.remove(findRetryModel)

                val homeRecommendationFeedViewModel = HomeRecommendationFeedViewModel()
                homeRecommendationFeedViewModel.feedTabModel = t
                homeRecommendationFeedViewModel.isNewData = true
                visitableMutableList.add(homeRecommendationFeedViewModel)

                val newHomeViewModel = currentData?.copy(
                        list = visitableMutableList)

                _homeLiveData.value = newHomeViewModel
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                val currentData = _homeLiveData.value
                val visitableMutableList: MutableList<Visitable<*>> = currentData?.list?.toMutableList()?: mutableListOf()

                val findLoadingModel = _homeLiveData.value?.list?.find {
                    visitable -> visitable is HomeLoadingMoreModel
                }
                val findRetryModel = _homeLiveData.value?.list?.find {
                    visitable -> visitable is HomeRetryModel
                }

                visitableMutableList.remove(findLoadingModel)
                visitableMutableList.remove(findRetryModel)
                visitableMutableList.add(HomeRetryModel())

                val newHomeViewModel = currentData?.copy(
                        list = visitableMutableList)

                _homeLiveData.value = newHomeViewModel
            }

        })
    }

    override fun getStickyContent() {
        stickyLoginUseCase?.setParams(StickyLoginConstant.Page.HOME)
        stickyLoginUseCase?.execute(
                { (response) ->
                    for (ticker in response.tickers) {
                        if (ticker.layout == StickyLoginConstant.LAYOUT_FLOATING) {
                            view?.setStickyContent(ticker)
                            return@execute
                        }
                    }
                    view?.hideStickyLogin()
                }
        ) { throwable: Throwable? ->
            view?.hideStickyLogin()
        }
    }

    fun getPlayBanner(){
        playCardHomeUseCase.execute(
                onSuccess = {
                    onPlayBannerSuccess(it)
                },
                onError = {}
        )
    }

    fun onPlayBannerSuccess(playCardHome: PlayCardHome) {
        val currentData = _homeLiveData.value
        val visitableMutableList: MutableList<Visitable<*>> = currentData?.list?.toMutableList()?: mutableListOf()

        val findPlayCardModel = _homeLiveData.value?.list?.find { visitable ->
            visitable is PlayCardViewModel
        }
        val indexOfPlayCardModel = visitableMutableList.indexOf(findPlayCardModel)

        if (findPlayCardModel is PlayCardViewModel) {
            findPlayCardModel.setPlayCardHome(playCardHome)
            visitableMutableList[indexOfPlayCardModel] = findPlayCardModel
            val newHomeViewModel = currentData?.copy(
                    list = visitableMutableList)
            _homeLiveData.value = newHomeViewModel
        }
    }

    private fun viewNeedToShowGeolocationComponent(): Boolean {
        if (isViewAttached) {
            return view?.needToShowGeolocationComponent()?:false
        }
        return false
    }

    private fun insertSuggestedReview(suggestedProductReview: SuggestedProductReview) {
        val findReviewViewModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is ReviewViewModel }
        val currentList = _homeLiveData.value?.list?.toMutableList()
        currentList?.let {
            val indexOfReviewViewModel = currentList.indexOf(findReviewViewModel)
            if (findReviewViewModel is ReviewViewModel) {
                val newFindReviewViewModel = findReviewViewModel.copy(
                        suggestedProductReview = suggestedProductReview
                )
                it[indexOfReviewViewModel] = newFindReviewViewModel

                val newHomeViewModel = _homeLiveData.value?.copy(
                        list = it
                )
                _homeLiveData.value = newHomeViewModel
            }
        }
    }

    fun removeSuggestedReview() {
        val findReviewViewModel =
                _homeLiveData.value?.list?.find { visitable -> visitable is ReviewViewModel }
        val currentList = _homeLiveData.value?.list?.toMutableList()
        currentList?.let {
            if (findReviewViewModel is ReviewViewModel) {
                it.remove(findReviewViewModel)
                val newHomeViewModel = _homeLiveData.value?.copy(
                        list = it
                )
                _homeLiveData.value = newHomeViewModel
            }
        }
    }

    private fun getReviewData() {
        if (viewNeedToShowGeolocationComponent()) {
            removeSuggestedReview()
        } else {
            getSuggestedReview()
        }
    }

    private fun evaluateGeolocationComponent(homeViewModel: HomeViewModel): HomeViewModel {
        if (!viewNeedToShowGeolocationComponent()) {
            val findGeolocationModel =
                    homeViewModel.list.find { visitable -> visitable is GeolocationPromptViewModel }
            val currentList = homeViewModel.list.toMutableList()

            currentList.let {
                it.remove(findGeolocationModel)
                val newHomeViewModel = homeViewModel.copy(list = it)
                return newHomeViewModel
            }
        }
        return homeViewModel
    }

    companion object {
        private const val CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED"
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        const val FLAG_FROM_NETWORK = 99
        const val FLAG_FROM_CACHE = 98
    }

    init {
        compositeSubscription = CompositeSubscription()
        subscription = Subscriptions.empty()
        initHeaderViewModelData()
        launchCatchError(coroutineContext,  block = {
            homeFlowData.collect {
                if (it?.isCache == false) {
                    _homeLiveData.value = evaluateGeolocationComponent(it)
                    evaluateRecommendationSection()
                    getFeedTabData()
                    getHeaderData()
                    getPlayData()
                    getReviewData()
                } else {
                    _homeLiveData.value = it
                }
            }
        }) {
            Timber.tag(HomePresenter::class.java.name).e(it)
            _updateNetworkLiveData.value = Resource.error(Throwable(), null)
        }
        refreshHomeData()
    }

    private fun getPlayData() {
        val detectPlayCardModel = _homeLiveData.value?.list?.find {
            visitable -> visitable is PlayCardViewModel
        }
        detectPlayCardModel?.let {
            getPlayBanner()
        }
    }

    private fun evaluateRecommendationSection() {
        val homeViewModel = _homeLiveData.value
        val detectHomeRecom = homeViewModel?.list?.find { visitable -> visitable is HomeRecommendationFeedViewModel }
        detectHomeRecom?.let {
            val currentList = homeViewModel.list.toMutableList()
            currentList.add(HomeLoadingMoreModel())
            _homeLiveData.value = homeViewModel.copy(list = currentList)
        }
    }

    override fun onCloseGeolocation() {
        val homeViewModel = _homeLiveData.value
        val detectGeolocation = homeViewModel?.list?.find { visitable -> visitable is GeolocationPromptViewModel }
        detectGeolocation?.let {
            val currentList = homeViewModel.list.toMutableList()
            currentList.remove(it)
            _homeLiveData.value = homeViewModel.copy(list = currentList)
        }
    }

    override fun onCloseTicker() {
        val homeViewModel = _homeLiveData.value
        val detectTicker = homeViewModel?.list?.find { visitable -> visitable is TickerViewModel }
        detectTicker?.let {
            val currentList = homeViewModel.list.toMutableList()
            currentList.remove(it)
            _homeLiveData.value = homeViewModel.copy(list = currentList)
        }
    }
}
