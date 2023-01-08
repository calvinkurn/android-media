package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestApplyFilter : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given recipes response empty when applyFilter should add empty state with isFilterSelected true`() {
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

        val selectedFilters = listOf(
            SelectedFilter(id = "1", parentId = "1")
        )

        onGetRecipes_thenReturn(getRecipesEmptyResponse)

        viewModel.applyFilter(selectedFilters)

        val expectedVisitableList = listOf(
            RecipeHeaderUiModel,
            RecipeFilterUiModel(selectedFiltersCount = 1),
            RecipeEmptyStateUiModel(
                isFilterSelected = true,
                title = ""
            )
        )

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given selected filters when applyFilter should update selected filters and recipe list params`() {
        val warehouseId = 5L
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        val selectedFilters = listOf(
            SelectedFilter(
                id = "Newest",
                parentId = "sort_by"
            ),
            SelectedFilter(
                id = "1",
                parentId = "ingredient_ids"
            ),
            SelectedFilter(
                id = "2",
                parentId = "ingredient_ids"
            ),
            SelectedFilter(
                id = "3",
                parentId = "tag_ids"
            ),
            SelectedFilter(
                id = "4",
                parentId = "tag_ids"
            ),
            SelectedFilter(
                id = "30To60",
                parentId = "duration"
            ),
            SelectedFilter(
                id = "LessThan5",
                parentId = "portion"
            )
        )

        val recipeListParamSlot = slot<RecipeListParam>()

        onGetRecipes_thenReturn(getRecipesResponse, recipeListParamSlot)
        onGetWarehouseId_thenReturn(warehouseId)

        viewModel.applyFilter(selectedFilters)

        val actualSelectedFilter = viewModel.selectedFilters
        val actualRecipeListParam = recipeListParamSlot.captured

        verifyGetRecipeListParams(
            expectedPage = 1,
            expectedWarehouseId = "5",
            expectedSortByParams = "Newest",
            expectedTagIdsParam = "3,4",
            expectedIngredientIdsParam = "1,2",
            expectedDurationParam = "30To60",
            expectedPortionParam = "LessThan5",
            actualRecipeListParam = actualRecipeListParam,
        )
        assertEquals(selectedFilters, actualSelectedFilter)
    }
}