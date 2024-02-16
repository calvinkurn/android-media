package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.ui.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetLoadMoreRecipeBookmarkTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the loadMore() function, the request should be successful and the response data are obtained (10 recipes of the first page and 8 recipes of the second page)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val secondPageResponse = "recipebookmark/recipebookmarksuccessequalsto8hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        val expectedItemList =
            (
                firstPageResponse.tokonowGetRecipeBookmarks.data.recipes +
                    secondPageResponse.tokonowGetRecipeBookmarks.data.recipes
                )
                .mapResponseToUiModelList()

        verifyList(expectedItemList = expectedItemList)
    }

    @Test
    fun `when calling the loadMore() function, the request should be successful and the response data are obtained (10 recipes of the first and the second page)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val secondPageResponse = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        val expectedItemList =
            (
                firstPageResponse.tokonowGetRecipeBookmarks.data.recipes +
                    secondPageResponse.tokonowGetRecipeBookmarks.data.recipes
                )
                .mapResponseToUiModelList()

        verifyList(expectedItemList = expectedItemList)
    }

    @Test
    fun `when calling the loadMore() function, the request should fail and the data are only from loadFirstPage() (10 recipes)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val secondPageResponse = "recipebookmark/recipebookmarkerror.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = secondPageResponse
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        verifyList(
            expectedItemList = firstPageResponse.tokonowGetRecipeBookmarks
                .data.recipes.mapResponseToUiModelList()
        )
    }

    @Test
    fun `when calling the loadMore() function, the request should fail because useCase throws an throwable and the data are only from loadFirstPage() (10 recipes)`(): Unit = runBlocking {
        val firstPageResponse = mockRecipeBookmark()

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        val throwable = Throwable()
        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                throwable = throwable
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        verifyList(
            expectedItemList = firstPageResponse
                .tokonowGetRecipeBookmarks
                .data
                .recipes
                .mapResponseToUiModelList()
        )
    }

    @Test
    fun `when calling the loadMore() function but noNeedLoadMore is false, the last of layout is RecipeProgressBarUiModel and the position is not at the end of the bottom should not get any data`(): Unit = runBlocking {
        mockNoNeedLoadMoreAndLayout()

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = false))

        verifyList(expectedItemList = null)
    }

    @Test
    fun `when calling the loadMore() function but noNeedLoadMore is false, the last of layout is RecipeProgressBarUiModel and even the position is at the end of the bottom should not get any data`(): Unit = runBlocking {
        mockNoNeedLoadMoreAndLayout()

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = false))

        verifyList(expectedItemList = null)
    }

    @Test
    fun `given hasNext response false when scrolledToBottom should only call get recipe bookmark use case ONCE`() {
        val response = "recipebookmark/recipebookmarksuccessequalsto8hasnextfalse.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = response
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        verifyGetRecipeBookmarkUseCaseCalled(times = 1)
    }

    @Test
    fun `given visitable list empty when scrolledToBottom should NOT call get recipe bookmark use case`() {
        viewModel
            .mockPrivateField(
                name = "visitableList",
                value = SnapshotStateList<RecipeProgressBarUiModel>()
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        verifyGetRecipeBookmarkUseCaseCalled(times = 0)
    }

    @Test
    fun `given hasNext response false and scrolledToBottom true when loadMore should only call get recipe bookmark use case ONCE`() {
        val response = "recipebookmark/recipebookmarksuccessequalsto8hasnextfalse.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = response
            )

        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        mockNoNeedLoadMoreAndLayout()

        viewModel.onEvent(RecipeBookmarkEvent.LoadMoreRecipeBookmarkList(scrolledToBottom = true))

        verifyGetRecipeBookmarkUseCaseCalled(times = 1)
    }

    private fun mockNoNeedLoadMoreAndLayout() {
        val visitableList = SnapshotStateList<RecipeProgressBarUiModel>()
        visitableList.add(RecipeProgressBarUiModel())

        viewModel
            .mockPrivateField(
                name = "noNeedLoadMore",
                value = false
            )

        viewModel
            .mockPrivateField(
                name = "visitableList",
                value = visitableList
            )
    }
}
