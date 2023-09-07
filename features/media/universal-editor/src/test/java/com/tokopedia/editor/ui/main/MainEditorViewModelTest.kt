package com.tokopedia.editor.ui.main

import android.graphics.BitmapFactory
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.faker.FakeVideoFlattenRepository
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.provider.ResourceProvider
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory

@RunWith(RobolectricTestRunner::class)
class MainEditorViewModelTest {

    private val navigationToolRepository = mockk<NavigationToolRepository>()
    private val fakeVideoFlattenRepository = FakeVideoFlattenRepository()
    private val resourceProvider = mockk<ResourceProvider>()
    private val paramFetcher = mockk<EditorParamFetcher>()

    private val dispatchers = CoroutineTestDispatchers

    private lateinit var viewModel: MainEditorViewModel

    @Before
    fun setUp() {
        viewModel = MainEditorViewModel(
            navigationToolRepository,
            fakeVideoFlattenRepository,
            resourceProvider,
            dispatchers,
            paramFetcher
        )

        mockkStatic(::isVideoFormat)
    }

    @Test
    fun `it should be able to set global param and fetch navigation tool`() {
        // Given
        val param = UniversalEditorParam()

        mockParamFetcher()
        mockNavigationTool()

        // When
        onEvent(MainEditorEvent.SetupView(param))

        // Then
        assertTrue(viewModel.mainEditorState.value.param == param)
        assertTrue(viewModel.mainEditorState.value.tools.isNotEmpty())
    }

    @Test
    fun `it should be able to add a new text`() = runTest {
        // Given
        val model = InputTextModel(text = "add a new text")

        // Verify
        onEvent(MainEditorEvent.AddInputTextPage)

        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.OpenInputText)
        assertTrue(effects[1] is MainEditorEffect.ParentToolbarVisibility)

        // Verify
        onEvent(MainEditorEvent.InputTextResult(model))
        assertTrue(viewModel.inputTextState.value.model?.text == model.text)
    }

    @Test
    fun `it should be able to modify the text`() = runTest {
        // Given
        val typographyId = 123
        val model = InputTextModel()

        // Verify
        onEvent(MainEditorEvent.EditInputTextPage(typographyId, model))
        assertTrue(viewModel.inputTextState.value.viewId.isMoreThanZero())

        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.OpenInputText)
        assertTrue(effects[1] is MainEditorEffect.ParentToolbarVisibility)

        // Verify
        val newModel = InputTextModel(text = "a new text")
        onEvent(MainEditorEvent.InputTextResult(newModel))
        assertTrue(viewModel.inputTextState.value.model?.text == newModel.text)
    }

    @Test
    fun `it should be able get input text result`() = runTest {
        // Given
        val expectedValue = "test"
        val model = InputTextModel(expectedValue)

        // When
        onEvent(MainEditorEvent.InputTextResult(model))

        // Then
        assertTrue(viewModel.inputTextState.value.model?.text == expectedValue)
    }

    @Test
    fun `it should not be able get input text result`() = runTest {
        // Given
        val model = InputTextModel()

        // When
        onEvent(MainEditorEvent.InputTextResult(model))

        // Then
        assertTrue(viewModel.inputTextState.value.model == null)
    }

    @Test
    fun `it should able to export a video file`() = runTest {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())

        fakeVideoFlattenRepository.emit("result")
        every { isVideoFormat(any()) } returns true

        // When
        onEvent(MainEditorEvent.ExportMedia(bitmap))

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.FinishEditorPage)
        assertTrue(effects[2] is MainEditorEffect.HideLoading)
    }

    @Test
    fun `it should fail to export a video file and show error toast`() = runTest {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())

        every { resourceProvider.getString(any()) } returns ""
        fakeVideoFlattenRepository.emit("")

        // When
        onEvent(MainEditorEvent.ExportMedia(bitmap))

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.ShowToastErrorMessage)
        assertTrue(effects[2] is MainEditorEffect.HideLoading)
    }

    @Test
    fun `it should be able get reset active input text model`() = runTest {
        // When
        onEvent(MainEditorEvent.ResetActiveInputText)

        // Then
        assertTrue(viewModel.inputTextState.value.model == null)
    }

    private fun recordEffect(): List<MainEditorEffect> {
        val dispatchers = CoroutineTestDispatchers
        val scope = CoroutineScope(dispatchers.immediate)
        val effects = mutableListOf<MainEditorEffect>()

        scope.launch {
            viewModel.uiEffect.collect {
                effects.add(it)
            }
        }

        scope.cancel()
        return effects
    }

    private fun mockParamFetcher() {
        every { paramFetcher.set(any()) } just Runs
    }

    private fun mockNavigationTool() {
        every { navigationToolRepository.tools() } returns listOf(
            NavigationTool(ToolType.TEXT, ToolType.TEXT.toString(), 0),
            NavigationTool(ToolType.PLACEMENT, ToolType.PLACEMENT.toString(), 0),
            NavigationTool(ToolType.AUDIO_MUTE, ToolType.AUDIO_MUTE.toString(), 0),
        )
    }

    private fun onEvent(event: MainEditorEvent) {
        viewModel.onEvent(event)
    }
}
