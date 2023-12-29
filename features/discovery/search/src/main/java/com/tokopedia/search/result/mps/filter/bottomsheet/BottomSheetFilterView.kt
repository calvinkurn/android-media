package com.tokopedia.search.result.mps.filter.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.filter.R
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.Callback
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.mvvm.RefreshableView

class BottomSheetFilterView(
    private val viewModel: BottomSheetFilterViewModel?,
    context: Context?,
    private val fragmentManager: FragmentManager,
): RefreshableView<BottomSheetFilterState>,
    ContextProvider by WeakReferenceContextProvider(context) {

    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private val parameter
        get() = viewModel?.paramater ?: mapOf()

    override fun refresh(state: BottomSheetFilterState) {
        if (state.isOpen) openBottomSheetFilter(state)
        else dismissBottomSheetFilter()
    }

    private fun openBottomSheetFilter(bottomSheetFilterState: BottomSheetFilterState) {
        val bottomSheetFilterModel = bottomSheetFilterState.dynamicFilterModel

        sortFilterBottomSheet = sortFilterBottomSheet ?: sortFilterBottomSheet(bottomSheetFilterModel)

        if (bottomSheetFilterModel != null)
            sortFilterBottomSheet?.setDynamicFilterModel(bottomSheetFilterModel)
    }

    private fun sortFilterBottomSheet(bottomSheetFilterModel: DynamicFilterModel?) =
        SortFilterBottomSheet().apply {
            show(
                this@BottomSheetFilterView.fragmentManager,
                parameter,
                bottomSheetFilterModel,
                sortFilterBottomSheetCallback(),
            )

            setOnDismissListener {
                viewModel?.closeBottomSheetFilter()
            }
        }

    private fun sortFilterBottomSheetCallback() = object : Callback {
        override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
            viewModel?.applyFilter(applySortFilterModel.mapParameter)
        }

        override fun getResultCount(mapParameter: Map<String, String>) {
            sortFilterBottomSheet?.setResultCountText(filterCountText())
        }
    }

    private fun filterCountText() =
        context?.getString(R.string.bottom_sheet_filter_finish_button_no_count) ?: ""

    private fun dismissBottomSheetFilter() {
        if (sortFilterBottomSheet != null) {
            sortFilterBottomSheet?.dismiss()
            sortFilterBottomSheet = null
        }
    }
}
