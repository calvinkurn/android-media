package com.tokopedia.tokopedianow.categoryfilter.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.categoryfilter.presentation.fragment.TokoNowCategoryFilterFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*

class TokoNowCategoryFilterActivity : BaseTokoNowActivity() {

    companion object {
        const val EXTRA_SELECTED_CATEGORY_FILTER = "extra_selected_category_filter"
        const val REQUEST_CODE_CATEGORY_FILTER_BOTTOM_SHEET = 1001
    }

    override fun getFragment(): Fragment {
        val selectedFilter = intent?.getParcelableExtra<SelectedSortFilter>(EXTRA_SELECTED_CATEGORY_FILTER)
        return TokoNowCategoryFilterFragment.newInstance(selectedFilter)
    }
}
