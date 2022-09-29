package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel.ChipType.MORE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.sortfilter.presentation.model.SelectedFilter
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTest : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given enableHeaderBackground true when onViewCreated should add header item to visitable list`() {
        viewModel.enableHeaderBackground = true

        viewModel.onViewCreated()

        val moreFilterChip = RecipeChipFilterUiModel(
            id = "1",
            type = MORE_FILTER
        )

        val expectedVisitableList = listOf(
            RecipeHeaderUiModel,
            RecipeFilterUiModel(listOf(moreFilterChip))
        )

        val expectedEnableHeaderBackground = true
        val actualEnableHeaderBackground = viewModel.enableHeaderBackground

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)

        viewModel.showHeaderBackground
            .verifyValueEquals(true)

        assertEquals(expectedEnableHeaderBackground, actualEnableHeaderBackground)
    }

    @Test
    fun `given enableHeaderBackground false when onViewCreated should NOT add header item to visitable list`() {
        viewModel.enableHeaderBackground = false

        viewModel.onViewCreated()

        val moreFilterChip = RecipeChipFilterUiModel(
            id = "1",
            type = MORE_FILTER
        )

        val expectedVisitableList = listOf(
            RecipeFilterUiModel(listOf(moreFilterChip))
        )

        val expectedEnableHeaderBackground = false
        val actualEnableHeaderBackground = viewModel.enableHeaderBackground

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)

        viewModel.showHeaderBackground
            .verifyValueEquals(false)

        assertEquals(expectedEnableHeaderBackground, actualEnableHeaderBackground)
    }

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
    fun `when get recipes error should add server error to visitable list`() {
        onGetRecipes_thenReturn(NullPointerException())

        viewModel.getRecipeList()

        val expectedVisitableList = listOf(TokoNowServerErrorUiModel)

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)
    }

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
            expectedIngredientIdsParams = null,
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
    fun `given selected filters when applyFilter should update selected filters and recipe list params`() {
        val warehouseId = 5L
        val getRecipesResponse = "recipelist/get_recipes_response.json"
            .jsonToObject<TokoNowGetRecipes>()

        val selectedFilters = listOf(
            SelectedFilter(
                id = "Newest",
                parentId = "1"
            ),
            SelectedFilter(
                id = "1",
                parentId = "2"
            ),
            SelectedFilter(
                id = "2",
                parentId = "2"
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
            expectedIngredientIdsParams = "1,2",
            actualRecipeListParam = actualRecipeListParam,
        )
        assertEquals(selectedFilters, actualSelectedFilter)
    }
}