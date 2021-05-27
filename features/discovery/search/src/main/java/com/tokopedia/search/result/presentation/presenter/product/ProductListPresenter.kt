package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.AddToCartParams
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.AddToCartResult
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.mapper.InspirationCarouselProductDataViewMapper
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.GlobalNavDataView
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.RelatedDataView
import com.tokopedia.search.result.presentation.model.SearchInTokopediaDataView
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.createSearchProductDefaultFilter
import com.tokopedia.search.utils.createSearchProductDefaultQuickFilter
import com.tokopedia.search.utils.getValueString
import com.tokopedia.search.utils.toSearchParams
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.model.Badge
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
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
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.max

class ProductListPresenter @Inject constructor(
        @param:Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
        private val searchProductFirstPageUseCase: UseCase<SearchProductModel>,
        @param:Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
        private val searchProductLoadMoreUseCase: UseCase<SearchProductModel>,
        private val recommendationUseCase: GetRecommendationUseCase,
        private val userSession: UserSessionInterface,
        @param:Named(SearchConstant.OnBoarding.LOCAL_CACHE_NAME)
        private val searchCoachMarkLocalCache: CoachMarkLocalCache,
        @param:Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
        private val getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
        @param:Named(SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE)
        private val getProductCountUseCase: Lazy<UseCase<String>>,
        @param:Named(SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE)
        private val getLocalSearchRecommendationUseCase: Lazy<UseCase<SearchProductModel>>,
        @param:Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE)
        private val getInspirationCarouselChipsUseCase: Lazy<UseCase<InspirationCarouselChipsProductModel>>,
        private val topAdsUrlHitter: TopAdsUrlHitter,
        private val schedulersProvider: SchedulersProvider,
        remoteConfig: Lazy<RemoteConfig>,
): BaseDaggerPresenter<ProductListSectionContract.View>(),
        ProductListSectionContract.Presenter {

    companion object {
        private val searchNoResultCodeList = listOf(1, 2, 3, 4, 5, 6, 8)
        private val showBroadMatchResponseCodeList = listOf("0", "4", "5")
        private val generalSearchTrackingRelatedKeywordResponseCodeList = listOf("3", "4", "5", "6")
        private val showSuggestionResponseCodeList = listOf("3", "6", "7")
        private val trackRelatedKeywordResponseCodeList = listOf("3", "6")
        private val showInspirationCarouselLayout = listOf(
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID,
                SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
        )
        private val showInspirationCardType = listOf(
                SearchConstant.InspirationCard.TYPE_ANNOTATION,
                SearchConstant.InspirationCard.TYPE_CATEGORY,
                SearchConstant.InspirationCard.TYPE_GUIDED,
                SearchConstant.InspirationCard.TYPE_CURATED,
                SearchConstant.InspirationCard.TYPE_RELATED,
        )
        private const val SEARCH_PAGE_NAME_RECOMMENDATION = "empty_search"
        private const val DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu"
        private const val DEFAULT_USER_ID = "0"
        private const val QUICK_FILTER_MINIMUM_SIZE = 2
        private val LOCAL_SEARCH_KEY_PARAMS = listOf(
                SearchApiConst.NAVSOURCE,
                SearchApiConst.SRP_PAGE_ID,
                SearchApiConst.SRP_PAGE_TITLE,
        )
        private const val EMPTY_LOCAL_SEARCH_RESPONSE_CODE = "11"
    }

    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()

    private var enableGlobalNavWidget = true
    private var additionalParams = ""
    private var isFirstTimeLoad = false
    override var isTickerHasDismissed = false
        private set
    override var startFrom = 0
        private set
    override var isBottomSheetFilterEnabled = true
        private set
    private var totalData = 0
    private var hasLoadData = false
    private var responseCode = ""
    private var topAdsCount = 1
    private var navSource = ""
    private var pageId = ""
    private var pageTitle = ""
    private var searchRef = ""
    private var autoCompleteApplink = ""
    private var isGlobalNavWidgetAvailable = false
    private var isShowHeadlineAdsBasedOnGlobalNav = false

    private var productList = mutableListOf<Visitable<*>>()
    private var inspirationCarouselDataView = mutableListOf<InspirationCarouselDataView>()
    private var inspirationCardDataView = mutableListOf<InspirationCardDataView>()
    private var topAdsImageViewModelList = mutableListOf<TopAdsImageViewModel>()
    private var suggestionDataView: SuggestionDataView? = null
    private var relatedDataView: RelatedDataView? = null
    override val quickFilterOptionList = mutableListOf<Option>()
    private var dynamicFilterModel: DynamicFilterModel? = null
    private var threeDotsProductItem: ProductItemDataView? = null
    private var firstProductPositionWithBOELabel = -1
    private var hasFullThreeDotsOptions = false
    private var cpmModel: CpmModel? = null
    private var cpmDataList: MutableList<CpmData>? = null
    private var isABTestNavigationRevamp = false
    private var isEnableChooseAddress = false
    private var chooseAddressData: LocalCacheModel? = null
    private var bannerDataView: BannerDataView? = null

    override fun attachView(view: ProductListSectionContract.View) {
        super.attachView(view)

        hasFullThreeDotsOptions = getHasFullThreeDotsOptions()
        isABTestNavigationRevamp = isABTestNavigationRevamp()
        isEnableChooseAddress = view.isChooseAddressWidgetEnabled
        if (isEnableChooseAddress) chooseAddressData = view.chooseAddressData
    }

    private fun isABTestNavigationRevamp(): Boolean {
        return try {
            (view.abTestRemoteConfig?.getString(AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD)
                    == AbTestPlatform.NAVIGATION_VARIANT_REVAMP)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getHasFullThreeDotsOptions(): Boolean {
        return try {
            (view.abTestRemoteConfig?.getString(SearchConstant.ABTestRemoteConfigKey.AB_TEST_KEY_THREE_DOTS_SEARCH)
                    == SearchConstant.ABTestRemoteConfigKey.AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID

    override val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    override val deviceId: String
        get() = userSession.deviceId

    override fun onPriceFilterTickerDismissed() {
        isTickerHasDismissed = true
    }

    override fun hasNextPage(): Boolean {
        return startFrom < totalData
    }

    override fun clearData() {
        startFrom = 0
        totalData = 0
    }

    override fun onViewCreated() {
        val isFirstActiveTab = view.isFirstActiveTab

        if (isFirstActiveTab && !hasLoadData) {
            hasLoadData = true
            onViewFirstTimeLaunch()
        }
    }

    private fun onViewFirstTimeLaunch() {
        isFirstTimeLoad = true
        view.reloadData()
    }

    override fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean) {
        if (isViewVisible) {
            view.setupSearchNavigation()
            view.trackScreenAuthenticated()

            if (isViewAdded && !hasLoadData) {
                hasLoadData = true
                onViewFirstTimeLaunch()
            }
        }
    }

    override fun loadMoreData(searchParameter: Map<String, Any>) {
        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation()
        else searchProductLoadMore(searchParameter)
    }

    private fun searchProductLoadMore(searchParameter: Map<String, Any>) {
        if (isViewNotAttached) return

        val requestParams = createInitializeSearchParam(searchParameter)
        enrichWithRelatedSearchParam(requestParams)
        enrichWithAdditionalParams(requestParams)

        val useCaseRequestParams = createSearchProductRequestParams(requestParams)

        // Unsubscribe first in case user has slow connection, and the previous loadMoreUseCase has not finished yet.
        searchProductLoadMoreUseCase.unsubscribe()
        searchProductLoadMoreUseCase.execute(useCaseRequestParams, getLoadMoreDataSubscriber(requestParams.parameters))
    }

    private fun createInitializeSearchParam(searchParameter: Map<String, Any>): RequestParams {
        val requestParams = RequestParams.create()

        putRequestParamsOtherParameters(requestParams, searchParameter)
        putRequestParamsChooseAddress(requestParams)
        requestParams.putAll(searchParameter)

        return requestParams
    }

    private fun putRequestParamsChooseAddress(requestParams: RequestParams) {
        if (!isEnableChooseAddress) return

        val chooseAddressData = chooseAddressData ?: return
        requestParams.putAllString(chooseAddressData.toSearchParams())
    }

    private fun putRequestParamsOtherParameters(requestParams: RequestParams, searchParameter: Map<String, Any>) {
        putRequestParamsSearchParameters(requestParams, searchParameter)
        putRequestParamsTopAdsParameters(requestParams)
        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter)
    }

    private fun putRequestParamsSearchParameters(requestParams: RequestParams, searchParameter: Map<String, Any>) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        requestParams.putString(SearchApiConst.ROWS, getSearchRows())
        requestParams.putString(SearchApiConst.OB, getSearchSort(searchParameter))
        requestParams.putString(SearchApiConst.START, startFrom.toString())
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE)
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE)
        requestParams.putString(SearchApiConst.Q, getSearchQuery(searchParameter).omitNewlineAndPlusSign())
        requestParams.putString(SearchApiConst.UNIQUE_ID, getUniqueId())
        requestParams.putString(SearchApiConst.USER_ID, userId)
    }

    private fun getSearchRows() = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS

    private fun getSearchSort(searchParameter: Map<String, Any>): String {
        val sort = searchParameter.getValueString(SearchApiConst.OB)
        return if (sort.isNotEmpty()) sort else SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun getSearchQuery(searchParameter: Map<String, Any>): String {
        return searchParameter
                .getValueString(SearchApiConst.Q)
                .omitNewlineAndPlusSign()
    }

    private fun String.omitNewlineAndPlusSign() =
            replace("\n", "").replace("+", " ")

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn) AuthHelper.getMD5Hash(userSession.userId)
        else AuthHelper.getMD5Hash(userSession.deviceId)
    }

    private fun putRequestParamsTopAdsParameters(requestParams: RequestParams) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2)
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP)
        requestParams.putString(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        requestParams.putBoolean(TopAdsParams.KEY_WITH_TEMPLATE, true)
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage())
    }

    private fun getTopAdsKeyPage(): Int {
        return try {
            val defaultValueStart = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
            startFrom / defaultValueStart + 1
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            0
        }
    }

    private fun putRequestParamsDepartmentIdIfNotEmpty(requestParams: RequestParams, searchParameter: Map<String, Any>) {
        val departmentId = searchParameter.getValueString(SearchApiConst.SC)

        if (departmentId.isNotEmpty()) {
            requestParams.putString(SearchApiConst.SC, departmentId)
            requestParams.putString(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId)
        }
    }

    private fun enrichWithRelatedSearchParam(requestParams: RequestParams) {
        requestParams.putBoolean(SearchApiConst.RELATED, true)
    }

    private fun enrichWithAdditionalParams(requestParams: RequestParams) {
        val additionalParams = UrlParamUtils.getParamMap(additionalParams)
        requestParams.putAllString(additionalParams)
    }

    private fun createSearchProductRequestParams(requestParams: RequestParams): RequestParams {
        val isLocalSearch: Boolean = isLocalSearch()
        val isSkipGlobalNavWidget = isLocalSearch || view.isAnyFilterActive || view.isAnySortActive

        return RequestParams.create().apply {
            putObject(SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS, requestParams.parameters)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, isLocalSearch)
            putBoolean(SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV, isSkipGlobalNavWidget)
        }
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
        startFrom += SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }

    private fun loadMoreDataSubscriberOnNext(
            searchParameter: Map<String, Any>,
            searchProductModel: SearchProductModel,
    ) {
        if (isViewNotAttached) return

        val productDataView = createProductDataView(searchProductModel)

        additionalParams = productDataView.additionalParams

        if (productDataView.productList.isEmpty())
            getViewToProcessEmptyResultDuringLoadMore(searchProductModel.searchProduct)
        else
            getViewToShowMoreData(searchParameter, searchProductModel, productDataView)

        totalData = productDataView.totalData
    }

    private fun createProductDataView(searchProductModel: SearchProductModel): ProductDataView {
        val lastProductItemPosition = view.lastProductItemPositionFromCache

        val productDataView = ProductViewModelMapper().convertToProductViewModel(
                lastProductItemPosition,
                searchProductModel,
                pageTitle,
                isLocalSearch()
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

    private fun getViewToProcessEmptyResultDuringLoadMore(searchProduct: SearchProductModel.SearchProduct) {
        val list = mutableListOf<Visitable<*>>()

        processBroadMatch(searchProduct, list)
        addSearchInTokopedia(searchProduct, list)

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
        view.addLoading()
        view.updateScrollListener()
    }

    private fun constructVisitableListLoadMore(
            productDataView: ProductDataView,
            searchProductModel: SearchProductModel,
            searchParameter: Map<String, Any>,
    ): List<Visitable<*>> {
        val list = createProductItemVisitableList(productDataView)
        productList.addAll(list)

        val searchProduct = searchProductModel.searchProduct
        processHeadlineAds(searchParameter, list)
        processTopAdsImageViewModel(searchParameter, list)
        processInspirationCardPosition(searchParameter, list)
        processInspirationCarouselPosition(searchParameter, list)
        processBannerAndBroadmatchInSamePosition(searchProduct, list)
        processBanner(searchProduct, list)
        processBroadMatch(searchProduct, list)
        addSearchInTokopedia(searchProduct, list)

        return list
    }

    private fun createProductItemVisitableList(productDataView: ProductDataView): MutableList<Visitable<*>> {
        val list: MutableList<Visitable<*>> = productDataView.productList.toMutableList()
        if (isLocalSearch()) return list

        var j = 0
        for (i in 0 until productDataView.getTotalItem()) {
            try {
                // Already surrounded by try catch per looping, safe to force nullable
                if (productDataView.adsModel!!.templates.size <= 0) continue
                if (productDataView.adsModel!!.templates[i].isIsAd) {
                    val topAds = productDataView.adsModel!!.data[j]
                    val item = ProductItemDataView()
                    item.productID = topAds.product.id
                    item.isTopAds = true
                    item.topadsImpressionUrl = topAds.product.image.s_url
                    item.topadsClickUrl = topAds.productClickUrl
                    item.topadsWishlistUrl = topAds.productWishlistUrl
                    item.topadsClickShopUrl = topAds.shopClickUrl
                    item.productName = topAds.product.name
                    item.price = topAds.product.priceFormat
                    item.shopCity = topAds.shop.location
                    item.imageUrl = topAds.product.image.s_ecs
                    item.imageUrl300 = topAds.product.image.m_ecs
                    item.imageUrl700 = topAds.product.image.m_ecs
                    item.isWishlisted = topAds.product.isWishlist
                    item.ratingString = topAds.product.productRatingFormat
                    item.badgesList = mapBadges(topAds.shop.badges)
                    item.isNew = topAds.product.isProductNewLabel
                    item.shopID = topAds.shop.id
                    item.shopName = topAds.shop.name
                    item.isShopOfficialStore = topAds.shop.isShop_is_official
                    item.isShopPowerMerchant = topAds.shop.isGoldShop
                    item.shopUrl = topAds.shop.uri
                    item.originalPrice = topAds.product.campaign.originalPrice
                    item.discountPercentage = topAds.product.campaign.discountPercentage
                    item.labelGroupList = mapLabelGroupList(topAds.product.labelGroupList)
                    item.freeOngkirDataView = mapFreeOngkir(topAds.product.freeOngkir)
                    item.position = topAdsCount
                    item.categoryID = topAds.product.category.id
                    item.categoryBreadcrumb = topAds.product.categoryBreadcrumb
                    item.productUrl = topAds.product.uri
                    item.minOrder = topAds.product.productMinimumOrder
                    list.add(i, item)
                    j++
                    topAdsCount++
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        return list
    }

    private fun isLocalSearch() = navSource.isNotEmpty() && pageId.isNotEmpty()

    private fun mapBadges(badges: List<Badge>): List<BadgeItemDataView> {
        return badges.map { badge ->
            BadgeItemDataView(badge.imageUrl, badge.title, badge.isShow)
        }
    }

    private fun mapLabelGroupList(labelGroupList: List<LabelGroup>): List<LabelGroupDataView> {
        return labelGroupList.map { labelGroup ->
            LabelGroupDataView(
                    labelGroup.position, labelGroup.type, labelGroup.title, labelGroup.imageUrl
            )
        }
    }

    private fun mapFreeOngkir(freeOngkir: FreeOngkir): FreeOngkirDataView {
        return FreeOngkirDataView(freeOngkir.isActive, freeOngkir.imageUrl)
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
        view.showNetworkError(startFrom, error)
        view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), error)
    }

    private fun decrementStart() {
        startFrom -= SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt()
    }

    override fun loadData(searchParameter: Map<String, Any>) {
        view ?: return

        navSource = searchParameter.getValueString(SearchApiConst.NAVSOURCE)
        pageId = searchParameter.getValueString(SearchApiConst.SRP_PAGE_ID)
        pageTitle = searchParameter.getValueString(SearchApiConst.SRP_PAGE_TITLE)
        searchRef = searchParameter.getValueString(SearchApiConst.SEARCH_REF)
        additionalParams = ""

        val requestParams = createInitializeSearchParam(searchParameter)
        enrichWithRelatedSearchParam(requestParams)

        val useCaseRequestParams = createSearchProductRequestParams(requestParams)

        view.stopPreparePagePerformanceMonitoring()
        view.startNetworkRequestPerformanceMonitoring()

        // Unsubscribe first in case user has slow connection, and the previous loadDataUseCase has not finished yet.
        searchProductFirstPageUseCase.unsubscribe()
        searchProductFirstPageUseCase.execute(useCaseRequestParams, getLoadDataSubscriber(requestParams.parameters))
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
        view.showNetworkError(0, throwable)
        view.hideRefreshLayout()
        view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), throwable)
    }

    private fun loadDataSubscriberOnNext(searchParameter: Map<String, Any>, searchProductModel: SearchProductModel) {
        if (isViewNotAttached) return

        view.stopNetworkRequestPerformanceMonitoring()
        view.startRenderPerformanceMonitoring()

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

    private fun getViewToProcessSearchResult(searchParameter: Map<String, Any>, searchProductModel: SearchProductModel) {
        updateValueEnableGlobalNavWidget()

        val productDataView = createFirstProductDataView(searchProductModel)

        responseCode = productDataView.responseCode ?: ""
        suggestionDataView = productDataView.suggestionModel
        relatedDataView = productDataView.relatedDataView
        bannerDataView = productDataView.bannerDataView
        autoCompleteApplink = productDataView.autocompleteApplink ?: ""
        totalData = productDataView.totalData

        doInBackground<ProductDataView>(productDataView, this::sendTrackingNoSearchResult)

        view.setAutocompleteApplink(productDataView.autocompleteApplink)
        view.setDefaultLayoutType(productDataView.defaultView)

        if (productDataView.productList.isEmpty()) {
            getViewToHandleEmptyProductList(searchProductModel.searchProduct, productDataView)
        } else {
            getViewToShowProductList(searchParameter, searchProductModel, productDataView)
            processDefaultQuickFilter(searchProductModel)
            processQuickFilter(searchProductModel.quickFilterModel)
        }

        view.updateScrollListener()

        if (isFirstTimeLoad)
            getViewToSendTrackingSearchAttempt(productDataView)
    }

    private fun updateValueEnableGlobalNavWidget() {
        if (isViewNotAttached) return

        enableGlobalNavWidget = !view.isLandingPage
    }

    private fun sendTrackingNoSearchResult(productDataView: ProductDataView) {
        try {
            val alternativeKeyword = productDataView.relatedDataView?.relatedKeyword ?: ""
            val responseCode = productDataView.responseCode
            val responseCodeInt = responseCode.toIntOrZero()
            val keywordProcess = productDataView.keywordProcess

            if (searchNoResultCodeList.contains(responseCodeInt))
                view.sendTrackingForNoResult(responseCode, alternativeKeyword, keywordProcess)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun createFirstProductDataView(searchProductModel: SearchProductModel): ProductDataView {
        view.clearLastProductItemPositionFromCache()

        return createProductDataView(searchProductModel)
    }

    private fun getViewToHandleEmptyProductList(
            searchProduct: SearchProductModel.SearchProduct,
            productDataView: ProductDataView,
    ) {
        view.hideQuickFilterShimmering()

        if (isShowBroadMatch()) {
            getViewToShowBroadMatchToReplaceEmptySearch()
        } else {
            if (!productDataView.errorMessage.isNullOrEmpty()) {
                getViewToHandleEmptySearchWithErrorMessage(searchProduct)
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

    private fun getViewToHandleEmptySearchWithErrorMessage(searchProduct: SearchProductModel.SearchProduct) {
        view.removeLoading()
        view.setBannedProductsErrorMessage(createBannedProductsErrorMessageAsList(searchProduct))
        view.trackEventImpressionBannedProducts(true)
    }

    private fun createBannedProductsErrorMessageAsList(searchProduct: SearchProductModel.SearchProduct): List<Visitable<*>> {
        return listOf(BannedProductsEmptySearchDataView(searchProduct.header.errorMessage))
    }

    private fun getViewToShowEmptySearch(productDataView: ProductDataView) {
        val globalNavDataView = getGlobalNavViewModel(productDataView)
        val isBannerAdsAllowed = globalNavDataView == null

        clearData()
        view.removeLoading()
        view.setEmptyProduct(globalNavDataView, createEmptySearchViewModel(isBannerAdsAllowed))
    }

    private fun getGlobalNavViewModel(productDataView: ProductDataView): GlobalNavDataView? {
        val isGlobalNavWidgetAvailable = productDataView.globalNavDataView != null && enableGlobalNavWidget
        return if (isGlobalNavWidgetAvailable) productDataView.globalNavDataView else null
    }

    private fun createEmptySearchViewModel(isBannerAdsAllowed: Boolean): EmptySearchProductDataView {
        val emptySearchViewModel = EmptySearchProductDataView()
        emptySearchViewModel.isBannerAdsAllowed = isBannerAdsAllowed
        emptySearchViewModel.isFilterActive = view.isAnyFilterActive

        if (isShowLocalSearchRecommendation() && !view.isAnyFilterActive) {
            emptySearchViewModel.isLocalSearch = true
            emptySearchViewModel.globalSearchApplink = constructGlobalSearchApplink()
            emptySearchViewModel.keyword = view.queryKey
            emptySearchViewModel.pageTitle = pageTitle
        }

        return emptySearchViewModel
    }

    private fun isShowBroadMatchWithEmptyLocalSearch() =
            responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
                    && (relatedDataView?.broadMatchDataViewList?.isNotEmpty() == true)

    private fun isShowLocalSearchRecommendation() =
            responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
                    && isLocalSearch()

    private fun getViewToShowRecommendationItem() {
        view.addLoading()

        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation()
        else getGlobalSearchRecommendation()
    }

    private fun getLocalSearchRecommendation() {
        getLocalSearchRecommendationUseCase.get().execute(
                createLocalSearchRequestParams(),
                createLocalSearchRecommendationSubscriber()
        )
    }

    private fun createLocalSearchRequestParams() =
            RequestParams.create().apply {
                putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
                putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)
                putString(SearchApiConst.NAVSOURCE, navSource)
                putString(SearchApiConst.SRP_PAGE_TITLE, pageTitle)
                putString(SearchApiConst.SRP_PAGE_ID, pageId)
                putString(SearchApiConst.START, startFrom.toString())
                putString(SearchApiConst.ROWS, getSearchRows())
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
        if (startFrom == 0)
            visitableList.add(SearchProductTitleDataView(pageTitle, isRecommendationTitle = true))

        visitableList.addAll(productDataView.productList)

        incrementStart()
        totalData = searchProductModel.searchProduct.header.totalData

        view.removeLoading()
        view.addLocalSearchRecommendation(visitableList)
        if (hasNextPage()) view.addLoading()
        view.updateScrollListener()
    }

    private fun createProductViewModelMapperLocalSearchRecommendation(searchProductModel: SearchProductModel): ProductDataView {
        if (startFrom == 0) view.clearLastProductItemPositionFromCache()

        return createProductDataView(searchProductModel)
    }

    private fun getGlobalSearchRecommendation() {
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

        if (isABTestNavigationRevamp && !isEnableChooseAddress)
            list.add(SearchProductCountDataView(list.size, searchProduct.header.totalDataText))

        addPageTitle(list)

        isGlobalNavWidgetAvailable = getIsGlobalNavWidgetAvailable(productDataView)
        if (isGlobalNavWidgetAvailable) {
            productDataView.globalNavDataView?.let {
                list.add(it)

                view.sendImpressionGlobalNav(it)
                isShowHeadlineAdsBasedOnGlobalNav = it.isShowTopAds
            }
        }

        if (isEnableChooseAddress)
            list.add(ChooseAddressDataView())

        productDataView.tickerModel?.let {
            if (!isTickerHasDismissed && it.text.isNotEmpty()) {
                list.add(it)
                view.trackEventImpressionTicker(it.typeId)
            }
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

        productDataView.cpmModel?.let {
            cpmModel = it
            cpmDataList = it.data
        }

        topAdsCount = 1
        productList = createProductItemVisitableList(productDataView)
        list.addAll(productList)

        processHeadlineAds(searchParameter, list)
        additionalParams = productDataView.additionalParams

        inspirationCarouselDataView = productDataView.inspirationCarouselDataView.toMutableList()
        processInspirationCarouselPosition(searchParameter, list)

        inspirationCardDataView = productDataView.inspirationCardDataView.toMutableList()
        processInspirationCardPosition(searchParameter, list)

        processBannerAndBroadmatchInSamePosition(searchProduct, list)
        processBanner(searchProduct, list)
        processBroadMatch(searchProduct, list)
        topAdsImageViewModelList = searchProductModel.getTopAdsImageViewModelList().toMutableList()
        processTopAdsImageViewModel(searchParameter, list)

        addSearchInTokopedia(searchProduct, list)
        firstProductPositionWithBOELabel = getFirstProductPositionWithBOELabel(list)

        view.removeLoading()
        view.setProductList(list)
        view.backToTop()
        if (productDataView.totalData > getSearchRows().toIntOrZero())
            view.addLoading()
        view.stopTracePerformanceMonitoring()
    }

    private fun getFirstProductPositionWithBOELabel(list: List<Visitable<*>>): Int {
        if (productList.isEmpty()) return -1

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

    private fun addSearchInTokopedia(searchProduct: SearchProductModel.SearchProduct, list: MutableList<Visitable<*>>) {
        if (isLastPage(searchProduct) && isLocalSearch()) {
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

    private fun processHeadlineAds(searchParameter: Map<String, Any>, visitableList: MutableList<Visitable<*>>) {
        val cpmDataList = cpmDataList ?: return
        val canProcessHeadlineAds = isHeadlineAdsAllowed() && cpmDataList.isNotEmpty()

        if (!canProcessHeadlineAds) return

        val cpmDataIterator = cpmDataList.iterator()

        while (cpmDataIterator.hasNext()) {
            val data = cpmDataIterator.next()
            val position = if (data.cpm == null) -1 else data.cpm.position

            if (position < 0 || !shouldShowCpmShop(data)) {
                cpmDataIterator.remove()
                continue
            }

            if (position > productList.size) continue

            try {
                val cpmDataView = createCpmDataView(data) ?: continue

                if (position == 0 || position == 1) processHeadlineAdsAtTop(visitableList, cpmDataView)
                else processHeadlineAdsAtPosition(visitableList, position, cpmDataView)

                cpmDataIterator.remove()
            } catch (exception: java.lang.Exception) {
                exception.printStackTrace()
                view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), exception)
            }
        }
    }

    private fun isHeadlineAdsAllowed(): Boolean {
        return !isLocalSearch()
                && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav)
    }

    private fun shouldShowCpmShop(cpmData: CpmData?): Boolean {
        cpmData ?: return false
        val cpm = cpmData.cpm ?: return false

        return if (isViewWillRenderCpmShop(cpm)) true
        else isViewWillRenderCpmDigital(cpm)
    }

    private fun isViewWillRenderCpmShop(cpm: Cpm): Boolean {
        return cpm.cpmShop != null
                && cpm.cta.isNotEmpty()
                && cpm.promotedText.isNotEmpty()
    }

    private fun isViewWillRenderCpmDigital(cpm: Cpm) = cpm.templateId == 4

    private fun createCpmDataView(cpmData: CpmData): CpmDataView? {
        if (cpmModel == null) return null
        val cpmForViewModel = createCpmForViewModel(cpmData) ?: return null
        return CpmDataView(cpmForViewModel)
    }

    private fun createCpmForViewModel(cpmData: CpmData): CpmModel? {
        val cpmModel = cpmModel ?: return null

        return CpmModel().apply {
            header = cpmModel.header
            status = cpmModel.status
            error = cpmModel.error
            data = listOf(cpmData)
        }
    }

    private fun processHeadlineAdsAtTop(visitableList: MutableList<Visitable<*>>, cpmDataView: CpmDataView) {
        if (productList.isEmpty()) return

        val firstProductIndex = visitableList.indexOf(productList[0])
        if (firstProductIndex !in visitableList.indices) return

        visitableList.add(firstProductIndex, cpmDataView)
    }

    private fun processHeadlineAdsAtPosition(visitableList: MutableList<Visitable<*>>, position: Int, cpmDataView: CpmDataView) {
        val headlineAdsVisitableList = listOf(
                SeparatorDataView(),
                cpmDataView,
                SeparatorDataView(),
        )

        val product = productList[position - 1]
        val headlineAdsIndex = visitableList.indexOf(product) + 1
        visitableList.addAll(headlineAdsIndex, headlineAdsVisitableList)
    }

    private fun createBannedProductsTickerDataView(errorMessage: String): BannedProductsTickerDataView {
        val htmlErrorMessage = "$errorMessage Gunakan browser"
        return BannedProductsTickerDataView(htmlErrorMessage)
    }

    private fun processInspirationCardPosition(searchParameter: Map<String, Any>, list: MutableList<Visitable<*>>) {
        if (inspirationCardDataView.isEmpty()) return

        val inspirationCardViewModelIterator = inspirationCardDataView.iterator()
        while (inspirationCardViewModelIterator.hasNext()) {
            val data = inspirationCardViewModelIterator.next()

            if (data.position <= 0) {
                inspirationCardViewModelIterator.remove()
                continue
            }

            if (data.position <= productList.size && shouldShowInspirationCard(data.type)) {
                try {
                    val product = productList[data.position - 1]
                    list.add(list.indexOf(product) + 1, data)
                    inspirationCardViewModelIterator.remove()
                } catch (exception: Throwable) {
                    exception.printStackTrace()
                    view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), exception)
                }
            }
        }
    }

    private fun shouldShowInspirationCard(type: String) = showInspirationCardType.contains(type)

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
                    val product = productList[data.position - 1]
                    list.add(list.indexOf(product) + 1, data)
                    inspirationCarouselViewModelIterator.remove()
                } catch (exception: java.lang.Exception) {
                    exception.printStackTrace()
                    view.logWarning(UrlParamUtils.generateUrlParamString(searchParameter as Map<String?, Any>), exception)
                }
            }
        }
    }

    private fun isInvalidInspirationCarousel(data: InspirationCarouselDataView): Boolean {
        if (data.position <= 0) return true
        val firstOption = data.options.getOrNull(0)
        return data.layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
                && firstOption != null
                && !firstOption.hasProducts()
    }

    private fun shouldShowInspirationCarousel(layout: String) = showInspirationCarouselLayout.contains(layout)

    private fun processBannerAndBroadmatchInSamePosition(
            searchProduct: SearchProductModel.SearchProduct,
            list: MutableList<Visitable<*>>,
    ) {
        val bannerDataView = bannerDataView ?: return
        val relatedDataView = relatedDataView ?: return

        if (isShowBanner() && isShowBroadMatch()) {
            if (bannerDataView.position == -1 && relatedDataView.position == 0) {
                processBroadMatchAtBottom(searchProduct, list)
                processBannerAtBottom(searchProduct, list)
            } else if (bannerDataView.position == 0 && relatedDataView.position == 1) {
                processBroadMatchAtTop(list)
                processBannerAtTop(list)
            }
        }
    }

    private fun isShowBanner() = bannerDataView?.imageUrl?.isNotEmpty() == true

    private fun processBannerAtBottom(searchProduct: SearchProductModel.SearchProduct, list: MutableList<Visitable<*>>) {
        if (!isLastPage(searchProduct)) return

        bannerDataView?.let {
            list.add(it)
            bannerDataView = null
        }
    }

    private fun processBannerAtTop(list: MutableList<Visitable<*>>) {
        bannerDataView?.let {
            list.add(list.indexOf(productList[0]), it)
            bannerDataView = null
        }
    }

    private fun processBanner(searchProduct: SearchProductModel.SearchProduct, list: MutableList<Visitable<*>>) {
        try {
            if (!isShowBanner()) return
            val bannerDataView = bannerDataView ?: return

            if (bannerDataView.position == -1) processBannerAtBottom(searchProduct, list)
            else if (bannerDataView.position == 0) processBannerAtTop(list)
            else processBannerAtPosition(list)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun processBannerAtPosition(list: MutableList<Visitable<*>>) {
        val bannerDataView = bannerDataView ?: return
        if (productList.size < bannerDataView.position) return

        val productItemVisitableIndex = bannerDataView.position - 1
        val productItemVisitable = productList[productItemVisitableIndex]
        val bannerVisitableIndex = list.indexOf(productItemVisitable) + 1

        list.add(bannerVisitableIndex, bannerDataView)
        this.bannerDataView = null
    }

    private fun processBroadMatch(searchProduct: SearchProductModel.SearchProduct, list: MutableList<Visitable<*>>) {
        try {
            if (!isShowBroadMatch()) return

            val broadMatchPosition = relatedDataView?.position ?: -1
            if (broadMatchPosition == 0) processBroadMatchAtBottom(searchProduct, list)
            else if (broadMatchPosition == 1) processBroadMatchAtTop(list)
            else if (broadMatchPosition > 1) processBroadMatchAtPosition(list, broadMatchPosition)
        } catch (exception: Throwable) {
            exception.printStackTrace()
        }
    }

    private fun processBroadMatchAtBottom(searchProduct: SearchProductModel.SearchProduct, list: MutableList<Visitable<*>>) {
        if (isLastPage(searchProduct)) {
            list.add(SeparatorDataView())
            addBroadMatchToVisitableList(list)
        }
    }

    private fun isLastPage(searchProduct: SearchProductModel.SearchProduct): Boolean {
        val hasNextPage = startFrom < searchProduct.header.totalData
        return !hasNextPage
    }

    private fun processBroadMatchAtTop(list: MutableList<Visitable<*>>) {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()
        addBroadMatchToVisitableList(broadMatchVisitableList)
        broadMatchVisitableList.add(SeparatorDataView())

        list.addAll(list.indexOf(productList[0]), broadMatchVisitableList)
    }

    private fun processBroadMatchAtPosition(list: MutableList<Visitable<*>>, broadMatchPosition: Int) {
        if (productList.size < broadMatchPosition) return

        val broadMatchVisitableList = mutableListOf<Visitable<*>>()
        broadMatchVisitableList.add(SeparatorDataView())
        addBroadMatchToVisitableList(broadMatchVisitableList)
        broadMatchVisitableList.add(SeparatorDataView())

        val productItemAtBroadMatchPosition = productList[broadMatchPosition - 1]
        val broadMatchIndex = list.indexOf(productItemAtBroadMatchPosition) + 1
        list.addAll(broadMatchIndex, broadMatchVisitableList)
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
                    exception.printStackTrace()
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

    private fun processDefaultQuickFilter(searchProductModel: SearchProductModel) {
        val quickFilter = searchProductModel.quickFilterModel

        if (quickFilter.filter.size < QUICK_FILTER_MINIMUM_SIZE) {
            searchProductModel.quickFilterModel = createSearchProductDefaultQuickFilter()
        }
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        if (dynamicFilterModel == null)
            view.initFilterControllerForQuickFilter(quickFilterData.filter)

        val sortFilterItems = mutableListOf<SortFilterItem>()
        quickFilterOptionList.clear()

        quickFilterData.filter.forEach { filter ->
            val options = filter.options
            quickFilterOptionList.addAll(options)
            sortFilterItems.addAll(convertToSortFilterItem(filter.title, options))
        }

        if (sortFilterItems.isNotEmpty()) {
            view.hideQuickFilterShimmering()
            view.setQuickFilter(sortFilterItems)
        }
    }

    private fun convertToSortFilterItem(title: String, options: List<Option>) =
            options.map { option ->
                createSortFilterItem(title, option)
            }

    private fun createSortFilterItem(title: String, option: Option): SortFilterItem {
        val item = SortFilterItem(title) {
            view.onQuickFilterSelected(option)
        }

        setSortFilterItemState(item, option)

        return item
    }

    private fun setSortFilterItemState(item: SortFilterItem, option: Option) {
        if (view.isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
    }

    private fun getViewToSendTrackingSearchAttempt(productDataView: ProductDataView) {
        if (isViewNotAttached) return

        isFirstTimeLoad = false

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

        productDataView.productList.forEachIndexed { i, productItemDataView ->
            val productId = productItemDataView.productID
            val categoryIdString = productItemDataView.categoryID.toString()
            val categoryName = productItemDataView.categoryName

            if (i < 3) {
                prodIdArray.add(productId)
                afProdIds.put(productId)
                moengageTrackingCategory[categoryIdString] = categoryName
            }

            categoryIdMapping.add(categoryIdString)
            categoryNameMapping.add(categoryName)
        }

        view.sendTrackingEventAppsFlyerViewListingSearch(afProdIds, query, prodIdArray)
        view.sendTrackingEventMoEngageSearchAttempt(query, productDataView.productList.isNotEmpty(), moengageTrackingCategory)
        view.sendTrackingGTMEventSearchAttempt(
                GeneralSearchTrackingModel(
                        createGeneralSearchTrackingEventCategory(),
                        createGeneralSearchTrackingEventLabel(productDataView, query),
                        userId,
                        productDataView.productList.isNotEmpty().toString(),
                        StringUtils.join(categoryIdMapping, ","),
                        StringUtils.join(categoryNameMapping, ","),
                        createGeneralSearchTrackingRelatedKeyword(productDataView)
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
            view.showMessageFailedWishlistAction(wishlistResult.isAddWishlist)
        } else {
            view.trackWishlistRecommendationProductLoginUser(!productCardOptionsModel.isWishlisted)
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult.isAddWishlist)
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
            view.showMessageFailedWishlistAction(wishlistResult.isAddWishlist)
        } else {
            view.trackWishlistProduct(createWishlistTrackingModel(
                    productCardOptionsModel,
                    productCardOptionsModel.wishlistResult.isAddWishlist
            ))
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult.isAddWishlist)
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

        view.sendProductImpressionTrackingEvent(item, getSuggestedRelatedKeyword(), getDimension90())
    }

    private fun getSuggestedRelatedKeyword(): String {
        if (!trackRelatedKeywordResponseCodeList.contains(responseCode)) return ""
        val relatedDataView = relatedDataView ?: return ""

        return if (relatedDataView.relatedKeyword.isNotEmpty()) relatedDataView.relatedKeyword
        else ""
    }

    private fun getDimension90(): String {
        return if (isLocalSearch()) "$pageTitle.$navSource.local_search.$pageId"
        else searchRef
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

        view.sendGTMTrackingProductClick(item, userId, getSuggestedRelatedKeyword(), getDimension90())
    }

    override fun getProductCount(mapParameter: Map<String, String>?) {
        if (isViewNotAttached) return
        if (mapParameter == null) {
            view.setProductCount("0")
            return
        }

        val getProductCountRequestParams = createGetProductCountRequestParams(mapParameter)
        val getProductCountSubscriber = createGetProductCountSubscriber()
        getProductCountUseCase.get().execute(getProductCountRequestParams, getProductCountSubscriber)
    }

    private fun createGetProductCountRequestParams(mapParameter: Map<String, String>): RequestParams {
        val requestParams = createInitializeSearchParam(mapParameter)

        enrichWithRelatedSearchParam(requestParams)

        requestParams.putString(SearchApiConst.ROWS, "0")

        return requestParams
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
            getDynamicFilterUseCase.get().execute(
                    createRequestDynamicFilterParams(searchParameter),
                    createGetDynamicFilterModelSubscriber()
            )
        }
    }

    private fun createRequestDynamicFilterParams(searchParameter: Map<String, Any>): RequestParams? {
        val requestParams = RequestParams.create()

        putRequestParamsChooseAddress(requestParams)
        requestParams.putAll(searchParameter)
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT)
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE)

        return requestParams
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

    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        if (isViewNotAttached) return

        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingImpressBroadMatchAds(broadMatchItemDataView)

        view.trackBroadMatchImpression(broadMatchItemDataView)
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

        view.trackEventClickBroadMatchItem(broadMatchItemDataView)
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
        productCardOptionsModel.hasVisitShop = hasFullThreeDotsOptions
        productCardOptionsModel.hasShareProduct = hasFullThreeDotsOptions
        productCardOptionsModel.hasAddToCart = hasFullThreeDotsOptions

        productCardOptionsModel.isWishlisted = item.isWishlisted
        productCardOptionsModel.keyword = view.queryKey
        productCardOptionsModel.productId = item.productID
        productCardOptionsModel.isTopAds = item.isTopAds || item.isOrganicAds
        productCardOptionsModel.topAdsWishlistUrl = item.topadsWishlistUrl ?: ""
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

    override fun handleAddToCartAction(productCardOptionModel: ProductCardOptionsModel) {
        if (isViewNotAttached) return

        val addToCartResult = productCardOptionModel.addToCartResult
        if (!addToCartResult.isUserLoggedIn)
            view.launchLoginActivity("")
        else
            handleAddToCartForLoginUser(productCardOptionModel.addToCartResult)

        threeDotsProductItem = null
    }

    private fun handleAddToCartForLoginUser(addToCartResult: AddToCartResult) {
        if (addToCartResult.isSuccess)
            handleAddToCartSuccess(addToCartResult)
        else
            handleAddToCartError(addToCartResult)
    }

    private fun handleAddToCartSuccess(addToCartResult: AddToCartResult) {
        if (threeDotsProductItem == null || isViewNotAttached) return
        val threeDotsProductItem = threeDotsProductItem ?: return

        val cartId = addToCartResult.cartId
        val addToCartDataLayer = threeDotsProductItem.getProductAsATCObjectDataLayer(cartId)

        if (threeDotsProductItem.isAds)
            sendTrackingATCProductAds(threeDotsProductItem)

        view.trackSuccessAddToCartEvent(threeDotsProductItem.isAds, addToCartDataLayer)
        view.showAddToCartSuccessMessage()
    }

    private fun sendTrackingATCProductAds(threeDotsProductItem: ProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
                view.className,
                threeDotsProductItem.topadsClickUrl,
                threeDotsProductItem.productID,
                threeDotsProductItem.productName,
                threeDotsProductItem.imageUrl,
                SearchConstant.TopAdsComponent.TOP_ADS
        )
    }

    private fun handleAddToCartError(addToCartResult: AddToCartResult) {
        if (isViewNotAttached) return

        view.showAddToCartFailedMessage(addToCartResult.errorMessage)
    }

    override fun handleVisitShopAction() {
        if (isViewNotAttached) return
        val threeDotsProductItem = threeDotsProductItem ?: return

        if (threeDotsProductItem.isTopAds)
            sendTrackingVisitShopProductAds(threeDotsProductItem)

        view.routeToShopPage(threeDotsProductItem.shopID)
        view.trackEventGoToShopPage(threeDotsProductItem.getProductAsShopPageObjectDataLayer())

        this.threeDotsProductItem = null
    }

    private fun sendTrackingVisitShopProductAds(threeDotsProductItem: ProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
                view.className,
                threeDotsProductItem.topadsClickShopUrl,
                threeDotsProductItem.productID,
                threeDotsProductItem.productName,
                threeDotsProductItem.imageUrl,
                SearchConstant.TopAdsComponent.TOP_ADS
        )
    }

    override fun handleChangeView(position: Int, currentLayoutType: SearchConstant.ViewType) {
        if (isViewNotAttached) return

        when (currentLayoutType) {
            SearchConstant.ViewType.LIST -> switchToBigGridView(position)
            SearchConstant.ViewType.SMALL_GRID -> switchToListView(position)
            SearchConstant.ViewType.BIG_GRID -> switchToSmallGridView(position)
        }
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

    override fun onLocalizingAddressSelected() {
        if (isViewNotAttached) return

        chooseAddressData = view.chooseAddressData
        dynamicFilterModel = null

        view.reloadData()
    }

    override fun onViewResumed() {
        if (isViewNotAttached) return
        val chooseAddressData = chooseAddressData ?: return
        val isAddressDataUpdated = view.getIsLocalizingAddressHasUpdated(chooseAddressData)

        if (isAddressDataUpdated)
            onLocalizingAddressSelected()
    }

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
                searchParameter
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
    ) {
        getInspirationCarouselChipsUseCase.get().unsubscribe()

        getInspirationCarouselChipsUseCase.get().execute(
                createGetInspirationCarouselChipProductsRequestParams(clickedInspirationCarouselOption, searchParameter),
                createGetInspirationCarouselChipProductsSubscriber(adapterPosition, clickedInspirationCarouselOption)
        )
    }

    private fun createGetInspirationCarouselChipProductsRequestParams(
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
            searchParameter: Map<String, Any>,
    ): RequestParams {
        val requestParams = createInitializeSearchParam(searchParameter)

        requestParams.putString(SearchApiConst.IDENTIFIER, clickedInspirationCarouselOption.identifier)

        return requestParams
    }

    private fun createGetInspirationCarouselChipProductsSubscriber(
            adapterPosition: Int,
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
    ): Subscriber<InspirationCarouselChipsProductModel> {
        return object : Subscriber<InspirationCarouselChipsProductModel>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable) {}

            override fun onNext(inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel) {
                getInspirationCarouselChipsSuccess(
                        adapterPosition,
                        inspirationCarouselChipsProductModel,
                        clickedInspirationCarouselOption
                )
            }
        }
    }

    private fun getInspirationCarouselChipsSuccess(
            adapterPosition: Int,
            inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel,
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
    ) {
        if (isViewNotAttached) return

        val mapper = InspirationCarouselProductDataViewMapper()
        val productList = mapper.convertToInspirationCarouselProductDataView(
                inspirationCarouselChipsProductModel.searchProductCarouselByIdentifier.product,
                clickedInspirationCarouselOption.optionPosition,
                clickedInspirationCarouselOption.inspirationCarouselType,
                clickedInspirationCarouselOption.layout,
                this::productLabelGroupToLabelGroupDataView,
                clickedInspirationCarouselOption.title
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

    override fun detachView() {
        super.detachView()

        getDynamicFilterUseCase.get()?.unsubscribe()
        searchProductFirstPageUseCase.unsubscribe()
        searchProductLoadMoreUseCase.unsubscribe()
        recommendationUseCase.unsubscribe()
        getProductCountUseCase.get()?.unsubscribe()
        getLocalSearchRecommendationUseCase.get()?.unsubscribe()
        getInspirationCarouselChipsUseCase.get()?.unsubscribe()
        if (compositeSubscription?.isUnsubscribed == true) unsubscribeCompositeSubscription()
    }

    private fun unsubscribeCompositeSubscription() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
    }
}