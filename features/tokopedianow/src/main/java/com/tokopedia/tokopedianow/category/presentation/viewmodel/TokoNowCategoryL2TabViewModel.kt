package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.isNotFilterAndSortKey
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2QuickFilterMapper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addDivider
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addEmptyState
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addFeedbackWidget
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.createMapParameter
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterNotLoadedLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.findItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.findProductCardItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.getProductIndex
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapCategoryTabLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToQuickFilter
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.updateAllProductQuantity
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_CARD_ITEM
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.mapper.CategoryMenuMapper.mapCategoryMenuData
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.findAdsProductCarousel
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam.Companion.SRC_DIRECTORY_TOKONOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking
import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.VisitableMapper.updateProductItem
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFeedbackFieldToggleUseCase
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import javax.inject.Inject

class TokoNowCategoryL2TabViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val getProductCountUseCase: UseCase<String>,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getFeedbackToggleUseCase: GetFeedbackFieldToggleUseCase,
    private val aceSearchParamMapper: AceSearchParamMapper,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    companion object {
        const val DEFAULT_CATEGORY_ID = "0"
        const val DEFAULT_DEEPLINK_PARAM = "category/l2"
        const val DEEPLINK_PARAM_LVL_3 = "?sc=%s"
        const val URL_PARAM_LVL_2 = "?exclude_sc=%s"
        const val URL_PARAM_LVL_3 = "&sc=%s"
        const val PAGE_TYPE_CATEGORY = "cat%s"
        const val CATEGORY_LVL_2 = 2
        const val CATEGORY_LVL_3 = 3

        private const val FIRST_PAGE = 1
        private const val PRODUCT_ROWS = 9
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    private val _filterProductCountLiveData = MutableLiveData("")
    private val _dynamicFilterModelLiveData = MutableLiveData<DynamicFilterModel?>(null)
    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val _routeAppLinkLiveData = MutableLiveData<String>()
    private val _updateToolbarNotification = MutableLiveData<Unit>()
    private val _atcDataTracker = MutableLiveData<CategoryAtcTrackerModel>()
    private val _clickWishlistTracker = MutableLiveData<Pair<Int, String>>()
    private val _clickSimilarProductTracker = MutableLiveData<Pair<Int, String>>()
    private val _shareLiveData = SingleLiveEvent<CategorySharingModel>()

    val filterProductCountLiveData: LiveData<String> = _filterProductCountLiveData
    val dynamicFilterModelLiveData: LiveData<DynamicFilterModel?> = _dynamicFilterModelLiveData
    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    val routeAppLinkLiveData: LiveData<String> = _routeAppLinkLiveData
    val updateToolbarNotification: LiveData<Unit> = _updateToolbarNotification
    val atcDataTracker: LiveData<CategoryAtcTrackerModel> = _atcDataTracker
    val clickWishlistTracker: LiveData<Pair<Int, String>> = _clickWishlistTracker
    val clickSimilarProductTracker: LiveData<Pair<Int, String>> = _clickSimilarProductTracker
    val shareLiveData: LiveData<CategorySharingModel> = _shareLiveData

    private val filterController = FilterController()
    private val visitableList = mutableListOf<Visitable<*>>()

    private var page = FIRST_PAGE
    private var quickFilterData: DataValue = DataValue()
    private var categoryData: CategoryL2TabData = CategoryL2TabData()
    private var filterBottomSheetOpened: Boolean = false

    private var getProductJob: Job? = null
    private var isAllProductShown = false

    private val categoryIdL1: String
        get() = categoryData.categoryIdL1
    private val categoryIdL2: String
        get() = categoryData.categoryIdL2
    private val queryParams
        get() = categoryData.queryParamMap

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        updateProductCartQuantity(miniCartData)
    }

    fun onViewCreated(data: CategoryL2TabData) {
        setCategoryData(data)
        initAffiliateCookie()
        loadFirstPage()
    }

    fun onCartQuantityChanged(
        product: ProductCardCompactUiModel,
        shopId: String,
        quantity: Int,
        layoutType: String
    ) {
        val productId = product.productId
        val isVariant = product.isVariant
        val stock = product.availableStock

        onCartQuantityChanged(
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            stock = stock,
            isVariant = isVariant,
            onSuccessAddToCart = {
                trackAddToCart(product, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = { _, _ ->
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { _, _ ->
                updateToolbarNotification()
            }
        )
    }

    fun updateProductCartQuantity(miniCartData: MiniCartSimplifiedData) {
        visitableList.updateAllProductQuantity(
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        updateVisitableListLiveData()
    }

    fun removeProductRecommendationWidget() {
        visitableList.removeItem<TokoNowProductRecommendationUiModel>()
        updateVisitableListLiveData()
    }

    fun getCategoryIdForTracking() =
        if (categoryIdL2.isNotEmpty()) {
            "$categoryIdL1/$categoryIdL2"
        } else {
            categoryIdL1
        }

    fun updateWishlistStatus(productId: String, hasBeenWishlist: Boolean) {
        val index = visitableList.updateProductItem(
            productId = productId,
            hasBeenWishlist = hasBeenWishlist
        )
        trackClickWishlistButton(index, productId)
        updateVisitableListLiveData()
    }

    fun routeToProductDetailPage(productId: String, appLink: String = "") {
        val uri = appLink.ifEmpty {
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
        }
        val affiliateLink = createAffiliateLink(uri)
        _routeAppLinkLiveData.postValue(affiliateLink)
    }

    fun onOpenFilterPage() {
        if (filterBottomSheetOpened) return
        filterBottomSheetOpened = true

        launchCatchError(block = {
            val dynamicFilterModel = dynamicFilterModelLiveData.value

            val getFilterResponse = if (dynamicFilterModel == null) {
                val response = getSortFilterUseCase.execute(createRequestQueryParams())
                val filterList = response.data.filter

                filterController.appendFilterList(queryParams, filterList)
                response
            } else {
                dynamicFilterModel
            }

            _dynamicFilterModelLiveData.postValue(getFilterResponse)
        }) {
            filterBottomSheetOpened = false
        }
    }

    fun getProductCount(option: Option) {
        val queryParamWithoutOption = queryParams.toMutableMap().apply { removeOption(option) }
        val mapParameter = queryParamWithoutOption + mapOf(option.key to option.value)
        getProductCount(mapParameter)
    }

    fun getProductCount(mapParameter: Map<String, String>) {
        getProductCountUseCase.cancelJobs()
        getProductCountUseCase.execute(
            { _filterProductCountLiveData.postValue(it) },
            {},
            createGetProductCountRequestParams(mapParameter)
        )
    }

    fun getMapParameter(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
            put(SearchApiConst.NAVSOURCE, TOKONOW_DIRECTORY)
            put(SearchApiConst.SOURCE, TOKONOW_DIRECTORY)
            put(SearchApiConst.SRP_PAGE_ID, categoryIdL1)
            putAll(queryParams)
        }
    }

    fun applyQuickFilter(filter: Filter, option: Option) {
        val isSelected = getQuickFilterIsSelected(filter)
        applyFilter(option, !isSelected)
    }

    fun applySortFilter(sortFilter: ApplySortFilterModel) {
        filterController.refreshMapParameter(sortFilter.mapParameter)
        refreshQueryParamFromFilterController()
        refreshPage()
    }

    fun applyFilterFromCategoryChooser(selectedOption: Option) {
        removeAllCategoryFilter(selectedOption)
        applyFilter(selectedOption, true)
    }

    fun onDismissFilterBottomSheet() {
        filterBottomSheetOpened = false
    }

    fun refreshPage() {
        resetPageState()
        loadFirstPage()
        getMiniCart()
    }

    fun onScroll(atTheBottomOfThePage: Boolean) {
        if (atTheBottomOfThePage) loadMore()
    }

    fun onRemoveFilter(option: Option) {
        resetSortFilterIfExclude(option)
        resetSortFilterIfPminPmax(option)
        filterController.refreshMapParameter(queryParams)
        applyFilter(option, false)
    }

    fun getCategoryMenuData() {
        launchCatchError(block = {
            getCategoryMenuDataAsync().await()
        }) {
        }
    }

    fun onClickSimilarProduct(productId: String) {
        val index = visitableList.getProductIndex(productId)
        _clickSimilarProductTracker.postValue(Pair(index, productId))
    }

    fun onResume() {
        getMiniCart()
        updateSharingDataModel()
    }

    fun removeTicker() {
        visitableList.removeFirst { it is TokoNowTickerUiModel }
        updateVisitableListLiveData()
    }

    private fun setCategoryData(data: CategoryL2TabData) {
        val tickerData = data.tickerData
        val blockAddToCart = tickerData.blockAddToCart
        this.hasBlockedAddToCart = blockAddToCart
        this.categoryData = data
    }

    private fun loadFirstPage() {
        launchCatchError(block = {
            page = FIRST_PAGE
            val getProductResponse = getCategoryProduct()
            val productList = getProductResponse.searchProduct.data.productList

            if (productList.isNotEmpty()) {
                val tickerData = categoryData.tickerData
                val components = categoryData.componentList
                val categoryDetail = categoryData.categoryDetail
                val totalData = getProductResponse.searchProduct.header.totalData
                val filterMap = filterController.getParameter()
                isAllProductShown = totalData == productList.count()

                visitableList.mapCategoryTabLayout(
                    categoryIdL2,
                    tickerData,
                    categoryDetail,
                    filterMap,
                    components
                )

                updateVisitableListLiveData()
                getComponentsData(getProductResponse)
                addCategoryRecommendationItem(isAllProductShown)
            } else {
                showEmptyState(getProductResponse)
            }

            page++
        }) {
        }.let {
            getProductJob = it
        }
    }

    private suspend fun getComponentsData(getProductResponse: AceSearchProductModel) {
        visitableList.filterNotLoadedLayout().forEach {
            when (it) {
                is CategoryQuickFilterUiModel -> getQuickFilterAsync(it).await()
                is TokoNowAdsCarouselUiModel -> getAdsProductListAsync(it).await()
                is CategoryProductListUiModel -> addProductList(it, getProductResponse)
            }
        }
    }

    private fun loadMore() {
        if (getProductJob?.isCompleted != false && !isAllProductShown) {
            launchCatchError(block = {
                showLoadMoreLoading()
                val getProductResponse = getCategoryProduct()
                val productList = getProductResponse.searchProduct.data.productList
                addProductCardItems(getProductResponse)
                isAllProductShown = productList.isEmpty()
                addCategoryRecommendationItem(isAllProductShown)
                hideLoadMoreLoading()
                page++
            }) {
            }.let {
                getProductJob = it
            }
        }
    }

    private fun addCategoryRecommendationItem(allProductShown: Boolean) {
        visitableList.addCategoryRecommendation(
            categoryData.componentList,
            categoryData.categoryDetail,
            allProductShown
        )
        updateVisitableListLiveData()
    }

    private fun showEmptyState(aceSearchResponse: AceSearchProductModel) {
        launchCatchError(block = {
            val violation = aceSearchResponse.searchProduct.data.violation
            val quickFilterItem = visitableList.findItem<CategoryQuickFilterUiModel>()
                ?: CategoryQuickFilterUiModel(id = CategoryStaticLayoutId.QUICK_FILTER)
            isAllProductShown = true

            visitableList.clear()
            visitableList.add(quickFilterItem)
            visitableList.addEmptyState(
                violation = violation
            )
            visitableList.addCategoryMenu()
            visitableList.addDivider()
            visitableList.addProductRecommendation()
            updateVisitableListLiveData()

            getQuickFilterAsync(quickFilterItem).await()
            getCategoryMenuDataAsync().await()
            getFeedbackToggleAsync().await()
            getMiniCartAsync().await()
        }) {
        }
    }

    private fun getQuickFilterAsync(item: CategoryQuickFilterUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val requestParams = createRequestQueryParams(
                source = QUICK_FILTER_TOKONOW_DIRECTORY
            )

            val quickFilterResponse = getSortFilterUseCase.execute(
                queryParams = requestParams
            )
            quickFilterData = quickFilterResponse.data

            val categoryFilterResponse = getCategoryFilter()
            initFilterController(quickFilterResponse, categoryFilterResponse)

            val quickFilter = CategoryL2QuickFilterMapper.mapQuickFilter(
                quickFilterUiModel = item,
                quickFilterResponse = quickFilterResponse,
                categoryFilterResponse = categoryFilterResponse,
                filterController = filterController
            ).copy(mapParameter = queryParams)

            visitableList.mapToQuickFilter(
                quickFilterUiModel = quickFilter
            )

            updateVisitableListLiveData()
            updateSharingDataModel()
        }) {
        }
    }

    private fun initFilterController(
        quickFilterResponse: DynamicFilterModel,
        categoryFilterResponse: DynamicFilterModel
    ) {
        val filterList = quickFilterResponse.data.filter +
            categoryFilterResponse.data.filter
        filterController.initFilterController(queryParams, filterList)
    }

    private fun addProductList(
        item: CategoryProductListUiModel,
        getProductResponse: AceSearchProductModel
    ) {
        visitableList.remove(item)
        addProductCardItems(getProductResponse)
    }

    private fun addProductCardItems(getProductResponse: AceSearchProductModel) {
        visitableList.addProductCardItems(
            response = getProductResponse,
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        updateVisitableListLiveData()
    }

    private fun getAdsProductListAsync(item: TokoNowAdsCarouselUiModel): Deferred<Any?> {
        return asyncCatchError(block = {
            val params = createProductAdsParam()
            val response = getProductAdsUseCase.execute(params)

            if (response.productList.isNotEmpty()) {
                visitableList.mapProductAdsCarousel(
                    item = item,
                    response = response,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            } else {
                visitableList.remove(item)
            }

            updateVisitableListLiveData()
        }) {
            visitableList.remove(item)
        }
    }

    private suspend fun getCategoryProduct(): AceSearchProductModel {
        val queryParams = createRequestQueryParams(
            source = CATEGORY_TOKONOW_DIRECTORY,
            rows = PRODUCT_ROWS,
            page = page
        )
        return getCategoryProductUseCase.execute(queryParams)
    }

    private suspend fun getCategoryFilter(): DynamicFilterModel {
        val filterParams = createCategoryFilterQueryParams()
        return getSortFilterUseCase.execute(filterParams)
    }

    private fun getCategoryMenuDataAsync(): Deferred<Unit?> {
        val warehouseId = addressData.getWarehouseId().toString()

        return asyncCatchError(block = {
            val localCacheModel = addressData.getAddressData()
            val warehouses = AddressMapper.mapToWarehousesData(localCacheModel)
            val response = getCategoryListUseCase.execute(
                warehouses,
                CATEGORY_LEVEL_DEPTH
            )
            visitableList.mapCategoryMenuData(response.data, warehouseId)
            updateVisitableListLiveData()
        }) {
            visitableList.mapCategoryMenuData(emptyList(), warehouseId)
            updateVisitableListLiveData()
        }
    }

    private fun getFeedbackToggleAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            val toggle = getFeedbackToggleUseCase.execute()
            if (!toggle.isActive) return@asyncCatchError
            visitableList.addFeedbackWidget()
            updateVisitableListLiveData()
        }) {
        }
    }

    private fun getMiniCartAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            getMiniCart()
        }) {
        }
    }

    private fun applyFilter(option: Option, isApplied: Boolean) {
        filterController.setFilter(
            option = option,
            isFilterApplied = isApplied,
            isCleanUpExistingFilterWithSameKey = option.isCategoryOption
        )

        refreshQueryParamFromFilterController()
        refreshPage()
    }

    private fun createCategoryFilterQueryParams(): Map<String?, Any?> {
        val queryParams = createRequestQueryParams(
            source = CATEGORY_TOKONOW_DIRECTORY
        )
        val mapParameter = createMapParameter(queryParams)
        val categoryFilterQueryParams = mutableMapOf<String?, Any?>()

        val filterParams = FilterHelper
            .createParamsWithoutExcludes(mapParameter)

        categoryFilterQueryParams.putAll(filterParams)
        return categoryFilterQueryParams
    }

    private fun createRequestQueryParams(
        source: String = TOKONOW_DIRECTORY,
        rows: Int = DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE,
        page: Int? = FIRST_PAGE
    ): MutableMap<String?, Any?> {
        return mutableMapOf<String?, Any?>().apply {
            val aceSearchParams = createAceSearchParams(source, rows, page)
            val filterParams = createFilterParams(source)

            putAll(aceSearchParams)
            putAll(filterParams)
        }
    }

    private fun createFilterParams(source: String): MutableMap<String, String> {
        return FilterHelper.createParamsWithoutExcludes(queryParams)
            .toMutableMap()
            .also {
                it[SearchApiConst.NAVSOURCE] = source
                it[SearchApiConst.SOURCE] = source
            }
    }

    private fun createAceSearchParams(
        source: String,
        rows: Int,
        page: Int?
    ): MutableMap<String?, Any?> {
        return aceSearchParamMapper.createRequestParams(
            source = source,
            srpPageId = categoryIdL1,
            sc = categoryIdL2,
            rows = rows,
            page = page
        )
    }

    private fun createGetProductCountRequestParams(
        mapParameter: Map<String, String>
    ): RequestParams {
        val getProductCountParams = createRequestQueryParams(rows = 0, page = null)
        getProductCountParams.putAll(FilterHelper.createParamsWithoutExcludes(mapParameter))

        val getProductCountRequestParams = RequestParams.create()
        getProductCountRequestParams.putAll(getProductCountParams)

        return getProductCountRequestParams
    }

    private fun createProductAdsParam(): Map<String?, Any> {
        val getProductAdsParam = GetProductAdsParam(
            categoryId = categoryIdL2,
            addressData = addressData.getAddressData(),
            src = SRC_DIRECTORY_TOKONOW,
            userId = userSession.userId
        ).generateQueryParams()

        return getProductAdsParam.also {
            it.putAll(FilterHelper.createParamsWithoutExcludes(queryParams))
        }
    }

    private fun refreshQueryParamFromFilterController() {
        queryParams.clear()
        queryParams.putAll(filterController.getParameter())
    }

    private fun getQuickFilterIsSelected(filter: Filter) =
        filter.options.any {
            if (it.key.contains(OptionHelper.EXCLUDE_PREFIX)) {
                false
            } else {
                filterController.getFilterViewState(it)
            }
        }

    private fun MutableMap<String, String>.removeOption(option: Option) {
        remove(option.key)
        remove(OptionHelper.getKeyRemoveExclude(option))
    }

    private fun removeAllCategoryFilter(chosenCategoryFilter: Option) {
        queryParams.removeOption(chosenCategoryFilter)
        filterController.refreshMapParameter(queryParams)
    }

    private fun resetSortFilterIfExclude(option: Option) {
        val isOptionKeyHasExclude = option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)

        if (!isOptionKeyHasExclude) return

        queryParams.remove(option.key)
        queryParams.entries.retainAll { it.isNotFilterAndSortKey() }
        queryParams[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun resetSortFilterIfPminPmax(option: Option) {
        val isOptionKeyHasPminPmax = option.key == SearchApiConst.PMIN || option.key == SearchApiConst.PMAX

        if (!isOptionKeyHasPminPmax) return

        queryParams.remove(SearchApiConst.PMIN)
        queryParams.remove(SearchApiConst.PMAX)
    }

    private fun showLoadMoreLoading() {
        visitableList.addLoadMoreLoading()
        updateVisitableListLiveData()
    }

    private fun hideLoadMoreLoading() {
        visitableList.removeLoadMoreLoading()
        updateVisitableListLiveData()
    }

    private fun updateVisitableListLiveData() {
        val items = visitableList.toMutableList()
        _visitableListLiveData.postValue(items)
    }

    private fun resetPageState() {
        page = FIRST_PAGE
        isAllProductShown = false
        filterBottomSheetOpened = false
        _dynamicFilterModelLiveData.postValue(null)
    }

    private fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(Unit)
    }

    private fun trackAddToCart(
        product: ProductCardCompactUiModel,
        quantity: Int,
        layoutType: String
    ) {
        when (layoutType) {
            PRODUCT_ADS_CAROUSEL -> trackProductAdsAddToCart(product, quantity)
            PRODUCT_CARD_ITEM -> trackProductAddToCart(product, quantity)
        }
    }

    private fun trackProductAdsAddToCart(
        product: ProductCardCompactUiModel,
        quantity: Int
    ) {
        visitableList.findAdsProductCarousel(product.productId).let { item ->
            val trackerModel = CategoryAtcTrackerModel(
                index = item.position,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                quantity = quantity,
                shopId = item.shopId,
                shopName = item.shopName,
                shopType = item.shopType,
                categoryBreadcrumbs = item.categoryBreadcrumbs,
                product = item.productCardModel,
                layoutType = PRODUCT_ADS_CAROUSEL
            )
            _atcDataTracker.postValue(trackerModel)
        }
    }

    private fun trackProductAddToCart(
        product: ProductCardCompactUiModel,
        quantity: Int
    ) {
        visitableList.findProductCardItem(product.productId).let { item ->
            val trackerModel = CategoryAtcTrackerModel(
                index = item.position,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = categoryIdL2,
                quantity = quantity,
                shopId = item.shop.id,
                shopName = item.shop.name,
                shopType = item.shopType,
                categoryBreadcrumbs = item.categoryBreadcrumbs,
                product = item.productCardModel,
                layoutType = PRODUCT_CARD_ITEM
            )
            _atcDataTracker.postValue(trackerModel)
        }
    }

    private fun trackClickWishlistButton(index: Int, productId: String) {
        _clickWishlistTracker.postValue(Pair(index, productId))
    }

    private fun updateSharingDataModel() {
        var categoryIdLvl2 = categoryIdL2
        var categoryIdLvl3 = DEFAULT_CATEGORY_ID
        val categoryDetail = categoryData.categoryDetail

        queryParams.forEach {
            when (it.key) {
                "${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}" -> categoryIdLvl2 = it.value
                SearchApiConst.SC -> categoryIdLvl3 = it.value
            }
        }

        val title = getTitleCategory(categoryIdLvl3)
        val constructedLink = getConstructedLink(categoryDetail.data.url, categoryIdLvl2, categoryIdLvl3)
        val utmCampaignList = getUtmCampaignList(categoryIdLvl2, categoryIdLvl3)

        _shareLiveData.postValue(
            CategorySharingModel(
                categoryIdLvl2 = categoryIdLvl2,
                categoryIdLvl3 = categoryIdLvl3,
                title = title,
                deeplinkParam = constructedLink.first,
                url = constructedLink.second,
                utmCampaignList = utmCampaignList
            )
        )
    }

    private fun getTitleCategory(categoryIdLvl3: String): String {
        val filterList = quickFilterData.filter
        return if (categoryIdLvl3 != DEFAULT_CATEGORY_ID && filterList.isNotEmpty()) {
            filterList.first().title.removePrefix(CategoryTracking.Misc.PREFIX_ALL).trim()
        } else {
            categoryData.title
        }
    }

    private fun getConstructedLink(categoryUrl: String, categoryIdLvl2: String, categoryIdLvl3: String): Pair<String, String> {
        var deeplinkParam = "$DEFAULT_DEEPLINK_PARAM/$categoryIdL1"
        var url = categoryUrl

        deeplinkParam += "/$categoryIdLvl2"
        url += String.format(URL_PARAM_LVL_2, categoryIdLvl2)

        if (categoryIdLvl3 != DEFAULT_CATEGORY_ID) {
            deeplinkParam += String.format(DEEPLINK_PARAM_LVL_3, categoryIdLvl3)
            url += String.format(URL_PARAM_LVL_3, categoryIdLvl3)
        }

        return Pair(deeplinkParam, url)
    }

    private fun getUtmCampaignList(categoryIdLvl2: String, categoryIdLvl3: String): List<String> {
        val categoryId: String
        val categoryLvl: Int
        when {
            categoryIdLvl3 != DEFAULT_CATEGORY_ID -> {
                categoryLvl = CATEGORY_LVL_3
                categoryId = categoryIdLvl3
            }
            else -> {
                categoryLvl = CATEGORY_LVL_2
                categoryId = categoryIdLvl2
            }
        }
        return listOf(String.format(PAGE_TYPE_CATEGORY, categoryLvl), categoryId)
    }
}
