package com.tokopedia.tokopedianow.recipelist.presentation.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder

class RecipeListListener(private val view: RecipeListView) : RecipeViewHolder.RecipeItemListener {

    override fun onClickItem(recipe: RecipeUiModel) {
        goToRecipeDetail(recipe)
    }

    private fun goToRecipeDetail(recipe: RecipeUiModel) {
        val context = view.context()
        val appLink = UriUtil.buildUriAppendParam(
            ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
            mapOf(PARAM_RECIPE_ID to recipe.id)
        )
        RouteManager.route(context, appLink)
    }
}