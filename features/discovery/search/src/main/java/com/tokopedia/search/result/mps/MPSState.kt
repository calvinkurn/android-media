package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.filter.common.FilterState
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.utils.mvvm.SearchUiState
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel as ChooseAddressModel

data class MPSState(
    val parameter: Map<String, String> = mapOf(),
    val result: State<List<Visitable<*>>> = State.Loading(),
    val chooseAddressModel: ChooseAddressModel? = null,
    val quickFilterDataViewList: List<QuickFilterDataView> = listOf(),
    val filterState: FilterState = FilterState(),
): SearchUiState {

    fun success(mpsModel: MPSModel) = copy(
        result = State.Success(data = successData(mpsModel)),
        quickFilterDataViewList = mpsModel.quickFilterModel.filter.map { QuickFilterDataView(it) },
        filterState = filterState.from(parameter, mpsModel.quickFilterModel.filter),
    )

    private fun successData(mpsModel: MPSModel): List<Visitable<MPSTypeFactory>> =
        if (mpsModel.shopList.isNotEmpty())
            listOf(ChooseAddressDataView) +
                mpsShopWidgetList(mpsModel)
        else
            listOf(MPSEmptyStateKeywordDataView)

    private fun mpsShopWidgetList(mpsModel: MPSModel) =
        mpsModel.shopList.map(MPSShopWidgetDataView::create)

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

    fun loading() = copy(
        result = State.Loading(),
        quickFilterDataViewList = listOf(),
    )

    fun applyQuickFilter(quickFilterDataView: QuickFilterDataView): MPSState {
        val option = quickFilterDataView.firstOption ?: return this

        val isFilterApplied = filterState.isFilterApplied(option)
        val filterStateSelected = filterState.setFilter(option, !isFilterApplied)

        return copy(
            parameter = filterStateSelected.parameter,
            filterState = filterStateSelected,
        )
    }
}
