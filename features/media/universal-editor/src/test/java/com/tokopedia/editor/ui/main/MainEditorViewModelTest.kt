package com.tokopedia.editor.ui.main

import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.types.ToolType
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainEditorViewModelTest {

    private val navigationToolRepository = mockk<NavigationToolRepository>()
    private val paramFetcher = mockk<EditorParamFetcher>()

    private lateinit var viewModel: MainEditorViewModel

    @Before
    fun setUp() {
        viewModel = MainEditorViewModel(
            navigationToolRepository,
            paramFetcher
        )
    }

    @Test
    fun `it should be able to set global param and fetch navigation tool`() {
        // Given
        val param = UniversalEditorParam()

        every { paramFetcher.set(any()) } just Runs
        every { navigationToolRepository.tools() } returns listOf(
            NavigationTool(ToolType.TEXT, ToolType.TEXT.toString(), 0),
            NavigationTool(ToolType.PLACEMENT, ToolType.PLACEMENT.toString(), 0),
            NavigationTool(ToolType.AUDIO_MUTE, ToolType.AUDIO_MUTE.toString(), 0),
        )

        // When
        viewModel.onEvent(MainEditorEvent.SetupView(param))

        // Then
        assertTrue(viewModel.mainEditorState.value.param == param)
        assertTrue(viewModel.mainEditorState.value.tools.isNotEmpty())
    }

    @Test
    fun `it should be able to add a new text`() = runTest {
        // Given
        val model = InputTextModel()

        // When
        viewModel.onEvent(MainEditorEvent.ClickInputTextTool(model))

        // Then
        assertTrue(viewModel.uiEffect.first() is MainEditorEffect.OpenInputText)
        assertTrue(viewModel.inputTextState.value.isEdited.not())
    }

    @Test
    fun `it should be able to modify the text`() = runTest {
        // Given
        val model = InputTextModel(
            text = "new text"
        )

        // When
        viewModel.onEvent(MainEditorEvent.ClickInputTextTool(model, true))

        // Then
        assertTrue(viewModel.uiEffect.first() is MainEditorEffect.OpenInputText)
        assertTrue(viewModel.inputTextState.value.previousString.isNotEmpty())
        assertTrue(viewModel.inputTextState.value.isEdited)
    }

    @Test
    fun `it should be able get input text result`() = runTest {
        // Given
        val model = InputTextModel()

        // When
        viewModel.onEvent(MainEditorEvent.InputTextResult(model))

        // Then
        assertTrue(viewModel.inputTextState.value.model != null)
    }

    @Test
    fun `it should be able get reset active input text model`() = runTest {
        // When
        viewModel.onEvent(MainEditorEvent.ResetActiveInputText)

        // Then
        assertTrue(viewModel.inputTextState.value.model == null)
    }

}
