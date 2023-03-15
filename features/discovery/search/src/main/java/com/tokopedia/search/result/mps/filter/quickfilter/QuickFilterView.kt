package com.tokopedia.search.result.mps.filter.quickfilter

import com.tokopedia.filter.common.FilterState
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.search.result.mps.MPSState
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class QuickFilterView(
    private val mpsViewModel: MPSViewModel?,
) {

    private var quickFilterDataViewList: List<QuickFilterDataView>? = null

    fun refreshQuickFilter(
        sortFilterView: SortFilter?,
        mpsState: MPSState,
    ) {
        if (this.quickFilterDataViewList == mpsState.quickFilterDataViewList
            || sortFilterView == null) return

        this.quickFilterDataViewList = mpsState.quickFilterDataViewList

        configureSortFilterView(sortFilterView, mpsState)
    }

    private fun configureSortFilterView(sortFilterView: SortFilter, mpsState: MPSState) {
        if (quickFilterDataViewList?.isEmpty() == true) hideQuickFilterView(sortFilterView)
        else showQuickFilterView(sortFilterView, mpsState)
    }

    private fun hideQuickFilterView(sortFilterView: SortFilter) {
        sortFilterView.hide()
    }

    private fun showQuickFilterView(
        sortFilterView: SortFilter,
        mpsState: MPSState
    ) {
        sortFilterView.show()

        sortFilterView.sortFilterHorizontalScrollView.scrollX = 0
        sortFilterView.parentListener = ::openBottomSheetFilter
        sortFilterView.addItem(sortFilterItemList(mpsState))
    }

    private fun sortFilterItemList(mpsState: MPSState) =
        quickFilterDataViewList?.mapTo(ArrayList()) {
            sortFilterItem(it, mpsState)
        } ?: arrayListOf()

    private fun sortFilterItem(
        quickFilterDataView: QuickFilterDataView,
        mpsState: MPSState,
    ): SortFilterItem =
        SortFilterItem(quickFilterDataView.filter.title).apply {
            type = getType(quickFilterDataView, mpsState.filterState)
            listener = { onQuickFilterClicked(quickFilterDataView) }
        }

    private fun getType(
        quickFilterDataView: QuickFilterDataView,
        filterState: FilterState,
    ): String {
        val isOptionSelected = filterState.isFilterApplied(quickFilterDataView.firstOption)

        return if (isOptionSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }

    private fun onQuickFilterClicked(quickFilterDataView: QuickFilterDataView) {
        mpsViewModel?.onQuickFilterSelected(quickFilterDataView)
    }

    private fun openBottomSheetFilter() {

    }
}
