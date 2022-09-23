package com.tokopedia.media.preview.ui.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.getFileFormatByMimeType
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
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

    private val saveToGalleryRepository = mockk<SaveToGalleryRepository>()
    private val imageCompressorRepository = mockk<ImageCompressionRepository>()

    private lateinit var viewModel: PreviewViewModel

    @Before
    fun setup() {
        mockkStatic(::getFileFormatByMimeType)

        viewModel = PreviewViewModel(
            imageCompressorRepository,
            saveToGalleryRepository,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `check isLoading not empty`() {
        // When
        coEvery { imageCompressorRepository.compress(any()) } returns listOf()
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
        every { saveToGalleryRepository.dispatch(any()) } returns null
        coEvery { imageCompressorRepository.compress(any()) } returns listOf()

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
        every { getFileFormatByMimeType(any(), any(), any()) } returns true
        every { saveToGalleryRepository.dispatch(any()) } returns null
        coEvery { imageCompressorRepository.compress(any()) } answers {
            firstArg()
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