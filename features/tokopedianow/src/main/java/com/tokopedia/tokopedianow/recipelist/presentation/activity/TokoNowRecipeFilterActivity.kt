package com.tokopedia.tokopedianow.recipelist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipelist.presentation.fragment.TokoNowRecipeFilterFragment
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import java.util.ArrayList

class TokoNowRecipeFilterActivity : BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        val selectedFilters = intent.getParcelableArrayListExtra<SelectedFilter>(EXTRA_SELECTED_FILTER).orEmpty()
        val pageName = intent.getStringExtra(TokoNowRecipeFilterFragment.EXTRA_PAGE_NAME).orEmpty()
        return TokoNowRecipeFilterFragment.newInstance(
            selectedFilters = ArrayList(selectedFilters),
            pageName = pageName
        )
    }
}