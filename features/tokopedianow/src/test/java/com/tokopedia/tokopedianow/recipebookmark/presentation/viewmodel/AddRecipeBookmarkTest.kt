package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.verifyEquals
import com.tokopedia.tokopedianow.util.TestUtils.verifyFail
import com.tokopedia.tokopedianow.util.TestUtils.verifyThrowable
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddRecipeBookmarkTest: TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the addRecipeBookmark() function, the request should be successful and add back item which has been removed`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        mockRemoveBookmark()

        val addResponse = "recipebookmark/addrecipebookmarksuccess.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        addRecipeBookmarkUseCase
            .mockAddRecipeBookmark(
                response = addResponse
            )

        getRemoveAndAddRecipeBookmark()

        verifyList(
            isAdding = true,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should fail and show toaster`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        mockRemoveBookmark()

        val addResponse = "recipebookmark/addrecipebookmarkerror.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        addRecipeBookmarkUseCase
            .mockAddRecipeBookmark(
                response = addResponse
            )

        getRemoveAndAddRecipeBookmark()

        viewModel
            .toaster
            .value
            .verifyFail()
            .verifyEquals(
                data = ToasterUiModel(
                    model = ToasterModel(
                        message = "failed",
                        recipeId = "7",
                        isSuccess = false
                    ),
                    position = 0,
                    isRemoving = false
                )
            )

        verifyList(
            isAdding = false,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
        removeAndVerifyToaster()
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should fail, throws an throwable and show toaster`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        mockRemoveBookmark()

        val throwable = Throwable()

        addRecipeBookmarkUseCase
            .mockAddRecipeBookmark(
                throwable = throwable
            )

        getRemoveAndAddRecipeBookmark()

        viewModel
            .toaster
            .value
            .verifyFail()
            .verifyEquals(
                data = ToasterUiModel(
                    model = ToasterModel(
                        message = "",
                        recipeId = "7",
                        isSuccess = false
                    ),
                    position = 0,
                    isRemoving = false
                )
            )
            .verifyThrowable(
                throwable = throwable
            )

        verifyList(
            isAdding = false,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
        removeAndVerifyToaster()
    }

    @Test
    fun `when calling the addRecipeBookmark() function, the request should be successful but cannot add back item because there is no recipe bookmarks data`(): Unit = runBlocking {
        val addResponse = "recipebookmark/addrecipebookmarksuccess.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        addRecipeBookmarkUseCase
            .mockAddRecipeBookmark(
                response = addResponse
            )

        viewModel
            .addRecipeBookmark(
                position = 0,
                recipeId = "7",
                isRemoving = false
            )

        viewModel.loadRecipeBookmarks
            .value
            .verifyEquals(
                data = emptyList()
            )
    }

    private fun getRemoveAndAddRecipeBookmark() {
        viewModel
            .loadFirstPage()

        viewModel
            .removeRecipeBookmark(
                title = "Test Create Recipe 7",
                position = 0,
                recipeId = "7",
                isRemoving = true
            )

        viewModel
            .addRecipeBookmark(
                position = 0,
                recipeId = "7",
                isRemoving = false
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