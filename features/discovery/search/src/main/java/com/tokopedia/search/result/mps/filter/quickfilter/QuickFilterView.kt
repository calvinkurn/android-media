package com.tokopedia.search.result.mps.filter.quickfilter

import com.tokopedia.filter.common.FilterState
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.search.utils.mvvm.RefreshableView
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class QuickFilterView(
    private val viewModel: QuickFilterViewModel?,
    private val sortFilterView: SortFilter?,
): RefreshableView<QuickFilterState> {

    override fun refresh(state: QuickFilterState) {
        if (state.data.isEmpty()) hideQuickFilterView()
        else showQuickFilterView(state)
    }

    private fun hideQuickFilterView() {
        sortFilterView?.hide()
    }

    private fun showQuickFilterView(quickFilterState: QuickFilterState) {
        sortFilterView?.run {
            show()

            sortFilterHorizontalScrollView.scrollX = 0
            parentListener = ::openBottomSheetFilter
            addItem(sortFilterItemList(quickFilterState))
        }
    }

    private fun sortFilterItemList(quickFilterState: QuickFilterState) =
        quickFilterState.data.mapTo(ArrayList()) { sortFilterItem(it, quickFilterState) }

    private fun sortFilterItem(
        quickFilterDataView: QuickFilterDataView,
        quickFilterState: QuickFilterState,
    ): SortFilterItem =
        SortFilterItem(quickFilterDataView.filter.title).apply {
            type = getType(quickFilterDataView, quickFilterState.filterState)
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
        viewModel?.onQuickFilterSelected(quickFilterDataView)
    }

    private fun openBottomSheetFilter() {
        viewModel?.openBottomSheetFilter()
    }
}
