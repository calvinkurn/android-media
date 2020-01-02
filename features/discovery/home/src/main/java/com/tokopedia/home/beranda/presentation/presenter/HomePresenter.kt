package com.tokopedia.home.beranda.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.dynamicbanner.domain.PlayCardHomeUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.data.usecase.PlayUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.helper.map
import com.tokopedia.home.beranda.presentation.view.HomeContract
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.subscriber.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
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
                    private val playUseCase: PlayUseCase,
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


    private var isCache = true

    val homeLiveData: LiveData<HomeViewModel> = homeUseCase.getHomeData().map {
        if(fetchFirstData) fetchFirstData = false
        homeDataMapper.mapToHomeViewModel(it, isCache)
    }

    private val _updateNetworkLiveData = MutableLiveData<Resource<Any>>()
    val updateNetworkLiveData: LiveData<Resource<Any>> get() = _updateNetworkLiveData

    private var currentCursor = ""
    private lateinit var headerViewModel: HeaderViewModel
    private var fetchFirstData = false
    private val REQUEST_DELAY_HOME_DATA: Long = TimeUnit.MINUTES.toMillis(3) // 3 minutes
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
                if(isViewAttached){
                    view?.onErrorGetReviewData()
                }
            }

            override fun onNext(suggestedProductReview: SuggestedProductReview?) {
                if(isViewAttached) {
                    view?.onSuccessGetReviewData(suggestedProductReview)
                }
            }
        })
    }

    override fun getHomeData() {
        initHeaderViewModelData()
        _updateNetworkLiveData.value = Resource.loading(null)
        lastRequestTimeSendGeolocation = System.currentTimeMillis()
        launchCatchError(coroutineContext, block = {
            val resource = homeUseCase.updateHomeData()
            isCache = false
            _updateNetworkLiveData.value = resource
        }){
            Timber.tag(HomePresenter::class.java.name).e(it)
            _updateNetworkLiveData.value = Resource.error(Throwable(), null)
        }
    }

    override fun updateHomeData() {
        lastRequestTimeHomeData = System.currentTimeMillis()
        launchCatchError(coroutineContext, block = {
            val resource = homeUseCase.updateHomeData()
            isCache = false
            _updateNetworkLiveData.value = resource
        }){
            Timber.tag(HomePresenter::class.java.name).e(it)
            _updateNetworkLiveData.value = Resource.error(Throwable(), null)
        }
    }

    private fun initHeaderViewModelData() {
        if (userSession.isLoggedIn) {
            getHeaderViewModel().isPendingTokocashChecked = false
        }
    }

    fun setCache(isCache: Boolean){
        this.isCache = isCache
    }

    override fun updateHeaderTokoCashData(homeHeaderWalletAction: HomeHeaderWalletAction) {
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().homeHeaderWalletActionData = homeHeaderWalletAction
        view?.updateHeaderItem(getHeaderViewModel())
    }

    override fun showPopUpIntroWalletOvo(applinkActivation: String) {
        view?.showPopupIntroOvo(applinkActivation)
    }

    override fun onHeaderTokocashError() {
        getHeaderViewModel().setWalletDataError()
        getHeaderViewModel().homeHeaderWalletActionData = null
        view?.updateHeaderItem(getHeaderViewModel())
    }

    override fun updateHeaderTokoCashPendingData(cashBackData: CashBackData) {
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().cashBackData = cashBackData
        getHeaderViewModel().isPendingTokocashChecked = true
        view?.updateHeaderItem(getHeaderViewModel())
    }

    override fun onHeaderTokopointError() {
        getHeaderViewModel().setTokoPointDataError()
        getHeaderViewModel().tokoPointDrawerData = null
        getHeaderViewModel().tokopointsDrawerHomeData = null
        view?.updateHeaderItem(getHeaderViewModel())
    }

    override fun onRefreshTokoPoint() {
        getHeaderViewModel().setTokoPointDataSuccess()
        getHeaderViewModel().tokoPointDrawerData = null
        getHeaderViewModel().tokopointsDrawerHomeData = null
        view?.updateHeaderItem(getHeaderViewModel())
        getTokopoint()
    }

    override fun onRefreshTokoCash() {
        if (!userSession.isLoggedIn) return
        getHeaderViewModel().setWalletDataSuccess()
        getHeaderViewModel().homeHeaderWalletActionData = null
        view?.updateHeaderItem(getHeaderViewModel())
        getTokocashBalance()
    }

    override fun getShopInfo(url: String, shopDomain: String) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view?.openWebViewURL(url)
                }
            }

            override fun onNext(shopInfo: ShopInfo) {
                if (shopInfo.info != null) {
                    view?.startShopInfo(shopInfo.info.shopId)
                } else {
                    view?.openWebViewURL(url)
                }
            }
        })
    }

    override fun openProductPageIfValid(url: String, shopDomain: String) {
        getShopInfoByDomainUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(shopDomain), object : Subscriber<ShopInfo>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view?.openWebViewURL(url)
                }
            }

            override fun onNext(shopInfo: ShopInfo) {
                if (shopInfo.info != null) {
                    view?.startDeeplinkShopInfo(url)
                } else {
                    view?.openWebViewURL(url)
                }
            }
        })
    }

    override fun getHeaderData(initialStart: Boolean) {
        if (!userSession.isLoggedIn) return
        if (initialStart && headerViewModel != null) {
            if (headerViewModel.homeHeaderWalletActionData == null) getTokocashBalance()
            if (headerViewModel.tokopointsDrawerHomeData == null) getTokopoint()
        } else {
            getTokocashBalance()
            getTokopoint()
        }
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
        private const val CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED"
        private var lastRequestTimeHomeData: Long = 0
        private var lastRequestTimeSendGeolocation: Long = 0
        const val FLAG_FROM_NETWORK = 99
        const val FLAG_FROM_CACHE = 98
    }

    init {
        compositeSubscription = CompositeSubscription()
        subscription = Subscriptions.empty()
    }
}
