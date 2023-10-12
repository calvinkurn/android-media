package com.tokopedia.tokopedianow.categoryfilter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categoryfilter.presentation.activity.TokoNowCategoryFilterActivity.Companion.EXTRA_SELECTED_CATEGORY_FILTER
import com.tokopedia.tokopedianow.categoryfilter.presentation.bottomsheet.TokoNowCategoryFilterBottomSheet
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*

class TokoNowCategoryFilterFragment : Fragment() {

    companion object {
        fun newInstance(selectedFilter: SelectedSortFilter?): TokoNowCategoryFilterFragment {
            return TokoNowCategoryFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SELECTED_CATEGORY_FILTER, selectedFilter)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_category_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedFilter = arguments?.getParcelable<SelectedSortFilter>(EXTRA_SELECTED_CATEGORY_FILTER)
        val bottomSheet = TokoNowCategoryFilterBottomSheet.newInstance(selectedFilter)
        bottomSheet.show(childFragmentManager)
    }
}
