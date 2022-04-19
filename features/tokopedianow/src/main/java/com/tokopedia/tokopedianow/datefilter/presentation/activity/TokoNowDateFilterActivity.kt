package com.tokopedia.tokopedianow.datefilter.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*

class TokoNowDateFilterActivity: BaseTokoNowActivity() {
    companion object {
        const val EXTRA_SELECTED_DATE_FILTER = "extra_selected_date_filter"
        const val REQUEST_CODE_DATE_FILTER_BOTTOMSHEET = 1455
    }

    override fun getFragment(): Fragment {
        val selectedFilter = intent?.getParcelableExtra<SelectedDateFilter>(EXTRA_SELECTED_DATE_FILTER)
        return TokoNowDateFilterFragment.newInstance(selectedFilter)
    }
}