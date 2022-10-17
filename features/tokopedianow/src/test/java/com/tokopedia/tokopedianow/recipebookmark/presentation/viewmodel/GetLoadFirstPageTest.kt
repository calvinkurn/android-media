package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.verifyEquals
import com.tokopedia.tokopedianow.util.TestUtils.verifyErrorCode
import com.tokopedia.tokopedianow.util.TestUtils.verifyFail
import com.tokopedia.tokopedianow.util.TestUtils.verifySuccess
import com.tokopedia.tokopedianow.util.TestUtils.verifyThrowable
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetLoadFirstPageTest: TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the loadFirstPage() function, the request should be successful and the response data are obtained (10 recipes and hasNext is true)`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel
            .loadFirstPage()

        viewModel
            .loadRecipeBookmarks
            .value
            .verifySuccess()
            .verifyEquals(
                data = recipeBookmarkResponse
                    .tokonowGetRecipeBookmarks
                    .data
                    .recipes
                    .mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadFirstPage() function, the request should be successful and the response data are obtained (8 recipes and hasNext is true)`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarksuccessequalsto8hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel
            .loadFirstPage()

        viewModel
            .loadRecipeBookmarks
            .value
            .verifySuccess()
            .verifyEquals(
                data = recipeBookmarkResponse
                    .tokonowGetRecipeBookmarks
                    .data
                    .recipes
                    .mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadFirstPage() function, the request should fail and the error code is obtained`(): Unit = runBlocking {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarkerror.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        viewModel
            .loadFirstPage()

        viewModel
            .loadRecipeBookmarks
            .value
            .verifyFail()
            .verifyErrorCode(
                errorCode = recipeBookmarkResponse
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

        viewModel
            .loadFirstPage()

        viewModel
            .loadRecipeBookmarks
            .value
            .verifyFail()
            .verifyThrowable(
                throwable = throwable
            )
    }

}