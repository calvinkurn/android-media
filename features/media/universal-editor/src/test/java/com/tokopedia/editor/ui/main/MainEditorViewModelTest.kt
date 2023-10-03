package com.tokopedia.editor.ui.main

import android.graphics.BitmapFactory
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalytics
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.faker.FakeImageFlattenRepository
import com.tokopedia.editor.faker.FakeVideoFlattenRepository
import com.tokopedia.editor.ui.main.uimodel.InputTextParam
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.provider.ResourceProvider
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.picker.common.utils.isImageFormat
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory


@RunWith(RobolectricTestRunner::class)
class MainEditorViewModelTest {

    private val navigationToolRepository = mockk<NavigationToolRepository>()
    private val fakeVideoFlattenRepository = FakeVideoFlattenRepository()
    private val resourceProvider = mockk<ResourceProvider>()
    private val paramFetcher = mockk<EditorParamFetcher>()
    private val fakeImageFlattenRepository = FakeImageFlattenRepository()
    private val analytics = mockk<MainEditorAnalytics>()

    private val dispatchers = CoroutineTestDispatchers

    private lateinit var viewModel: MainEditorViewModel

    @JvmField
    @Rule
    var tempFolder = TemporaryFolder()

    @Before
    fun setUp() {
        viewModel = MainEditorViewModel(
            navigationToolRepository,
            fakeVideoFlattenRepository,
            fakeImageFlattenRepository,
            resourceProvider,
            dispatchers,
            paramFetcher,
            analytics
        )

        mockkStatic(::isVideoFormat)
        mockkStatic(::isImageFormat)
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
        mockAnalytics()
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
        val filePath = tempFolder.newFile("dummy.mp4")

        every { isVideoFormat(any()) } returns true
        mockAnalytics()
        mockParamFetcher()
        mockNavigationTool()

        // When
        onEvent(MainEditorEvent.SetupView(
            UniversalEditorParam(
                paths = listOf(filePath.path)
            )
        ))
        onEvent(MainEditorEvent.ExportMedia(bitmap))
        fakeVideoFlattenRepository.emit("result")

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.HideLoading)
        assertTrue(effects[2] is MainEditorEffect.FinishEditorPage)
    }

    @Test
    fun `it should fail to export a video file and show error toast`() = runTest {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())
        val filePath = tempFolder.newFile("dummy.mp4")

        every { resourceProvider.getString(any()) } returns ""
        mockAnalytics()
        mockParamFetcher()
        mockNavigationTool()

        // When
        onEvent(MainEditorEvent.SetupView(
            UniversalEditorParam(
                paths = listOf(filePath.path)
            )
        ))
        onEvent(MainEditorEvent.ExportMedia(bitmap))
        fakeVideoFlattenRepository.emit("")

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.HideLoading)
        assertTrue(effects[2] is MainEditorEffect.ShowToastErrorMessage)
    }

    @Test
    fun `it should able to export a image file`() = runTest {
        // Given
        val bitmap = ShadowBitmapFactory.create("asd", BitmapFactory.Options())
        val filePath = tempFolder.newFile("dummy.png")

        every { resourceProvider.getString(any()) } returns ""
        every { isImageFormat(any()) } returns true
        mockAnalytics()
        mockParamFetcher()
        mockNavigationTool()

        // When
        onEvent(MainEditorEvent.SetupView(
            UniversalEditorParam(
                paths = listOf(filePath.path)
            )
        ))
        onEvent(MainEditorEvent.ExportMedia(
            canvasTextBitmap = bitmap,
            imageBitmap = bitmap
        ))
        fakeImageFlattenRepository.emit("result")

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.HideLoading)
        assertTrue(effects[2] is MainEditorEffect.FinishEditorPage)
    }

    @Test
    fun `it should fail to export a image file`() = runTest {
        // Given
        val bitmap = ShadowBitmapFactory.create("asd", BitmapFactory.Options())
        val filePath = tempFolder.newFile("dummy.png")

        every { resourceProvider.getString(any()) } returns ""
        every { isImageFormat(any()) } returns true
        mockAnalytics()
        mockParamFetcher()
        mockNavigationTool()

        // When
        onEvent(MainEditorEvent.SetupView(
            UniversalEditorParam(
                paths = listOf(filePath.path)
            )
        ))
        onEvent(MainEditorEvent.ExportMedia(
            canvasTextBitmap = bitmap,
            imageBitmap = bitmap
        ))
        fakeImageFlattenRepository.emit("")

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowLoading)
        assertTrue(effects[1] is MainEditorEffect.HideLoading)
        assertTrue(effects[2] is MainEditorEffect.ShowToastErrorMessage)
    }

    @Test
    fun `it should be able get reset active input text model`() = runTest {
        // When
        onEvent(MainEditorEvent.ResetActiveInputText)

        // Then
        assertTrue(viewModel.inputTextState.value.model == null)
    }

    @Test
    fun `should update text flag on text is added`() = runTest {
        // When
        onEvent(MainEditorEvent.HasTextAdded(true))

        // Verify
        assertTrue(viewModel.mainEditorState.value.hasTextAdded)
    }

    @Test
    fun `should trigger placement page open sequence`() = runTest {
        // When
        mockParamFetcher()
        mockAnalytics()
        onEvent(MainEditorEvent.PlacementImagePage)

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.OpenPlacementPage)
    }

    @Test
    fun `should trigger input text page open sequence`() = runTest {
        // When
        mockParamFetcher()
        onEvent(MainEditorEvent.EditInputTextPage(
            viewId = 0,
            model = InputTextModel()
        ))

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.OpenInputText)
    }

    @Test
    fun `should update image when receive result from placement page`() = runTest {
        // Given
        val newImage = ImagePlacementModel(
            path = "cache/new_image.png",
            scale = 1f,
            angle = 0f,
            translateX = 0f,
            translateY = 0f
        )

        // When
        onEvent(MainEditorEvent.PlacementImageResult(newImage))

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.UpdatePagerSourcePath)
    }

    @Test
    fun `should flatten with mute audio`() = runTest {
        // When
        onEvent(MainEditorEvent.ManageVideoAudio)

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.RemoveAudioState)
        assertTrue(viewModel.mainEditorState.value.isRemoveAudio)
    }

    @Test
    fun `should flatten with audio`() = runTest {
        // When
        onEvent(MainEditorEvent.ManageVideoAudio)
        onEvent(MainEditorEvent.ManageVideoAudio)

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.RemoveAudioState)
        assertFalse(viewModel.mainEditorState.value.isRemoveAudio)
    }

    @Test
    fun `should reset current active input text`() = runTest {
        // Given
        val expectedInputTextParam = InputTextParam()

        // When
        onEvent(MainEditorEvent.ResetActiveInputText)

        // Verify
        assertEquals(expectedInputTextParam, viewModel.inputTextState.value)
    }

    @Test
    fun `should close editor home page on close clicked`() = runTest {
        // When
        mockAnalytics()
        onEvent(MainEditorEvent.ClickHeaderCloseButton(true))

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.CloseMainEditorPage)
    }

    @Test
    fun `should close editor home page on close clicked with confirmation dialog`() = runTest {
        // When
        mockAnalytics()
        onEvent(MainEditorEvent.HasTextAdded(true))
        onEvent(MainEditorEvent.ClickHeaderCloseButton())

        // Verify
        val effects = recordEffect()
        assertTrue(effects[0] is MainEditorEffect.ShowCloseDialogConfirmation)
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
        every { paramFetcher.get() } returns UniversalEditorParam(
            paths = listOf("cache/dummy_path.png")
        )
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

    private fun mockAnalytics() {
        every { analytics.backPageClick() } just runs
        every { analytics.toolTextClick() } just runs
        every { analytics.finishPageClick(any(), any(), any()) } just runs
        every { analytics.toolAdjustCropClick() } just runs
    }
}
