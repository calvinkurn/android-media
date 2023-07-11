package com.tokopedia.media.editor.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.editor.data.repository.AddLogoFilterRepository
import com.tokopedia.media.editor.data.repository.BitmapCreationRepository
import com.tokopedia.media.editor.ui.uimodel.EditorAddLogoUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.getTokopediaCacheDir
import com.tokopedia.picker.common.PICKER_URL_FILE_CODE
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.FileUtil
import io.mockk.coEvery
import org.junit.Rule
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class EditorViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()

    private val saveImageRepo = mockk<SaveImageRepository>()
    private val userSession = mockk<UserSessionInterface>()
    private val bitmapCreationRepository = mockk<BitmapCreationRepository>()

    private val viewModel = EditorViewModel(
        saveImageRepo,
        userSession,
        bitmapCreationRepository,
        coroutineScopeRule.dispatchers
    )

    @Test
    fun `get edit state should get edit state`() {
        // When
        viewModel.initStateList(pathSampleList)

        // Then
        assertTrue(viewModel.getEditState(pathSampleList.first()) != null)
        assertEquals(viewModel.editStateList.values.size, pathSampleList.size)
    }

    @Test
    fun `get null edit state should return null`() {
        // Given
        val key = videoKey

        // When
        viewModel.initStateList(pathSampleList)

        // Then
        assertEquals(null, viewModel.getEditState(key))
    }

    @Test
    fun `set state should get updated index`() {
        // Given
        val updateIndex = 1

        // When
        viewModel.initStateList(pathSampleList)
        viewModel.addEditState(pathSampleList[updateIndex], EditorDetailUiModel())

        // Then
        assertEquals(updateIndex, viewModel.updatedIndexItem.value)
    }

    @Test
    fun `add new item should add item on collection`() {
        // When
        viewModel.initStateList(pathSampleList)
        viewModel.addEditState(videoKey, EditorDetailUiModel())

        // Then
        assertEquals(pathSampleList.size + 1, viewModel.editStateList.values.size)
    }

    @Test
    fun `remove background state on undo should clear state`() {
        // Given
        val stateTarget = pathSampleList[0]
        val removeBackgroundPath = removeBackgroundPath
        val editState = EditorDetailUiModel()
        editState.removeBackgroundUrl = removeBackgroundPath

        // When
        viewModel.initStateList(pathSampleList)
        viewModel.addEditState(stateTarget, EditorDetailUiModel())
        viewModel.addEditState(stateTarget, editState)
        viewModel.undoState(stateTarget)
        viewModel.undoState(stateTarget)
        viewModel.undoState(stateTarget)
        viewModel.addEditState(stateTarget, editState)

        // Then
        val asd = viewModel.getEditState(stateTarget)
        assertEquals(removeBackgroundPath, asd?.editList?.last()?.removeBackgroundUrl)
    }

    @Test
    fun `add new item on undo state should increase state number`() {
        // Given
        val updateTarget = pathSampleList[0]
        val stateNumber = 5

        // When
        viewModel.initStateList(pathSampleList)
        for (i in 0 until stateNumber) {
            viewModel.addEditState(updateTarget, EditorDetailUiModel(), false)
        }
        viewModel.undoState(updateTarget)
        viewModel.addEditState(updateTarget, EditorDetailUiModel())

        // Then
        assertEquals(stateNumber, viewModel.getEditState(updateTarget)?.editList?.size)
    }

    @Test
    fun `set editor param should get editor param`() {
        // Given
        val editorParam = EditorParam().apply {
            withRemoveBackground()
        }
        val totalTools = editorParam.editorToolsList().size

        // When
        viewModel.setEditorParam(editorParam)

        // Then
        assertEquals(totalTools, viewModel.editorParam.value?.editorToolsList()?.size)
    }

    @Test
    fun `undo image state should return active index `() {
        // Given
        val undoTarget = pathSampleList[1]

        // When
        viewModel.initStateList(pathSampleList)
        viewModel.addEditState(undoTarget, EditorDetailUiModel())
        viewModel.undoState(undoTarget)
        viewModel.undoState(undoTarget)

        // Then
        assertEquals(1, viewModel.getEditState(undoTarget)?.backValue)
    }

    @Test
    fun `undo empty image state should return null`() {
        // When
        viewModel.initStateList(pathSampleList)
        val undoResult = viewModel.undoState(videoKey)

        // Then
        assertEquals(null, undoResult)
    }

    @Test
    fun `redo image state should return active index`() {
        // Given
        val redoTarget = pathSampleList[1]

        // When
        viewModel.initStateList(pathSampleList)
        viewModel.addEditState(redoTarget, EditorDetailUiModel())
        viewModel.undoState(redoTarget)
        viewModel.redoState(redoTarget)
        viewModel.redoState(redoTarget)

        // Then
        assertEquals(0, viewModel.getEditState(redoTarget)?.backValue)
    }

    @Test
    fun `redo empty image state should return null`() {
        // When
        viewModel.initStateList(pathSampleList)
        val redoResult = viewModel.redoState(videoKey)

        // Then
        assertEquals(null, redoResult)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `on page finish return result`() {
        // Given
        val dataList = createUiModelState(0, -1)
        dataList[2].editList.clear()
        dataList[3].editList.clear()

        // When
        viewModel.finishPage(
            dataList
        )

        assertNotNull(viewModel.editorResult.getOrAwaitValue())
    }

    @Test
    fun `on page finish with overlay logo`() {
        // Given
        val dataList = createUiModelState(-1, -1)
        dataList[0].editList[0].addLogoValue.overlayLogoUrl = "overlay.png"

        // When
        coEvery { saveImageRepo.flattenImage(any(), any(), any()) } returns "result_overlay.png"
        viewModel.finishPage(
            dataList
        )

        viewModel.editorResult.getOrAwaitValue().let { finalUrlList ->
            assertNotNull(finalUrlList)
            assertEquals(dataList.size, finalUrlList.size)
        }
    }

    @Test
    fun `on page finish with overlay text`() {
        // Given
        val dataList = createUiModelState(-1, -1)
        dataList[0].editList[0].addTextValue = EditorAddTextUiModel(
            textValue = "content",
            textImagePath = "overlay.png"
        )

        // When
        coEvery { saveImageRepo.flattenImage(any(), any(), any()) } returns "result_overlay.png"
        viewModel.finishPage(
            dataList
        )

        // Then
        viewModel.editorResult.getOrAwaitValue().let { finalUrlList ->
            assertNotNull(finalUrlList)
            assertEquals(dataList.size, finalUrlList.size)
        }
    }

    @Test
    fun `on page finish with failed generate overlay text`() {
        // Given
        val dataList = createUiModelState(-1, -1)
        dataList[0].editList[0].addTextValue = EditorAddTextUiModel(
            textValue = "content"
        )

        // When
        coEvery { saveImageRepo.flattenImage(any(), any(), any()) } returns "result_overlay.png"
        viewModel.finishPage(
            dataList
        )

        // Then
        viewModel.editorResult.getOrAwaitValue().let { finalUrlList ->
            // Then
            assertNotNull(finalUrlList)
            assertEquals(dataList.size, finalUrlList.size)
        }
    }

    @Test
    fun `save image to cache`() {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())

        // When
        every { saveImageRepo.saveToCache(any(), any(), any()) } returns null
        viewModel.saveToCache(
            bitmap,
            sourcePath = ""
        )

        // Then
        verify { saveImageRepo.saveToCache(any(), any(), any()) }
    }

    @Test
    fun `check if user have shop`() {
        // Given
        var isShopAvail = false

        // When
        every { userSession.hasShop() } returns true
        isShopAvail = viewModel.isShopAvailable()

        // Then
        assertEquals(true, isShopAvail)
    }

    @Test
    fun `check if user have no shop`() {
        // Given
        var isShopAvail = false

        // When
        every { userSession.hasShop() } returns false
        isShopAvail = viewModel.isShopAvailable()

        // Then
        assertEquals(false, isShopAvail)
    }

    @Test
    fun `check memory is overflow true`() {
        // Given
        val width = 100
        val height = 100
        var isMemoryOverflow = false

        // When
        every { bitmapCreationRepository.isBitmapOverflow(any(), any()) } returns true
        isMemoryOverflow = viewModel.isMemoryOverflow(width, height)

        // Then
        assertEquals(true, isMemoryOverflow)
    }

    @Test
    fun `check memory is overflow false`() {
        // Given
        val width = 100
        val height = 100
        var isMemoryOverflow = false

        // When
        every { bitmapCreationRepository.isBitmapOverflow(any(), any()) } returns false
        isMemoryOverflow = viewModel.isMemoryOverflow(width, height)

        // Then
        assertEquals(false, isMemoryOverflow)
    }

    @Test
    fun `check crop is success`() {
        // Given
        val source = ShadowBitmapFactory.create("", BitmapFactory.Options())
        var resultSource: Bitmap? = null
        val cropRotateData = EditorCropRotateUiModel(
            offsetX = 10,
            offsetY = 20,
            imageWidth = 100,
            imageHeight = 100
        )

        // When
        every { bitmapCreationRepository.createBitmap(any()) } returns source
        resultSource = viewModel.cropImage(source, cropRotateData)

        // Then
        assertNotNull(resultSource)
    }

    private fun createUiModelState(excludeIndex: Int, cameraIndex: Int): List<EditorUiModel> {
        return pathSampleList.mapIndexed { index, path ->
            val stateList = listOf<EditorDetailUiModel>().toMutableList()
            val originalPath = if (cameraIndex == index) "$tokopediaCacheDir/$path" else path

            if (index != excludeIndex) {
                stateList.add(
                    EditorDetailUiModel(originalUrl = originalPath, resultUrl = originalPath)
                )
            }

            EditorUiModel(
                originalUrl = originalPath,
                editList = stateList
            )
        }
    }

    companion object {
        private const val tokopediaCacheDir = "com.tokopedia.tkpd/cache/Tokopedia"
        private const val removeBackgroundPath = "/storage/sdcard/Pictures/remove_background.jpg"
        private const val addLogoPath = "/storage/sdcard/Pictures/add_logo.jpg"

        private val pathSampleList = listOf(
            "/storage/sdcard/Pictures/Image1.jpg",
            "/storage/sdcard/Pictures/Image2.jpeg",
            "/storage/sdcard/Pictures/$PICKER_URL_FILE_CODE.jpeg",
            "${getTokopediaCacheDir()}/$PICKER_URL_FILE_CODE.png",
            "/storage/sdcard/Pictures/Image3.png"
        )

        private const val videoKey = "/storage/sdcard/Pictures/Video1.mp4"
    }
}
