package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.utils.UrlParamUtils.keywords
import com.tokopedia.filter.common.FilterState
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.search.result.mps.addtocart.AddToCartState
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateFilterDataView
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import com.tokopedia.search.result.mps.violationstate.TypeViolation
import com.tokopedia.search.result.mps.violationstate.ViolationStateDataView
import com.tokopedia.search.result.mps.filter.bottomsheet.BottomSheetFilterState
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterState
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.utils.PaginationState
import com.tokopedia.search.utils.mvvm.SearchUiState
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel as ChooseAddressModel

data class MPSState(
    val parameter: Map<String, String> = mapOf(),
    val result: State<List<Visitable<*>>> = State.Loading(),
    val chooseAddressModel: ChooseAddressModel? = null,
    val quickFilterState: QuickFilterState = QuickFilterState(),
    val filterState: FilterState = FilterState(),
    val loadMoreThrowable: Throwable? = null,
    val paginationState: PaginationState = PaginationState(),
    val addToCartState: AddToCartState = AddToCartState(),
    val bottomSheetFilterState: BottomSheetFilterState = BottomSheetFilterState()
) : SearchUiState {

    private val shopIdSet: Set<String>
        get() = (result.data?.filterIsInstance<MPSShopWidgetDataView>() ?: emptyList())
            .map { it.id }
            .toSet()

    val visitableList
        get() = result.data ?: listOf()
    val startFrom
        get() = paginationState.startFrom
    val hasNextPage
        get() = paginationState.hasNextPage
    val isBottomSheetFilterOpen
        get() = bottomSheetFilterState.isOpen
    val bottomSheetFilterModel
        get() = bottomSheetFilterState.dynamicFilterModel
    val quickFilterDataViewList
        get() = quickFilterState.data

    private val bottomSheetFilterList: List<Filter>
        get() = bottomSheetFilterModel?.data?.filter ?: listOf()

    fun success(mpsModel: MPSModel): MPSState =
        this.updatePageStates(mpsModel)
            .updateResult(mpsModel)

    private fun updatePageStates(mpsModel: MPSModel): MPSState = copy(
        filterState = filterState.from(
            parameter = parameter,
            filterList = mpsModel.quickFilterList + bottomSheetFilterList
        ),
        paginationState = PaginationState(totalData = mpsModel.totalData).incrementStart()
    )

    private fun updateResult(mpsModel: MPSModel) = copy(
        result = State.Success(data = createResponseData(mpsModel)),
        quickFilterState = quickFilterState.success(mpsModel, filterState)
    )

    private fun createResponseData(mpsModel: MPSModel): List<Visitable<*>> =
        if (mpsModel.responseCode.isViolationResponseCode()) {
            restrictedStateVisitableList(mpsModel)
        } else {
            createSuccessData(mpsModel)
        }

    private fun String.isViolationResponseCode(): Boolean {
        return this == VIOLATION_BLACKLIST || this ==  VIOLATION_BANNED
    }

    private fun createSuccessData(mpsModel: MPSModel): List<Visitable<*>> =
        if (mpsModel.shopList.isNotEmpty())
            listOf(ChooseAddressDataView) +
                mpsShopWidgetList(mpsModel) +
                loadMoreVisitableList()
        else
            emptyStateVisitableList()

    private fun mpsShopWidgetList(mpsModel: MPSModel): List<Visitable<*>> =
        mpsModel.shopList.map {
            MPSShopWidgetDataView.create(it, parameter.keywords())
        }

    private fun mpsShopWidgetListLoadMore(
        mpsModel: MPSModel
    ): List<Visitable<*>> {
        return mpsModel.shopList
            .filterNot { shop -> shop.id in shopIdSet }
            .map { MPSShopWidgetDataView.create(it, parameter.keywords()) }
    }

    private fun loadMoreVisitableList(): List<Visitable<*>> =
        if (paginationState.hasNextPage) listOf(LoadingMoreModel())
        else listOf()

    private fun emptyStateVisitableList() =
        if (filterState.isFilterActive)
            listOf(MPSEmptyStateFilterDataView)
        else
            listOf(MPSEmptyStateKeywordDataView)

    private fun restrictedStateVisitableList(mpsModel: MPSModel) =
        if (mpsModel.responseCode == VIOLATION_BLACKLIST)
            listOf(ViolationStateDataView(TypeViolation.BLACKLISTED))
        else
            listOf(ViolationStateDataView(TypeViolation.BANNED))

    fun error(throwable: Throwable) = copy(
        result = State.Error(
            message = "",
            data = null,
            throwable = throwable
        )
    )

    fun chooseAddress(chooseAddressModel: ChooseAddressModel) = copy(
        chooseAddressModel = chooseAddressModel
    )

    fun reload() = MPSState(
        parameter = parameter,
        chooseAddressModel = chooseAddressModel,
        bottomSheetFilterState = BottomSheetFilterState(
            dynamicFilterModel = bottomSheetFilterModel
        )
    )

    fun applyQuickFilter(quickFilterDataView: QuickFilterDataView): MPSState {
        val option = quickFilterDataView.firstOption ?: return this

        val isFilterApplied = filterState.isFilterApplied(option)
        val filterStateUpdated = filterState.setFilter(option, !isFilterApplied)

        return copy(
            parameter = filterStateUpdated.parameter,
            filterState = filterStateUpdated
        )
    }

    fun loadMore() = copy(loadMoreThrowable = null)

    fun successLoadMore(mpsModel: MPSModel) =
        this.incrementStart()
            .updateResultLoadMore(mpsModel)

    private fun incrementStart() = copy(paginationState = paginationState.incrementStart())

    private fun updateResultLoadMore(mpsModel: MPSModel) = copy(
        result = State.Success(data = successLoadMoreData(mpsModel))
    )

    private fun successLoadMoreData(mpsModel: MPSModel): List<Visitable<*>> =
        visitableList.filter { it !is LoadingMoreModel } +
            mpsShopWidgetListLoadMore(mpsModel) +
            loadMoreVisitableList()

    fun errorLoadMore(throwable: Throwable) = copy(loadMoreThrowable = throwable)

    fun successAddToCart(addToCartDataModel: AddToCartDataModel) = copy(
        addToCartState = addToCartState.success(addToCartDataModel)
    )

    fun errorAddToCart(addToCartException: Throwable) = copy(
        addToCartState = addToCartState.error(addToCartException)
    )

    fun addToCartMessageDismissed() = copy(addToCartState = addToCartState.dismiss())

    fun openBottomSheetFilter() = copy(
        bottomSheetFilterState = bottomSheetFilterState.openBottomSheetFilter()
    )

    fun setBottomSheetFilterModel(dynamicFilterModel: DynamicFilterModel) = copy(
        bottomSheetFilterState = bottomSheetFilterState.setBottomSheetFilterModel(dynamicFilterModel),
        filterState = filterState.appendFilterList(dynamicFilterModel.data.filter)
    )

    fun closeBottomSheetFilter() = copy(
        bottomSheetFilterState = bottomSheetFilterState.closeBottomSheetFilter()
    )

    fun applyFilter(parameter: Map<String, String>) = copy(parameter = parameter)

    fun resetFilter() = copy(parameter = filterState.resetFilters().parameter)

    companion object {
        private const val VIOLATION_BLACKLIST= "2"
        private const val VIOLATION_BANNED= "8"
    }
}
