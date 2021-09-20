package com.tokopedia.tokopedianow.datefilter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.activity.TokoNowDateFilterActivity.Companion.EXTRA_SELECTED_DATE_FILTER
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*

class TokoNowDateFilterFragment: Fragment() {

    companion object {
        fun newInstance(selectedFilter: SelectedDateFilter?): TokoNowDateFilterFragment {
            return TokoNowDateFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SELECTED_DATE_FILTER, selectedFilter)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_date_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCategoryListBottomSheet()
    }

    private fun showCategoryListBottomSheet() {
        val selectedFilter = arguments?.getParcelable<SelectedDateFilter>(EXTRA_SELECTED_DATE_FILTER)
        TokoNowDateFilterBottomSheet.newInstance().show(childFragmentManager,selectedFilter)
    }
}