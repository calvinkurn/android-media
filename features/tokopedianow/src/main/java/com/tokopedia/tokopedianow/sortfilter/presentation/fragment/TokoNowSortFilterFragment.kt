package com.tokopedia.tokopedianow.sortfilter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.sortfilter.presentation.activity.TokoNowSortFilterActivity.Companion.SORT_VALUE
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet
import com.tokopedia.tokopedianow.sortfilter.presentation.uimodel.SortFilterUiModel

class TokoNowSortFilterFragment : Fragment() {

    companion object {
        fun newInstance(sortValue: Int): TokoNowSortFilterFragment {
            return TokoNowSortFilterFragment().apply {
                arguments = Bundle().apply {
                    putInt(SORT_VALUE, sortValue)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSortFilterBottomSheet()
    }

    private fun showSortFilterBottomSheet() {
        val title = getString(R.string.tokopedianow_sort_filter_title)
        val sort = arguments?.getInt(SORT_VALUE).orZero()
        val items = listOf(
            SortFilterUiModel(
                titleRes = R.string.tokopedianow_sort_filter_item_most_frequently_bought_bottomsheet,
                isChecked = sort == TokoNowSortFilterBottomSheet.FREQUENTLY_BOUGHT,
                isLastItem = false,
                value = TokoNowSortFilterBottomSheet.FREQUENTLY_BOUGHT
            ),
            SortFilterUiModel(
                titleRes = R.string.tokopedianow_sort_filter_item_last_bought_bottomsheet,
                isChecked = sort == TokoNowSortFilterBottomSheet.LAST_BOUGHT,
                isLastItem = true,
                value = TokoNowSortFilterBottomSheet.LAST_BOUGHT
            )
        )

        val bottomSheet = TokoNowSortFilterBottomSheet.newInstance().apply {
            setTitle(title)
            filterItemsDisplayed = items
            sortValue = sort
        }

        bottomSheet.show(childFragmentManager)
    }
}