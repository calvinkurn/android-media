package com.tokopedia.tokomart.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HARDCODED_SHOP_ID_PLEASE_DELETE
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
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
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
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseSearchCategoryViewModel(
        protected val baseDispatcher: CoroutineDispatchers,
        queryParamMap: Map<String, String>,
        protected val getFilterUseCase: UseCase<DynamicFilterModel>,
        protected val getProductCountUseCase: UseCase<String>,
        protected val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
        protected val addToCartUseCase: AddToCartUseCase,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
        protected val abTestPlatformWrapper: ABTestPlatformWrapper,
): BaseViewModel(baseDispatcher.io) {

    protected val filterController = FilterController()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()
    protected val queryParamMutable = queryParamMap.toMutableMap()

    val queryParam: Map<String, String> = queryParamMutable
    val hasGlobalMenu: Boolean
    val shopId: String = HARDCODED_SHOP_ID_PLEASE_DELETE
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

    protected val isShowMiniCartMutableLiveData = MutableLiveData(false)
    val isShowMiniCartLiveData: LiveData<Boolean> = isShowMiniCartMutableLiveData

    protected val miniCartWidgetMutableLiveData = MutableLiveData<MiniCartWidgetData?>(null)
    val miniCartWidgetLiveData: LiveData<MiniCartWidgetData?> = miniCartWidgetMutableLiveData

    protected val updatedVisitableIndicesMutableLiveData = MutableLiveData<Event<List<Int>>>(null)
    val updatedVisitableIndicesLiveData: LiveData<Event<List<Int>>> = updatedVisitableIndicesMutableLiveData

    protected val isRefreshPageMutableLiveData = MutableLiveData(false)
    val isRefreshPageLiveData: LiveData<Boolean> = isRefreshPageMutableLiveData

    protected val addToCartErrorMessageMutableLiveData = MutableLiveData<String>(null)
    val addToCartErrorMessageLiveData: LiveData<String> = addToCartErrorMessageMutableLiveData

    protected var totalData = 0
    protected var totalFetchedData = 0
    protected var nextPage = 1

    init {
        updateQueryParamWithDefaultSort()

        hasGlobalMenu = isABTestNavigationRevamp()
    }

    private fun isABTestNavigationRevamp() =
            getNavigationExpVariant() == NAVIGATION_VARIANT_REVAMP

    private fun getNavigationExpVariant() =
            abTestPlatformWrapper
                    .getABTestRemoteConfig()
                    ?.getString(NAVIGATION_EXP_TOP_NAV, NAVIGATION_VARIANT_OLD)

    private fun updateQueryParamWithDefaultSort() {
        queryParamMutable[SearchApiConst.OB] = DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    abstract fun onViewCreated()

    protected open fun createRequestParams(): RequestParams {
        val tokonowQueryParam = createTokonowQueryParams()

        val requestParams = RequestParams.create()
        requestParams.putObject(TOKONOW_QUERY_PARAMS, tokonowQueryParam)

        return requestParams
    }

    private fun createTokonowQueryParams(): MutableMap<String, Any> {
        val tokonowQueryParam = mutableMapOf<String, Any>()

        appendMandatoryParams(tokonowQueryParam)
        appendPaginationParam(tokonowQueryParam)
        tokonowQueryParam.putAll(FilterHelper.createParamsWithoutExcludes(queryParam))

        return tokonowQueryParam
    }

    protected open fun appendMandatoryParams(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE
        tokonowQueryParam[SearchApiConst.USER_WAREHOUSE_ID] = HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
    }

    protected open fun appendPaginationParam(tokonowQueryParam: MutableMap<String, Any>) {
        tokonowQueryParam[SearchApiConst.PAGE] = nextPage
        tokonowQueryParam[SearchApiConst.USE_PAGE] = true
    }

    protected fun onGetFirstPageSuccess(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.aceSearchProductData.productList.size
        autoCompleteApplink = contentDataView.aceSearchProductData.autocompleteApplink

        val filterList =
                headerDataView.quickFilterDataValue.filter +
                headerDataView.categoryFilterDataValue.filter

        filterController.initFilterController(queryParamMutable, filterList)

        createVisitableListFirstPage(headerDataView, contentDataView)
        clearVisitableListLiveData()
        updateVisitableListLiveData()
        updateIsRefreshPage()
        updateNextPageData()
    }

    private fun createVisitableListFirstPage(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        visitableList.clear()

        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView): List<Visitable<*>> {
        val headerList = mutableListOf(
                ChooseAddressDataView(),
                createBannerDataView(headerDataView),
                TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton),
        )

        val categoryFilter = headerDataView.categoryFilterDataValue.filter.getOrNull(0)
        if (categoryFilter != null) {
            headerList.add(CategoryFilterDataView(createCategoryFilterItemList(categoryFilter)))
        }

        headerList.add(QuickFilterDataView(createQuickFilterItemList(headerDataView)))
        headerList.add(ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText))

        return headerList
    }

    private fun createBannerDataView(headerDataView: HeaderDataView): BannerDataView {
        val channel = headerDataView.bannerChannel
        val position = 1
        val channelModel = DynamicChannelComponentMapper.mapChannelToComponent(channel, position)

        return BannerDataView(channelModel)
    }

    private fun createCategoryFilterItemList(categoryFilter: Filter) =
            categoryFilter.options.map {
                CategoryFilterItemDataView(it, filterController.getFilterViewState(it))
            }

    private fun createQuickFilterItemList(headerDataView: HeaderDataView) =
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
                onFilterChipSelected(option, !isSelected)
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

    private fun onFilterChipSelected(option: Option, isFilterApplied: Boolean) {
        filterController.setFilter(
                option = option,
                isFilterApplied = isFilterApplied,
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

        onViewCreated()
    }

    protected open fun openL3FilterPage(selectedFilter: Filter) {
        if (isL3FilterPageOpenMutableLiveData.value != null) return

        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    protected open fun createContentVisitableList(contentDataView: ContentDataView) =
            contentDataView.aceSearchProductData.productList.map(::mapToProductItemDataView)

    protected open fun mapToProductItemDataView(product: Product): ProductItemDataView {
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
                }
        )
    }

    protected open fun createVariantATCDataView(product: Product) =
            if (product.childs.isNotEmpty())
                VariantATCDataView()
            else null

    protected open fun createNonVariantATCDataView(product: Product) =
            if (product.childs.isEmpty())
                NonVariantATCDataView(
                    minQuantity = product.minOrder,
                    maxQuantity = product.stock,
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

    protected fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    protected fun updateVisitableListLiveData() {
        visitableListMutableLiveData.value = visitableList
    }

    protected fun updateIsRefreshPage() {
        isRefreshPageMutableLiveData.value = true
    }

    protected open fun updateNextPageData() {
        val hasNextPage = totalData > totalFetchedData

        hasNextPageMutableLiveData.value = hasNextPage

        if (hasNextPage) nextPage++
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

    fun onViewClickCategoryFilterChip(option: Option, isSelected: Boolean) {
        queryParamMutable.remove(OptionHelper.getKeyRemoveExclude(option))
        queryParamMutable.remove(option.key)
        filterController.refreshMapParameter(queryParam)

        onFilterChipSelected(option, isSelected)
    }

    fun onViewApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        filterController.refreshMapParameter(applySortFilterModel.mapParameter)
        refreshQueryParamFromFilterController()

        onViewReloadPage()
    }

    fun onViewGetProductCount(mapParameter: Map<String, String>) {
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

    fun onViewGetProductCount(option: Option) {
        val mapParameter = queryParam + mapOf(option.key to option.value)
        onViewGetProductCount(mapParameter)
    }

    fun onViewApplyFilterFromCategoryChooser(chosenCategoryFilter: Option) {
        filterController.setFilter(
                option = chosenCategoryFilter,
                isFilterApplied = true,
                isCleanUpExistingFilterWithSameKey = chosenCategoryFilter.isCategoryOption
        )

        refreshQueryParamFromFilterController()

        onViewDismissL3FilterPage()

        onViewReloadPage()
    }

    fun onViewDismissL3FilterPage() {
        isL3FilterPageOpenMutableLiveData.value = null
    }

    open fun onViewResumed() {
        getMiniCartListSimplifiedUseCase.cancelJobs()
        getMiniCartListSimplifiedUseCase.setParams(listOf()) // Add shop id
        getMiniCartListSimplifiedUseCase.execute(::onGetMiniCartDataSuccess, ::onGetMiniCartDataFailed)
    }

    private fun onGetMiniCartDataSuccess(miniCartSimplifiedData: MiniCartSimplifiedData) {
        updateMiniCartWidgetData(miniCartSimplifiedData)
        onViewUpdateCartItems(miniCartSimplifiedData)
    }

    private fun updateMiniCartWidgetData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        miniCartWidgetMutableLiveData.value = miniCartSimplifiedData.miniCartWidgetData
        isShowMiniCartMutableLiveData.value = miniCartSimplifiedData.isShowMiniCartWidget
    }

    open fun onViewUpdateCartItems(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModelScope.launch {
            updateProductQuantityInBackground(miniCartSimplifiedData)
        }
    }

    private suspend fun updateProductQuantityInBackground(
            miniCartSimplifiedData: MiniCartSimplifiedData
    ) {
        withContext(baseDispatcher.io) {
            val cartItems = miniCartSimplifiedData.miniCartItems
            val cartItemsPartition = splitCartItemsVariantAndNonVariant(cartItems)
            val cartItemsNonVariant = cartItemsPartition.first
            val cartItemsVariant = cartItemsPartition.second
            val cartItemsVariantGrouped = cartItemsVariant.groupBy { it.productParentId }
            val updatedProductIndices = mutableListOf<Int>()

            visitableList.forEachIndexed { index, visitable ->
                if (visitable is ProductItemDataView)
                    updateProductItemQuantity(
                            index,
                            visitable,
                            cartItemsNonVariant,
                            cartItemsVariantGrouped,
                            updatedProductIndices
                    )
            }

            withContext(baseDispatcher.main) {
                updatedVisitableIndicesMutableLiveData.value = Event(updatedProductIndices)
            }
        }
    }

    private fun splitCartItemsVariantAndNonVariant(cartItems: List<MiniCartItem>) =
            cartItems.partition { it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID }

    private fun updateProductItemQuantity(
            index: Int,
            productItem: ProductItemDataView,
            cartItemsNonVariant: List<MiniCartItem>,
            cartItemsVariantGrouped: Map<String, List<MiniCartItem>>,
            updatedProductIndices: MutableList<Int>,
    ) {
        val productId = productItem.id
        val parentProductId = productItem.parentId
        val nonVariantATC = productItem.nonVariantATC
        val variantATC = productItem.variantATC

        if (nonVariantATC != null) {
            val cartItem = cartItemsNonVariant.find { it.productId == productId }
            val quantity = cartItem?.quantity ?: 0

            if (nonVariantATC.quantity != quantity) {
                nonVariantATC.quantity = quantity
                updatedProductIndices.add(index)
            }
        } else if (variantATC != null) {
            val totalQuantity = cartItemsVariantGrouped[parentProductId]?.sumBy { it.quantity } ?: 0

            if (variantATC.quantity != totalQuantity) {
                variantATC.quantity = totalQuantity
                updatedProductIndices.add(index)
            }
        }
    }

    private fun onGetMiniCartDataFailed(throwable: Throwable) {

    }

    open fun onViewATCProductNonVariant(productItem: ProductItemDataView, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
                productId = productItem.id,
                shopId = productItem.shop.id,
                quantity = quantity,
        )

        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute(::onAddToCartSuccess, ::onAddToCartFailed)
    }

    private fun onAddToCartSuccess(addToCartDataModel: AddToCartDataModel) {
        addToCartErrorMessageMutableLiveData.value = ""
    }

    private fun onAddToCartFailed(throwable: Throwable) {
        addToCartErrorMessageMutableLiveData.value = throwable.message
    }

    protected class HeaderDataView(
            val title: String = "",
            val hasSeeAllCategoryButton: Boolean = false,
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            categoryFilterDataValue: DataValue = DataValue(),
            val quickFilterDataValue: DataValue = DataValue(),
            val bannerChannel: Channels = Channels(),
    ) {
        val categoryFilterDataValue = DataValue(
                filter = FilterHelper.copyFilterWithOptionAsExclude(categoryFilterDataValue.filter)
        )
    }

    protected data class ContentDataView(
            val aceSearchProductData: SearchProductData = SearchProductData(),
    )

    companion object {
        private const val NO_VARIANT_PARENT_PRODUCT_ID = "0"
    }
}