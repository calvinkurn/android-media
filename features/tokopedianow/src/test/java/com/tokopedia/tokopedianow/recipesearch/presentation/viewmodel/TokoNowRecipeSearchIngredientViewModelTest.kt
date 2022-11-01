package com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel

import com.tokopedia.tokopedianow.recipelist.domain.model.TokonowRecipesFilterSort
import com.tokopedia.tokopedianow.recipesearch.presentation.uimodel.RecipeSearchIngredientUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeSearchIngredientViewModelTest : TokoNowRecipeSearchIngredientViewModelTestFixture() {

    @Test
    fun `given sort filter response when getIngredients should map ingredient items`() {
        val tokoNowRecipesFilterSort = "recipelist/get_sort_filter_response.json"
            .jsonToObject<TokonowRecipesFilterSort>()
        val sortFilterResponse = tokoNowRecipesFilterSort.response.data

        onGetSortFilter_thenReturn(sortFilterResponse)

        viewModel.getIngredients()

        val expectedIngredientItems = listOf(
            RecipeSearchIngredientUiModel(
                id = "53",
                title = "Balado",
                imgUrl = "https://images-staging.tokopedia.net/img/MKEzJd/2022/9/29/4a72ad37-909f-4677-8f97-4076a278c285.jpg",
                isChecked = false
            ),
            RecipeSearchIngredientUiModel(
                id = "11",
                title = "Test Create Ingredient 6",
                imgUrl = "",
                isChecked = false
            )
        )

        viewModel.ingredientItems
            .verifyValueEquals(expectedIngredientItems)
    }

    @Test
    fun `given sort filter error when getIngredients should map ingredient items`() {
        onGetSortFilter_thenReturn(NullPointerException())

        viewModel.getIngredients()

        viewModel.ingredientItems
            .verifyValueEquals(null)
    }

    @Test
    fun `given selected filter when check ingredient should update selected filters`() {
        val checkIngredient = true
        val tokoNowRecipesFilterSort = "recipelist/get_sort_filter_response.json"
            .jsonToObject<TokonowRecipesFilterSort>()
        val sortFilterResponse = tokoNowRecipesFilterSort.response.data

        onGetSortFilter_thenReturn(sortFilterResponse)

        viewModel.getIngredients()
        viewModel.onSelectIngredient("11", checkIngredient, "Test Create Ingredient 6")

        val expectedIngredientItems = listOf(
            RecipeSearchIngredientUiModel(
                id = "53",
                title = "Balado",
                imgUrl = "https://images-staging.tokopedia.net/img/MKEzJd/2022/9/29/4a72ad37-909f-4677-8f97-4076a278c285.jpg",
                isChecked = false
            ),
            RecipeSearchIngredientUiModel(
                id = "11",
                title = "Test Create Ingredient 6",
                imgUrl = "",
                isChecked = true
            )
        )

        val expectedSelectedFilter = listOf(
            SelectedFilter(
                id = "11",
                parentId = "ingredient_ids",
                text = "Test Create Ingredient 6"
            )
        )

        val actualSelectedFilters = viewModel.selectedFilters

        viewModel.ingredientItems
            .verifyValueEquals(expectedIngredientItems)

        assertEquals(expectedSelectedFilter, actualSelectedFilters)
    }

    @Test
    fun `given selected filter when uncheck ingredient should update selected filters`() {
        val checkIngredient = false
        val tokoNowRecipesFilterSort = "recipelist/get_sort_filter_response.json"
            .jsonToObject<TokonowRecipesFilterSort>()
        val sortFilterResponse = tokoNowRecipesFilterSort.response.data

        onGetSortFilter_thenReturn(sortFilterResponse)

        viewModel.selectedFilters = arrayListOf(
            SelectedFilter(
                id = "11",
                parentId = "ingredient_ids"
            )
        )

        viewModel.getIngredients()
        viewModel.onSelectIngredient("11", checkIngredient, "Test Create Ingredient 6")

        val expectedIngredientItems = listOf(
            RecipeSearchIngredientUiModel(
                id = "53",
                title = "Balado",
                imgUrl = "https://images-staging.tokopedia.net/img/MKEzJd/2022/9/29/4a72ad37-909f-4677-8f97-4076a278c285.jpg",
                isChecked = false
            ),
            RecipeSearchIngredientUiModel(
                id = "11",
                title = "Test Create Ingredient 6",
                imgUrl = "",
                isChecked = false
            )
        )

        val expectedSelectedFilter = emptyList<SelectedFilter>()
        val actualSelectedFilters = viewModel.selectedFilters

        viewModel.ingredientItems
            .verifyValueEquals(expectedIngredientItems)

        assertEquals(expectedSelectedFilter, actualSelectedFilters)
    }
}

