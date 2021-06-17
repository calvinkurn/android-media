package com.tokopedia.tokomart.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_SOURCE_SEARCH
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ADDRESS_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_CITY_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_DISTRICT_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LAT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_LONG
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_POST_CODE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.home_component.mapper.DynamicChannelComponentMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_OLD
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_REVAMP
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProductData
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.NonVariantATCDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.VariantATCDataView
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseSearchCategoryViewModel(
        protected val baseDispatcher: CoroutineDispatchers,
        queryParamMap: Map<String, String>,
        protected val getFilterUseCase: UseCase<DynamicFilterModel>,
        protected val getProductCountUseCase: UseCase<String>,
        protected val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        protected val addToCartUseCase: AddToCartUseCase,
        protected val updateCartUseCase: UpdateCartUseCase,
        protected val getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
        protected val abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseViewModel(baseDispatcher.io) {

    protected val filterController = FilterController()
    protected val chooseAddressDataView = ChooseAddressDataView()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()
    protected val queryParamMutable = queryParamMap.toMutableMap()
    protected var totalData = 0
    protected var totalFetchedData = 0
    protected var nextPage = 1
    protected var chooseAddressData: LocalCacheModel? = null
    protected var currentProductPosition: Int = 1
    private var cartItemsNonVariant: List<MiniCartItem>? = null
    private var cartItemsVariantGrouped: Map<String, List<MiniCartItem>>? = null

    val queryParam: Map<String, String> = queryParamMutable
    val hasGlobalMenu: Boolean
    var warehouseId = ""
        private set
    var autoCompleteApplink = ""
        private set

    protected val visitableListMutableLiveData = MutableLiveData<List<Visitable<*>>>(visitableList)
    val visitableListLiveData: LiveData<List<Visitable<*>>> = visitableListMutableLiveData

    protected val hasNextPageMutableLiveData = MutableLiveData(false)
    val hasNextPageLiveData: LiveData<Boolean> = hasNextPageMutableLiveData

    protected val isFilterPageOpenMutableLiveData = MutableLiveData(false)
    val isFilterPageOpenLiveData: LiveData<Boolean> = isFilterPageOpenMutableLiveData

    protected val dynamicFilterModelMutableLiveData = MutableLiveData<DynamicFilterModel?>(null)
    val dynamicFilterModelLiveData: LiveData<DynamicFilterModel?> = dynamicFilterModelMutableLiveData

    protected val productCountAfterFilterMutableLiveData = MutableLiveData("")
    val productCountAfterFilterLiveData: LiveData<String> = productCountAfterFilterMutableLiveData

    protected val isL3FilterPageOpenMutableLiveData = MutableLiveData<Filter?>(null)
    val isL3FilterPageOpenLiveData: LiveData<Filter?> = isL3FilterPageOpenMutableLiveData

    protected val isShowMiniCartMutableLiveData = MutableLiveData<Boolean?>(null)
    val isShowMiniCartLiveData: LiveData<Boolean?> = isShowMiniCartMutableLiveData

    protected val miniCartWidgetMutableLiveData = MutableLiveData<MiniCartSimplifiedData?>(null)
    val miniCartWidgetLiveData: LiveData<MiniCartSimplifiedData?> = miniCartWidgetMutableLiveData

    protected val updatedVisitableIndicesMutableLiveData =
            SingleLiveEvent<List<Int>>()
    val updatedVisitableIndicesLiveData: LiveData<List<Int>> =
            updatedVisitableIndicesMutableLiveData

    protected val successATCMessageMutableLiveData = SingleLiveEvent<String>()
    val successATCMessageLiveData: LiveData<String> = successATCMessageMutableLiveData

    protected val errorATCMessageMutableLiveData = SingleLiveEvent<String>()
    val errorATCMessageLiveData: LiveData<String> = errorATCMessageMutableLiveData

    protected val isHeaderBackgroundVisibleMutableLiveData = MutableLiveData(true)
    val isHeaderBackgroundVisibleLiveData: LiveData<Boolean> = isHeaderBackgroundVisibleMutableLiveData

    protected val isContentLoadingMutableLiveData = MutableLiveData(true)
    val isContentLoadingLiveData: LiveData<Boolean> = isContentLoadingMutableLiveData

    protected val shopIdMutableLiveData = MutableLiveData("")
    val shopIdLiveData: LiveData<String> = shopIdMutableLiveData

    protected val isOutOfServiceMutableLiveData = MutableLiveData(false)
    val isOutOfServiceLiveData: LiveData<Boolean> = isOutOfServiceMutableLiveData

    init {
        showLoading()
        updateQueryParamWithDefaultSort()

        hasGlobalMenu = isABTestNavigationRevamp()
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
    }

    private fun showLoading() {
        isContentLoadingMutableLiveData.value = true
    }

    private fun updateQueryParamWithDefaultSort() {
        queryParamMutable[SearchApiConst.OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun isABTestNavigationRevamp() =
            getNavigationExpVariant() == NAVIGATION_VARIANT_REVAMP

    private fun getNavigationExpVariant() =
            abTestPlatformWrapper
                    .getABTestRemoteConfig()
                    ?.getString(NAVIGATION_EXP_TOP_NAV, NAVIGATION_VARIANT_OLD)

    open fun onViewCreated() {
        val shopId = chooseAddressData?.shop_id ?: ""
        val warehouseId = chooseAddressData?.warehouse_id ?: ""

        if (shopId.isValidId())
            processLoadDataWithShopId(shopId, warehouseId)
        else
            getShopIdBeforeLoadData()
    }

    protected open fun processLoadDataWithShopId(shopId: String, warehouseId: String) {
        this.shopIdMutableLiveData.value = shopId
        this.warehouseId = warehouseId

        if (warehouseId.isValidId())
            processLoadDataInCoverage()
        else
            showOutOfCoverage()
    }

    protected open fun processLoadDataInCoverage() {
        isOutOfServiceMutableLiveData.value = false

        loadFirstPage()
    }

    private fun getShopIdBeforeLoadData() {
        getShopAndWarehouseUseCase.getStateChosenAddress(
                ::onGetShopAndWarehouseSuccess,
                ::onGetShopAndWarehouseFailed,
                DEFAULT_VALUE_SOURCE_SEARCH
        )
    }

    private fun onGetShopAndWarehouseSuccess(state: GetStateChosenAddressResponse) {
        val tokonowData = state.tokonow
        val shopId = tokonowData.shopId.toString()
        val warehouseId = tokonowData.warehouseId.toString()

        processLoadDataWithShopId(shopId, warehouseId)

        refreshMiniCart()
    }

    private fun showOutOfCoverage() {
        isOutOfServiceMutableLiveData.value = true
    }

    protected open fun onGetShopAndWarehouseFailed(throwable: Throwable) {

    }

    protected abstract fun loadFirstPage()

    protected open fun createRequestParams(): RequestParams {
        val tokonowQueryParam = createTokonowQueryParams()

        val requestParams = RequestParams.create()
        requestParams.putObject(TOKONOW_QUERY_PARAMS, tokonowQueryParam)

        return requestParams
    }

    protected open fun createTokonowQueryParams(): MutableMap<String, Any> {
        val tokonowQueryParam = mutableMapOf<String, Any>()

        appendMandatoryParams(tokonowQueryParam)
        appendPaginationParam(tokonowQueryParam)
        appendQueryParam(tokonowQueryParam)

        return tokonowQueryParam
    }

    protected open fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        appendDeviceParam(tokonowQueryParam)
        appendChooseAddressParams(tokonowQueryParam)
    }

    private fun appendDeviceParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE
    }

    private fun appendChooseAddressParams(tokonowQueryParam: MutableMap<String, Any>) {
        val chooseAddressData = this.chooseAddressData ?: return

        if (chooseAddressData.city_id.isNotEmpty())
            tokonowQueryParam[USER_CITY_ID] = chooseAddressData.city_id
        if (chooseAddressData.address_id.isNotEmpty())
            tokonowQueryParam[USER_ADDRESS_ID] = chooseAddressData.address_id
        if (chooseAddressData.district_id.isNotEmpty())
            tokonowQueryParam[USER_DISTRICT_ID] = chooseAddressData.district_id
        if (chooseAddressData.lat.isNotEmpty())
            tokonowQueryParam[USER_LAT] = chooseAddressData.lat
        if (chooseAddressData.long.isNotEmpty())
            tokonowQueryParam[USER_LONG] = chooseAddressData.long
        if (chooseAddressData.postal_code.isNotEmpty())
            tokonowQueryParam[USER_POST_CODE] = chooseAddressData.postal_code
        if (chooseAddressData.warehouse_id.isNotEmpty())
            tokonowQueryParam[USER_WAREHOUSE_ID] = chooseAddressData.warehouse_id
    }

    protected open fun appendPaginationParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.PAGE] = nextPage
        tokonowQueryParam[SearchApiConst.USE_PAGE] = true
    }

    private fun appendQueryParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))
    }

    protected fun onGetFirstPageSuccess(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.aceSearchProductData.productList.size
        autoCompleteApplink = contentDataView.aceSearchProductData.autocompleteApplink
        currentProductPosition = 1

        val isEmptyProductList = contentDataView.aceSearchProductData.productList.isEmpty()

        initFilterController(headerDataView)
        createVisitableListFirstPage(headerDataView, contentDataView, isEmptyProductList)
        updateViewForFirstPage(isEmptyProductList)
    }

    private fun initFilterController(headerDataView: HeaderDataView) {
        val filterList =
                headerDataView.quickFilterDataValue.filter +
                        headerDataView.categoryFilterDataValue.filter

        filterController.initFilterController(queryParamMutable, filterList)
    }

    private fun createVisitableListFirstPage(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
            isEmptyProductList: Boolean,
    ) {
        visitableList.clear()

        if (isEmptyProductList)
            createVisitableListWithEmptyProduct()
        else
            createVisitableListWithProduct(headerDataView, contentDataView)
    }

    private fun createVisitableListWithEmptyProduct() {
        visitableList.add(chooseAddressDataView)
        visitableList.add(EmptyProductDataView(filterController.getActiveFilterOptionList()))
    }

    private fun createVisitableListWithProduct(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView): List<Visitable<*>> {
        val headerList = mutableListOf<Visitable<*>>()

        headerList.add(chooseAddressDataView)
        headerList.add(createBannerDataView(headerDataView))
        headerList.add(TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton))

        processCategoryFilter(headerList, headerDataView.categoryFilterDataValue)

        headerList.add(QuickFilterDataView(createQuickFilterItemList(headerDataView), queryParam))
        headerList.add(ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText))

        postProcessHeaderList(headerList)

        return headerList
    }

    protected open fun processCategoryFilter(
            headerList: MutableList<Visitable<*>>,
            categoryFilterDataValue: DataValue,
    ) {
        val categoryFilter = categoryFilterDataValue.filter.getOrNull(0)

        if (categoryFilter != null) {
            headerList.add(CategoryFilterDataView(createCategoryFilterItemList(categoryFilter)))
        }
    }

    protected fun createBannerDataView(headerDataView: HeaderDataView): BannerDataView {
        val channel = headerDataView.bannerChannel
        val position = 1
        val channelModel = DynamicChannelComponentMapper.mapChannelToComponent(channel, position)

        return BannerDataView(channelModel)
    }

    protected fun createCategoryFilterItemList(categoryFilter: Filter) =
            categoryFilter.options.map {
                CategoryFilterItemDataView(it, filterController.getFilterViewState(it))
            }

    protected fun createQuickFilterItemList(headerDataView: HeaderDataView) =
            headerDataView.quickFilterDataValue.filter.map {
                SortFilterItemDataView(
                        filter = it,
                        sortFilterItem = createSortFilterItem(it),
                )
            }

    private fun createSortFilterItem(filter: Filter): SortFilterItem {
        val option = filter.options.getOrNull(0) ?: Option()
        val isSelected = filterController.getFilterViewState(option)
        val chipType = getSortFilterItemType(isSelected)

        val sortFilterItem = SortFilterItem(filter.title, chipType)
        sortFilterItem.typeUpdated = false

        if (filter.options.size == 1) {
            sortFilterItem.listener = {
                filter(option, !isSelected)
            }

        }
        else {
            val listener = {
                openL3FilterPage(filter)
            }
            sortFilterItem.chevronListener = listener
            sortFilterItem.listener = listener
        }

        return sortFilterItem
    }

    private fun getSortFilterItemType(isSelected: Boolean) =
            if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

    private fun filter(option: Option, isApplied: Boolean) {
        filterController.setFilter(
                option = option,
                isFilterApplied = isApplied,
                isCleanUpExistingFilterWithSameKey = option.isCategoryOption,
        )

        refreshQueryParamFromFilterController()

        onViewReloadPage()
    }

    private fun refreshQueryParamFromFilterController() {
        queryParamMutable.clear()
        queryParamMutable.putAll(filterController.getParameter())
    }

    open fun onViewReloadPage() {
        totalData = 0
        totalFetchedData = 0
        nextPage = 1
        chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        dynamicFilterModelMutableLiveData.value = null

        showLoading()
        onViewCreated()
    }

    protected open fun openL3FilterPage(selectedFilter: Filter) {
        if (isL3FilterPageOpenMutableLiveData.value != null) return

        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    protected open fun postProcessHeaderList(headerList: MutableList<Visitable<*>>) {

    }

    protected open fun createContentVisitableList(contentDataView: ContentDataView) =
            contentDataView.aceSearchProductData.productList.mapIndexed(::mapToProductItemDataView)

    protected open fun mapToProductItemDataView(index: Int, product: Product): ProductItemDataView {
        return ProductItemDataView(
                id = product.id,
                imageUrl300 = product.imageUrl300,
                name = product.name,
                price = product.price,
                priceInt = product.priceInt,
                discountPercentage = product.discountPercentage,
                originalPrice = product.originalPrice,
                parentId = product.parentId,
                shop = ProductItemDataView.Shop(
                        id = product.shop.id,
                ),
                ratingAverage = product.ratingAverage,
                variantATC = createVariantATCDataView(product),
                nonVariantATC = createNonVariantATCDataView(product),
                labelGroupDataViewList = product.labelGroupList.map { labelGroup ->
                    LabelGroupDataView(
                            url = labelGroup.url,
                            title = labelGroup.title,
                            position = labelGroup.position,
                            type = labelGroup.type,
                    )
                },
                labelGroupVariantDataViewList = product.labelGroupVariantList.map { labelGroupVariant ->
                    LabelGroupVariantDataView(
                            title = labelGroupVariant.title,
                            type = labelGroupVariant.type,
                            typeVariant = labelGroupVariant.typeVariant,
                            hexColor = labelGroupVariant.hexColor,
                    )
                },
                sourceEngine = product.sourceEngine,
                boosterList = product.boosterList,
                position = currentProductPosition++,
        )
    }

    protected open fun createVariantATCDataView(product: Product) =
            if (product.childs.isNotEmpty())
                VariantATCDataView(
                        quantity = getProductVariantTotalQuantity(product.parentId)
                )
            else null

    protected open fun createNonVariantATCDataView(product: Product) =
            if (product.childs.isEmpty())
                NonVariantATCDataView(
                        minQuantity = product.minOrder,
                        maxQuantity = product.stock,
                        quantity = getProductNonVariantQuantity(product.id)
                )
            else null

    private fun MutableList<Visitable<*>>.addFooter() {
        if (isLastPage())
            addAll(createFooterVisitableList())
        else
            add(loadingMoreModel)
    }

    protected open fun isLastPage() = totalFetchedData >= totalData

    protected open fun createFooterVisitableList() = listOf<Visitable<*>>()

    private fun updateViewForFirstPage(isEmptyProductList: Boolean) {
        clearVisitableListLiveData()
        updateVisitableListLiveData()

        updateNextPageData()
        updateHeaderBackgroundVisibility(!isEmptyProductList)

        showPageContent()
    }

    protected fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    protected fun updateVisitableListLiveData() {
        visitableListMutableLiveData.value = visitableList
    }

    protected open fun updateNextPageData() {
        val hasNextPage = totalData > totalFetchedData

        hasNextPageMutableLiveData.value = hasNextPage

        if (hasNextPage) nextPage++
    }

    private fun updateHeaderBackgroundVisibility(isVisible: Boolean) {
        isHeaderBackgroundVisibleMutableLiveData.value = isVisible
    }

    private fun showPageContent() {
        isContentLoadingMutableLiveData.value = false
    }

    open fun onLoadMore() {
        if (hasLoadedAllData()) return

        executeLoadMore()
    }

    protected open fun hasLoadedAllData() = totalData <= totalFetchedData

    abstract fun executeLoadMore()

    protected open fun onGetLoadMorePageSuccess(contentDataView: ContentDataView) {
        totalFetchedData += contentDataView.aceSearchProductData.productList.size

        updateVisitableListForNextPage(contentDataView)
        updateVisitableListLiveData()
        updateNextPageData()
    }

    protected open fun updateVisitableListForNextPage(contentDataView: ContentDataView) {
        visitableList.remove(loadingMoreModel)
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    open fun onViewOpenFilterPage() {
        if (isFilterPageOpenLiveData.value == true) return

        isFilterPageOpenMutableLiveData.value = true

        if (dynamicFilterModelLiveData.value != null) return

        val getFilterRequestParams = RequestParams.create()
        getFilterRequestParams.putAll(createTokonowQueryParams())

        getFilterUseCase.cancelJobs()
        getFilterUseCase.execute(
                ::onGetFilterSuccess,
                ::onGetFilterFailed,
                getFilterRequestParams
        )
    }

    protected open fun onGetFilterSuccess(dynamicFilterModel: DynamicFilterModel) {
        filterController.appendFilterList(queryParam, dynamicFilterModel.data.filter)
        dynamicFilterModelMutableLiveData.value = dynamicFilterModel
    }

    protected open fun onGetFilterFailed(throwable: Throwable) {

    }

    open fun onViewDismissFilterPage() {
        isFilterPageOpenMutableLiveData.value = false
    }

    open fun onViewClickCategoryFilterChip(option: Option, isSelected: Boolean) {
        removeFilterWithExclude(option)
        filter(option, isSelected)
    }

    private fun removeFilterWithExclude(option: Option) {
        queryParamMutable.remove(OptionHelper.getKeyRemoveExclude(option))
        queryParamMutable.remove(option.key)
        filterController.refreshMapParameter(queryParam)
    }

    open fun onViewApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        refreshQueryParamFromFilterController()

        onViewReloadPage()
    }

    open fun onViewGetProductCount(mapParameter: Map<String, String>) {
        getProductCountUseCase.cancelJobs()
        getProductCountUseCase.execute(
                ::onGetProductCountSuccess,
                ::onGetProductCountFailed,
                createGetProductCountRequestParams(mapParameter)
        )
    }

    protected open fun createGetProductCountRequestParams(
            mapParameter: Map<String, String>
    ): RequestParams {
        val getProductCountParams = mutableMapOf<String, Any>()
        appendMandatoryParams(getProductCountParams)
        getProductCountParams[SearchApiConst.ROWS] = 0
        getProductCountParams.putAll(FilterHelper.createParamsWithoutExcludes(mapParameter))

        val getProductCountRequestParams = RequestParams.create()
        getProductCountRequestParams.putAll(getProductCountParams)

        return getProductCountRequestParams
    }

    protected open fun onGetProductCountSuccess(countText: String) {
        productCountAfterFilterMutableLiveData.value = countText
    }

    protected open fun onGetProductCountFailed(throwable: Throwable) {
        onGetProductCountSuccess("0")
    }

    open fun onViewGetProductCount(option: Option) {
        val mapParameter = queryParam + mapOf(option.key to option.value)
        onViewGetProductCount(mapParameter)
    }

    open fun onViewApplyFilterFromCategoryChooser(chosenCategoryFilter: Option) {
        onViewDismissL3FilterPage()
        onViewClickCategoryFilterChip(chosenCategoryFilter, true)
    }

    open fun onViewDismissL3FilterPage() {
        isL3FilterPageOpenMutableLiveData.value = null
    }

    open fun onViewResumed() {
        refreshMiniCart()

        val isChooseAddressUpdated = getIsChooseAddressUpdated()
        if (isChooseAddressUpdated)
            onViewReloadPage()
    }

    protected open fun refreshMiniCart() {
        val shopId = shopIdLiveData.value ?: ""
        if (!shopId.isValidId()) return

        getMiniCartListSimplifiedUseCase.cancelJobs()
        getMiniCartListSimplifiedUseCase.setParams(listOf(shopId))
        getMiniCartListSimplifiedUseCase.execute(
                ::onGetMiniCartDataSuccess,
                ::onGetMiniCartDataFailed,
        )
    }

    private fun String.isValidId() = this.isNotEmpty() && this != "0"

    private fun onGetMiniCartDataSuccess(miniCartSimplifiedData: MiniCartSimplifiedData) {
        updateMiniCartWidgetData(miniCartSimplifiedData)
        onViewUpdateCartItems(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartWidgetMutableLiveData.value = miniCartSimplifiedData
        isShowMiniCartMutableLiveData.value = miniCartSimplifiedData.isShowMiniCartWidget
    }

    open fun onViewUpdateCartItems(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModelScope.launch {
            updateMiniCartInBackground(miniCartSimplifiedData)
        }
    }

    private suspend fun updateMiniCartInBackground(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        withContext(baseDispatcher.io) {
            updateMiniCartProperties(miniCartSimplifiedData)

            if (visitableList.isEmpty()) return@withContext

            val updatedProductIndices = mutableListOf<Int>()

            visitableList.forEachIndexed { index, visitable ->
                if (visitable is ProductItemDataView)
                    updateProductItemQuantity(index, visitable, updatedProductIndices)
            }

            withContext(baseDispatcher.main) {
                updatedVisitableIndicesMutableLiveData.value = updatedProductIndices
            }
        }
    }

    private fun updateMiniCartProperties(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val viewModel = this@BaseSearchCategoryViewModel
        val cartItemsPartition =
                splitCartItemsVariantAndNonVariant(miniCartSimplifiedData.miniCartItems)

        viewModel.cartItemsNonVariant = cartItemsPartition.first
        viewModel.cartItemsVariantGrouped =
                cartItemsPartition.second.groupBy { it.productParentId }
    }

    private fun splitCartItemsVariantAndNonVariant(miniCartItems: List<MiniCartItem>) =
            miniCartItems.partition { it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID }

    private fun updateProductItemQuantity(
            index: Int,
            productItem: ProductItemDataView,
            updatedProductIndices: MutableList<Int>,
    ) {
        val productId = productItem.id
        val parentProductId = productItem.parentId
        val nonVariantATC = productItem.nonVariantATC
        val variantATC = productItem.variantATC

        if (nonVariantATC != null) {
            val quantity = getProductNonVariantQuantity(productId)

            if (nonVariantATC.quantity != quantity) {
                nonVariantATC.quantity = quantity
                updatedProductIndices.add(index)
            }
        } else if (variantATC != null) {
            val totalQuantity = getProductVariantTotalQuantity(parentProductId)

            if (variantATC.quantity != totalQuantity) {
                variantATC.quantity = totalQuantity
                updatedProductIndices.add(index)
            }
        }
    }

    private fun getProductNonVariantQuantity(productId: String): Int {
        val cartItem = cartItemsNonVariant?.find { it.productId == productId }
        return cartItem?.quantity ?: 0
    }

    private fun getProductVariantTotalQuantity(parentProductId: String): Int {
        val cartItemsVariantGrouped = cartItemsVariantGrouped
        val miniCartItemsWithSameParentId = cartItemsVariantGrouped?.get(parentProductId)
        val totalQuantity = miniCartItemsWithSameParentId?.sumBy { it.quantity }

        return totalQuantity ?: 0
    }

    private fun onGetMiniCartDataFailed(throwable: Throwable) {
        isShowMiniCartMutableLiveData.value = false
    }

    private fun getIsChooseAddressUpdated(): Boolean {
        return chooseAddressData?.let {
            chooseAddressWrapper.isChooseAddressUpdated(it)
        } ?: false
    }

    open fun onViewATCProductNonVariant(productItem: ProductItemDataView, quantity: Int) {
        val nonVariantATC = productItem.nonVariantATC ?: return
        if (nonVariantATC.quantity == quantity) return

        if (nonVariantATC.quantity == 0)
            addToCart(productItem, quantity)
        else
            updateCart(productItem, quantity)
    }

    private fun addToCart(productItem: ProductItemDataView, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
                productId = productItem.id,
                shopId = productItem.shop.id,
                quantity = quantity,
        )

        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            onAddToCartSuccess(
                    productItem,
                    it.data.quantity,
                    it.errorMessage.joinToString(separator = ", "),
            )
        }, {
            onAddToCartFailed(it)
        })
    }

    private fun onAddToCartSuccess(
            productItem: ProductItemDataView,
            quantity: Int,
            successMessage: String,
    ) {
        updateProductNonVariantQuantity(productItem, quantity)
        updateCartMessageSuccess(successMessage)
        refreshMiniCart()
    }

    private fun updateProductNonVariantQuantity(
            productItem: ProductItemDataView,
            quantity: Int,
    ) {
        productItem.nonVariantATC?.quantity = quantity
    }

    private fun updateCartMessageSuccess(successMessage: String) {
        successATCMessageMutableLiveData.value = successMessage
    }

    private fun onAddToCartFailed(throwable: Throwable) {
        errorATCMessageMutableLiveData.value = throwable.message ?: ""
    }

    private fun updateCart(
            productItem: ProductItemDataView,
            quantity: Int,
    ) {
        val miniCartItem = cartItemsNonVariant?.find { it.productId == productItem.id }
                ?: return
        miniCartItem.quantity = quantity
        updateCartUseCase.setParams(listOf(miniCartItem))
        updateCartUseCase.execute({
            onAddToCartSuccess(productItem, quantity, it.data.message)
        }, {
            onAddToCartFailed(it)
        })
    }

    fun onLocalizingAddressSelected() {
        onViewReloadPage()
    }

    fun onViewRemoveFilter(option: Option) {
        val isOptionKeyHasExclude = option.key.startsWith(OptionHelper.EXCLUDE_PREFIX)
        if (isOptionKeyHasExclude)
            removeFilterWithExclude(option)

        filter(option, false)
    }

    protected class HeaderDataView(
            val title: String = "",
            val hasSeeAllCategoryButton: Boolean = false,
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            categoryFilterDataValue: DataValue = DataValue(),
            quickFilterDataValue: DataValue = DataValue(),
            val bannerChannel: Channels = Channels(),
    ) {
        val categoryFilterDataValue = DataValue(
                filter = FilterHelper.copyFilterWithOptionAsExclude(categoryFilterDataValue.filter)
        )

        val quickFilterDataValue = DataValue(
                filter = quickFilterDataValue.filter.map { filter ->
                    filter.clone(options = createOptionListWithExclude(filter))
                }
        )

        private fun createOptionListWithExclude(filter: Filter) =
                filter.options.map { option ->
                    option.clone().also { copyOption ->
                        modifyOptionKeyInCategoryFilter(copyOption)
                    }
                }

        private fun modifyOptionKeyInCategoryFilter(option: Option) {
            val isCategoryFilter = isInCategoryFilter(option)

            if (isCategoryFilter)
                option.key = OptionHelper.EXCLUDE_PREFIX + option.key
        }

        private fun isInCategoryFilter(optionToCheck: Option): Boolean {
            val categoryOptionList = categoryFilterDataValue.filter.map { it.options }.flatten()

            return categoryOptionList.any {
                it.key.removePrefix(OptionHelper.EXCLUDE_PREFIX) == optionToCheck.key
                        && it.value == optionToCheck.value
            }
        }
    }

    protected data class ContentDataView(
            val aceSearchProductData: SearchProductData = SearchProductData(),
    )

    companion object {
        private const val NO_VARIANT_PARENT_PRODUCT_ID = "0"
    }
}