package com.tokopedia.tokopedianow.recipelist.presentation.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder

class RecipeListListener(
    private val view: RecipeListView,
    private val analytics: RecipeListAnalytics,
    private val warehouseId: String,
    private val pageName: String
) : RecipeViewHolder.RecipeItemListener {

    override fun onClickItem(recipe: RecipeUiModel, position: Int) {
        goToRecipeDetail(recipe)
        analytics.clickRecipeCard(
            recipeId = recipe.id,
            recipeTitle = recipe.title,
            warehouseId = warehouseId,
            position = position,
            pageName = pageName
        )
    }

    override fun onImpressItem(recipe: RecipeUiModel, position: Int) {
        analytics.impressRecipeCard(
            recipeId = recipe.id,
            recipeTitle = recipe.title,
            warehouseId = warehouseId,
            position = position,
            pageName = pageName
        )
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