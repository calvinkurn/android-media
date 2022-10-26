package com.tokopedia.media.editor.ui.activity

import android.graphics.BitmapFactory
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import org.junit.Rule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory

@RunWith(RobolectricTestRunner::class)
class EditorViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val saveImageRepo = mockk<SaveImageRepository>()

    private val viewModel = EditorViewModel(
        saveImageRepo
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
        val removeBackgroundPath = "/storage/sdcard/Pictures/remove_background.jpg"
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
        val totalTools = editorParam.editorToolsList.size

        // When
        viewModel.setEditorParam(editorParam)

        // Then
        assertEquals(totalTools, viewModel.editorParam.value?.editorToolsList?.size)
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
    fun `save image to gallery`() {
        // Given
        val dataList = pathSampleList.mapIndexed { index, path ->
            val stateList = listOf<EditorDetailUiModel>().toMutableList()

            if (index == 1) {
                stateList.add(
                    EditorDetailUiModel(originalUrl = path, resultUrl = path)
                )
            }

            EditorUiModel(originalUrl = path,
                editList = stateList
            )
        }

        // When
        every { saveImageRepo.saveToGallery(any(), any())}.answers {
            (args[1] as (List<String>) -> Unit).invoke(pathSampleList)
        }
        viewModel.saveToGallery(dataList) {}

        // Then
        verify { saveImageRepo.saveToGallery(any(), any()) }
    }

    @Test
    fun `clean cache`() {
        // When
        every { saveImageRepo.clearEditorCache() } just Runs
        viewModel.cleanImageCache()

        // Then
        verify { saveImageRepo.clearEditorCache() }
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

    companion object {
        private val pathSampleList = listOf(
            "/storage/sdcard/Pictures/Image1.jpg",
            "/storage/sdcard/Pictures/Image2.jpeg",
            "/storage/sdcard/Pictures/Image3.png"
        )

        private const val videoKey = "/storage/sdcard/Pictures/Video1.mp4"
    }
}