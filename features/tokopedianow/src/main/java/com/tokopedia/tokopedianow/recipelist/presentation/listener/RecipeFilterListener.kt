package com.tokopedia.tokopedianow.recipelist.presentation.listener

import android.content.Intent
import com.tokopedia.tokopedianow.recipehome.presentation.activity.TokoNowRecipeFilterActivity
import com.tokopedia.tokopedianow.recipehome.presentation.fragment.TokoNowRecipeFilterFragment.Companion.EXTRA_SELECTED_FILTER_IDS
import com.tokopedia.tokopedianow.recipelist.base.fragment.BaseTokoNowRecipeListFragment.Companion.REQUEST_CODE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel.ChipType.MORE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeChipFilterViewHolder.RecipeChipFilterListener
import java.util.*

class RecipeFilterListener(private val view: RecipeListView) : RecipeChipFilterListener {

    override fun onClickItem(filter: RecipeChipFilterUiModel) {
        if (filter.type == MORE_FILTER) {
            val selectedFilterIds = view.viewModel().selectedFilters.map { it.id }
            val intent = Intent(view.context(), TokoNowRecipeFilterActivity::class.java)
            intent.putStringArrayListExtra(EXTRA_SELECTED_FILTER_IDS, ArrayList(selectedFilterIds))
            view.fragment().startActivityForResult(intent, REQUEST_CODE_FILTER)
        }
    }
}