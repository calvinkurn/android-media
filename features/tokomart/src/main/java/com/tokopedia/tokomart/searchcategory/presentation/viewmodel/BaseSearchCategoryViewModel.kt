package com.tokopedia.tokomart.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.util.DummyDataViewGenerator
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

abstract class BaseSearchCategoryViewModel(
        baseDispatcher: CoroutineDispatchers,
        queryParamMap: Map<String, String>,
        protected val getFilterUseCase: UseCase<DynamicFilterModel>,
        protected val getProductCountUseCase: UseCase<String>,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
): BaseViewModel(baseDispatcher.io) {

    protected val filterController = FilterController()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()

    protected val queryParamMutable = queryParamMap.toMutableMap()
    val queryParam: Map<String, String> = queryParamMutable

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

    protected var totalData = 0
    protected var totalFetchedData = 0
    protected var nextPage = 1

    init {
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

        tokonowQueryParam.appendMandatoryParams()
        tokonowQueryParam.appendDeviceParam()
        tokonowQueryParam.appendPaginationParam()
        tokonowQueryParam.putAll(queryParam)

        return tokonowQueryParam
    }

    protected open fun MutableMap<String, Any>.appendMandatoryParams() {

    }

    protected open fun MutableMap<String, Any>.appendDeviceParam() {
        this[SearchApiConst.DEVICE] = DEFAULT_VALUE_OF_PARAMETER_DEVICE
    }

    protected open fun MutableMap<String, Any>.appendPaginationParam() {
        this[SearchApiConst.PAGE] = nextPage
        this[SearchApiConst.USE_PAGE] = true
    }

    protected fun onGetFirstPageSuccess(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.productList.size

        val filterList = headerDataView.quickFilterDataValue.filter + headerDataView.categoryFilterDataValue.filter
        filterController.initFilterController(
                queryParamMutable,
                filterList
        )

        createVisitableListFirstPage(headerDataView, contentDataView)
        clearVisitableListLiveData()
        updateVisitableListLiveData()
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

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView) = listOf(
            ChooseAddressDataView(),
            DummyDataViewGenerator.generateBannerDataView(),
            TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton),
            CategoryFilterDataView(createCategoryFilterItemList(headerDataView)),
            QuickFilterDataView(createQuickFilterItemList(headerDataView)),
            ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText),
    )

    private fun createCategoryFilterItemList(headerDataView: HeaderDataView) =
            headerDataView.categoryFilterDataValue.filter.map {
                val option = it.options.getOrNull(0) ?: Option()
                CategoryFilterItemDataView(option, filterController.getFilterViewState(option))
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
            if (isSelected)
                ChipsUnify.TYPE_SELECTED
            else
                ChipsUnify.TYPE_NORMAL

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
        isL3FilterPageOpenMutableLiveData.value = selectedFilter
    }

    protected open fun createContentVisitableList(contentDataView: ContentDataView) =
            contentDataView.productList.map { product ->
                ProductItemDataView(
                        id = product.id,
                        imageUrl300 = product.imageUrl300,
                        name = product.name,
                        price = product.price,
                        priceInt = product.priceInt,
                        discountPercentage = product.discountPercentage,
                        originalPrice = product.originalPrice,
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

    private fun MutableList<Visitable<*>>.addFooter() {
        if (isLastPage())
            addAll(createFooterVisitableList())
        else
            add(loadingMoreModel)
    }

    protected open fun isLastPage() = totalFetchedData >= totalData

    protected open fun createFooterVisitableList() = listOf<Visitable<*>>()

    private fun clearVisitableListLiveData() {
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

    open fun onLoadMore() {
        if (hasLoadedAllData()) return

        executeLoadMore()
    }

    protected open fun hasLoadedAllData() = totalData <= totalFetchedData

    abstract fun executeLoadMore()

    protected open fun onGetLoadMorePageSuccess(contentDataView: ContentDataView) {
        totalFetchedData += contentDataView.productList.size

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
                this::onGetFilterSuccess,
                this::onGetFilterFailed,
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

    fun onCategoryFilterClicked(option: Option, isSelected: Boolean) {
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
                this::onGetProductCountSuccess,
                this::onGetProductCountFailed,
                createGetProductCountRequestParams(mapParameter)
        )
    }

    protected open fun createGetProductCountRequestParams(mapParameter: Map<String, String>): RequestParams {
        val getProductCountParams = mutableMapOf<String, Any>()
        getProductCountParams.putAll(mapParameter)
        getProductCountParams.appendMandatoryParams()
        getProductCountParams.appendDeviceParam()
        getProductCountParams[SearchApiConst.ROWS] = 0

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

    protected data class HeaderDataView(
            val title: String = "",
            val hasSeeAllCategoryButton: Boolean = false,
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            val categoryFilterDataValue: DataValue = DataValue(),
            val quickFilterDataValue: DataValue = DataValue(),
    )

    protected data class ContentDataView(
            val productList: List<Product> = listOf(),
    )
}