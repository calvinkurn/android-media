package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestBookmark : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given add recipe bookmark success when addRecipeBookmark should update toaster live data success`() {
        val recipeId = "1"
        val position = 1
        val title = "Kentang"

        val addBookmarkResponse = "recipebookmark/addrecipebookmarksuccess.json"
            .jsonToObject<AddRecipeBookmarkResponse>()

        onAddBookmark_thenReturn(addBookmarkResponse)

        viewModel.addRecipeBookmark(recipeId, position, title)

        val expectedBookmarkToaster = ToasterUiModel(
            isRemoving = false,
            position = position,
            model = ToasterModel(
                title = title,
                recipeId = recipeId,
                isSuccess = true
            )
        )

        viewModel.showBookmarkToaster
            .verifyValueEquals(expectedBookmarkToaster)
    }

    @Test
    fun `given add recipe bookmark error when addRecipeBookmark should update toaster live data error`() {
        val recipeId = "1"
        val position = 1
        val title = "Kentang"

        onAddBookmark_thenReturn(NullPointerException())

        viewModel.addRecipeBookmark(recipeId, position, title)

        val expectedBookmarkToaster = ToasterUiModel(
            isRemoving = false,
            position = position,
            model = ToasterModel(
                recipeId = recipeId,
                isSuccess = false
            )
        )

        viewModel.showBookmarkToaster
            .verifyValueEquals(expectedBookmarkToaster)
    }

    @Test
    fun `given remove recipe bookmark success when removeRecipeBookmark should update toaster live data success`() {
        val recipeId = "1"
        val position = 1
        val title = "Kentang"

        val removeBookmarkResponse = "recipebookmark/removerecipebookmarksuccess.json"
            .jsonToObject<RemoveRecipeBookmarkResponse>()

        onRemoveBookmark_thenReturn(removeBookmarkResponse)

        viewModel.removeRecipeBookmark(recipeId, position, title)

        val expectedBookmarkToaster = ToasterUiModel(
            isRemoving = true,
            position = position,
            model = ToasterModel(
                title = title,
                recipeId = recipeId,
                isSuccess = true
            )
        )

        viewModel.showBookmarkToaster
            .verifyValueEquals(expectedBookmarkToaster)
    }

    @Test
    fun `given remove recipe bookmark error when removeRecipeBookmark should update toaster live data error`() {
        val recipeId = "1"
        val position = 1
        val title = "Kentang"

        onRemoveBookmark_thenReturn(NullPointerException())

        viewModel.removeRecipeBookmark(recipeId, position, title)

        val expectedBookmarkToaster = ToasterUiModel(
            isRemoving = true,
            position = position,
            model = ToasterModel(
                recipeId = recipeId,
                isSuccess = false
            )
        )

        viewModel.showBookmarkToaster
            .verifyValueEquals(expectedBookmarkToaster)
    }
}