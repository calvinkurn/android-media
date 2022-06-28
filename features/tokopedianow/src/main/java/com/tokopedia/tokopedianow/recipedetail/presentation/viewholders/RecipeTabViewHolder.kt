package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel

class RecipeTabViewHolder(itemView: View): AbstractViewHolder<RecipeTabUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_tab
    }

    override fun bind(tab: RecipeTabUiModel) {

    }
}