package com.tokopedia.home.beranda.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.helper.HomeLiveData
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.presentation.view.HomeContract
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel
import com.tokopedia.home.beranda.presentation.view.subscriber.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.listener.ImpressionListener
import com.tokopedia.dynamicbanner.domain.PlayCardHomeUseCase
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeReviewSuggestedUseCase
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.*
import retrofit2.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomePresenter(private val userSession: UserSessionInterface,
                    private val getShopInfoByDomainUseCase: GetShopInfoByDomainUseCase,
                    private val coroutineDispatcher: CoroutineDispatcher) :
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
    lateinit var homeRepository: HomeRepository

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

    private val _homeData = HomeLiveData(HomeViewModel())
    val homeLiveData: LiveData<HomeViewModel> get() = _homeData
    private var homeSource: LiveData<Resource<HomeViewModel>> = MutableLiveData()

    private var currentCursor = ""
    private lateinit var headerViewModel: HeaderViewModel
    private var fetchFirstData = false
    private val REQUEST_DELAY_HOME_DATA: Long = 180000 // 3 minutes
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
            updateHomeData()
        }
        if (needSendGeolocationRequest && view!!.hasGeolocationPermission()) {
            view!!.detectAndSendLocation()
        }
        tokocashBalance
        tokopoint
        searchHint
        getStickyContent()
    }

    fun sendGeolocationData() {
        sendGeolocationInfoUseCase!!.createObservable(RequestParams.EMPTY)
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

    override fun updateHomeData() {
        launch(coroutineContext){
            _homeData.removeSource(homeSource)
            homeSource = homeRepository.getHomeData()
            _homeData.addSource(homeSource){
                it.data?.let {data ->
                    if(it.status == Resource.Status.SUCCESS){
                        view?.configureHomeFlag(data.homeFlag)
                        view?.updateListOnResume(ArrayList(data.list))
                        view?.addImpressionToTrackingQueue(ArrayList(data.list))
                        lastRequestTimeHomeData = System.currentTimeMillis()
                    }
                }

            }
        }
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
                view?.onErrorGetReviewData()
            }

            override fun onNext(suggestedProductReview: SuggestedProductReview?) {
                view?.onSuccessGetReviewData(suggestedProductReview)
            }
        })
    }

    override fun getHomeData() {
        initHeaderViewModelData()
        launch(coroutineContext){
            _homeData.removeSource(homeSource)
            homeSource = homeRepository.getHomeData()
            _homeData.addSource(homeSource){
                when(it.status){
                    Resource.Status.LOADING -> view?.showLoading()
                    Resource.Status.SUCCESS, Resource.Status.CACHE -> {
                        view?.hideLoading()
                        it.data?.let {data ->
                            _homeData.value = data
                            if (data.list.size > VISITABLE_SIZE_WITH_DEFAULT_BANNER) {
                                val flag = if(it.status == Resource.Status.SUCCESS) FLAG_FROM_NETWORK else FLAG_FROM_CACHE
                                view?.configureHomeFlag(data.homeFlag)
                                view?.setItems(ArrayList(data.list), flag)
                                if(flag == FLAG_FROM_NETWORK) view?.addImpressionToTrackingQueue(ArrayList<HomeVisitable<*>>(data.list))
                                if (isDataValid(ArrayList<HomeVisitable<*>>(data.list))) {
                                    view?.removeNetworkError()
                                } else {
                                    showNetworkError()
                                }
                                if(it.status == Resource.Status.SUCCESS){
                                    lastRequestTimeHomeData = System.currentTimeMillis()
                                } else { }
                            } else {
                                view?.showNetworkError(com.tokopedia.network.ErrorHandler.getErrorMessage(Throwable()))
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        it.data?.let {data ->
                            view?.configureHomeFlag(data.homeFlag)
                            view?.setItems(ArrayList(data.list), FLAG_FROM_CACHE)
                        }
                        view?.hideLoading()
                        view?.showNetworkError(com.tokopedia.network.ErrorHandler.getErrorMessage(it.error))

                    }
                }

            }
        }
//        val homeLocalSubscriber = createHomeDataSubscriber()
//        homeLocalSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_CACHE)
//        subscription = homeUseCase.getExecuteObservable(RequestParams.EMPTY)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io()) //                .doOnNext(homeViewModel ->
//                {
//                    HomeDataSubscriber homeNetworkSubscriber = createHomeDataSubscriber();
//                    homeNetworkSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_NETWORK);
//                    compositeSubscription.add(getDataFromNetwork().subscribe(homeNetworkSubscriber));
//                })
//                .observeOn(AndroidSchedulers.mainThread()) //                .onErrorResumeNext(throwable -> {
//                    homeLocalSubscriber.setFlag(HomeDataSubscriber.FLAG_FROM_NETWORK);
//                    return getDataFromNetwork();
//                })
//                .subscribe(homeLocalSubscriber)
//        compositeSubscription.add(subscription)
    }

    private fun initHeaderViewModelData() {
        if (userSession.isLoggedIn) {
            getHeaderViewModel().isPendingTokocashChecked = false
        }
    }

//    private fun createHomeDataSubscriber(): HomeDataSubscriber {
//        return HomeDataSubscriber(this)
//    }

    override fun updateHeaderTokoCashData(homeHeaderWalletAction: HomeHeaderWalletAction) {
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().homeHeaderWalletActionData = homeHeaderWalletAction
        view!!.updateHeaderItem(getHeaderViewModel())
    }

    override fun showPopUpIntroWalletOvo(applinkActivation: String) {
        view!!.showPopupIntroOvo(applinkActivation)
    }

    override fun onHeaderTokocashError() {
        getHeaderViewModel().setWalletDataError()
        getHeaderViewModel().homeHeaderWalletActionData = null
        view!!.updateHeaderItem(getHeaderViewModel())
    }

    override fun updateHeaderTokoCashPendingData(cashBackData: CashBackData) {
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().cashBackData = cashBackData
        getHeaderViewModel().isPendingTokocashChecked = true
        view!!.updateHeaderItem(getHeaderViewModel())
    }

    override fun onHeaderTokopointError() {
        getHeaderViewModel().setTokoPointDataError()
        getHeaderViewModel().tokoPointDrawerData = null
        getHeaderViewModel().tokopointsDrawerHomeData = null
        view!!.updateHeaderItem(getHeaderViewModel())
    }

    override fun onRefreshTokoPoint() {
        getHeaderViewModel().setTokoPointDataSuccess()
        getHeaderViewModel().tokoPointDrawerData = null
        getHeaderViewModel().tokopointsDrawerHomeData = null
        view!!.updateHeaderItem(getHeaderViewModel())
        tokopoint
    }

    override fun onRefreshTokoCash() {
        if (!userSession.isLoggedIn) return
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().homeHeaderWalletActionData = null
        view!!.updateHeaderItem(getHeaderViewModel())
        tokocashBalance
    }

    override fun getShopInfo(url: String, shopDomain: String) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view!!.openWebViewURL(url)
                }
            }

            override fun onNext(shopInfo: ShopInfo) {
                if (shopInfo.info != null) {
                    view!!.startShopInfo(shopInfo.info.shopId)
                } else {
                    view!!.openWebViewURL(url)
                }
            }
        })
    }

    override fun openProductPageIfValid(url: String, shopDomain: String) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view!!.openWebViewURL(url)
                }
            }

            override fun onNext(shopInfo: ShopInfo) {
                if (shopInfo.info != null) {
                    view!!.startDeeplinkShopInfo(url)
                } else {
                    view!!.openWebViewURL(url)
                }
            }
        })
    }

    override fun getHeaderData(initialStart: Boolean) {
        if (!userSession.isLoggedIn) return
        if (initialStart && headerViewModel != null) {
            if (headerViewModel.homeHeaderWalletActionData == null) tokocashBalance
            if (headerViewModel.tokopointsDrawerHomeData == null) tokopoint
        } else {
            tokocashBalance
            tokopoint
        }
    }

    fun setCursor(currentCursor: String) {
        this.currentCursor = currentCursor
    }

    fun hasNextPageFeed(): Boolean {
        return CURSOR_NO_NEXT_PAGE_FEED != currentCursor
    }

    private fun getHeaderViewModel(): HeaderViewModel {
        if (!this::headerViewModel.isInitialized) {
            headerViewModel = HeaderViewModel()
        }
        headerViewModel.isUserLogin = userSession.isLoggedIn
        return headerViewModel
    }

    fun setFetchFirstData(fetchFirstData: Boolean) {
        this.fetchFirstData = fetchFirstData
    }

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    fun showNetworkError() {
        view?.showNetworkError()
    }

