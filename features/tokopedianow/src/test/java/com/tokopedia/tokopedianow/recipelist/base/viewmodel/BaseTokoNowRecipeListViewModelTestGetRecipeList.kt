package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestGetRecipeList :
    BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `when get recipes success should add visitable items to visitable list`() {
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()

        val tags = listOf(
            TagUiModel(
                tag = "Test Create Tag 2",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "Test Create Tag 3",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "Test Create Tag 4",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "1",
                shouldFormatTag = true
            )
        )

        val expectedVisitableList = listOf(
            RecipeCountUiModel(14),
            RecipeUiModel(
                id = "28",
                title = "Kangkung",
                portion = 4,
                duration = 30,
                tags = tags,
                thumbnail = "https://images-staging.tokopedia.net/img/DqYeeh/2022/9/14/97f92d84-6b16-47cd-9af7-c3e77ac31dc5.jpg",
                isBookmarked = true
            )
        )

        val expectedSelectedFilters = emptyList<SelectedFilter>()
        val actualSelectedFilters = viewModel.selectedFilters

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)

        viewModel.showProgressBar
            .verifyValueEquals(false)

        assertEquals(expectedSelectedFilters, actualSelectedFilters)
    }

    @Test
    fun `given get recipes response empty when getRecipeList should add empty state to visitable list`() {
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

        viewModel.getRecipeList()

        val expectedVisitableList = listOf(
            RecipeEmptyStateUiModel(
                isFilterSelected = false,
                title = ""
            )
        )

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `when get recipes error should add server error to visitable list`() {
        onGetRecipes_thenReturn(NullPointerException())

        viewModel.getRecipeList()

        val expectedVisitableList = listOf(TokoNowServerErrorUiModel)

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)
    }
}