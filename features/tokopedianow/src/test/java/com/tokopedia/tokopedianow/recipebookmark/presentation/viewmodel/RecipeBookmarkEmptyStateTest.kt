package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkAction
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeBookmarkEmptyStateTest : TokoNowRecipeBookmarkViewModelTestFixture() {

    companion object {
        private const val ERROR_PAGE_NOT_FOUND = "404"
        private const val ERROR_MAINTENANCE = "502"
        private const val ERROR_SERVER = "500"
    }

    @Test
    fun `given ERROR_PAGE_NOT_FOUND when click empty state button should update uiAction with PressBackButton`() = runTest {
        val event = RecipeBookmarkEvent.ClickEmptyStateActionButton(
            errorCode = ERROR_PAGE_NOT_FOUND
        )

        observeUiAction()

        viewModel.onEvent(event)

        verifyUiAction(
            expectedUiAction = RecipeBookmarkAction.PressBackButton
        )
    }

    @Test
    fun `given ERROR_MAINTENANCE when click empty state button should update uiAction with PressBackButton`() = runTest {
        val event = RecipeBookmarkEvent.ClickEmptyStateActionButton(
            errorCode = ERROR_MAINTENANCE
        )

        observeUiAction()

        viewModel.onEvent(event)

        verifyUiAction(
            expectedUiAction = RecipeBookmarkAction.PressBackButton
        )
    }

    @Test
    fun `given ERROR_SERVER when click empty state button should call get recipe bookmark use case`() = runTest {
        val event = RecipeBookmarkEvent.ClickEmptyStateActionButton(
            errorCode = ERROR_SERVER
        )

        viewModel.onEvent(event)

        verifyGetRecipeBookmarkUseCaseCalled()
    }

    @Test
    fun `given press back btn event when onEvent called should update uiAction with PressBackButton`() = runTest {
        val event = RecipeBookmarkEvent.PressBackButton

        observeUiAction()

        viewModel.onEvent(event)

        verifyUiAction(
            expectedUiAction = RecipeBookmarkAction.PressBackButton
        )
    }
}
