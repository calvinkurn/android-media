package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.SAVE_LAST_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.AddToCartParams
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import com.tokopedia.search.result.product.ads.AdsInjector
import com.tokopedia.search.result.product.ads.AdsLowOrganic
import com.tokopedia.search.result.product.banned.BannedProductsPresenterDelegate
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenter
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.RelatedDataView
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.cpm.BannerAdsPresenterDelegate
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterPresenter
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.grid.ProductGridType
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenter
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenterDelegate
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenter
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenter
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenterDelegate
import com.tokopedia.search.result.product.localsearch.EMPTY_LOCAL_SEARCH_RESPONSE_CODE
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.pagination.PaginationImpl
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_MAP_PRODUCT_DATA_VIEW
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_PROCESS_FILTER
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_SHOW_PRODUCT_LIST
import com.tokopedia.search.result.product.performancemonitoring.runCustomMetric
import com.tokopedia.search.result.product.postprocessing.PostProcessingFilter
import com.tokopedia.search.result.product.recommendation.RecommendationPresenterDelegate
import com.tokopedia.search.result.product.reimagine.Reimagine
import com.tokopedia.search.result.product.reimagine.ReimagineDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.responsecode.ResponseCodeImpl
import com.tokopedia.search.result.product.responsecode.ResponseCodeProvider
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenter
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationPresenterDelegate
import com.tokopedia.search.result.product.similarsearch.SimilarSearchOnBoardingPresenterDelegate
import com.tokopedia.search.result.product.suggestion.SuggestionPresenter
import com.tokopedia.search.result.product.ticker.TickerPresenter
import com.tokopedia.search.result.product.visitable.VisitableFactory
import com.tokopedia.search.result.product.visitable.VisitableFactoryFirstPageData
import com.tokopedia.search.result.product.visitable.VisitableFactorySecondPageData
import com.tokopedia.search.result.product.wishlist.WishlistPresenter
import com.tokopedia.search.result.product.wishlist.WishlistPresenterDelegate
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.createSearchProductDefaultQuickFilter
import com.tokopedia.search.utils.getUserId
import com.tokopedia.search.utils.getValueString
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import rx.Observable
import rx.Subscriber
import rx.functions.Action1
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.max
import com.tokopedia.filter.quick.SortFilterItem as SortFilterItemReimagine

