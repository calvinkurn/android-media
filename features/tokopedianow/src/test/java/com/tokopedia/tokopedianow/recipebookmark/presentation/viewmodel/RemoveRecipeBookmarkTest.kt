package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.recipebookmark.ui.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoveRecipeBookmarkTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should be successful, show toaster and remove one item of recipe bookmarks`() = runTest {
        val removeResponse = "recipebookmark/removerecipebookmarksuccess.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        observeUiAction()
        mockRemoveRecipeBookmark(removeResponse)

        getAndRemoveRecipeBookmark()

        val expectedToaster = ToasterUiModel(
            model = ToasterModel(
                title = "Test Create Recipe 7",
                message = "",
                recipeId = "7",
                isSuccess = true
            ),
            position = 0,
            isRemoving = true
        )

        verifyToaster(expectedToaster)

        verifyList(
            expectedItemList = recipeList,
            isAdding = false
        )
    }

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should fail and add back item which has been removed`() = runTest {
        val removeResponse = "recipebookmark/removerecipebookmarkerror.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        observeUiAction()
        mockRemoveRecipeBookmark(removeResponse)

        getAndRemoveRecipeBookmark()

        val expectedToaster = ToasterUiModel(
            model = ToasterModel(
                message = "failed",
                recipeId = "7",
                isSuccess = false
            ),
            position = 0,
            isRemoving = true
        )

        verifyToaster(expectedToaster)

        verifyList(
            expectedItemList = recipeList,
            isAdding = true
        )
    }

    @Test
    fun `when calling the removeRecipeBookmark() function, the request should fail, throws an throwable and add back item which has been removed`() = runTest {
        val throwable = Throwable()

        observeUiAction()
        mockRemoveRecipeBookmark(throwable)

        getAndRemoveRecipeBookmark()

        val expectedToaster = ToasterUiModel(
            model = ToasterModel(
                recipeId = "7",
                isSuccess = false,
                throwable = throwable
            ),
            position = 0,
            isRemoving = true
        )

        verifyToaster(expectedToaster)

        verifyList(
            isAdding = true,
            expectedItemList = recipeList
        )
    }

    private fun getAndRemoveRecipeBookmark() {
        viewModel.onEvent(RecipeBookmarkEvent.LoadRecipeBookmarkList)

        viewModel.onEvent(
            RecipeBookmarkEvent.RemoveRecipeBookmark(
                title = "Test Create Recipe 7",
                position = 0,
                recipeId = "7",
                isRemoving = true
            )
        )
    }
}
