package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.tokopedianow.util.TestUtils.verifyEquals
import com.tokopedia.tokopedianow.util.TestUtils.verifySuccess
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetLoadMoreRecipeBookmarkTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the loadMore() function, the request should be successful and the response data are obtained (10 recipes of the first page and 8 recipes of the second page)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.loadFirstPage()

        val secondPageResponse = "recipebookmark/recipebookmarksuccessequalsto8hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.loadMore(
            isAtTheBottomOfThePage = true
        )

        viewModel.moreRecipeBookmarks.value
            .verifySuccess()
            .verifyEquals(
                data = (firstPageResponse.tokonowGetRecipeBookmarks.data.recipes + secondPageResponse.tokonowGetRecipeBookmarks.data.recipes)
                    .mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadMore() function, the request should be successful and the response data are obtained (10 recipes of the first and the second page)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.loadFirstPage()

        val secondPageResponse = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.loadMore(
            isAtTheBottomOfThePage = true
        )

        viewModel.moreRecipeBookmarks.value
            .verifySuccess()
            .verifyEquals(
                data = (firstPageResponse.tokonowGetRecipeBookmarks.data.recipes + secondPageResponse.tokonowGetRecipeBookmarks.data.recipes)
                    .mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadMore() function, the request should fail and the data are only from loadFirstPage() (10 recipes)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.loadFirstPage()

        val secondPageResponse = "recipebookmark/recipebookmarkerror.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.loadMore(
            isAtTheBottomOfThePage = true
        )

        viewModel.moreRecipeBookmarks.value
            .verifySuccess()
            .verifyEquals(
                data = firstPageResponse.tokonowGetRecipeBookmarks.data.recipes.mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadMore() function, the request should fail because useCase throws an throwable and the data are only from loadFirstPage() (10 recipes)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.loadFirstPage()

        val throwable = Throwable()
        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                throwable = throwable
            )

        viewModel.loadMore(
            isAtTheBottomOfThePage = true
        )

        viewModel.moreRecipeBookmarks.value
            .verifySuccess()
            .verifyEquals(
                data = firstPageResponse
                    .tokonowGetRecipeBookmarks
                    .data
                    .recipes
                    .mapResponseToUiModelList()
            )
    }

    @Test
    fun `when calling the loadMore() function but noNeedLoadMore is false, the last of layout is RecipeProgressBarUiModel and the position is not at the end of the bottom should not get any data`(): Unit = runBlocking {
        mockNoNeedLoadMoreAndLayout()

        viewModel
            .loadMore(
                isAtTheBottomOfThePage = false
            )

        Assert.assertEquals(viewModel
            .moreRecipeBookmarks
            .value,
            null
        )
    }

    @Test
    fun `when calling the loadMore() function but noNeedLoadMore is false, the last of layout is RecipeProgressBarUiModel and even the position is at the end of the bottom should not get any data`(): Unit = runBlocking {
        mockNoNeedLoadMoreAndLayout()

        viewModel
            .loadMore(
                isAtTheBottomOfThePage = true
            )

        Assert.assertEquals(viewModel
            .moreRecipeBookmarks
            .value,
            null
        )
    }

    @Test
    fun `when calling the loadMore() function but noNeedLoadMore by default is true and even the position is at the end of the bottom should get isOnScrollNotNeeded true`() = runBlocking {
        viewModel
            .loadMore(
                isAtTheBottomOfThePage = true
            )

        Assert.assertEquals(viewModel
            .isOnScrollNotNeeded
            .value,
            true
        )
    }

    private fun mockNoNeedLoadMoreAndLayout() {
        viewModel
            .mockPrivateField(
                name = "noNeedLoadMore",
                value = false
            )

        viewModel
            .mockPrivateField(
                name = "layout",
                value = listOf(
                    RecipeProgressBarUiModel()
                )
            )
    }

}