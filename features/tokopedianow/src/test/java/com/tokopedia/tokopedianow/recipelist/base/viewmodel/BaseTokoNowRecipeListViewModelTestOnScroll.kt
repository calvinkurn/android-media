package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestOnScroll : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given scrolled to bottom of recipe list when onScroll should call load more recipes`() {
        val lastVisibleItemIndex = 1 // last item index
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()
        val recipeListParamSlot = slot<RecipeListParam>()

        onGetWarehouseId_thenReturn(warehouseId = 1)
        onGetRecipes_thenReturn(getRecipesResponse, recipeListParamSlot)

        viewModel.getRecipeList()
        viewModel.onScroll(lastVisibleItemIndex)
        viewModel.onScroll(lastVisibleItemIndex)

        val actualRecipeListParam = recipeListParamSlot.captured

        verifyGetRecipeListParams(
            expectedPage = 2,
            expectedWarehouseId = "1",
            expectedSortByParams = null,
            expectedTagIdsParam = null,
            expectedIngredientIdsParam = null,
            expectedDurationParam = null,
            expectedPortionParam = null,
            actualRecipeListParam = actualRecipeListParam,
        )
        verifyGetRecipeListUseCaseCalled(times = 2)
    }

    @Test
    fun `given has NOT scrolled to bottom of recipe list when onScroll should call get recipe list ONCE`() {
        val lastVisibleItemIndex = 0
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()
        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 1)
    }

    @Test
    fun `given hasNext false when onScroll should call get recipe list ONCE`() {
        val hasNext = false
        val lastVisibleItemIndex = 1
        val getRecipes = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        val metadata = getRecipes.response.metadata.copy(hasNext = hasNext)
        val response = getRecipes.response.copy(metadata = metadata)
        val getRecipesResponse = getRecipes.copy(response = response)

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()
        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 1)
    }

    @Test
    fun `given visitable list contains LoadingMoreModel should call get recipe list ONCE`() {
        val lastVisibleItemIndex = 2
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()

        addItemToVisitableList(LoadingMoreModel())

        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 1)
    }

    @Test
    fun `given hasNext true when load more should set removeScrollListener false`() {
        val hasNext = true
        val lastVisibleItemIndex = 1
        val getRecipes = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        val metadata = getRecipes.response.metadata.copy(hasNext = hasNext)
        val response = getRecipes.response.copy(metadata = metadata)
        val getRecipesResponse = getRecipes.copy(response = response)

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()
        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 2)

        viewModel.removeScrollListener
            .verifyValueEquals(false)
    }

    @Test
    fun `given hasNext false when load more should set removeScrollListener false`() {
        val hasNext = false
        val lastVisibleItemIndex = 1
        val firstGetRecipeResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        val metadata = firstGetRecipeResponse.response.metadata.copy(hasNext = hasNext)
        val response = firstGetRecipeResponse.response.copy(metadata = metadata)
        val secondGetRecipesResponse = firstGetRecipeResponse.copy(response = response)

        onGetRecipes_thenReturn(firstGetRecipeResponse)

        viewModel.getRecipeList()

        onGetRecipes_thenReturn(secondGetRecipesResponse)

        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 2)

        viewModel.removeScrollListener
            .verifyValueEquals(true)
    }

    @Test
    fun `given scrolled to bottom of recipe list when load more error should remove load more progress bar`() {
        val lastVisibleItemIndex = 1 // last item index
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()

        onGetRecipes_thenReturn(NullPointerException())

        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 2)

        val expectedLoadMoreProgressBar = null
        val actualLoadMoreProgressBar = viewModel.visitableList.value?.firstOrNull {
            it is LoadingMoreModel
        }

        assertEquals(expectedLoadMoreProgressBar, actualLoadMoreProgressBar)
    }

    @Test
    fun `given progress bar shown when onScroll should call get recipe list use case ONCE`() {
        val lastVisibleItemIndex = 1 // last item index
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        onGetRecipes_thenReturn(getRecipesResponse)

        viewModel.getRecipeList()

        setShowProgressBar(shown = true)

        viewModel.onScroll(lastVisibleItemIndex)

        verifyGetRecipeListUseCaseCalled(times = 1)
    }
}