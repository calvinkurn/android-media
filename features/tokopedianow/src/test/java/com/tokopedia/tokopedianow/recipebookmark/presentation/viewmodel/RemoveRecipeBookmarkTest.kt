package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.util.TestUtils.verifyEquals
import com.tokopedia.tokopedianow.util.TestUtils.verifyFail
import com.tokopedia.tokopedianow.util.TestUtils.verifySuccess
import com.tokopedia.tokopedianow.util.TestUtils.verifyThrowable
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RemoveRecipeBookmarkTest: TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should be successful, show toaster and remove one item of recipe bookmarks`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        val removeResponse = "recipebookmark/removerecipebookmarksuccess.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                response = removeResponse
            )

        getAndRemoveRecipeBookmark()

        viewModel
            .toaster
            .value
            .verifySuccess()
            .verifyEquals(
                data = ToasterUiModel(
                    model = ToasterModel(
                        title = "Test Create Recipe 7",
                        message = "",
                        recipeId = "7",
                        isSuccess = true
                    ),
                    position = 0,
                    isRemoving = true
                )
            )

        verifyList(
            isAdding = false,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
        removeAndVerifyToaster()
    }

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should fail and add back item which has been removed`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        val removeResponse = "recipebookmark/removerecipebookmarkerror.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                response = removeResponse
            )

        getAndRemoveRecipeBookmark()

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
                    isRemoving = true
                )
            )

        verifyList(
            isAdding = true,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
    }

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should fail, throws an throwable and add back item which has been removed`(): Unit = runBlocking {
        val recipeBookmarkResponse = mockRecipeBookmark()

        val throwable = Throwable()

        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                throwable = throwable
            )

        getAndRemoveRecipeBookmark()

        viewModel
            .toaster
            .value
            .verifyFail()
            .verifyEquals(
                data = ToasterUiModel(
                    model = ToasterModel(
                        recipeId = "7",
                        isSuccess = false
                    ),
                    position = 0,
                    isRemoving = true
                )
            )
            .verifyThrowable(
                throwable = throwable
            )

        verifyList(
            isAdding = true,
            recipeBookmarkResponse = recipeBookmarkResponse
        )
    }

    private fun getAndRemoveRecipeBookmark() {
        viewModel
            .loadFirstPage()

        viewModel
            .removeRecipeBookmark(
                title = "Test Create Recipe 7",
                position = 0,
                recipeId = "7",
                isRemoving = true
            )
    }

}