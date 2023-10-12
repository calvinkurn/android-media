package com.tokopedia.search.result.mps.filter.quickfilter

import com.tokopedia.filter.common.FilterState
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.utils.mvvm.SearchUiState

data class QuickFilterState(
    val data: List<QuickFilterDataView> = listOf(),
    val filterState: FilterState = FilterState(),
): SearchUiState {

    fun success(mpsModel: MPSModel, filterState: FilterState) = copy(
        data = quickFilterData(mpsModel, filterState),
        filterState = filterState,
    )

    private fun quickFilterData(
        mpsModel: MPSModel,
        filterState: FilterState,
    ): List<QuickFilterDataView> {
        return if (mpsModel.shopList.isEmpty() && !filterState.isFilterActive) listOf()
        else mpsModel.quickFilterList.map { QuickFilterDataView(it) }
    }
}