@Suppress("LongParameterList")
class ProductListPresenter @Inject constructor(
    @param:Named(SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    private val searchProductFirstPageUseCase: UseCase<SearchProductModel>,
    @param:Named(SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    private val searchProductLoadMoreUseCase: UseCase<SearchProductModel>,
    private val userSession: UserSessionInterface,
    @param:Named(LOCAL_CACHE_NAME)
    private val searchCoachMarkLocalCache: CoachMarkLocalCache,
    @param:Named(GET_DYNAMIC_FILTER_USE_CASE)
    private val getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
    @param:Named(GET_PRODUCT_COUNT_USE_CASE)
    private val getProductCountUseCase: Lazy<UseCase<String>>,
    @param:Named(GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE)
    private val getLocalSearchRecommendationUseCase: Lazy<UseCase<SearchProductModel>>,
    @param:Named(SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE)
    private val getInspirationCarouselChipsUseCase: Lazy<UseCase<InspirationCarouselChipsProductModel>>,
    @param:Named(SAVE_LAST_FILTER_USE_CASE)
    private val saveLastFilterUseCase: Lazy<UseCase<Int>>,
    private val addToCartUseCase: AddToCartUseCase,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val schedulersProvider: SchedulersProvider,
    private val topAdsHeadlineHelper : TopAdsHeadlineHelper,
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    private val chooseAddressDelegate: ChooseAddressPresenterDelegate,
    private val bannerDelegate: BannerPresenterDelegate,
    private val requestParamsGenerator: RequestParamsGenerator,
    private val paginationImpl: PaginationImpl,
    private val lastFilterPresenterDelegate: LastFilterPresenterDelegate,
    private val sameSessionRecommendationPresenterDelegate: SameSessionRecommendationPresenterDelegate,
    private val bannedProductsPresenterDelegate: BannedProductsPresenterDelegate,
    private val inspirationListAtcPresenterDelegate: InspirationListAtcPresenterDelegate,
    private val broadMatchDelegate: BroadMatchPresenterDelegate,
    private val suggestionPresenter: SuggestionPresenter,
    private val tickerPresenter: TickerPresenter,
    private val safeSearchPresenter: SafeSearchPresenter,
    wishlistPresenterDelegate: WishlistPresenterDelegate,
    dynamicFilterModelProvider: DynamicFilterModelProvider,
    bottomSheetFilterPresenter: BottomSheetFilterPresenter,
    private val visitableFactory: VisitableFactory,
    private val inspirationCarouselPresenter: InspirationCarouselPresenterDelegate,
    private val recommendationPresenterDelegate: RecommendationPresenterDelegate,
    private val adsLowOrganic: AdsLowOrganic,
    @Named(SearchConstant.AB_TEST_REMOTE_CONFIG)
    private val remoteConfig: RemoteConfig,
    private val responseCodeImpl: ResponseCodeImpl,
    private val similarSearchOnBoardingPresenterDelegate: SimilarSearchOnBoardingPresenterDelegate,
    private val reimagineDelegate: ReimagineDelegate,
): BaseDaggerPresenter<ProductListSectionContract.View>(),
    ProductListSectionContract.Presenter,
    Pagination by paginationImpl,
    BannerAdsPresenter by BannerAdsPresenterDelegate(topAdsHeadlineHelper),
    DynamicFilterModelProvider by dynamicFilterModelProvider,
    LastFilterPresenter by lastFilterPresenterDelegate,
    InspirationListAtcPresenter by inspirationListAtcPresenterDelegate,
    BroadMatchPresenter by broadMatchDelegate,
    TickerPresenter by tickerPresenter,
    SafeSearchPresenter by safeSearchPresenter,
    WishlistPresenter by wishlistPresenterDelegate,
    BottomSheetFilterPresenter by bottomSheetFilterPresenter,
    InspirationCarouselPresenter by inspirationCarouselPresenter,
    ResponseCodeProvider by responseCodeImpl,
    Reimagine by reimagineDelegate {

    companion object {
        private val generalSearchTrackingRelatedKeywordResponseCodeList = listOf("3", "4", "5", "6")
        private const val QUICK_FILTER_MINIMUM_SIZE = 2
        private val LOCAL_SEARCH_KEY_PARAMS = listOf(
                SearchApiConst.NAVSOURCE,
                SearchApiConst.SRP_PAGE_ID,
                SearchApiConst.SRP_PAGE_TITLE,
        )
        private const val RESPONSE_CODE_RELATED = "3"
        private const val RESPONSE_CODE_SUGGESTION = "6"
        private const val REQUEST_TIMEOUT_RESPONSE_CODE = "15"
    }

    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    private val performanceMonitoring: PageLoadTimePerformanceInterface? =
        performanceMonitoringProvider.get()

    private var enableGlobalNavWidget = true
    private var additionalParams = ""
    private var hasLoadData = false
    private var navSource = ""
    private var pageId = ""
    private var pageTitle = ""
    private var searchRef = ""
    private var dimension90 = ""
    private var autoCompleteApplink = ""
    private var externalReference = ""
    private var productListType = ""
    private var keywordIntention = -1

    private var productList = mutableListOf<Visitable<*>>()
    override val quickFilterList = mutableListOf<Filter>()
    private var threeDotsProductItem: ProductItemDataView? = null
    private var firstProductPositionWithBOELabel = -1
    private var suggestionKeyword = ""
    private var relatedKeyword = ""
    override var pageComponentId: String = ""
        private set
    private val adsInjector = AdsInjector()
    private val postProcessingFilter = PostProcessingFilter()
    private val newCardType by lazy {
        try {
            remoteConfig.getString(RollenceKey.PRODUCT_CARD_EXPERIMENT, "")
        } catch (ignored: Exception) {
            ""
        }
    }


    override fun attachView(view: ProductListSectionContract.View) {
        super.attachView(view)

        chooseAddressDelegate.updateChooseAddress()
    }

    override val userId: String
        get() = getUserId(userSession)

    override val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    //region Load Data / Load More / Recommendations
    override fun clearData() {
        postProcessingFilter.resetCount()
        paginationImpl.clearData()
        adsLowOrganic.clearData()
    }

    override fun onViewCreated() {
        val isFirstActiveTab = view.isFirstActiveTab

        if (isFirstActiveTab && !hasLoadData)
            onViewFirstTimeLaunch()
    }

    private fun onViewFirstTimeLaunch() {
        hasLoadData = true
        view.reloadData()
    }

    override fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean) {
        if (isViewVisible) {
            view.trackScreenAuthenticated()

            if (isViewAdded && !hasLoadData)
                onViewFirstTimeLaunch()

            chooseAddressDelegate.reCheckChooseAddressData(::refreshData)
        }
    }

    override fun loadMoreData(searchParameter: Map<String, Any>) {
        if (hasNextPage())
            loadSearchNextPage(searchParameter)
        else
            adsLowOrganic.loadNextPage(searchParameter, createAdsLowOrganicProductData()) {
                view.updateScrollListener()
                incrementStart()
            }
    }

    private fun loadSearchNextPage(searchParameter: Map<String, Any>) {
        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation()
        else searchProductLoadMore(searchParameter)
    }

    private fun searchProductLoadMore(searchParameter: Map<String, Any>) {
        if (isViewNotAttached) return

        val requestParams = requestParamsGenerator.createInitializeSearchParam(
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )
        requestParamsGenerator.enrichWithRelatedSearchParam(requestParams)
        enrichWithAdditionalParams(requestParams)

        val useCaseRequestParams = requestParamsGenerator.createSearchProductRequestParams(
            requestParams,
            isLocalSearch(),
            view.isAnyFilterActive,
            view.isAnySortActive,
            topAdsHeadlineHelper.seenAds.toString(),
        )

        // Unsubscribe first in case user has slow connection, and the previous loadMoreUseCase has not finished yet.
        searchProductLoadMoreUseCase.unsubscribe()
        searchProductLoadMoreUseCase.execute(useCaseRequestParams, getLoadMoreDataSubscriber(requestParams.parameters))
    }

    private fun enrichWithAdditionalParams(requestParams: RequestParams) {
        val additionalParams = UrlParamUtils.getParamMap(additionalParams)
        requestParams.putAllString(additionalParams)
    }

    private fun getLoadMoreDataSubscriber(searchParameter: Map<String, Any>): Subscriber<SearchProductModel> {
        return object : Subscriber<SearchProductModel>() {
            override fun onStart() {
                loadMoreDataSubscriberOnStart()
            }

            override fun onNext(searchProductModel: SearchProductModel) {
                loadMoreDataSubscriberOnNext(searchParameter, searchProductModel)
            }

            override fun onCompleted() {
                loadMoreDataSubscriberOnComplete()
            }

            override fun onError(error: Throwable?) {
                loadMoreDataSubscriberOnError(searchParameter, error)
            }
        }
    }

    private fun loadMoreDataSubscriberOnStart() {
        if (isViewAttached) incrementStart()
    }

    private fun incrementStart() {
        paginationImpl.incrementStart()
    }

    private fun loadMoreDataSubscriberOnNext(
            searchParameter: Map<String, Any>,
            searchProductModel: SearchProductModel,
    ) {
        if (isViewNotAttached) return

        val productDataView = createProductDataView(searchProductModel)

        additionalParams = productDataView.additionalParams

        if (productDataView.productList.isEmpty()) {
            postProcessingFilter.checkPostProcessingFilter(
                searchProductModel.isPostProcessing,
                searchParameter,
                totalData,
                ::loadMoreData
            ) {
                getViewToProcessEmptyResultDuringLoadMore()
            }
        } else {
            postProcessingFilter.resetCount()
            getViewToShowMoreData(searchParameter, searchProductModel, productDataView)
        }

        paginationImpl.totalData = productDataView.totalData
    }

    private fun createProductDataView(
        searchProductModel: SearchProductModel,
    ): ProductDataView {
        val lastProductItemPosition = view.lastProductItemPositionFromCache
        val keyword = view.queryKey

        val productDataView = ProductViewModelMapper().convertToProductViewModel(
            lastProductItemPosition,
            searchProductModel,
            pageTitle,
            isLocalSearch(),
            dimension90,
            keyword,
            isShowLocalSearchRecommendation(),
            externalReference,
            newCardType,
        )

        saveLastProductItemPositionToCache(lastProductItemPosition, productDataView.productList)

        return productDataView
    }

    private fun saveLastProductItemPositionToCache(
            lastProductItemPosition: Int,
            productItemDataViewList: List<ProductItemDataView>,
    ) {
        val newLastProductPosition = lastProductItemPosition +
                if (productItemDataViewList.isNotEmpty()) productItemDataViewList.size else 0

        view.saveLastProductItemPositionToCache(newLastProductPosition)
    }

    private fun getViewToProcessEmptyResultDuringLoadMore() {
        val list = visitableFactory.createEmptyResultDuringLoadMoreVisitableList(
            responseCode,
            productList,
            isLocalSearch(),
            constructGlobalSearchApplink()
        )

        view.removeLoading()
        view.addProductList(list)
    }

    private fun getViewToShowMoreData(
            searchParameter: Map<String, Any>,
            searchProductModel: SearchProductModel,
            productDataView: ProductDataView,
    ) {
        val loadMoreVisitableList =
                constructVisitableListLoadMore(productDataView, searchProductModel, searchParameter)

        view.removeLoading()
        view.addProductList(loadMoreVisitableList)
        if (hasNextPage()) view.addLoading()
        view.updateScrollListener()
    }

    private fun constructVisitableListLoadMore(
            productDataView: ProductDataView,
            searchProductModel: SearchProductModel,
            searchParameter: Map<String, Any>,
    ): List<Visitable<*>> {
        val productListType = newCardType.ifBlank { searchProductModel.getProductListType() }
        val loadMoreProductList = createProductItemVisitableList(
            productDataView,
            searchParameter,
            productListType,
            searchProductModel.isShowButtonAtc,
        )
        productList.addAll(loadMoreProductList)

        return visitableFactory.createLoadMoreVisitableList(
            VisitableFactorySecondPageData(
                isLocalSearch(),
                responseCode,
                productList,
                searchProductModel,
                externalReference,
                constructGlobalSearchApplink(),
                loadMoreProductList,
            )
        )
    }

    private fun createProductItemVisitableList(
        productDataView: ProductDataView,
        searchParameter: Map<String, Any>,
        productListType: String,
        showButtonAtc: Boolean,
    ): List<Visitable<*>> {
        return if (isHideProductAds(productDataView))
            productDataView.productList
        else
            adsInjector.injectAds(
                productDataView.productList,
                productDataView.adsModel,
                searchParameter,
                dimension90,
                productListType,
                externalReference,
                productDataView.keywordIntention,
                showButtonAtc,
            )
    }

    private fun isHideProductAds(productDataView: ProductDataView) : Boolean {
        return isLocalSearch() || productDataView.isAdvancedNegativeKeywordSearch()
    }

    private fun isLocalSearch() = navSource.isNotEmpty() && pageId.isNotEmpty()

    private fun loadMoreDataSubscriberOnComplete() {
        if (isViewNotAttached) return

        view.hideRefreshLayout()
    }

    private fun loadMoreDataSubscriberOnError(searchParameter: Map<String, Any>, error: Throwable?) {
        if (isViewNotAttached) return

        decrementStart()

        view.removeLoading()
        view.hideRefreshLayout()
        view.showNetworkError(error)
        view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), error)
    }

    private fun decrementStart() {
        paginationImpl.decrementStart()
    }

    override fun loadData(searchParameter: Map<String, Any>) {
        view ?: return

        navSource = searchParameter.getValueString(SearchApiConst.NAVSOURCE)
        pageId = searchParameter.getValueString(SearchApiConst.SRP_PAGE_ID)
        pageTitle = searchParameter.getValueString(SearchApiConst.SRP_PAGE_TITLE)
        searchRef = searchParameter.getValueString(SearchApiConst.SEARCH_REF)
        externalReference = searchParameter.getValueString(SearchApiConst.SRP_EXT_REF)
        dimension90 = Dimension90Utils.getDimension90(searchParameter)
        additionalParams = ""

        val requestParams = requestParamsGenerator.createInitializeSearchParam(
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )
        requestParamsGenerator.enrichWithRelatedSearchParam(requestParams)

        val useCaseRequestParams = requestParamsGenerator.createSearchProductRequestParams(
            requestParams,
            isLocalSearch(),
            view.isAnyFilterActive,
            view.isAnySortActive,
            topAdsHeadlineHelper.seenAds.toString(),
        )

        performanceMonitoring?.stopPreparePagePerformanceMonitoring()
        performanceMonitoring?.startNetworkRequestPerformanceMonitoring()

        // Unsubscribe first in case user has slow connection,
        // and the previous loadDataUseCase has not finished yet.
        searchProductFirstPageUseCase.unsubscribe()
        searchProductFirstPageUseCase.execute(
            useCaseRequestParams,
            getLoadDataSubscriber(requestParams.parameters)
        )
    }

    private fun getLoadDataSubscriber(searchParameter: Map<String, Any>): Subscriber<SearchProductModel> {
        return object : Subscriber<SearchProductModel>() {
            override fun onStart() {
                loadDataSubscriberOnStart()
            }

            override fun onCompleted() {
                loadDataSubscriberOnComplete()
            }

            override fun onError(error: Throwable?) {
                loadDataSubscriberOnError(searchParameter, error)
            }

            override fun onNext(searchProductModel: SearchProductModel) {
                loadDataSubscriberOnNext(searchParameter, searchProductModel)
            }
        }
    }

    private fun loadDataSubscriberOnStart() {
        if (isViewNotAttached) return

        view.showRefreshLayout()
        incrementStart()
    }

    private fun loadDataSubscriberOnComplete() {
        if (isViewNotAttached) return

        view.hideRefreshLayout()
    }

    private fun loadDataSubscriberOnError(searchParameter: Map<String, Any>, throwable: Throwable?) {
        if (isViewNotAttached) return

        decrementStart()
        view.removeLoading()
        view.showNetworkError(throwable)
        view.hideRefreshLayout()
        view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), throwable)
    }

    private fun loadDataSubscriberOnNext(
        searchParameter: Map<String, Any>,
        searchProductModel: SearchProductModel,
    ) {
        performanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
        performanceMonitoring?.startRenderPerformanceMonitoring()
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC) {
            getViewToProcessSearchProductModel(searchParameter, searchProductModel)
        }
    }

    private fun getViewToProcessSearchProductModel(
        searchParameter: Map<String, Any>,
        searchProductModel: SearchProductModel,
    ) {
        if (isViewNotAttached) return

        if (isSearchRedirected(searchProductModel))
            getViewToRedirectSearch(searchProductModel)
        else
            getViewToProcessSearchResult(searchParameter, searchProductModel)
    }

    private fun isSearchRedirected(searchProductModel: SearchProductModel): Boolean {
        val redirection = searchProductModel.searchProduct.data.redirection

        return redirection.redirectApplink.isNotEmpty()
    }

    private fun getViewToRedirectSearch(searchProductModel: SearchProductModel) {
        val productDataView = createFirstProductDataView(searchProductModel)
        getViewToSendTrackingSearchAttempt(productDataView)

        val applink = searchProductModel.searchProduct.data.redirection.redirectApplink
        view.redirectSearchToAnotherPage(applink)
    }

    private fun getViewToProcessSearchResult(
        searchParameter: Map<String, Any>,
        searchProductModel: SearchProductModel,
    ) {
        updateValueEnableGlobalNavWidget()

        val productDataView = createFirstProductDataView(searchProductModel)

        responseCodeImpl.setResponseCode(productDataView.responseCode ?: "")
        suggestionPresenter.setSuggestionDataView(productDataView.suggestionModel)
        broadMatchDelegate.setRelatedDataView(productDataView.relatedDataView)
        bannerDelegate.setBannerData(productDataView.bannerDataView)
        autoCompleteApplink = productDataView.autocompleteApplink ?: ""
        paginationImpl.totalData = productDataView.totalData
        lastFilterPresenterDelegate.categoryIdL2 = productDataView.categoryIdL2
        relatedKeyword = searchProductModel.searchProduct.data.related.relatedKeyword
        suggestionKeyword = searchProductModel.searchProduct.data.suggestion.suggestion
        pageComponentId = productDataView.pageComponentId
        productListType = searchProductModel.getProductListType()
        keywordIntention = searchProductModel.keywordIntention

        view.setAutocompleteApplink(productDataView.autocompleteApplink)
        view.setDefaultLayoutType(productDataView.defaultView)
        view.setProductGridType(ProductGridType.getProductGridType(productDataView.gridType))

        if (!productDataView.isQuerySafe) view.showAdultRestriction()

        if (productDataView.productList.isEmpty()) {
            postProcessingFilter.checkPostProcessingFilter(
                searchProductModel.isPostProcessing,
                searchParameter,
                productDataView.totalData,
                ::loadData
            ) {
                getViewToHandleEmptyProductList(
                    searchProductModel.searchProduct,
                    productDataView,
                )
            }
        } else {
            postProcessingFilter.resetCount()

            runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_SHOW_PRODUCT_LIST) {
                getViewToShowProductList(searchParameter, searchProductModel, productDataView)
            }
        }

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_PROCESS_FILTER) {
            processFilters(searchProductModel)
        }

        getViewToSendTrackingSearchAttempt(productDataView)
    }

    private fun updateValueEnableGlobalNavWidget() {
        if (isViewNotAttached) return

        enableGlobalNavWidget = !view.isLandingPage
    }

    private fun createFirstProductDataView(searchProductModel: SearchProductModel): ProductDataView {
        return runCustomMetric(
            performanceMonitoring,
            SEARCH_RESULT_PLT_RENDER_LOGIC_MAP_PRODUCT_DATA_VIEW,
        ) {
            view.clearLastProductItemPositionFromCache()

            createProductDataView(searchProductModel)
        }
    }

    private fun getViewToHandleEmptyProductList(
        searchProduct: SearchProductModel.SearchProduct,
        productDataView: ProductDataView,
    ) {
        if (broadMatchDelegate.isShowBroadMatch(responseCode))
            processEmptySearchBroadMatch(productDataView)
        else
            processEmptySearchNonBroadMatch(searchProduct, productDataView)

        view.updateScrollListener()
    }

    private fun processEmptySearchBroadMatch(productDataView: ProductDataView) {
        val visitableList = mutableListOf<Visitable<*>>()
        adsLowOrganic.processAdsLowOrganic(
            isHideProductAds = isHideProductAds(productDataView),
            keyword = view.queryKey,
            topAdsModel = productDataView.adsModel,
            productData = createAdsLowOrganicProductData(),
            action = visitableList::addAll,
        )
        broadMatchDelegate.processBroadMatchReplaceEmptySearch(visitableList::addAll)

        view.removeLoading()
        view.setProductList(visitableList)
    }

    private fun createAdsLowOrganicProductData() =
        AdsLowOrganic.SearchPageProductData(
            dimension90,
            productListType,
            externalReference,
            keywordIntention,
        )

    private fun processEmptySearchNonBroadMatch(
        searchProduct: SearchProductModel.SearchProduct,
        productDataView: ProductDataView
    ) {
        val globalNavDataView = getGlobalNavViewModel(productDataView)

        if (bannedProductsPresenterDelegate.isBannedProducts(searchProduct))
            bannedProductsPresenterDelegate.processBannedProducts(
                searchProduct,
                getGlobalNavViewModel(productDataView)
            )
        else if (productDataView.violation != null)
            getViewToHandleViolation(productDataView, globalNavDataView)
        else
            getViewToShowEmptySearch(productDataView, globalNavDataView)

        getViewToShowRecommendationItem()
    }

    private fun getGlobalNavViewModel(productDataView: ProductDataView): GlobalNavDataView? {
        val isGlobalNavWidgetAvailable = productDataView.globalNavDataView != null && enableGlobalNavWidget
        return if (isGlobalNavWidgetAvailable) productDataView.globalNavDataView else null
    }

    private fun getViewToHandleViolation(
        productDataView: ProductDataView,
        globalNavDataView: GlobalNavDataView?,
    ) {
        view.removeLoading()
        view.addProductList(createViolationVisitableList(productDataView, globalNavDataView))
    }

    private fun createViolationVisitableList(
        productDataView: ProductDataView,
        globalNavDataView: GlobalNavDataView?,
    ) : List<Visitable<*>> {
        val violation = productDataView.violation ?: return emptyList()
        return mutableListOf<Visitable<*>>().apply {
            globalNavDataView?.let { add(it) }

            add(violation)
        }
    }

    private fun getViewToShowEmptySearch(
        productDataView: ProductDataView,
        globalNavDataView: GlobalNavDataView?,
    ) {
        view.removeLoading()
        view.setProductList(constructEmptyStateProductList(productDataView, globalNavDataView))
    }

    private fun constructEmptyStateProductList(
        productDataView: ProductDataView,
        globalNavDataView: GlobalNavDataView?,
    ): List<Visitable<*>> =
        mutableListOf<Visitable<*>>().apply {
            globalNavDataView?.let { add(it) }

            add(createEmptyStateDataView())

            adsLowOrganic.processAdsLowOrganic(
                isHideProductAds = isHideProductAds(productDataView),
                keyword = view.queryKey,
                topAdsModel = productDataView.adsModel,
                productData = createAdsLowOrganicProductData(),
                action = ::addAll,
            )

            broadMatchDelegate.processBroadMatchInEmptyLocalSearch(responseCode, ::addAll)
        }

    private fun createEmptyStateDataView(): EmptyStateDataView {
        val isAnyFilterActive = view.isAnyFilterActive

        return EmptyStateDataView.create(
            isFilterActive = isAnyFilterActive,
            keyword = view.queryKey,
            localSearch = emptyStateLocalSearch(isAnyFilterActive),
            isShowAdsLowOrganic = adsLowOrganic.isEnabled,
        )
    }

    private fun emptyStateLocalSearch(isAnyFilterActive: Boolean): EmptyStateDataView.LocalSearch? =
        if (isShowLocalSearchRecommendation() && !isAnyFilterActive)
            EmptyStateDataView.LocalSearch(
                applink = constructGlobalSearchApplink(),
                pageTitle = pageTitle,
            )
        else
            null

    private fun isShowLocalSearchRecommendation() =
        responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
            && isLocalSearch()

    private fun getViewToShowRecommendationItem() {
        if (isShowLocalSearchRecommendation()) showLocalSearchRecommendationBelowEmptyState()
        else if (isShowGlobalSearchRecommendation()) getGlobalSearchRecommendation()
    }

    private fun showLocalSearchRecommendationBelowEmptyState() {
        clearData()
        view.addLoading()

        getLocalSearchRecommendation()
    }

    private fun getLocalSearchRecommendation() {
        val localSearchParams = requestParamsGenerator.createLocalSearchRequestParams(
            navSource,
            pageTitle,
            pageId,
        )

        getLocalSearchRecommendationUseCase.get().execute(
            localSearchParams,
            createLocalSearchRecommendationSubscriber()
        )
    }

    private fun createLocalSearchRecommendationSubscriber(): Subscriber<SearchProductModel> {
        return object : Subscriber<SearchProductModel>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(searchProductModel: SearchProductModel) {
                getLocalSearchRecommendationSuccess(searchProductModel)
            }
        }
    }

    private fun getLocalSearchRecommendationSuccess(searchProductModel: SearchProductModel) {
        if (isViewNotAttached) return

        val productDataView = createProductViewModelMapperLocalSearchRecommendation(searchProductModel)

        val visitableList = mutableListOf<Visitable<*>>()
        if (isFirstPage()) {
            visitableList.add(SearchProductTitleDataView(pageTitle, isRecommendationTitle = true))
            productList.clear()
        }

        productList.addAll(productDataView.productList)
        visitableList.addAll(productDataView.productList)

        incrementStart()
        paginationImpl.totalData = searchProductModel.searchProduct.header.totalData

        view.removeLoading()
        view.addLocalSearchRecommendation(visitableList)
        if (hasNextPage()) view.addLoading()
        view.updateScrollListener()
    }

    private fun createProductViewModelMapperLocalSearchRecommendation(searchProductModel: SearchProductModel): ProductDataView {
        if (isFirstPage()) view.clearLastProductItemPositionFromCache()

        return createProductDataView(searchProductModel)
    }

    private fun isShowGlobalSearchRecommendation() =
        !view.isAnyFilterActive && !adsLowOrganic.isEnabled

    private fun getGlobalSearchRecommendation() {
        view.addLoading()

        recommendationPresenterDelegate.getRecommendation()
    }

    private fun getViewToShowProductList(
            searchParameter: Map<String, Any>,
            searchProductModel: SearchProductModel,
            productDataView: ProductDataView,
    ) {
        adsInjector.resetTopAdsPosition()

        val productListType = newCardType.ifBlank { searchProductModel.getProductListType() }
        productList = createProductItemVisitableList(
            productDataView,
            searchParameter,
            productListType,
            searchProductModel.isShowButtonAtc,
        ).toMutableList()

        val visitableList = visitableFactory.createFirstPageVisitableList(
            VisitableFactoryFirstPageData(
                productDataView,
                pageTitle,
                getIsGlobalNavWidgetAvailable(productDataView),
                isLocalSearch(),
                isTickerHasDismissed,
                responseCode,
                productList,
                searchProductModel,
                externalReference,
                constructGlobalSearchApplink(),
            )
        )

        additionalParams = productDataView.additionalParams
        firstProductPositionWithBOELabel = getFirstProductPositionWithBOELabel(visitableList)

        view.removeLoading()
        view.setProductList(visitableList)
        view.backToTop()
        if (hasNextPage())
            view.addLoading()

        view.updateScrollListener()

        checkShouldShowViewTypeOnBoarding(productListType)
    }

    private fun getFirstProductPositionWithBOELabel(list: List<Visitable<*>>): Int {
        val product = productList.firstOrNull {
            (it as ProductItemDataView).hasLabelGroupFulfillment
        }

        product ?: return -1

        val firstProductPositionWithBOELabel = list.indexOf(product)
        return max(firstProductPositionWithBOELabel, -1)
    }

    private fun getIsGlobalNavWidgetAvailable(productDataView: ProductDataView): Boolean {
        return productDataView.globalNavDataView != null
                && enableGlobalNavWidget
                && !view.isAnyFilterActive
                && !view.isAnySortActive
    }

    private fun constructGlobalSearchApplink(): String {
        val queryParams = UrlParamUtils.getQueryParams(autoCompleteApplink)
        val globalSearchQueryParams = UrlParamUtils.removeKeysFromQueryParams(queryParams, LOCAL_SEARCH_KEY_PARAMS)

        return ApplinkConstInternalDiscovery.SEARCH_RESULT +
                (if (globalSearchQueryParams.isNotEmpty()) "?$globalSearchQueryParams" else "")
    }

    private fun processFilters(searchProductModel: SearchProductModel) {
        view.hideQuickFilterShimmering()

        val hasProducts = searchProductModel.searchProduct.data.productList.isNotEmpty()
        val willProcessFilter = hasProducts || view.isAnyFilterActive

        if (!willProcessFilter) return

        view.setAutoFilterToggle(searchProductModel.backendFiltersToggle)

        processDefaultQuickFilter(searchProductModel)
        initFilterController(searchProductModel)
        processQuickFilter(searchProductModel.quickFilterModel)

        view.setSortFilterIndicatorCounter()
    }

    private fun processDefaultQuickFilter(searchProductModel: SearchProductModel) {
        val quickFilter = searchProductModel.quickFilterModel

        if (quickFilter.filter.size < QUICK_FILTER_MINIMUM_SIZE) {
            searchProductModel.quickFilterModel = createSearchProductDefaultQuickFilter()
        }
    }

    private fun initFilterController(searchProductModel: SearchProductModel) {
        if (dynamicFilterModel != null) return

        val quickFilterList = searchProductModel.quickFilterModel.filter
        val inspirationWidgetFilterList =
            searchProductModel.searchInspirationWidget.asFilterList()
        val filterList = quickFilterList + inspirationWidgetFilterList

        view.initFilterController(filterList)
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        quickFilterList.clear()
        quickFilterList.addAll(quickFilterData.filter)

        if (isReimagine()) {
            val sortFilterItems = quickFilterData.mapFilter(::sortFilterItemReimagine)
            if (sortFilterItems.isNotEmpty())
                view.setQuickFilterReimagine(sortFilterItems)
        } else {
            val sortFilterItems = quickFilterData.mapFilter(::sortFilterItem)
            if (sortFilterItems.isNotEmpty())
                view.setQuickFilter(sortFilterItems)
        }
    }

    private fun <T> DataValue.mapFilter(transform: (Filter) -> T): List<T> =
        filter.map(transform)

    private fun sortFilterItemReimagine(filter: Filter): SortFilterItemReimagine {
        val (isChipSelected, title, isSingleFilter) = quickFilterData(filter)

        return SortFilterItemReimagine(
            title = title,
            isSelected = isChipSelected,
            hasChevron = !isSingleFilter,
        )
    }

    private fun quickFilterData(filter: Filter): Triple<Boolean, String, Boolean> {
        val options = filter.options
        val isChipSelected = options.any { view.isFilterSelected(it) }
        val selectedOptionsOnCurrentFilter = options.filter { view.isFilterSelected(it) }
        val title = createSortFilterTitle(filter, selectedOptionsOnCurrentFilter)
        val isSingleFilter = options.size == 1

        return Triple(isChipSelected, title, isSingleFilter)
    }

    @Suppress("MagicNumber")
    private fun createSortFilterTitle(filter: Filter, activeOptions: List<Option>): String {
        val optionSize = activeOptions.size

        return when {
            optionSize == 1 -> activeOptions.first().name
            optionSize > 1 -> "$optionSize ${filter.title}"
            else -> filter.chipName
        }
    }

    private fun sortFilterItem(filter: Filter): SortFilterItem {
        val (isChipSelected, title, hasChevron) = quickFilterData(filter)
        val item = SortFilterItem(title)

        setSortFilterItemListener(item, filter, hasChevron)
        setSortFilterItemState(item, isChipSelected)

        return item
    }

    private fun setSortFilterItemState(item: SortFilterItem, isChipSelected: Boolean) {
        if (isChipSelected) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        } else {
            item.type = ChipsUnify.TYPE_NORMAL
        }
    }

    @Suppress("MagicNumber")
    private fun setSortFilterItemListener(
        item: SortFilterItem,
        filter: Filter,
        isSingleFilter: Boolean,
    ) {
        if (isSingleFilter) {
            item.listener = {
                view.onQuickFilterSelected(filter, filter.options.first(), dimension90)
            }
        } else {
            item.listener = {
                onDropDownQuickFilterClick(filter)
            }
            item.chevronListener = {
                onDropDownQuickFilterClick(filter)
            }
        }
    }

    private fun onDropDownQuickFilterClick(filter: Filter) {
        view.openBottomsheetMultipleOptionsQuickFilter(filter)
    }

    override fun onApplyDropdownQuickFilter(optionList: List<Option>?) {
        view.applyDropdownQuickFilter(optionList)
        view.trackEventApplyDropdownQuickFilter(optionList, dimension90)
    }

    private fun getViewToSendTrackingSearchAttempt(productDataView: ProductDataView) {
        if (isViewNotAttached) return

        doInBackground(productDataView, this::sendGeneralSearchTracking)
    }

    private fun <T> doInBackground(observable: T, onNext: Action1<in T>) {
        val compositeSubscription = compositeSubscription ?: return

        val subscription = Observable
                .just(observable)
                .subscribeOn(schedulersProvider.computation())
                .subscribe(onNext, Throwable::printStackTrace)

        compositeSubscription.add(subscription)
    }

    private fun sendGeneralSearchTracking(productDataView: ProductDataView) {
        if (isViewNotAttached) return

        val query = view.queryKey

        if (query.isEmpty()) return

        val afProdIds = JSONArray()
        val moengageTrackingCategory = HashMap<String?, String?>()
        val categoryIdMapping = HashSet<String?>()
        val categoryNameMapping = HashSet<String?>()
        val prodIdArray = ArrayList<String?>()
        val allProdIdArray = ArrayList<String?>()

        productDataView.productList.forEachIndexed { i, productItemDataView ->
            val productId = productItemDataView.productID
            val categoryIdString = productItemDataView.categoryID.toString()
            val categoryName = productItemDataView.categoryName
            allProdIdArray.add(productId)

            if (i < SearchConstant.GENERAL_SEARCH_TRACKING_PRODUCT_COUNT) {
                prodIdArray.add(productId)
                afProdIds.put(productId)
                moengageTrackingCategory[categoryIdString] = categoryName
            }

            categoryIdMapping.add(categoryIdString)
            categoryNameMapping.add(categoryName)
        }

        view.sendTrackingEventAppsFlyerViewListingSearch(afProdIds, query, prodIdArray, allProdIdArray)
        view.sendTrackingEventMoEngageSearchAttempt(query, productDataView.productList.isNotEmpty(), moengageTrackingCategory)
        view.sendTrackingGTMEventSearchAttempt(
            GeneralSearchTrackingModel(
                createGeneralSearchTrackingEventCategory(),
                createGeneralSearchTrackingEventLabel(productDataView, query),
                userId,
                productDataView.productList.isNotEmpty().toString(),
                StringUtils.join(categoryIdMapping, ","),
                StringUtils.join(categoryNameMapping, ","),
                createGeneralSearchTrackingRelatedKeyword(productDataView),
                dimension90,
                productDataView.backendFilters,
                productDataView.pageComponentId,
                externalReference,
            )
        )
    }

    private fun createGeneralSearchTrackingEventCategory() =
            SearchEventTracking.Category.EVENT_TOP_NAV + if (pageTitle.isEmpty()) "" else " - $pageTitle"


    private fun createGeneralSearchTrackingEventLabel(productDataView: ProductDataView, query: String): String {
        val source = getTopNavSource(productDataView.globalNavDataView)

        return String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                query,
                getKeywordProcess(productDataView),
                getResponseCode(productDataView),
                source,
                getNavSourceForGeneralSearchTracking(),
                getPageTitleForGeneralSearchTracking(),
                productDataView.totalData
        )
    }

    private fun getResponseCode(productDataView: ProductDataView): String? {
        return if(productDataView.productList.isEmpty() && productDataView.responseCode.equals("0")){
             REQUEST_TIMEOUT_RESPONSE_CODE
        } else productDataView.responseCode
    }

    private fun getTopNavSource(globalNavDataView: GlobalNavDataView?): String {
        globalNavDataView ?: return SearchEventTracking.NONE

        return if (globalNavDataView.source.isEmpty()) SearchEventTracking.OTHER
        else globalNavDataView.source
    }

    private fun getKeywordProcess(productDataView: ProductDataView): String {
        val keywordProcess = productDataView.keywordProcess
        return if (keywordProcess.isNullOrEmpty()) "0" else keywordProcess
    }

    private fun getNavSourceForGeneralSearchTracking(): String {
        return if (navSource.isEmpty()) SearchEventTracking.NONE else navSource
    }

    private fun getPageTitleForGeneralSearchTracking(): String {
        return if (pageTitle.isEmpty()) SearchEventTracking.NONE else pageTitle
    }

    private fun createGeneralSearchTrackingRelatedKeyword(productDataView: ProductDataView): String {
        val previousKeyword: String = getPreviousKeywordForGeneralSearchTracking()
        val alternativeKeyword: String = getAlternativeKeywordForGeneralSearchTracking(productDataView)
        return "$previousKeyword - $alternativeKeyword"
    }

    private fun getPreviousKeywordForGeneralSearchTracking(): String {
        if (isViewNotAttached) return SearchEventTracking.NONE

        val previousKeyword = view.previousKeyword
        return if (previousKeyword.isEmpty()) SearchEventTracking.NONE else previousKeyword
    }

    private fun getAlternativeKeywordForGeneralSearchTracking(productDataView: ProductDataView): String {
        return when {
            isAlternativeKeywordFromRelated(productDataView) ->
                getAlternativeKeywordFromRelated(productDataView.relatedDataView)
            isAlternativeKeywordFromSuggestion(productDataView) ->
                productDataView.suggestionModel?.suggestion ?: ""
            else ->
                SearchEventTracking.NONE
        }
    }

    private fun isAlternativeKeywordFromRelated(productDataView: ProductDataView): Boolean {
        val responseCode = productDataView.responseCode
        val isResponseCodeForRelatedKeyword = generalSearchTrackingRelatedKeywordResponseCodeList.contains(responseCode)
        val canConstructAlternativeKeywordFromRelated = canConstructAlternativeKeywordFromRelated(productDataView)

        return isResponseCodeForRelatedKeyword && canConstructAlternativeKeywordFromRelated
    }

    private fun canConstructAlternativeKeywordFromRelated(productDataView: ProductDataView): Boolean {
        val relatedDataView = productDataView.relatedDataView ?: return false
        val relatedKeyword = relatedDataView.relatedKeyword
        val broadMatchDataViewList = relatedDataView.broadMatchDataViewList

        return relatedKeyword.isNotEmpty() || broadMatchDataViewList.isNotEmpty()
    }

    private fun getAlternativeKeywordFromRelated(relatedDataView: RelatedDataView?): String {
        relatedDataView ?: return ""

        val broadMatchKeywords =
                if (relatedDataView.broadMatchDataViewList.isNotEmpty())
                    relatedDataView.broadMatchDataViewList.joinToString(
                            separator = ",",
                            transform = BroadMatchDataView::keyword,
                    )
                else ""

        val shouldAppendComma = relatedDataView.relatedKeyword.isNotEmpty() && broadMatchKeywords.isNotEmpty()

        return relatedDataView.relatedKeyword +
                (if (shouldAppendComma) "," else "") +
                broadMatchKeywords
    }

    private fun isAlternativeKeywordFromSuggestion(productDataView: ProductDataView): Boolean {
        val responseCode = productDataView.responseCode ?: ""
        val isResponseCodeForSuggestion = responseCode == "7"
        val suggestionModel = productDataView.suggestionModel ?: return false
        val suggestionIsNotEmpty = suggestionModel.suggestion.isNotEmpty()

        return isResponseCodeForSuggestion && suggestionIsNotEmpty
    }
    //endregion

    //region Product Impression, Click, and Three Dots
    override fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int) {
        if (isViewNotAttached || item == null) return

        if (item.isTopAds) getViewToTrackImpressedTopAdsProduct(item)
        else getViewToTrackImpressedOrganicProduct(item)

        checkShouldShowBOELabelOnBoarding(adapterPosition)
    }

    private fun getViewToTrackImpressedTopAdsProduct(item: ProductItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
                view.className,
                item.topadsImpressionUrl,
                item.productID,
                item.productName,
                item.imageUrl,
                SearchConstant.TopAdsComponent.TOP_ADS
        )

        view.sendTopAdsGTMTrackingProductImpression(item)
    }

    private fun getViewToTrackImpressedOrganicProduct(item: ProductItemDataView) {
        if (item.isOrganicAds) {
            topAdsUrlHitter.hitImpressionUrl(
                    view.className,
                    item.topadsImpressionUrl,
                    item.productID,
                    item.productName,
                    item.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )
        }

        view.sendProductImpressionTrackingEvent(item, getSuggestedRelatedKeyword())
    }

    private fun getSuggestedRelatedKeyword(): String = when (responseCode) {
        RESPONSE_CODE_RELATED -> relatedKeyword
        RESPONSE_CODE_SUGGESTION -> suggestionKeyword
        else -> ""
    }

    private fun checkShouldShowBOELabelOnBoarding(position: Int) {
        if (isViewAttached && checkProductWithBOELabel(position))
            if (shouldShowBoeCoachmark())
                view.showOnBoarding(firstProductPositionWithBOELabel)
    }

    private fun checkShouldShowViewTypeOnBoarding(productListType: String) {
        if (isViewAttached
            && productListType == SearchConstant.ProductListType.LIST_VIEW
            && shouldShowViewTypeCoachmark()
        ) {
            view.enableProductViewTypeOnBoarding()
        }
    }

    private fun checkProductWithBOELabel(position: Int): Boolean {
        return firstProductPositionWithBOELabel >= 0
                && firstProductPositionWithBOELabel == position
    }

    private fun shouldShowBoeCoachmark(): Boolean {
        return searchCoachMarkLocalCache.shouldShowBoeCoachmark()
    }

    private fun shouldShowViewTypeCoachmark(): Boolean {
        return searchCoachMarkLocalCache.shouldShowViewTypeCoachmark()
    }

    override fun onProductClick(item: ProductItemDataView?, adapterPosition: Int) {
        if (isViewNotAttached || item == null) return

        trackProductClick(item)
        if (item.isTopAds || item.isOrganicAds) trackProductTopAdsClick(item)

        sameSessionRecommendationPresenterDelegate.requestSameSessionRecommendation(
            item,
            adapterPosition,
            dimension90,
            externalReference,
            chooseAddressDelegate.getChooseAddressParams(),
        )

        similarSearchOnBoardingPresenterDelegate.checkShouldDisplaySimilarSearchThreeDotsCoachmark(
            item,
            adapterPosition,
        )

        view.routeToProductDetail(item, adapterPosition)
    }

    override fun trackProductClick(item: ProductItemDataView) {
        if (item.isTopAds) view.sendTopAdsGTMTrackingProductClick(item)
        else view.sendGTMTrackingProductClick(item, userId, getSuggestedRelatedKeyword())
    }

    private fun trackProductTopAdsClick(item: ProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
            view.className,
            item.topadsClickUrl,
            item.productID,
            item.productName,
            item.imageUrl,
            if (item.isTopAds) SearchConstant.TopAdsComponent.TOP_ADS
            else SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun onProductAddToCart(item: ProductItemDataView) {
        if (item.shouldOpenVariantBottomSheet()) {
            view.openVariantBottomSheet(item)
            if (item.isTopAds || item.isOrganicAds) trackProductTopAdsClick(item)
        } else {
            executeAtcCommon(item)
        }
    }

    private fun executeAtcCommon(data: ProductItemDataView) {
        val requestParams = data.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(
            {
                onAddToCartUseCaseSuccess(it, data)
            },
            ::onAddToCartUseCaseFailed
        )
    }

    private fun ProductItemDataView.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = productID,
            shopId = shopID,
            quantity = minOrder,
            productName = productName,
            price = price,
            userId = if (userSession.isLoggedIn) userSession.userId else "0"
        )
    }

    private fun onAddToCartUseCaseSuccess(
        addToCartDataModel: AddToCartDataModel?,
        productItemDataView: ProductItemDataView,
    ) {
        view.updateSearchBarNotification()

        val message = addToCartDataModel?.data?.message?.firstOrNull() ?: ""
        view.openAddToCartToaster(message, true)

        trackProductClick(productItemDataView)
        view.sendGTMTrackingProductATC(productItemDataView, addToCartDataModel?.data?.cartId)
        if (productItemDataView.isTopAds || productItemDataView.isOrganicAds)
            trackProductTopAdsClick(productItemDataView)
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        val message = throwable?.message ?: ""
        view.openAddToCartToaster(message, false)
    }

    override fun onThreeDotsClick(item: ProductItemDataView, adapterPosition: Int) {
        if (isViewNotAttached) return

        threeDotsProductItem = item

        view.trackEventLongPress(item.productID)
        view.showProductCardOptions(createProductCardOptionsModel(item))
    }

    private fun createProductCardOptionsModel(item: ProductItemDataView): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()

        productCardOptionsModel.hasWishlist = item.isWishlistButtonEnabled
        productCardOptionsModel.hasSimilarSearch = true

        productCardOptionsModel.isWishlisted = item.isWishlisted
        productCardOptionsModel.keyword = view.queryKey
        productCardOptionsModel.productId = item.productID
        productCardOptionsModel.isTopAds = item.isTopAds || item.isOrganicAds
        productCardOptionsModel.topAdsWishlistUrl = item.topadsWishlistUrl ?: ""
        productCardOptionsModel.topAdsClickUrl = item.topadsClickUrl ?: ""
        productCardOptionsModel.isRecommendation = false
        productCardOptionsModel.screenName = SearchEventTracking.Category.SEARCH_RESULT
        productCardOptionsModel.seeSimilarProductEvent = SearchTracking.EVENT_CLICK_SEARCH_RESULT
        productCardOptionsModel.addToCartParams = AddToCartParams(item.minOrder)
        productCardOptionsModel.categoryName = item.categoryString!!
        productCardOptionsModel.productName = item.productName
        productCardOptionsModel.formattedPrice = item.price
        productCardOptionsModel.productImageUrl = item.imageUrl
        productCardOptionsModel.productUrl = item.productUrl

        val shop = ProductCardOptionsModel.Shop()
        shop.shopId = item.shopID
        shop.shopName = item.shopName
        shop.shopUrl = item.shopUrl

        productCardOptionsModel.shop = shop

        return productCardOptionsModel
    }
    //endregion

    override fun onViewResumed() {
        chooseAddressDelegate.reCheckChooseAddressData(::refreshData)
    }

    override fun onLocalizingAddressSelected() {
        chooseAddressDelegate.updateChooseAddress(::refreshData)
    }

    private fun refreshData() {
        if (isViewNotAttached) return

        clearDynamicFilter()

        view.reloadData()
    }

    override fun detachView() {
        super.detachView()

        getDynamicFilterUseCase.get()?.unsubscribe()
        searchProductFirstPageUseCase.unsubscribe()
        searchProductLoadMoreUseCase.unsubscribe()
        getProductCountUseCase.get()?.unsubscribe()
        getLocalSearchRecommendationUseCase.get()?.unsubscribe()
        getInspirationCarouselChipsUseCase.get()?.unsubscribe()
        saveLastFilterUseCase.get()?.unsubscribe()
        recommendationPresenterDelegate.detachView()
        onSafeSearchViewDestroyed()

        if (compositeSubscription?.isUnsubscribed == true) unsubscribeCompositeSubscription()
    }

    private fun unsubscribeCompositeSubscription() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
    }
}
