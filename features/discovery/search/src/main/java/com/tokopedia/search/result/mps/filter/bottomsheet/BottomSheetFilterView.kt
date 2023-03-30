package com.tokopedia.search.result.mps.filter.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.filter.R
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.Callback
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.mps.MPSState
import com.tokopedia.search.result.mps.MPSViewModel

class BottomSheetFilterView(
    private val context: Context?,
    private val mpsViewModel: MPSViewModel?,
    private val fragmentManager: FragmentManager,
) {

    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var bottomSheetFilterState: BottomSheetFilterState? = null

    private val parameter
        get() = mpsViewModel?.stateFlow?.value?.parameter ?: mapOf()

    fun refreshBottomSheetFilter(mpsState: MPSState) {
        if (bottomSheetFilterState == mpsState.bottomSheetFilterState) return

        bottomSheetFilterState = mpsState.bottomSheetFilterState

        val isBottomSheetFilterOpen = bottomSheetFilterState?.isBottomSheetFilterOpen ?: false

        if (isBottomSheetFilterOpen) openBottomSheetFilter()
        else dismissBottomSheetFilter()
    }

    private fun openBottomSheetFilter() {
        val bottomSheetFilterModel = bottomSheetFilterState?.bottomSheetFilterModel

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
                mpsViewModel?.closeBottomSheetFilter()
            }
        }

    private fun sortFilterBottomSheetCallback() = object : Callback {
        override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
            mpsViewModel?.applyFilter(applySortFilterModel.mapParameter)
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
