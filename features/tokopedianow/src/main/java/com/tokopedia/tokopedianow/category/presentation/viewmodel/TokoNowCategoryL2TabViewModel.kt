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
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.isNotFilterAndSortKey
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2QuickFilterMapper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterNotLoadedLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToCategoryTabLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToQuickFilter
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.updateAllProductQuantity
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam.Companion.SRC_DIRECTORY_TOKONOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.VisitableMapper.updateProductItem
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.Violation
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowCategoryL2TabViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val getProductCountUseCase: UseCase<String>,
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
        private const val FIRST_PAGE = 1
        private const val PRODUCT_ROWS = 9
    }

    private val _filterProductCountLiveData = MutableLiveData("")
    private val _dynamicFilterModelLiveData = MutableLiveData<DynamicFilterModel?>(null)
    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val _routeAppLinkLiveData = MutableLiveData<String>()
    private val _emptyStateLiveData = MutableLiveData<CategoryEmptyStateModel>()

    val filterProductCountLiveData: LiveData<String> = _filterProductCountLiveData
    val dynamicFilterModelLiveData: LiveData<DynamicFilterModel?> = _dynamicFilterModelLiveData
    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    val routeAppLinkLiveData: LiveData<String> = _routeAppLinkLiveData
    val emptyStateLiveData: LiveData<CategoryEmptyStateModel> = _emptyStateLiveData

    private val filterController = FilterController()
    private val visitableList = mutableListOf<Visitable<*>>()
    private val queryParams = mutableMapOf<String, String>()

    private var page = FIRST_PAGE
    private var categoryIdL1: String = ""
    private var categoryIdL2: String = ""
    private var components: List<Component> = emptyList()
    private var filterBottomSheetOpened: Boolean = false

    private var getProductJob: Job? = null
    private var violation: Violation = Violation()
    private var excludedFilter: Option? = null
    private var isAllProductShown = false

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        visitableList.updateAllProductQuantity(
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        updateVisitableListLiveData()
    }

    fun onViewCreated(
        categoryIdL1: String,
        categoryIdL2: String,
        components: List<Component>
    ) {
        setCategoryData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            components = components
        )
        initAffiliateCookie()
        loadFirstPage()
        getMiniCart()
    }

    fun onCartQuantityChanged(
        product: ProductCardCompactUiModel,
        shopId: String,
        quantity: Int
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
            onSuccessUpdateCart = { _, _ ->
                visitableList.updateAllProductQuantity(
                    productId = productId,
                    quantity = quantity,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
                updateVisitableListLiveData()
            }
        )
    }

    fun updateWishlistStatus(productId: String, hasBeenWishlist: Boolean) {
        launch {
            visitableList.updateProductItem(
                productId = productId,
                hasBeenWishlist = hasBeenWishlist
            )
            updateVisitableListLiveData()
        }
    }

    fun createProductDetailAppLink(productId: String, appLink: String = "") {
        launch {
            val uri = appLink.ifEmpty {
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productId
                )
            }
            val affiliateLink = createAffiliateLink(uri)
            _routeAppLinkLiveData.postValue(affiliateLink)
        }
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

    fun getFilterController(): FilterController {
        return filterController
    }

    fun onRemoveFilter(option: Option) {
        resetSortFilterIfExclude(option)
        resetSortFilterIfPminPmax(option)
        filterController.refreshMapParameter(queryParams)
        applyFilter(option, false)
    }

    private fun setCategoryData(
        categoryIdL1: String,
        categoryIdL2: String,
        components: List<Component>
    ) {
        this.categoryIdL1 = categoryIdL1
        this.categoryIdL2 = categoryIdL2
        this.components = components
    }

    private fun loadFirstPage() {
        launchCatchError(block = {
            page = FIRST_PAGE

            visitableList.clear()
            visitableList.mapToCategoryTabLayout(components)
            updateVisitableListLiveData()

            visitableList.filterNotLoadedLayout().forEach {
                when (it) {
                    is CategoryQuickFilterUiModel -> getQuickFilterAsync(it).await()
                    is TokoNowAdsCarouselUiModel -> getAdsProductListAsync(it).await()
                    is CategoryProductListUiModel -> getProductListAsync(it).await()
                }
            }
            page++
        }) {
        }.let {
            getProductJob = it
        }
    }

    private fun loadMore() {
        if (getProductJob?.isCompleted != false && !isAllProductShown) {
            launchCatchError(block = {
                showLoadMoreLoading()
                val productList = getProductList()
                isAllProductShown = productList.isEmpty()
                hideLoadMoreLoading()
                page++
            }) {
            }.let {
                getProductJob = it
            }
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

            val categoryFilterResponse = getCategoryFilter()
            initFilterController(quickFilterResponse, categoryFilterResponse)

            val quickFilter = CategoryL2QuickFilterMapper.mapQuickFilter(
                quickFilterUiModel = item,
                quickFilterResponse = quickFilterResponse,
                categoryFilterResponse = categoryFilterResponse,
                filterController = filterController
            )

            visitableList.mapToQuickFilter(
                quickFilterUiModel = quickFilter
            )

            findExcludedFilter(quickFilter)
            updateVisitableListLiveData()
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

    private fun getProductListAsync(item: CategoryProductListUiModel): Deferred<Any?> {
        return asyncCatchError(block = {
            visitableList.remove(item)
            val productList = getProductList()
            val isEmptyState = _emptyStateLiveData.value != null

            when {
                productList.isEmpty() -> showEmptyState()
                isEmptyState -> hideEmptyState()
            }
        }) {
        }
    }

    private fun getAdsProductListAsync(item: TokoNowAdsCarouselUiModel): Deferred<Unit?> {
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
        }
    }

    private suspend fun getProductList(): List<AceSearchProductModel.Product> {
        val queryParams = createRequestQueryParams(
            source = CATEGORY_TOKONOW_DIRECTORY,
            rows = PRODUCT_ROWS,
            page = page
        )

        val response = getCategoryProductUseCase.execute(queryParams)

        visitableList.addProductCardItems(
            response = response,
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        updateVisitableListLiveData()

        return response.searchProduct.data.productList
    }

    private suspend fun getCategoryFilter(): DynamicFilterModel {
        val filterParams = createCategoryFilterQueryParams()
        return getSortFilterUseCase.execute(filterParams)
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
        val mapParameter = mutableMapOf<String, String>()
        val categoryFilterQueryParams = mutableMapOf<String?, Any?>()

        createRequestQueryParams(source = CATEGORY_TOKONOW_DIRECTORY).forEach {
            it.key?.let { key ->
                mapParameter[key] = it.value.toString()
            }
        }

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

    private fun findExcludedFilter(quickFilter: CategoryQuickFilterUiModel) {
        for (filterItem in quickFilter.itemList) {
            val options = filterItem.filter.options
            if (options.count() == 1) return

            excludedFilter = options.find {
                it.key.startsWith(OptionHelper.EXCLUDE_PREFIX)
            }
        }
    }

    private fun showEmptyState() {
        val emptyStateModel = CategoryEmptyStateModel(
            queryParams,
            violation,
            excludedFilter,
            true
        )
        _emptyStateLiveData.postValue(emptyStateModel)
    }

    private fun hideEmptyState() {
        val emptyStateModel = CategoryEmptyStateModel(
            queryParams,
            violation,
            excludedFilter,
            false
        )
        _emptyStateLiveData.postValue(emptyStateModel)
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
        _visitableListLiveData.postValue(visitableList)
    }

    private fun resetPageState() {
        page = FIRST_PAGE
        isAllProductShown = false
        filterBottomSheetOpened = false
        _dynamicFilterModelLiveData.postValue(null)
    }
}
