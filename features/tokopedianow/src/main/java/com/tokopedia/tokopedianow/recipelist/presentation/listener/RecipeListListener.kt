package com.tokopedia.tokopedianow.recipelist.presentation.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder

class RecipeListListener(private val context: Context?): RecipeViewHolder.RecipeItemListener {

    override fun onClickItem(recipe: RecipeUiModel) {
        goToRecipeDetail(recipe)
    }

    private fun goToRecipeDetail(recipe: RecipeUiModel) {
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.RECIPE_DETAIL, recipe.id)
    }
}