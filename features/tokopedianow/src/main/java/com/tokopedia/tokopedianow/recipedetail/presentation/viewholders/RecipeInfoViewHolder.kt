package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel

class RecipeInfoViewHolder(itemView: View): AbstractViewHolder<RecipeInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_info
    }

    override fun bind(recipe: RecipeInfoUiModel) {

    }
}