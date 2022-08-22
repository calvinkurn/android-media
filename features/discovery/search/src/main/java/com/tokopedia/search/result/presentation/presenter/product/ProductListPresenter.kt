package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.SAVE_LAST_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.AddToCartParams
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterInput
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import com.tokopedia.search.result.presentation.model.BroadMatch
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchProduct
import com.tokopedia.search.result.presentation.model.CarouselOptionType
import com.tokopedia.search.result.presentation.model.CarouselProductType
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.DynamicCarouselOption
import com.tokopedia.search.result.presentation.model.DynamicCarouselProduct
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.RelatedDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.cpm.BannerAdsPresenterDelegate
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselProductDataViewMapper
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.pagination.PaginationImpl
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_MAP_PRODUCT_DATA_VIEW
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_PROCESS_FILTER
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_SHOW_PRODUCT_LIST
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_TDN
import com.tokopedia.search.result.product.performancemonitoring.runCustomMetric
import com.tokopedia.search.result.product.postprocessing.PostProcessingFilter
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.search.result.product.separator.VerticalSeparatorMapper
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.createSearchProductDefaultFilter
import com.tokopedia.search.utils.createSearchProductDefaultQuickFilter
import com.tokopedia.search.utils.getValueString
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
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
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.max

@Suppress("LongParameterList")
class ProductListPresenter @Inject constructor(
    @param:Named(SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    private val searchProductFirstPageUseCase: UseCase<SearchProductModel>,
    @param:Named(SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    private val searchProductLoadMoreUseCase: UseCase<SearchProductModel>,
    private val recommendationUseCase: GetRecommendationUseCase,
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
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val schedulersProvider: SchedulersProvider,
    private val topAdsHeadlineHelper : TopAdsHeadlineHelper,
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    private val chooseAddressDelegate: ChooseAddressPresenterDelegate,
    private val bannerDelegate: BannerPresenterDelegate,
    private val requestParamsGenerator: RequestParamsGenerator,
    private val paginationImpl: PaginationImpl,
): BaseDaggerPresenter<ProductListSectionContract.View>(),
    ProductListSectionContract.Presenter,
    Pagination by paginationImpl,
    BannerAdsPresenter by BannerAdsPresenterDelegate(topAdsHeadlineHelper){

    companion object {
        private val showBroadMatchResponseCodeList = listOf("0", "4", "5")
        private val generalSearchTrackingRelatedKeywordResponseCodeList = listOf("3", "4", "5", "6")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
        private val showInspirationCarouselLayout = listOf(
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT,
        )
        private val showInspirationCarouselLayoutWithVideo = showInspirationCarouselLayout + SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_VIDEO
        private const val SEARCH_PAGE_NAME_RECOMMENDATION = "empty_search"
        private const val DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu"
        private const val DEFAULT_USER_ID = "0"
        private const val QUICK_FILTER_MINIMUM_SIZE = 2
        private val LOCAL_SEARCH_KEY_PARAMS = listOf(
                SearchApiConst.NAVSOURCE,
                SearchApiConst.SRP_PAGE_ID,
                SearchApiConst.SRP_PAGE_TITLE,
        )
        private const val RESPONSE_CODE_RELATED = "3"
        private const val RESPONSE_CODE_SUGGESTION = "6"
        private const val EMPTY_LOCAL_SEARCH_RESPONSE_CODE = "11"
    }

    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    private val performanceMonitoring: PageLoadTimePerformanceInterface? =
        performanceMonitoringProvider.get()

    private var enableGlobalNavWidget = true
    private var additionalParams = ""
    override var isBottomSheetFilterEnabled = true
        private set
    private var hasLoadData = false
    private var responseCode = ""
    private var navSource = ""
    private var pageId = ""
    private var pageTitle = ""
    private var searchRef = ""
    private var dimension90 = ""
    private var autoCompleteApplink = ""
    private var externalReference = ""
    private var isGlobalNavWidgetAvailable = false
    private var isShowHeadlineAdsBasedOnGlobalNav = false

    private var productList = mutableListOf<Visitable<*>>()
    private var inspirationCarouselDataView = mutableListOf<InspirationCarouselDataView>()
    private var inspirationWidgetVisitable = mutableListOf<InspirationWidgetVisitable>()
    private var topAdsImageViewModelList = mutableListOf<TopAdsImageViewModel>()
    private var suggestionDataView: SuggestionDataView? = null
    private var relatedDataView: RelatedDataView? = null
    override val quickFilterList = mutableListOf<Filter>()
    override var dynamicFilterModel: DynamicFilterModel? = null
        private set
    private var threeDotsProductItem: ProductItemDataView? = null
    private var firstProductPositionWithBOELabel = -1
    private var categoryIdL2 = ""
    private var suggestionKeyword = ""
    private var relatedKeyword = ""
    override var pageComponentId: String = ""
        private set
    private val adsInjector = AdsInjector()
    private val postProcessingFilter = PostProcessingFilter()

    override fun attachView(view: ProductListSectionContract.View) {
        super.attachView(view)

        chooseAddressDelegate.updateChooseAddress()
    }

    //region AB Test booleans
    private val isABTestVideoWidget: Boolean by lazy {
        getABTestVideoWidget()
    }

    private fun getABTestVideoWidget() : Boolean {
        return try {
            val abTestVideoWidget = view.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_VIDEO_WIDGET,
                ""
            )
            RollenceKey.SEARCH_VIDEO_WIDGET_VARIANT == abTestVideoWidget
        } catch (e: Exception) {
            false
        }
    }
    //endregion

    override val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID

    override val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    //region Ticker
    override var isTickerHasDismissed = false
        private set

    override fun onPriceFilterTickerDismissed() {
        isTickerHasDismissed = true
    }
    //endregion

    //region Load Data / Load More / Recommendations
    override fun clearData() {
        postProcessingFilter.resetCount()
        paginationImpl.clearData()
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
            view.setupSearchNavigation()
            view.trackScreenAuthenticated()

            if (isViewAdded && !hasLoadData)
                onViewFirstTimeLaunch()

            chooseAddressDelegate.reCheckChooseAddressData(::refreshData)
        }
    }

    override fun loadMoreData(searchParameter: Map<String, Any>) {
        if (!hasNextPage()) return

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
        val list = mutableListOf<Visitable<*>>()

        processBroadMatch(list)
        addSearchInTokopedia(list)

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
        val list = createProductItemVisitableList(
            productDataView,
            searchParameter,
            searchProductModel.getProductListType()
        ).toMutableList()
        productList.addAll(list)

        processHeadlineAdsLoadMore(searchProductModel, list)
        processTopAdsImageViewModel(searchParameter, list)
        processInspirationWidgetPosition(searchParameter, list)
        processInspirationCarouselPosition(searchParameter, list)
        processBannerAndBroadmatchInSamePosition(list)
        bannerDelegate.processBanner(list, productList) { index, banner ->
            list.add(index, banner)
        }
        processBroadMatch(list)
        addSearchInTokopedia(list)

        return list
    }

    private fun createProductItemVisitableList(
        productDataView: ProductDataView,
        searchParameter: Map<String, Any>,
        productListType: String,
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
            )
    }

    private fun isHideProductAds(productDataView: ProductDataView) : Boolean {
        return isLocalSearch() || productDataView.isAdvancedNegativeKeywordSearch()
    }

    private fun isLocalSearch() = navSource.isNotEmpty() && pageId.isNotEmpty()

    private fun processHeadlineAdsLoadMore(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed()) return

        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel) { _, cpmDataList, isUseSeparator ->
            val verticalSeparator = if (isUseSeparator)
                VerticalSeparator.Both
            else VerticalSeparator.None
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator
            )
            processHeadlineAdsAtPosition(list, productList.size, cpmDataView)
        }
    }

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

        responseCode = productDataView.responseCode ?: ""
        suggestionDataView = productDataView.suggestionModel
        relatedDataView = productDataView.relatedDataView
        bannerDelegate.setBannerData(productDataView.bannerDataView)
        autoCompleteApplink = productDataView.autocompleteApplink ?: ""
        paginationImpl.totalData = productDataView.totalData
        categoryIdL2 = productDataView.categoryIdL2
        relatedKeyword = searchProductModel.searchProduct.data.related.relatedKeyword
        suggestionKeyword = searchProductModel.searchProduct.data.suggestion.suggestion
        pageComponentId = productDataView.pageComponentId

        view.setAutocompleteApplink(productDataView.autocompleteApplink)
        view.setDefaultLayoutType(productDataView.defaultView)

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
        if (isShowBroadMatch()) {
            getViewToShowBroadMatchToReplaceEmptySearch()
        } else {
            if (!productDataView.errorMessage.isNullOrEmpty()) {
                getViewToHandleEmptySearchWithErrorMessage(searchProduct, productDataView)
            } else if(productDataView.violation != null) {
                getViewToHandleViolation(productDataView)
            } else {
                getViewToShowEmptySearch(productDataView)

                if (isShowBroadMatchWithEmptyLocalSearch()) {
                    val visitableList = mutableListOf<Visitable<*>>()
                    addBroadMatchToVisitableList(visitableList)

                    view.addProductList(visitableList)
                }
            }

            getViewToShowRecommendationItem()
        }

        view.updateScrollListener()
    }

    private fun isShowBroadMatch() =
            showBroadMatchResponseCodeList.contains(responseCode)
                && (relatedDataView?.broadMatchDataViewList?.isNotEmpty() == true)

    private fun getViewToShowBroadMatchToReplaceEmptySearch() {
        val visitableList = mutableListOf<Visitable<*>>()

        addBroadMatchToVisitableList(visitableList)

        view.removeLoading()
        view.setProductList(visitableList)
        view.backToTop()
    }

    private fun addBroadMatchToVisitableList(visitableList: MutableList<Visitable<*>>) {
        suggestionDataView?.let {
            if (it.suggestionText.isNotEmpty())
                visitableList.add(it)

            suggestionDataView = null
        }

        relatedDataView?.let {
            visitableList.addAll(it.broadMatchDataViewList)
            relatedDataView = null
        }
    }

    private fun getViewToHandleEmptySearchWithErrorMessage(
        searchProduct: SearchProductModel.SearchProduct,
        productDataView: ProductDataView,
    ) {
        val bannedProductsVisitableList =
            createBannedProductsVisitableList(searchProduct, productDataView)

        view.removeLoading()
        view.setBannedProductsErrorMessage(bannedProductsVisitableList)
        view.trackEventImpressionBannedProducts(true)
    }

    private fun createBannedProductsVisitableList(
        searchProduct: SearchProductModel.SearchProduct,
        productDataView: ProductDataView,
    ): List<Visitable<*>> =
        mutableListOf<Visitable<*>>().apply {
            getGlobalNavViewModel(productDataView)?.let { globalNavDataView ->
                add(globalNavDataView)
            }

            add(BannedProductsEmptySearchDataView(searchProduct.header.errorMessage))
        }


    private fun getViewToHandleViolation(
        productDataView: ProductDataView,
    ) {
        val violationProductsVisitableList =
            createViolationVisitableList(productDataView)

        view.removeLoading()
        view.addProductList(violationProductsVisitableList)
    }

    private fun createViolationVisitableList(
        productDataView: ProductDataView,
    ) : List<Visitable<*>> {
        val violation = productDataView.violation ?: return emptyList()
        return mutableListOf<Visitable<*>>().apply {
            getGlobalNavViewModel(productDataView)?.let { globalNavDataView ->
                add(globalNavDataView)
            }

            add(violation)
            add(SeparatorDataView())
        }
    }

    private fun getViewToShowEmptySearch(productDataView: ProductDataView) {
        clearData()
        view.removeLoading()
        view.setProductList(constructEmptyStateProductList(productDataView))
    }

    private fun constructEmptyStateProductList(
        productDataView: ProductDataView,
    ): List<Visitable<*>> =
        mutableListOf<Visitable<*>>().apply {
            getGlobalNavViewModel(productDataView)?.let { add(it) }
            add(createEmptyStateDataView())
        }

    private fun getGlobalNavViewModel(productDataView: ProductDataView): GlobalNavDataView? {
        val isGlobalNavWidgetAvailable = productDataView.globalNavDataView != null && enableGlobalNavWidget
        return if (isGlobalNavWidgetAvailable) productDataView.globalNavDataView else null
    }

    private fun createEmptyStateDataView(): EmptyStateDataView {
        val isAnyFilterActive = view.isAnyFilterActive

        return EmptyStateDataView.create(
            isFilterActive = isAnyFilterActive,
            keyword = view.queryKey,
            localSearch = emptyStateLocalSearch(isAnyFilterActive),
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

    private fun isShowBroadMatchWithEmptyLocalSearch() =
            responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
                    && (relatedDataView?.broadMatchDataViewList?.isNotEmpty() == true)

    private fun isShowLocalSearchRecommendation() =
            responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
                    && isLocalSearch()

    private fun getViewToShowRecommendationItem() {
        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation()
        else if (!view.isAnyFilterActive) getGlobalSearchRecommendation()
    }

    private fun getLocalSearchRecommendation() {
        view.addLoading()

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

    private fun getGlobalSearchRecommendation() {
        view.addLoading()

        recommendationUseCase.execute(
                recommendationUseCase.getRecomParams(
                        pageNumber = 1,
                        xSource = DEFAULT_VALUE_X_SOURCE,
                        pageName = SEARCH_PAGE_NAME_RECOMMENDATION,
                        productIds = listOf()
                ),
                object : Subscriber<List<RecommendationWidget>>() {
                    override fun onCompleted() {
                        view.removeLoading()
                    }

                    override fun onError(e: Throwable?) {}

                    override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                        if (recommendationWidgets.isEmpty()) return

                        val recommendationItemDataView =
                                RecommendationViewModelMapper().convertToRecommendationItemViewModel(recommendationWidgets[0])
                        val items = mutableListOf<Visitable<*>>()
                        val recommendationWidget = recommendationWidgets[0]
                        val recommendationWidgetTitle =
                                if (recommendationWidget.title.isEmpty()) DEFAULT_PAGE_TITLE_RECOMMENDATION
                                else recommendationWidget.title
                        val recommendationTitleDataView = RecommendationTitleDataView(
                                recommendationWidgetTitle,
                                recommendationWidget.seeMoreAppLink,
                                recommendationWidget.pageName
                        )
                        items.add(recommendationTitleDataView)
                        items.addAll(recommendationItemDataView)

                        view.addRecommendationList(items)
                    }
                }
        )
    }


    private fun getViewToShowProductList(
            searchParameter: Map<String, Any>,
            searchProductModel: SearchProductModel,
            productDataView: ProductDataView,
    ) {
        val searchProduct = searchProductModel.searchProduct
        val list = mutableListOf<Visitable<*>>()

        if (!productDataView.isQuerySafe) view.showAdultRestriction()

        addPageTitle(list)

        isGlobalNavWidgetAvailable = getIsGlobalNavWidgetAvailable(productDataView)
        if (isGlobalNavWidgetAvailable) {
            productDataView.globalNavDataView?.let {
                list.add(it)
                isShowHeadlineAdsBasedOnGlobalNav = it.isShowTopAds
            }
        }

        addLastFilterDataView(list, productDataView)

        list.add(ChooseAddressDataView())

        productDataView.tickerModel?.let {
            if (!isTickerHasDismissed && it.text.isNotEmpty())
                list.add(it)
        }

        if (shouldShowSuggestion(productDataView)) {
            productDataView.suggestionModel?.let {
                list.add(it)
            }
        }

        if (searchProduct.header.errorMessage.isNotEmpty()) {
            list.add(createBannedProductsTickerDataView(searchProduct.header.errorMessage))
            view.trackEventImpressionBannedProducts(false)
        }

        adsInjector.resetTopAdsPosition()
        productList = createProductItemVisitableList(
            productDataView,
            searchParameter,
            searchProductModel.getProductListType(),
        ).toMutableList()
        list.addAll(productList)

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS) {
            processHeadlineAdsFirstPage(searchProductModel, list)
        }

        additionalParams = productDataView.additionalParams

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL) {
            inspirationCarouselDataView = productDataView.inspirationCarouselDataView.toMutableList()
            processInspirationCarouselPosition(searchParameter, list)
        }

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET) {
            inspirationWidgetVisitable = productDataView.inspirationWidgetDataView.toMutableList()
            processInspirationWidgetPosition(searchParameter, list)
        }

        processBannerAndBroadmatchInSamePosition(list)
        bannerDelegate.processBanner(list, productList) { index, banner ->
            list.add(index, banner)
        }

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH) {
            processBroadMatch(list)
        }

        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_TDN) {
            topAdsImageViewModelList =
                searchProductModel.getTopAdsImageViewModelList().toMutableList()

            processTopAdsImageViewModel(searchParameter, list)
        }

        addSearchInTokopedia(list)
        firstProductPositionWithBOELabel = getFirstProductPositionWithBOELabel(list)

        view.removeLoading()
        view.setProductList(list)
        view.backToTop()
        if (hasNextPage())
            view.addLoading()

        view.updateScrollListener()
    }

    private fun addLastFilterDataView(
        visitableList: MutableList<Visitable<*>>,
        productDataView: ProductDataView,
    ) {
        val lastFilterDataView = productDataView.lastFilterDataView

        if (lastFilterDataView.shouldShow()) {
            visitableList.add(lastFilterDataView)
        }
    }

    private fun getFirstProductPositionWithBOELabel(list: List<Visitable<*>>): Int {
        val product = productList.firstOrNull {
            (it as ProductItemDataView).hasLabelGroupFulfillment
        }

        product ?: return -1

        val firstProductPositionWithBOELabel = list.indexOf(product)
        return max(firstProductPositionWithBOELabel, -1)
    }

    private fun addPageTitle(list: MutableList<Visitable<*>>) {
        if (pageTitle.isEmpty()) return

        list.add(SearchProductTitleDataView(pageTitle, isRecommendationTitle = false))
    }

    private fun getIsGlobalNavWidgetAvailable(productDataView: ProductDataView): Boolean {
        return productDataView.globalNavDataView != null
                && enableGlobalNavWidget
                && !view.isAnyFilterActive
                && !view.isAnySortActive
    }

    private fun addSearchInTokopedia(list: MutableList<Visitable<*>>) {
        if (isLastPage() && isLocalSearch()) {
            val globalSearchApplink = constructGlobalSearchApplink()
            val searchInTokopediaDataView = SearchInTokopediaDataView(globalSearchApplink)
            list.add(searchInTokopediaDataView)
        }
    }

    private fun constructGlobalSearchApplink(): String {
        val queryParams = UrlParamUtils.getQueryParams(autoCompleteApplink)
        val globalSearchQueryParams = UrlParamUtils.removeKeysFromQueryParams(queryParams, LOCAL_SEARCH_KEY_PARAMS)

        return ApplinkConstInternalDiscovery.SEARCH_RESULT +
                (if (globalSearchQueryParams.isNotEmpty()) "?$globalSearchQueryParams" else "")
    }

    private fun shouldShowSuggestion(productDataView: ProductDataView): Boolean {
        return showSuggestionResponseCodeList.contains(responseCode)
                && (productDataView.suggestionModel?.suggestionText?.isNotEmpty() == true)
    }

    private fun processHeadlineAdsFirstPage(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed()) return
        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel, 1) { index, cpmDataList,  isUseSeparator ->
            val verticalSeparator = if(isUseSeparator && index != 0)
                VerticalSeparator.Both
            else VerticalSeparator.None
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator
            )
            if (index == 0)
                processHeadlineAdsAtTop(list, cpmDataView)
            else
                processHeadlineAdsAtPosition(list, productList.size, cpmDataView)
        }
    }

    private fun isHeadlineAdsAllowed(): Boolean {
        return !isLocalSearch()
                && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav)
    }

    private fun createCpmDataView(
        cpmModel: CpmModel,
        cpmData: ArrayList<CpmData>,
        verticalSeparator: VerticalSeparator,
    ): CpmDataView {
        val cpmForViewModel = createCpmForViewModel(cpmModel, cpmData)
        return CpmDataView(cpmForViewModel, verticalSeparator)
    }

    private fun createCpmForViewModel(cpmModel: CpmModel, cpmData: ArrayList<CpmData>): CpmModel {
        return CpmModel().apply {
            header = cpmModel.header
            status = cpmModel.status
            error = cpmModel.error
            data = cpmData
        }
    }

    private fun processHeadlineAdsAtTop(visitableList: MutableList<Visitable<*>>, cpmDataView: CpmDataView) {
        if (productList.isEmpty()) return

        val firstProductIndex = visitableList.indexOf(productList[0])
        if (firstProductIndex !in visitableList.indices) return

        visitableList.add(firstProductIndex, cpmDataView)
    }

    private fun processHeadlineAdsAtPosition(
            visitableList: MutableList<Visitable<*>>,
            position: Int,
            cpmDataView: CpmDataView,
    ) {
        val headlineAdsVisitableList = arrayListOf<Visitable<ProductListTypeFactory>>()
        headlineAdsVisitableList.add(cpmDataView)


        val product = productList[position - 1]
        val headlineAdsIndex = visitableList.indexOf(product) + 1
        visitableList.addAll(headlineAdsIndex, headlineAdsVisitableList)
    }

    private fun createBannedProductsTickerDataView(errorMessage: String): BannedProductsTickerDataView {
        val htmlErrorMessage = "$errorMessage Gunakan browser"
        return BannedProductsTickerDataView(htmlErrorMessage)
    }

    private fun processInspirationWidgetPosition(
        searchParameter: Map<String, Any>,
        list: MutableList<Visitable<*>>,
    ) {
        if (inspirationWidgetVisitable.isEmpty()) return

        val inspirationWidgetVisitableIterator = inspirationWidgetVisitable.iterator()
        while (inspirationWidgetVisitableIterator.hasNext()) {
            val data = inspirationWidgetVisitableIterator.next()
            val inspirationWidgetVisitableList = constructInspirationWidgetVisitableList(data)

            if (data.data.position < 0) {
                inspirationWidgetVisitableIterator.remove()
                continue
            }

            val widgetPosition = data.data.position
            if (widgetPosition <= productList.size) {
                try {
                    val productListPosition = maxOf(widgetPosition, 1)
                    val product = productList[productListPosition - 1]
                    val addIndex = minOf(widgetPosition, 1)
                    val visitableIndex = list.indexOf(product) + addIndex

                    list.addAll(visitableIndex, inspirationWidgetVisitableList)
                    inspirationWidgetVisitableIterator.remove()
                } catch (exception: Throwable) {
                    Timber.w(exception)
                    view.logWarning(
                        UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>),
                        exception,
                    )
                }
            }
        }
    }

    private fun constructInspirationWidgetVisitableList(
        data: InspirationWidgetVisitable
    ): List<Visitable<ProductListTypeFactory>> {
        val inspirationSizeVisitableList = mutableListOf<Visitable<ProductListTypeFactory>>()

        if (data.hasTopSeparator)
            inspirationSizeVisitableList.add(SeparatorDataView())

        inspirationSizeVisitableList.add(data)

        if (data.hasBottomSeparator)
            inspirationSizeVisitableList.add(SeparatorDataView())

        return inspirationSizeVisitableList
    }

    private fun processInspirationCarouselPosition(searchParameter: Map<String, Any>, list: MutableList<Visitable<*>>) {
        if (inspirationCarouselDataView.isEmpty()) return

        val inspirationCarouselViewModelIterator = inspirationCarouselDataView.iterator()
        while (inspirationCarouselViewModelIterator.hasNext()) {
            val data = inspirationCarouselViewModelIterator.next()

            if (isInvalidInspirationCarousel(data)) {
                inspirationCarouselViewModelIterator.remove()
                continue
            }

            if (data.position <= productList.size && shouldShowInspirationCarousel(data.layout)) {
                try {
                    val inspirationCarouselVisitableList = constructInspirationCarouselVisitableList(data)
                    val product = productList[data.position - 1]
                    list.addAll(list.indexOf(product) + 1, inspirationCarouselVisitableList)
                    inspirationCarouselViewModelIterator.remove()
                } catch (exception: java.lang.Exception) {
                    Timber.w(exception)
                    view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), exception)
                }
            }
        }
    }

    private fun isInvalidInspirationCarousel(data: InspirationCarouselDataView): Boolean {
        if (data.position <= 0) return true
        return isInvalidInspirationCarouselLayout(data)
    }

    private fun InspirationCarouselDataView.isFirstOptionHasNoProducts() : Boolean {
        val firstOption = options.getOrNull(0)
        return firstOption != null && !firstOption.hasProducts()
    }

    private fun InspirationCarouselDataView.isInvalidCarouselVideoLayout() : Boolean {
        return !isABTestVideoWidget && isVideoLayout() && isFirstOptionHasNoProducts()
    }

    private fun isInvalidInspirationCarouselLayout(data: InspirationCarouselDataView) : Boolean {
        return data.isInvalidCarouselChipsLayout() || data.isInvalidCarouselVideoLayout()
    }

    private fun shouldShowInspirationCarousel(layout: String): Boolean {
        val validInspirationCarouselLayout = if(!isABTestVideoWidget) {
            showInspirationCarouselLayout
        } else {
            showInspirationCarouselLayoutWithVideo
        }
        return validInspirationCarouselLayout.contains(layout)
    }

    private fun constructInspirationCarouselVisitableList(data: InspirationCarouselDataView) =
        when {
            data.isDynamicProductLayout() -> convertInspirationCarouselToBroadMatch(data)
            data.isValidVideoLayout() -> convertInspirationCarouselToInspirationCarouselVideo(data)
            else -> listOf(data)
        }
    private fun InspirationCarouselDataView.isValidVideoLayout() : Boolean {
        return isABTestVideoWidget && isVideoLayout()
    }
    private fun InspirationCarouselDataView.isInvalidCarouselChipsLayout() : Boolean {
        return layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
                && isFirstOptionHasNoProducts()
    }
    private fun InspirationCarouselDataView.isDynamicProductLayout() =
            layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT
    private fun InspirationCarouselDataView.isVideoLayout() =
            layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_VIDEO

    private fun convertInspirationCarouselToInspirationCarouselVideo(data: InspirationCarouselDataView) : List<Visitable<*>> {
        return listOf(InspirationCarouselVideoDataView(data))
    }

    private fun convertInspirationCarouselToBroadMatch(data: InspirationCarouselDataView): List<Visitable<*>> {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()

        if (data.title.isNotEmpty()) {
            broadMatchVisitableList.add(
                SuggestionDataView(data.title, verticalSeparator = VerticalSeparator.Top)
            )
            broadMatchVisitableList.addAll(data.options.mapToBroadMatchDataView(data.type, false))
        } else {
            broadMatchVisitableList.addAll(data.options.mapToBroadMatchDataView(data.type, true))
        }

        return broadMatchVisitableList
    }

    private fun List<InspirationCarouselDataView.Option>.mapToBroadMatchDataView(
        type: String,
        addTopSeparator: Boolean,
    ): List<Visitable<*>> {
        return mapIndexed { index, option ->
            val verticalSeparator = if (index == 0 && addTopSeparator) {
                VerticalSeparator.Top
            } else if (index == size - 1) {
                VerticalSeparator.Bottom
            } else {
                VerticalSeparator.None
            }
            BroadMatchDataView(
                keyword = option.title,
                subtitle = option.subtitle,
                applink = option.applink,
                carouselOptionType = determineCarouselOptionType(type, option),
                broadMatchItemDataViewList = option.product.mapIndexed { index, product ->
                    BroadMatchItemDataView(
                        id = product.id,
                        name = product.name,
                        price = product.price,
                        imageUrl = product.imgUrl,
                        url = product.url,
                        applink = product.applink,
                        priceString = product.priceStr,
                        ratingAverage = product.ratingAverage,
                        labelGroupDataList = product.labelGroupDataList,
                        badgeItemDataViewList = product.badgeItemDataViewList,
                        shopLocation = product.shopLocation,
                        shopName = product.shopName,
                        position = index + 1,
                        alternativeKeyword = option.title,
                        carouselProductType = determineInspirationCarouselProductType(
                            type,
                            option,
                            product,
                        ),
                        freeOngkirDataView = product.freeOngkirDataView,
                        isOrganicAds = product.isOrganicAds,
                        topAdsViewUrl = product.topAdsViewUrl,
                        topAdsClickUrl = product.topAdsClickUrl,
                        topAdsWishlistUrl = product.topAdsWishlistUrl,
                        componentId = product.componentId,
                        originalPrice = product.originalPrice,
                        discountPercentage = product.discountPercentage,
                        externalReference = externalReference,
                    )
                },
                cardButton = BroadMatchDataView.CardButton(
                    option.cardButton.title,
                    option.cardButton.applink,
                ),
                verticalSeparator = verticalSeparator,
            )
        }
    }

    private fun determineCarouselOptionType(
        type: String,
        option: InspirationCarouselDataView.Option
    ): CarouselOptionType =
        if (type == SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD) BroadMatch
        else DynamicCarouselOption(option)

    private fun determineInspirationCarouselProductType(
        type: String,
        option: InspirationCarouselDataView.Option,
        product: InspirationCarouselDataView.Option.Product,
    ): CarouselProductType {
        return if (type == SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD)
            BroadMatchProduct(false)
        else
            DynamicCarouselProduct(option.inspirationCarouselType, product)
    }

    private fun processBannerAndBroadmatchInSamePosition(
        list: MutableList<Visitable<*>>,
    ) {
        val relatedDataView = relatedDataView ?: return

        if (bannerDelegate.isShowBanner() && isShowBroadMatch()) {
            if (bannerDelegate.isLastPositionBanner && relatedDataView.position == 0) {
                processBroadMatchAtBottom(list)
                bannerDelegate.processBannerAtBottom(list) { _, banner ->
                    list.add(banner)
                }
            } else if (bannerDelegate.isFirstPositionBanner && relatedDataView.position == 1) {
                processBroadMatchAtTop(list)
                bannerDelegate.processBannerAtTop(list, productList) { index, banner ->
                    list.add(index, banner)
                }
            }
        }
    }

    private fun processBroadMatch(list: MutableList<Visitable<*>>) {
        try {
            if (!isShowBroadMatch()) return

            val broadMatchPosition = relatedDataView?.position ?: -1
            if (broadMatchPosition == 0) processBroadMatchAtBottom(list)
            else if (broadMatchPosition == 1) processBroadMatchAtTop(list)
            else if (broadMatchPosition > 1) processBroadMatchAtPosition(list, broadMatchPosition)
        } catch (exception: Throwable) {
            Timber.w(exception)
        }
    }

    private fun processBroadMatchAtBottom(list: MutableList<Visitable<*>>) {
        if (isLastPage()) {
            val broadMatchVisitableList = mutableListOf<Visitable<*>>()
            addBroadMatchToVisitableList(broadMatchVisitableList)
            list.addAll(VerticalSeparatorMapper.addVerticalSeparator(
                broadMatchVisitableList,
                addBottomSeparator = false
            ))
        }
    }

    private fun processBroadMatchAtTop(list: MutableList<Visitable<*>>) {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()
        addBroadMatchToVisitableList(broadMatchVisitableList)

        list.addAll(
            list.indexOf(productList[0]),
            VerticalSeparatorMapper.addVerticalSeparator(
                broadMatchVisitableList,
                addTopSeparator = false
            )
        )
    }

    private fun processBroadMatchAtPosition(list: MutableList<Visitable<*>>, broadMatchPosition: Int) {
        if (productList.size < broadMatchPosition) return

        val broadMatchVisitableList = mutableListOf<Visitable<*>>()
        addBroadMatchToVisitableList(broadMatchVisitableList)

        val productItemAtBroadMatchPosition = productList[broadMatchPosition - 1]
        val broadMatchIndex = list.indexOf(productItemAtBroadMatchPosition) + 1
        list.addAll(
            broadMatchIndex,
            VerticalSeparatorMapper.addVerticalSeparator(broadMatchVisitableList)
        )
    }

    private fun processTopAdsImageViewModel(searchParameter: Map<String, Any>, list: MutableList<Visitable<*>>) {
        if (topAdsImageViewModelList.isEmpty()) return

        val topAdsImageViewModelIterator = topAdsImageViewModelList.iterator()

        while (topAdsImageViewModelIterator.hasNext()) {
            val data = topAdsImageViewModelIterator.next()

            if (data.position <= 0) {
                topAdsImageViewModelIterator.remove()
                continue
            }

            if (data.position <= productList.size) {
                try {
                    processTopAdsImageViewModelInPosition(list, data)
                    topAdsImageViewModelIterator.remove()
                } catch (exception: java.lang.Exception) {
                    Timber.w(exception)
                    view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), exception)
                }
            }
        }
    }

    private fun processTopAdsImageViewModelInPosition(list: MutableList<Visitable<*>>, data: TopAdsImageViewModel) {
        val isTopPosition = data.position == 1
        val searchProductTopAdsImageDataView = SearchProductTopAdsImageDataView(data)
        if (isTopPosition) {
            val index = getIndexOfTopAdsImageViewModelAtTop(list)
            list.add(index, searchProductTopAdsImageDataView)
        } else {
            val product = productList[data.position - 1]
            list.add(list.indexOf(product) + 1, searchProductTopAdsImageDataView)
        }
    }

    private fun getIndexOfTopAdsImageViewModelAtTop(list: List<Visitable<*>>): Int {
        var index = 0
        while (shouldIncrementIndexForTopAdsImageViewModel(index, list)) index++
        return index
    }

    private fun shouldIncrementIndexForTopAdsImageViewModel(index: Int, list: List<Visitable<*>>): Boolean {
        if (index >= list.size) return false

        val visitable = list[index]
        val isCPMOrProductItem = visitable is CpmDataView || visitable is ProductItemDataView

        return !isCPMOrProductItem
    }

    private fun processFilters(searchProductModel: SearchProductModel) {
        view.hideQuickFilterShimmering()

        val hasProducts = searchProductModel.searchProduct.data.productList.isNotEmpty()
        val willProcessFilter = hasProducts || view.isAnyFilterActive

        if (!willProcessFilter) return

        processDefaultQuickFilter(searchProductModel)
        initFilterController(searchProductModel)
        processQuickFilter(searchProductModel.quickFilterModel)
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
        val sortFilterItems = mutableListOf<SortFilterItem>()
        quickFilterList.clear()
        quickFilterList.addAll(quickFilterData.filter)

        quickFilterData.filter.forEach { filter ->
            val options = filter.options
            sortFilterItems.add(createSortFilterItem(filter, options))
        }

        if (sortFilterItems.isNotEmpty())
            view.setQuickFilter(sortFilterItems)
    }

    private fun createSortFilterItem(filter: Filter, options: List<Option>): SortFilterItem {
        val isChipSelected = options.any { view.isFilterSelected(it) }
        val selectedOptionsOnCurrentFilter = options.filter { view.isFilterSelected(it) }
        val item = SortFilterItem(createSortFilterTitle(filter, selectedOptionsOnCurrentFilter))

        setSortFilterItemListener(item, filter, options)
        setSortFilterItemState(item, isChipSelected)

        return item
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

    private fun setSortFilterItemState(item: SortFilterItem, isChipSelected: Boolean) {
        if (isChipSelected) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        } else {
            item.type = ChipsUnify.TYPE_NORMAL
        }
    }

    @Suppress("MagicNumber")
    private fun setSortFilterItemListener(item: SortFilterItem, filter: Filter, options: List<Option>) {
        if (options.size == 1) {
            item.listener = {
                view.onQuickFilterSelected(filter, options.first())
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
        view.trackEventClickDropdownQuickFilter(filter.title)
    }

    override fun onApplyDropdownQuickFilter(optionList: List<Option>?) {
        view.applyDropdownQuickFilter(optionList)
        view.trackEventApplyDropdownQuickFilter(optionList)
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
                productDataView.responseCode,
                source,
                getNavSourceForGeneralSearchTracking(),
                getPageTitleForGeneralSearchTracking(),
                productDataView.totalData
        )
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

    //region Wishlist
    override fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (isViewNotAttached) return
        productCardOptionsModel ?: return

        if (productCardOptionsModel.isRecommendation)
            handleWishlistRecommendationProduct(productCardOptionsModel)
        else
            handleWishlistNonRecommendationProduct(productCardOptionsModel)
    }

    private fun handleWishlistRecommendationProduct(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (wishlistResult.isUserLoggedIn)
            handleWishlistRecommendationProductWithLoggedInUser(productCardOptionsModel)
        else
            handleWishlistRecommendationProductWithNotLoggedInUser(productCardOptionsModel)
    }

    private fun handleWishlistRecommendationProductWithLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (!wishlistResult.isSuccess) {
            view.showMessageFailedWishlistAction(wishlistResult)
        } else {
            view.trackWishlistRecommendationProductLoginUser(!productCardOptionsModel.isWishlisted)
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult)
            if (productCardOptionsModel.isTopAds) view.hitWishlistClickUrl(productCardOptionsModel)
        }
    }

    private fun handleWishlistRecommendationProductWithNotLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        view.trackWishlistRecommendationProductNonLoginUser()
        view.launchLoginActivity(productCardOptionsModel.productId)
    }

    private fun handleWishlistNonRecommendationProduct(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (wishlistResult.isUserLoggedIn)
            handleWishlistNonRecommendationProductWithLoggedInUser(productCardOptionsModel)
        else
            handleWishlistNonRecommendationProductWithNotLoggedInUser(productCardOptionsModel)
    }

    private fun handleWishlistNonRecommendationProductWithLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (!wishlistResult.isSuccess) {
            view.showMessageFailedWishlistAction(wishlistResult)
        } else {
            view.trackWishlistProduct(createWishlistTrackingModel(
                    productCardOptionsModel,
                    productCardOptionsModel.wishlistResult.isAddWishlist
            ))
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult)
            if (productCardOptionsModel.isTopAds) view.hitWishlistClickUrl(productCardOptionsModel)
        }
    }

    private fun createWishlistTrackingModel(
            productCardOptionsModel: ProductCardOptionsModel,
            isAddWishlist: Boolean,
    ): WishlistTrackingModel {
        val wishlistTrackingModel = WishlistTrackingModel()

        wishlistTrackingModel.productId = productCardOptionsModel.productId
        wishlistTrackingModel.isTopAds = productCardOptionsModel.isTopAds
        wishlistTrackingModel.keyword = view.queryKey
        wishlistTrackingModel.isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
        wishlistTrackingModel.isAddWishlist = isAddWishlist

        return wishlistTrackingModel
    }

    private fun handleWishlistNonRecommendationProductWithNotLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        view.trackWishlistProduct(createWishlistTrackingModel(
                productCardOptionsModel,
                !productCardOptionsModel.isWishlisted
        ))
        view.launchLoginActivity(productCardOptionsModel.productId)
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

    private fun checkProductWithBOELabel(position: Int): Boolean {
        return firstProductPositionWithBOELabel >= 0
                && firstProductPositionWithBOELabel == position
    }

    private fun shouldShowBoeCoachmark(): Boolean {
        return searchCoachMarkLocalCache.shouldShowBoeCoachmark()
    }

    override fun onProductClick(item: ProductItemDataView?, adapterPosition: Int) {
        if (isViewNotAttached || item == null) return

        if (item.isTopAds) getViewToTrackOnClickTopAdsProduct(item)
        else getViewToTrackOnClickOrganicProduct(item)

        view.routeToProductDetail(item, adapterPosition)
    }

    private fun getViewToTrackOnClickTopAdsProduct(item: ProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
                view.className,
                item.topadsClickUrl,
                item.productID,
                item.productName,
                item.imageUrl,
                SearchConstant.TopAdsComponent.TOP_ADS
        )

        view.sendTopAdsGTMTrackingProductClick(item)
    }

    private fun getViewToTrackOnClickOrganicProduct(item: ProductItemDataView) {
        if (item.isOrganicAds) {
            topAdsUrlHitter.hitClickUrl(
                    view.className,
                    item.topadsClickUrl,
                    item.productID,
                    item.productName,
                    item.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )
        }

        view.sendGTMTrackingProductClick(item, userId, getSuggestedRelatedKeyword())
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

    //region BottomSheet Filter
    override fun getProductCount(mapParameter: Map<String, String>?) {
        if (isViewNotAttached) return
        if (mapParameter == null) {
            view.setProductCount("0")
            return
        }

        val getProductCountRequestParams = requestParamsGenerator.createGetProductCountRequestParams(
            mapParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )
        val getProductCountSubscriber = createGetProductCountSubscriber()
        getProductCountUseCase.get().execute(getProductCountRequestParams, getProductCountSubscriber)
    }

    private fun createGetProductCountSubscriber(): Subscriber<String> {
        return object : Subscriber<String>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable) {
                setProductCount("0")
            }

            override fun onNext(productCountText: String) {
                setProductCount(productCountText)
            }
        }
    }

    private fun setProductCount(productCountText: String) {
        if (isViewNotAttached) return

        view.setProductCount(productCountText)
    }

    override fun openFilterPage(searchParameter: Map<String, Any>?) {
        if (isViewNotAttached || searchParameter == null) return
        if (!isBottomSheetFilterEnabled) return

        isBottomSheetFilterEnabled = false

        view.sendTrackingOpenFilterPage()
        view.openBottomSheetFilter(dynamicFilterModel)

        if (dynamicFilterModel == null) {
            val getDynamicFilterRequestParams = requestParamsGenerator.createRequestDynamicFilterParams(
                searchParameter,
                chooseAddressDelegate.getChooseAddressParams(),
            )
            getDynamicFilterUseCase.get().execute(
                    getDynamicFilterRequestParams,
                    createGetDynamicFilterModelSubscriber()
            )
        }
    }

    private fun createGetDynamicFilterModelSubscriber(): Subscriber<DynamicFilterModel> {
        return object : Subscriber<DynamicFilterModel>() {
            override fun onCompleted() { }

            override fun onNext(dynamicFilterModel: DynamicFilterModel) {
                handleGetDynamicFilterSuccess(dynamicFilterModel)
            }

            override fun onError(e: Throwable) {
                handleGetDynamicFilterFailed()
            }
        }
    }

    private fun handleGetDynamicFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        if (!dynamicFilterModel.isEmpty()) {
            this.dynamicFilterModel = dynamicFilterModel
            getViewToSetDynamicFilterModel(dynamicFilterModel)
        } else {
            handleGetDynamicFilterFailed()
        }
    }

    private fun getViewToSetDynamicFilterModel(dynamicFilterModel: DynamicFilterModel) {
        if (isViewNotAttached) return

        view.setDynamicFilter(dynamicFilterModel)
    }

    private fun handleGetDynamicFilterFailed() {
        getViewToSetDynamicFilterModel(createSearchProductDefaultFilter())
    }

    override fun onBottomSheetFilterDismissed() {
        isBottomSheetFilterEnabled = true
    }

    override fun onApplySortFilter(mapParameter: Map<String, Any>) {
        val keywordFromFilter = mapParameter[SearchApiConst.Q] ?: ""
        val currentKeyword = view?.queryKey ?: ""

        if (currentKeyword != keywordFromFilter)
            dynamicFilterModel = null
    }
    //endregion

    //region Broad Match impression and click
    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        if (isViewNotAttached) return

        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingImpressBroadMatchAds(broadMatchItemDataView)

        when(val carouselProductType = broadMatchItemDataView.carouselProductType) {
            is BroadMatchProduct -> view.trackEventImpressionBroadMatchItem(broadMatchItemDataView)
            is DynamicCarouselProduct -> view.trackDynamicProductCarouselImpression(
                    broadMatchItemDataView,
                    carouselProductType.type,
                    carouselProductType.inspirationCarouselProduct,
            )
        }
    }

    private fun sendTrackingImpressBroadMatchAds(broadMatchItemDataView: BroadMatchItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
                view.className,
                broadMatchItemDataView.topAdsViewUrl,
                broadMatchItemDataView.id,
                broadMatchItemDataView.name,
                broadMatchItemDataView.imageUrl,
                SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    override fun onBroadMatchItemClick(broadMatchItemDataView: BroadMatchItemDataView) {
        if (isViewNotAttached) return

        when(val carouselProductType = broadMatchItemDataView.carouselProductType) {
            is BroadMatchProduct -> view.trackEventClickBroadMatchItem(broadMatchItemDataView)
            is DynamicCarouselProduct -> view.trackDynamicProductCarouselClick(
                    broadMatchItemDataView,
                    carouselProductType.type,
                    carouselProductType.inspirationCarouselProduct,
            )
        }
        view.redirectionStartActivity(broadMatchItemDataView.applink, broadMatchItemDataView.url)

        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingClickBroadMatchAds(broadMatchItemDataView)
    }

    private fun sendTrackingClickBroadMatchAds(broadMatchItemDataView: BroadMatchItemDataView) {
        topAdsUrlHitter.hitClickUrl(
                view.className,
                broadMatchItemDataView.topAdsClickUrl,
                broadMatchItemDataView.id,
                broadMatchItemDataView.name,
                broadMatchItemDataView.imageUrl,
                SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    override fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView) {
        if (isViewNotAttached) return

        if (broadMatchDataView.carouselOptionType == BroadMatch)
            view.trackEventImpressionBroadMatch(broadMatchDataView)
    }

    override fun onBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView) {
        val applink = getModifiedApplinkToSearchResult(broadMatchDataView.applink)
        handleBroadMatchSeeMoreClick(broadMatchDataView, applink)
    }

    override fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView) {
        val applink = getModifiedApplinkToSearchResult(broadMatchDataView.cardButton.applink)
        handleBroadMatchSeeMoreClick(broadMatchDataView, applink)
    }

    private fun handleBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView, applink: String) {
        if (isViewNotAttached) return
        trackBroadMatchSeeMoreClick(broadMatchDataView)

        view.redirectionStartActivity(applink, broadMatchDataView.url)
    }

    private fun trackBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView) {
        when(val carouselOptionType = broadMatchDataView.carouselOptionType) {
            is BroadMatch -> view.trackEventClickSeeMoreBroadMatch(broadMatchDataView)
            is DynamicCarouselOption -> view.trackEventClickSeeMoreDynamicProductCarousel(
                broadMatchDataView,
                carouselOptionType.option.inspirationCarouselType,
                carouselOptionType.option,
            )
        }
    }

    private fun getModifiedApplinkToSearchResult(applink: String): String {
        return if (applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
            view.modifyApplinkToSearchResult(applink)
        else applink
    }
    //endregion

    //region Inspiration Carousel
    override fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        if (isViewNotAttached) return

        if(product.isOrganicAds) sendTrackingImpressInspirationCarouselAds(product)

        when(product.layout) {
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventImpressionInspirationCarouselGridItem(product)
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventImpressionInspirationCarouselChipsItem(product)
            else -> view.trackEventImpressionInspirationCarouselListItem(product)
        }
    }

    private fun sendTrackingImpressInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitImpressionUrl(
            view.className,
            product.topAdsViewUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product) {
        if (isViewNotAttached) return

        view.redirectionStartActivity(product.applink, product.url)

        when(product.layout) {
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventClickInspirationCarouselGridItem(product)
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventClickInspirationCarouselChipsItem(product)
            else -> view.trackEventClickInspirationCarouselListItem(product)
        }

        if(product.isOrganicAds) sendTrackingClickInspirationCarouselAds(product)
    }

    private fun sendTrackingClickInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitClickUrl(
            view.className,
            product.topAdsClickUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }
    //endregion

    //region Change View
    override fun handleChangeView(position: Int, currentLayoutType: SearchConstant.ViewType) {
        if (isViewNotAttached) return

        when (currentLayoutType) {
            SearchConstant.ViewType.LIST -> switchToBigGridView(position)
            SearchConstant.ViewType.SMALL_GRID -> switchToListView(position)
            SearchConstant.ViewType.BIG_GRID -> switchToSmallGridView(position)
        }
    }

    override fun onViewResumed() {
        chooseAddressDelegate.reCheckChooseAddressData(::refreshData)
    }

    private fun switchToBigGridView(position: Int) {
        view.switchSearchNavigationLayoutTypeToBigGridView(position)
        view.trackEventSearchResultChangeView(SearchConstant.DefaultViewType.VIEW_TYPE_NAME_BIG_GRID)
    }

    private fun switchToListView(position: Int) {
        view.switchSearchNavigationLayoutTypeToListView(position)
        view.trackEventSearchResultChangeView(SearchConstant.DefaultViewType.VIEW_TYPE_NAME_LIST)
    }

    private fun switchToSmallGridView(position: Int) {
        view.switchSearchNavigationLayoutTypeToSmallGridView(position)
        view.trackEventSearchResultChangeView(SearchConstant.DefaultViewType.VIEW_TYPE_NAME_SMALL_GRID)
    }
    //endregion

    override fun onLocalizingAddressSelected() {
        chooseAddressDelegate.updateChooseAddress(::refreshData)
    }

    private fun refreshData() {
        if (isViewNotAttached) return

        dynamicFilterModel = null

        view.reloadData()
    }

    //region Inspiration Carousel Chips
    override fun onInspirationCarouselChipsClick(
        adapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
    ) {
        if (isViewNotAttached) return

        changeActiveInspirationCarouselChips(inspirationCarouselViewModel, clickedInspirationCarouselOption)

        view.trackInspirationCarouselChipsClicked(clickedInspirationCarouselOption)
        view.refreshItemAtIndex(adapterPosition)

        if (clickedInspirationCarouselOption.hasProducts()) return

        getInspirationCarouselChipProducts(
                adapterPosition,
                clickedInspirationCarouselOption,
                searchParameter,
                inspirationCarouselViewModel.title,
        )
    }

    private fun changeActiveInspirationCarouselChips(
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
    ) {
        inspirationCarouselViewModel.options.forEach {
            it.isChipsActive = false
        }

        clickedInspirationCarouselOption.isChipsActive = true
    }

    private fun getInspirationCarouselChipProducts(
        adapterPosition: Int,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
        inspirationCarouselTitle: String,
    ) {
        getInspirationCarouselChipsUseCase.get().unsubscribe()

        val requestParams = requestParamsGenerator.createGetInspirationCarouselChipProductsRequestParams(
            clickedInspirationCarouselOption,
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )

        getInspirationCarouselChipsUseCase.get().execute(
            requestParams,
            createGetInspirationCarouselChipProductsSubscriber(
                adapterPosition,
                clickedInspirationCarouselOption,
                inspirationCarouselTitle,
            )
        )
    }

    private fun createGetInspirationCarouselChipProductsSubscriber(
        adapterPosition: Int,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        inspirationCarouselTitle: String,
    ): Subscriber<InspirationCarouselChipsProductModel> {
        return object : Subscriber<InspirationCarouselChipsProductModel>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable) {}

            override fun onNext(inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel) {
                getInspirationCarouselChipsSuccess(
                        adapterPosition,
                        inspirationCarouselChipsProductModel,
                        clickedInspirationCarouselOption,
                        inspirationCarouselTitle
                )
            }
        }
    }

    private fun getInspirationCarouselChipsSuccess(
        adapterPosition: Int,
        inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        inspirationCarouselTitle: String,
    ) {
        if (isViewNotAttached) return

        val mapper = InspirationCarouselProductDataViewMapper()
        val productList = mapper.convertToInspirationCarouselProductDataView(
            inspirationCarouselChipsProductModel.searchProductCarouselByIdentifier.product,
            clickedInspirationCarouselOption.optionPosition,
            clickedInspirationCarouselOption.inspirationCarouselType,
            clickedInspirationCarouselOption.layout,
            this::productLabelGroupToLabelGroupDataView,
            clickedInspirationCarouselOption.title,
            inspirationCarouselTitle,
            dimension90,
            externalReference,
        )

        clickedInspirationCarouselOption.product = productList

        view.refreshItemAtIndex(adapterPosition)
    }

    private fun productLabelGroupToLabelGroupDataView(
            productLabelGroupList: List<ProductLabelGroup>,
    ): List<LabelGroupDataView> {
        return productLabelGroupList.map {
            LabelGroupDataView(
                    it.position,
                    it.type,
                    it.title,
                    it.url,
            )
        }
    }
    //endregion

    //region Save Last Filter
    override fun updateLastFilter(
        searchParameter: Map<String, Any>,
        savedOptionList: List<SavedOption>,
    ) {
        val searchParams = requestParamsGenerator.createInitializeSearchParam(
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )
        val saveLastFilterInput = SaveLastFilterInput(
            lastFilter = savedOptionList,
            mapParameter = searchParams.parameters,
            categoryIdL2 = categoryIdL2,
        )

        val requestParams = RequestParams.create()
        requestParams.putObject(INPUT_PARAMS, saveLastFilterInput)

        saveLastFilterUseCase.get().unsubscribe()
        saveLastFilterUseCase.get().execute(requestParams, emptySubscriber())
    }

    private fun <T> emptySubscriber() = object : Subscriber<T>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable?) {}

        override fun onNext(t: T?) {}
    }

    override fun closeLastFilter(searchParameter: Map<String, Any>) {
        updateLastFilter(searchParameter, listOf())
    }
    //endregion

    override fun detachView() {
        super.detachView()

        getDynamicFilterUseCase.get()?.unsubscribe()
        searchProductFirstPageUseCase.unsubscribe()
        searchProductLoadMoreUseCase.unsubscribe()
        recommendationUseCase.unsubscribe()
        getProductCountUseCase.get()?.unsubscribe()
        getLocalSearchRecommendationUseCase.get()?.unsubscribe()
        getInspirationCarouselChipsUseCase.get()?.unsubscribe()
        saveLastFilterUseCase.get()?.unsubscribe()
        if (compositeSubscription?.isUnsubscribed == true) unsubscribeCompositeSubscription()
    }

    private fun unsubscribeCompositeSubscription() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
    }
}
