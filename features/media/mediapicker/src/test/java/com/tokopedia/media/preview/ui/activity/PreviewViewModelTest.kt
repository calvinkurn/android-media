package com.tokopedia.media.preview.ui.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.util.getFileFormatByMimeType
import com.tokopedia.picker.common.util.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PreviewViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val testCoroutineScope = TestCoroutineScope(
        coroutineScopeRule.dispatchers.main
    )

    private val saveToGalleryManagerMock = mockk<SaveToGalleryManager>()
    private val imageCompressorMock = mockk<ImageCompressionManager>()

    private lateinit var viewModel: PreviewViewModel

    @Before
    fun setup() {
        mockkStatic(::getFileFormatByMimeType)

        viewModel = PreviewViewModel(
            imageCompressorMock,
            saveToGalleryManagerMock
        )
    }

    @Test
    fun `check isLoading not empty`() {
        // When
        every { imageCompressorMock.compress(any()) } returns flow { }
        viewModel.files(mockMediaUiModel)

        // Then
        assert(viewModel.isLoading.value != null)

        clearAllMocks()
    }

    @Test
    fun `get result original asset`() {
        // Given
        lateinit var pickerResult: PickerResult

        // When
        every { getFileFormatByMimeType(any(), any(), any()) } returns false
        every { saveToGalleryManagerMock.dispatch(any()) } returns null
        every { imageCompressorMock.compress(any()) } returns flow {
            emit(
                listOf("")
            )
        }

        // Then
        testCoroutineScope.launch {
            viewModel.result
                .shareIn(this, SharingStarted.Eagerly, 1)
                .collect {
                    pickerResult = it
                    this.cancel()
                }
        }

        viewModel.files(mockMediaUiModel)
        assertEquals(mockMediaUiModel.size, pickerResult.originalPaths.size)

        clearAllMocks()
    }

    @Test
    fun `get result compressed image`() {
        // Given
        lateinit var pickerResult: PickerResult

        // When
        every { getFileFormatByMimeType(any(), any(), any()) } returns false
        every { saveToGalleryManagerMock.dispatch(any()) } returns null
        every { imageCompressorMock.compress(any()) } answers {
            flow {
                emit(firstArg())
            }
        }

        // Then
        testCoroutineScope.launch {
            viewModel.result
                .shareIn(this, SharingStarted.Eagerly, 1)
                .collect {
                    pickerResult = it
                    this.cancel()
                }
        }

        viewModel.files(mockMediaUiModel)
        assertEquals(expectedCompressedImageSize, pickerResult.compressedImages.size)

        clearAllMocks()
    }

    companion object {
        private val mockMediaUiModel = listOf(
            MediaUiModel(1, PickerFile("/path/img2.jpg"), isFromPickerCamera = true),
            MediaUiModel(2, PickerFile("/path/img3.jpg"), isFromPickerCamera = true),
            MediaUiModel(3, PickerFile("/path/img1.jpg")),
            MediaUiModel(4, PickerFile("/path/vid1.mp4")),
            MediaUiModel(5, PickerFile("/path/vid2.mp4")),
            MediaUiModel(6, PickerFile("/path/vid3.mp4")),
        )

        private val expectedCompressedImageSize =
            mockMediaUiModel.filter { it.isFromPickerCamera }.size
    }

}