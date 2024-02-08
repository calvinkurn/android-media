package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.ui.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetLoadFirstPageTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the loadFirstPage() function, the request should be successful and the response data are obtained (10 recipes and hasNext is true)`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val expectedItemList = recipeBookmarkResponse.tokonowGetRecipeBookmarks
            .data.recipes.mapResponseToUiModelList()

        verifyList(expectedItemList = expectedItemList)
    }

    @Test
    fun `when calling the loadFirstPage() function, the request should be successful and the response data are obtained (8 recipes and hasNext is true)`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarksuccessequalsto8hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val expectedItemList = recipeBookmarkResponse.tokonowGetRecipeBookmarks
            .data.recipes.mapResponseToUiModelList()

        verifyList(expectedItemList = expectedItemList)
    }

    @Test
    fun `when calling the loadFirstPage() function, the request should fail and the error code is obtained`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarkerror.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        verifyError(
            expectedStatusCode = recipeBookmarkResponse
                .tokonowGetRecipeBookmarks
                .header
                .statusCode
        )
    }

    @Test
    fun `when calling the loadFirstPage() function, the request should fail because useCase throws an throwable`(): Unit = runBlocking {
        val throwable = Throwable()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                throwable = throwable
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        verifyError(
            expectedThrowable = throwable
        )
    }
}
