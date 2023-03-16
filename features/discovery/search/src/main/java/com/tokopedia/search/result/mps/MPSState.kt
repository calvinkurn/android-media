package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.filter.common.FilterState
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateFilterDataView
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.utils.PaginationState
import com.tokopedia.search.utils.mvvm.SearchUiState
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel as ChooseAddressModel

data class MPSState(
    val parameter: Map<String, String> = mapOf(),
    val result: State<List<Visitable<*>>> = State.Loading(),
    val chooseAddressModel: ChooseAddressModel? = null,
    val quickFilterDataViewList: List<QuickFilterDataView> = listOf(),
    val filterState: FilterState = FilterState(),
    val loadMoreThrowable: Throwable? = null,
    val paginationState: PaginationState = PaginationState(),
): SearchUiState {

    val visitableList = result.data ?: listOf()
    val startFrom = paginationState.startFrom
    val hasNextPage = paginationState.hasNextPage

    fun success(mpsModel: MPSModel): MPSState =
        this.updateFilterState(mpsModel)
            .updateResult(mpsModel)

    private fun updateFilterState(mpsModel: MPSModel) = copy(
        filterState = filterState.from(parameter, mpsModel.quickFilterModel.filter)
    )

    private fun updateResult(mpsModel: MPSModel) = copy(
        result = State.Success(data = successData(mpsModel)),
        quickFilterDataViewList = quickFilterData(mpsModel),
        paginationState = PaginationState(totalData = mpsModel.totalData).incrementStart()
    )

    private fun successData(mpsModel: MPSModel): List<Visitable<MPSTypeFactory>> =
        if (mpsModel.shopList.isNotEmpty())
            listOf(ChooseAddressDataView) +
                mpsShopWidgetList(mpsModel)
        else {
            if (filterState.isFilterActive)
                listOf(MPSEmptyStateFilterDataView)
            else
                listOf(MPSEmptyStateKeywordDataView)
        }

    private fun mpsShopWidgetList(mpsModel: MPSModel) =
        mpsModel.shopList.map(MPSShopWidgetDataView::create)

    private fun quickFilterData(mpsModel: MPSModel): List<QuickFilterDataView> {
        return if (mpsModel.shopList.isEmpty() && !filterState.isFilterActive) listOf()
        else mpsModel.quickFilterModel.filter.map { QuickFilterDataView(it) }
    }

    fun error(throwable: Throwable) = copy(
        result = State.Error(
            message = "",
            data = null,
            throwable = throwable,
        )
    )

    fun chooseAddress(chooseAddressModel: ChooseAddressModel) = copy(
        chooseAddressModel = chooseAddressModel
    )

    fun reload() = MPSState(
        parameter = parameter,
        chooseAddressModel = chooseAddressModel,
    )

    fun applyQuickFilter(quickFilterDataView: QuickFilterDataView): MPSState {
        val option = quickFilterDataView.firstOption ?: return this

        val isFilterApplied = filterState.isFilterApplied(option)
        val filterStateUpdated = filterState.setFilter(option, !isFilterApplied)

        return copy(
            parameter = filterStateUpdated.parameter,
            filterState = filterStateUpdated,
        )
    }

    fun loadMore() = copy(loadMoreThrowable = null)

    fun successLoadMore(mpsModel: MPSModel) = copy(
        result = State.Success(data = visitableList + mpsShopWidgetList(mpsModel)),
        paginationState = paginationState.incrementStart(),
    )

    fun errorLoadMore(throwable: Throwable) = copy(
        loadMoreThrowable = throwable
    )
}
