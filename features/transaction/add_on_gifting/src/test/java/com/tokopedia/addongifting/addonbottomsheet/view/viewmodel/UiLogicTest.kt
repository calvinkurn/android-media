package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import com.tokopedia.addongifting.addonbottomsheet.view.UiEvent
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UiLogicTest : BaseAddOnTest() {

    @Test
    fun `WHEN has saved add on state and has no changed add on selection state THEN ui state should be STATE_DISMISS_BOTTOM_SHEET`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialSelectedState = true,
                        isAddOnSelected = true
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_DISMISS_BOTTOM_SHEET)
        job.cancel()
    }

    @Test
    fun `WHEN has saved add on state and has changed add on selection state to false THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialSelectedState = true,
                        isAddOnSelected = false
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has saved add on state and has changed add on selection state to true THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialSelectedState = false,
                        isAddOnSelected = true
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has saved add on state and has changed receiver name state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNoteTo = "A",
                        addOnNoteTo = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has saved add on state and has changed sender name state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNoteFrom = "A",
                        addOnNoteFrom = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has saved add on state and has changed note state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = true
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNote = "A",
                        addOnNote = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has no saved add on state and has changed add on selection state THEN ui state should be STATE_DISMISS_BOTTOM_SHEET`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = false
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialSelectedState = true,
                        isAddOnSelected = false
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_DISMISS_BOTTOM_SHEET)
        job.cancel()
    }

    @Test
    fun `WHEN has no saved add on state and has changed receiver name state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = false
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNoteTo = "A",
                        addOnNoteTo = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has no saved add on state and has changed sender name state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = false
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNoteFrom = "A",
                        addOnNoteFrom = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

    @Test
    fun `WHEN has no saved add on state and has changed note state THEN ui state should be STATE_SHOW_CLOSE_DIALOG_CONFIRMATION`() = runBlockingTest {
        // Given
        viewModel.hasSavedState = false
        viewModel.updateFragmentUiModel(
                addOnUiModel = AddOnUiModel(
                        initialAddOnNote = "A",
                        addOnNote = "B"
                )
        )

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.validateCloseAction()

        // Then
        assert(result.first().state == UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION)
        job.cancel()
    }

}