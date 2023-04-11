package com.tokopedia.tokopedianow.recipelist.presentation.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID
import com.tokopedia.tokopedianow.recipelist.analytics.RecipeListAnalytics
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.view.RecipeListView
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder
import com.tokopedia.user.session.UserSessionInterface

class RecipeListListener(
    private val view: RecipeListView,
    private val analytics: RecipeListAnalytics,
    private val viewModel: BaseTokoNowRecipeListViewModel,
    private val userSession: UserSessionInterface,
    private val directToLoginPage: () -> Unit
) : RecipeViewHolder.RecipeItemListener {

    override fun onClickItem(recipe: RecipeUiModel, position: Int) {
        goToRecipeDetail(recipe)
        analytics.clickRecipeCard(
            recipeId = recipe.id,
            recipeTitle = recipe.title,
            position = position
        )
    }

    override fun onImpressItem(recipe: RecipeUiModel, position: Int) {
        analytics.impressRecipeCard(
            recipeId = recipe.id,
            recipeTitle = recipe.title,
            position = position
        )
    }

    override fun onClickBookmark(recipe: RecipeUiModel, position: Int, isBookmarked: Boolean) {
        if (userSession.isLoggedIn) {
            if (isBookmarked) {
                viewModel.addRecipeBookmark(
                    recipeId = recipe.id,
                    position = position,
                    title = recipe.title
                )

                analytics.clickBookmarkRecipe(
                    recipeId = recipe.id,
                    recipeTitle = recipe.title
                )
            } else {
                viewModel.removeRecipeBookmark(
                    recipeId = recipe.id,
                    position = position,
                    title = recipe.title
                )

                analytics.clickUnBookmarkRecipe(
                    recipeId = recipe.id,
                    recipeTitle = recipe.title
                )
            }
        } else {
            directToLoginPage()
        }
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
