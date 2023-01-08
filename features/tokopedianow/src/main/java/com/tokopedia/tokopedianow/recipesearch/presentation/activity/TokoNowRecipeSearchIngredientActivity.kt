package com.tokopedia.tokopedianow.recipesearch.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipesearch.presentation.fragment.TokoNowRecipeSearchIngredientFragment
import com.tokopedia.tokopedianow.sortfilter.presentation.bottomsheet.TokoNowSortFilterBottomSheet.Companion.EXTRA_SELECTED_FILTER
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import java.util.ArrayList

class TokoNowRecipeSearchIngredientActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment {
        val selectedFilters = intent
            .getParcelableArrayListExtra<SelectedFilter>(EXTRA_SELECTED_FILTER) ?: ArrayList()
        return TokoNowRecipeSearchIngredientFragment.newInstance(selectedFilters)
    }
}