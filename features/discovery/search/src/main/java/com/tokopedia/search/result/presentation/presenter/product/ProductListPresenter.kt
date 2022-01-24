package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.IDENTIFIER
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SRP_COMPONENT_ID
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.SAVE_LAST_FILTER_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS
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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
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
import com.tokopedia.search.result.presentation.mapper.InspirationCarouselProductDataViewMapper
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.model.BroadMatch
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchProduct
import com.tokopedia.search.result.presentation.model.CarouselOptionType
import com.tokopedia.search.result.presentation.model.CarouselProductType
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.DynamicCarouselOption
import com.tokopedia.search.result.presentation.model.DynamicCarouselProduct
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
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.utils.SchedulersProvider
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.createSearchProductDefaultFilter
import com.tokopedia.search.utils.createSearchProductDefaultQuickFilter
import com.tokopedia.search.utils.getValueString
import com.tokopedia.search.utils.toSearchParams
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.TopAdsConstants.SEEN_ADS
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.model.Badge
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.LabelGroup
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
import java.util.HashSet
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
        private val topAdsHeadlineHelper : TopAdsHeadlineHelper
): BaseDaggerPresenter<ProductListSectionContract.View>(),
        ProductListSectionContract.Presenter {

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
        private const val RESPONSE_CODE_RELATED = "3"
        private const val RESPONSE_CODE_SUGGESTION = "6"
        private const val EMPTY_LOCAL_SEARCH_RESPONSE_CODE = "11"
    }

    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()

    private var enableGlobalNavWidget = true
    private var additionalParams = ""
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
    private var dimension90 = ""
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
    override var dynamicFilterModel: DynamicFilterModel? = null
        private set
    private var threeDotsProductItem: ProductItemDataView? = null
    private var firstProductPositionWithBOELabel = -1
    private var isEnableChooseAddress = false
    private var chooseAddressData: LocalCacheModel? = null
    private var bannerDataView: BannerDataView? = null
    private var categoryIdL2 = ""
    private var suggestionKeyword = ""
    private var relatedKeyword = ""
    override var pageComponentId: String = ""
        private set

    override fun attachView(view: ProductListSectionContract.View) {
        super.attachView(view)

        isEnableChooseAddress = view.isChooseAddressWidgetEnabled
        if (isEnableChooseAddress) chooseAddressData = view.chooseAddressData
    }

    //region AB Test booleans
    private val isABTestNegativeNoAds : Boolean  by lazy {
        getABTestNegativeNoAds()
    }

    private fun getABTestNegativeNoAds() :Boolean {
        return try {
            val abTestKeywordAdvNeg = view.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_ADVANCED_KEYWORD_ADV_NEG,
                ""
            )
            RollenceKey.SEARCH_ADVANCED_NEGATIVE_NO_ADS == abTestKeywordAdvNeg
        } catch (e: Exception) {
            false
        }
    }
    //endregion

    override val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID

    override val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    override val deviceId: String
        get() = userSession.deviceId

    //region Ticker
    override var isTickerHasDismissed = false
        private set

    override fun onPriceFilterTickerDismissed() {
        isTickerHasDismissed = true
    }
    //endregion

    //region Load Data / Load More / Recommendations
    override fun hasNextPage(): Boolean {
        return startFrom < totalData
    }

    override fun clearData() {
        startFrom = 0
        totalData = 0
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
            Timber.w(e)
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
        val hasFilter = view.isAnyFilterActive
        val hasSort = view.isAnySortActive
        val isSkipGlobalNavWidget = isLocalSearch || hasFilter || hasSort
        val isSkipGetLastFilterWidget = hasFilter || hasSort

        return RequestParams.create().apply {
            putObject(SEARCH_PRODUCT_PARAMS, requestParams.parameters)

            putBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, isLocalSearch)
            putBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, isLocalSearch)
            putBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, isLocalSearch)
            putBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, isLocalSearch)
            putBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, isSkipGlobalNavWidget)
            putBoolean(SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET, isSkipGetLastFilterWidget)
            putString(SEEN_ADS, topAdsHeadlineHelper.seenAds.toString())
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

    private fun isDisableAdsNegativeKeywords(productDataView: ProductDataView) :Boolean {
        return isABTestNegativeNoAds && productDataView.isAdvancedNegativeKeywordSearch()
    }

    private fun isHideProductAds(productDataView: ProductDataView) : Boolean {
        return isLocalSearch() || isDisableAdsNegativeKeywords(productDataView)
    }

    private fun createProductDataView(
        searchProductModel: SearchProductModel,
        pageTitleEventLabel: String = "",
    ): ProductDataView {
        val lastProductItemPosition = view.lastProductItemPositionFromCache
        val keyword = view.queryKey

        val productDataView = ProductViewModelMapper().convertToProductViewModel(
            lastProductItemPosition,
            searchProductModel,
            pageTitleEventLabel,
            isLocalSearch(),
            dimension90,
            keyword,
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

        processHeadlineAdsLoadMore(searchProductModel, list)
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
        if (isHideProductAds(productDataView)) return list
        val adsModel = productDataView.adsModel ?: return list

        var j = 0
        for (i in 0 until productDataView.getTotalItem()) {
            try {
                // Already surrounded by try catch per looping, safe to force nullable
                if (adsModel.templates.size <= 0) continue
                if (adsModel.templates[i].isIsAd) {
                    val topAds = adsModel.data[j]
                    val item = ProductItemDataView()
                    item.productID = topAds.product.id
                    item.isTopAds = true
                    item.topadsImpressionUrl = topAds.product.image.s_url
                    item.topadsClickUrl = topAds.productClickUrl
                    item.topadsWishlistUrl = topAds.productWishlistUrl
                    item.topadsClickShopUrl = topAds.shopClickUrl
                    item.topadsTag = topAds.tag
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
                    item.dimension90 = dimension90
                    list.add(i, item)
                    j++
                    topAdsCount++
                }
            } catch (e: java.lang.Exception) {
                Timber.w(e)
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

    private fun processHeadlineAdsLoadMore(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed()) return

        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel) { _, cpmDataList, isUseSeparator ->
            val cpmDataView = createCpmDataView(searchProductModel.cpmModel, cpmDataList)
            processHeadlineAdsAtPosition(list, productList.size, cpmDataView, isUseSeparator)
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
        dimension90 = Dimension90Utils.getDimension90(searchParameter)
        additionalParams = ""

        val requestParams = createInitializeSearchParam(searchParameter)
        enrichWithRelatedSearchParam(requestParams)

        val useCaseRequestParams = createSearchProductRequestParams(requestParams)

        view.stopPreparePagePerformanceMonitoring()
        view.startNetworkRequestPerformanceMonitoring()

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

    private fun getViewToProcessSearchResult(
        searchParameter: Map<String, Any>,
        searchProductModel: SearchProductModel,
    ) {
        updateValueEnableGlobalNavWidget()

        val productDataView = createFirstProductDataView(searchProductModel)

        responseCode = productDataView.responseCode ?: ""
        suggestionDataView = productDataView.suggestionModel
        relatedDataView = productDataView.relatedDataView
        bannerDataView = productDataView.bannerDataView
        autoCompleteApplink = productDataView.autocompleteApplink ?: ""
        totalData = productDataView.totalData
        categoryIdL2 = productDataView.categoryIdL2
        relatedKeyword = searchProductModel.searchProduct.data.related.relatedKeyword
        suggestionKeyword = searchProductModel.searchProduct.data.suggestion.suggestion
        pageComponentId = productDataView.pageComponentId

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

        getViewToSendTrackingSearchAttempt(productDataView)
    }

    private fun updateValueEnableGlobalNavWidget() {
        if (isViewNotAttached) return

        enableGlobalNavWidget = !view.isLandingPage
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

        return createProductDataView(searchProductModel, pageTitle)
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

        if (!isEnableChooseAddress)
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

        addLastFilterDataView(list, productDataView)

        if (isEnableChooseAddress)
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

        topAdsCount = 1
        productList = createProductItemVisitableList(productDataView)
        list.addAll(productList)

        processHeadlineAdsFirstPage(searchProductModel, list)

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

    private fun processHeadlineAdsFirstPage(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed()) return
        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel, 1) { index, cpmDataList,  isUseSeparator ->
            val cpmDataView = createCpmDataView(searchProductModel.cpmModel, cpmDataList)
            if (index == 0)
                processHeadlineAdsAtTop(list, cpmDataView)
            else
                processHeadlineAdsAtPosition(list, productList.size, cpmDataView, isUseSeparator)
        }
    }

    private fun isHeadlineAdsAllowed(): Boolean {
        return !isLocalSearch()
                && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav)
    }

    private fun createCpmDataView(cpmModel: CpmModel, cpmData: ArrayList<CpmData>): CpmDataView {
        val cpmForViewModel = createCpmForViewModel(cpmModel, cpmData)
        return CpmDataView(cpmForViewModel)
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
            isUseSeparator: Boolean,
    ) {
        val headlineAdsVisitableList = arrayListOf<Visitable<ProductListTypeFactory>>()
        if (isUseSeparator) {
            headlineAdsVisitableList.add(SeparatorDataView())
            headlineAdsVisitableList.add(cpmDataView)
            headlineAdsVisitableList.add(SeparatorDataView())
        } else {
            headlineAdsVisitableList.add(cpmDataView)
        }


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
                    Timber.w(exception)
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
        val firstOption = data.options.getOrNull(0)
        return data.layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
                && firstOption != null
                && !firstOption.hasProducts()
    }

    private fun shouldShowInspirationCarousel(layout: String) = showInspirationCarouselLayout.contains(layout)

    private fun constructInspirationCarouselVisitableList(data: InspirationCarouselDataView) =
            if (data.isDynamicProductLayout())
                convertInspirationCarouselToBroadMatch(data)
            else
                listOf(data)

    private fun InspirationCarouselDataView.isDynamicProductLayout() =
            layout == SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT

    private fun convertInspirationCarouselToBroadMatch(data: InspirationCarouselDataView): List<Visitable<*>> {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()

        broadMatchVisitableList.add(SeparatorDataView())
        if (data.title.isNotEmpty()) broadMatchVisitableList.add(SuggestionDataView(data.title))
        broadMatchVisitableList.addAll(data.options.mapToBroadMatchDataView(data.type))
        broadMatchVisitableList.add(SeparatorDataView())

        return broadMatchVisitableList
    }

    private fun List<InspirationCarouselDataView.Option>.mapToBroadMatchDataView(
            type: String,
    ): List<Visitable<*>> {
        return map { option ->
            BroadMatchDataView(
                keyword = option.title,
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
                    )
                }
            )
        }
    }

    private fun determineCarouselOptionType(
        type: String,
        option: InspirationCarouselDataView.Option
    ): CarouselOptionType =
        if (type == TYPE_INSPIRATION_CAROUSEL_KEYWORD) BroadMatch
        else DynamicCarouselOption(option)

    private fun determineInspirationCarouselProductType(
            type: String,
            option: InspirationCarouselDataView.Option,
            product: InspirationCarouselDataView.Option.Product,
    ): CarouselProductType {
        return if (type == TYPE_INSPIRATION_CAROUSEL_KEYWORD)
            BroadMatchProduct(false)
        else
            DynamicCarouselProduct(option.inspirationCarouselType, product)
    }

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
            Timber.w(throwable)
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
            Timber.w(exception)
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
            sortFilterItems.addAll(convertToSortFilterItem(filter, options))
        }

        if (sortFilterItems.isNotEmpty()) {
            view.hideQuickFilterShimmering()
            view.setQuickFilter(sortFilterItems)
        }
    }

    private fun convertToSortFilterItem(filter: Filter, options: List<Option>) =
            options.map { option ->
                createSortFilterItem(filter, option)
            }

    private fun createSortFilterItem(filter: Filter, option: Option): SortFilterItem {
        val item = SortFilterItem(filter.title) {
            view.onQuickFilterSelected(filter, option)
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
                view.filterParamString,
                productDataView.pageComponentId
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
        if (isViewNotAttached) return

        when(val carouselOptionType = broadMatchDataView.carouselOptionType) {
            is BroadMatch -> view.trackEventClickSeeMoreBroadMatch(broadMatchDataView)
            is DynamicCarouselOption -> view.trackEventClickSeeMoreDynamicProductCarousel(
                broadMatchDataView,
                carouselOptionType.option.inspirationCarouselType,
                carouselOptionType.option,
            )
        }

        val applink = if (broadMatchDataView.applink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
            view.modifyApplinkToSearchResult(broadMatchDataView.applink)
        else broadMatchDataView.applink

        view.redirectionStartActivity(applink, broadMatchDataView.url)
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

    //region Choose Address / LCA
    override fun onLocalizingAddressSelected() {
        if (isViewNotAttached) return

        chooseAddressData = view.chooseAddressData
        dynamicFilterModel = null

        view.reloadData()
    }
    //endregion

    override fun onViewResumed() {
        if (isViewNotAttached) return
        val chooseAddressData = chooseAddressData ?: return
        val isAddressDataUpdated = view.getIsLocalizingAddressHasUpdated(chooseAddressData)

        if (isAddressDataUpdated)
            onLocalizingAddressSelected()
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

        getInspirationCarouselChipsUseCase.get().execute(
            createGetInspirationCarouselChipProductsRequestParams(
                clickedInspirationCarouselOption,
                searchParameter
            ),
            createGetInspirationCarouselChipProductsSubscriber(
                adapterPosition,
                clickedInspirationCarouselOption,
                inspirationCarouselTitle,
            )
        )
    }

    private fun createGetInspirationCarouselChipProductsRequestParams(
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
    ): RequestParams {
        val requestParams = createInitializeSearchParam(searchParameter)

        requestParams.putString(IDENTIFIER, clickedInspirationCarouselOption.identifier)
        requestParams.putString(SRP_COMPONENT_ID, clickedInspirationCarouselOption.componentId)

        return requestParams
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
        val saveLastFilterInput = SaveLastFilterInput(
            lastFilter = savedOptionList,
            mapParameter = createInitializeSearchParam(searchParameter).parameters,
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

    override fun shopAdsImpressionCount(impressionCount: Int) {
        topAdsHeadlineHelper.seenAds = impressionCount
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
        saveLastFilterUseCase.get()?.unsubscribe()
        if (compositeSubscription?.isUnsubscribed == true) unsubscribeCompositeSubscription()
    }

    private fun unsubscribeCompositeSubscription() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
    }
}
