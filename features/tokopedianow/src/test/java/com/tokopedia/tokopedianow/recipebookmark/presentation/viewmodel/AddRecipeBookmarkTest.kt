package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipebookmark.ui.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeBookmarkTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the addRecipeBookmark() function, the request should be successful and add back item which has been removed`(): Unit = runBlocking {
        mockRemoveBookmark()

        val addResponse = "recipebookmark/addrecipebookmarksuccess.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        mockAddRecipeBookmark(addResponse)

        getRemoveAndAddRecipeBookmark()

        verifyList(isAdding = true)
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should fail and show toaster`() = runTest {
        val addResponse = "recipebookmark/addrecipebookmarkerror.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        observeUiAction()
        mockAddRecipeBookmark(addResponse)

        getRemoveAndAddRecipeBookmark()

        val expectedToaster = ToasterUiModel(
            model = ToasterModel(
                message = "failed",
                recipeId = "7",
                isSuccess = false
            ),
            position = 0,
            isRemoving = false
        )

        verifyToaster(expectedToaster = expectedToaster)

        verifyList(expectedItemList = recipeList)
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should fail, throws an throwable and show toaster`() = runTest {
        val throwable = Throwable()

        observeUiAction()
        mockRemoveBookmark()

        addRecipeBookmarkUseCase
            .mockAddRecipeBookmark(
                throwable = throwable
            )

        getRemoveAndAddRecipeBookmark()

        val expectedToaster = ToasterUiModel(
            model = ToasterModel(
                message = "",
                recipeId = "7",
                isSuccess = false,
                throwable = throwable
            ),
            position = 0,
            isRemoving = false
        )

        verifyToaster(expectedToaster)

        verifyList(isAdding = false)
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should be successful but cannot add back item because there is no recipe bookmarks data`(): Unit = runBlocking {
        val addResponse = "recipebookmark/addrecipebookmarksuccess.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        mockAddRecipeBookmark(addResponse)

        viewModel.onEvent(
            RecipeBookmarkEvent.AddRecipeBookmark(
                position = 0,
                recipeId = "7",
                isRemoving = false
            )
        )

        verifyList(expectedItemList = null)
    }

    private fun getRemoveAndAddRecipeBookmark() {
        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        viewModel.onEvent(
            RecipeBookmarkEvent.RemoveRecipeBookmark(
                title = "Test Create Recipe 7",
                position = 0,
                recipeId = "7",
                isRemoving = true
            )
        )

        viewModel.onEvent(
            RecipeBookmarkEvent.AddRecipeBookmark(
                position = 0,
                recipeId = "7",
                isRemoving = false
            )
        )
    }

    private fun mockRemoveBookmark() {
        val removeBookmarkResponse = "recipebookmark/removerecipebookmarksuccess.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                response = removeBookmarkResponse
            )
    }
}
