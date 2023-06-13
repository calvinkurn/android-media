package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestResetFilter : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `when resetFilter should refresh page`() {
        val recipesResponse = emptyList<RecipeResponse>()
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()
        val response = getRecipesResponse.response

        val getRecipesEmptyResponse = getRecipesResponse.copy(
            response = response.copy(
                data = response.data.copy(
                    recipes = recipesResponse
                )
            )
        )

        onGetRecipes_thenReturn(getRecipesEmptyResponse)

        viewModel.resetFilter()

        val expectedVisitableList = listOf(
            RecipeHeaderUiModel,
            RecipeFilterUiModel(selectedFiltersCount = 0),
            RecipeEmptyStateUiModel(isFilterSelected = false, title = "")
        )

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)
    }
}