//    internal class HomeDataSubscriber(var homePresenter: HomePresenter?) : Subscriber<HomeViewModel>() {
//        private var repositoryFlag = 0
//        fun setFlag(flag: Int) {
//            repositoryFlag = flag
//        }
//
//        override fun onStart() {
//            if (homePresenter != null && homePresenter!!.isViewAttached) {
//                homePresenter!!.view!!.showLoading()
//            }
//        }
//
//        override fun onCompleted() {
//            if (homePresenter != null && homePresenter!!.isViewAttached) {
//                homePresenter!!.view!!.hideLoading()
//                homePresenter!!.setFetchFirstData(false)
//                homePresenter = null
//            }
//        }
//
//        override fun onError(e: Throwable) {
//
//        }
//
//        override fun onNext(homeViewModel: HomeViewModel) {
//
//        }
//
//        companion object {
//            var FLAG_FROM_NETWORK = 99
//            var FLAG_FROM_CACHE = 98
//
//        }
//
//    }

    private fun isDataValid(visitables: List<HomeVisitable<*>>): Boolean {
        return containsInstance(visitables, BannerViewModel::class.java)
    }

    fun <E> containsInstance(list: List<E>, clazz: Class<out E>): Boolean {
        for (e in list) {
            if (clazz.isInstance(e)) {
                return true
            }
        }
        return false
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
    private val tokocashBalance: Unit
        private get() {
            compositeSubscription.add(getWalletBalanceUseCase?.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(TokocashHomeSubscriber(this)))
        }

    val tokocashPendingBalance: Unit
        get() {
            compositeSubscription.add(getPendingCasbackUseCase?.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(PendingCashbackHomeSubscriber(this)))
        }

    val tokopoint: Unit
        get() {
            val graphqlResponseObservable = tokopointsObservable
            if (graphqlResponseObservable != null) {
                compositeSubscription.add(graphqlResponseObservable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(TokopointHomeSubscriber(this)))
            }
        }

    val searchHint: Unit
        get() {
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
        getHeaderViewModel().setTokoPointDataSuccess()
        getHeaderViewModel().tokopointsDrawerHomeData = tokopointsDrawerHomeData?.tokopointsDrawer
        view?.updateHeaderItem(headerViewModel)
    }

    override fun updateKeywordSearch(keywordSearchData: KeywordSearchData) {
        view?.setHint(keywordSearchData.searchData)
    }

    override fun getFeedTabData() {
        getFeedTabUseCase?.execute(GetFeedTabsSubscriber(view))
    }

    private fun mappingHomeFeedModel(feedTabModelList: List<FeedTabModel>): Visitable<*> {
        val feedViewModel = HomeRecommendationFeedViewModel()
        feedViewModel.feedTabModel = feedTabModelList
        return feedViewModel
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

    override fun getPlayBanner(adapterPosition: Int){
        playCardHomeUseCase.execute(
                onSuccess = {
                    view?.setPlayContentBanner(it, adapterPosition)
                },
                onError = {}
        )

    }

    companion object {
        private val TAG = HomePresenter::class.java.simpleName
        private const val CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED"
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        private const val VISITABLE_SIZE_WITH_DEFAULT_BANNER = 1
        const val FLAG_FROM_NETWORK = 99
        const val FLAG_FROM_CACHE = 98
    }

    init {
        compositeSubscription = CompositeSubscription()
        subscription = Subscriptions.empty()
    }
}
