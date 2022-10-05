package com.tokopedia.tokopedianow.recipelist.presentation.listener

import android.content.Intent
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment.Companion.REQUEST_CODE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.activity.TokoNowRecipeFilterActivity
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeFilterViewHolder.RecipeChipFilterListener
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import java.util.*

class RecipeFilterListener(
    private val view: RecipeListView,
    private val analytics: RecipeListAnalytics
) : RecipeChipFilterListener {

    override fun onClickMoreFilter() {
        val selectedFilters = ArrayList(view.viewModel().selectedFilters)
        val intent = Intent(view.context(), TokoNowRecipeFilterActivity::class.java)
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_FILTER, selectedFilters)
        view.fragment().startActivityForResult(intent, REQUEST_CODE_FILTER)
        analytics.clickFilter()
    }
}