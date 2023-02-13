package com.tokopedia.media.preview.ui.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.media.util.test
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.getFileFormatByMimeType
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PreviewViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val saveToGalleryRepository = mockk<SaveToGalleryRepository>()
    private val imageCompressorRepository = mockk<ImageCompressionRepository>()
    private val paramCache = mockk<PickerCacheManager>()

    private lateinit var viewModel: PreviewViewModel

    @Before
    fun setup() {
        mockkStatic(::getFileFormatByMimeType)

        // as we don't need to validate the save-to-gallery,
        // then we can disabled it for all test cases.
        `save to gallery disabled`()

        `param manager get param detail`()

        viewModel = PreviewViewModel(
            imageCompressorRepository,
            saveToGalleryRepository,
            coroutineScopeRule.dispatchers,
            paramCache
        )
    }

    @Test
    fun `it should be state of loading is invoked`() {
        // When
        coEvery { imageCompressorRepository.compress(any()) } returns listOf()
        viewModel.files(mediaUiModelList)

        // Then
        assert(viewModel.isLoading.value != null)
        clearAllMocks()
    }

    @Test
    fun `it should be get an original media path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(false)
            `image compression is`(false)

            // When
            viewModel.files(mediaUiModelList)

            // Then
            assertEquals(mediaUiModelList.size, awaitItem().originalPaths.size)
        }
    }

    @Test
    fun `it should be cannot get an original media path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(false)
            `image compression is`(false)

            // When
            viewModel.files(emptyList())

            // Then
            assert(awaitItem().originalPaths.isEmpty())
        }
    }

    @Test
    fun `it should be get a video file path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(true)
            `image compression is`(false)

            // When
            viewModel.files(mediaUiModelList)

            // Then
            assert(awaitItem().videoFiles.isNotEmpty())
        }
    }

    @Test
    fun `it should be cannot get a video file path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(true)
            `image compression is`(false)

            // When
            viewModel.files(imageGalleryOnlyUiModelList)

            // Then
            assert(awaitItem().videoFiles.isEmpty())
        }
    }

    @Test
    fun `it should be get a compressed image path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(true)
            `image compression is`(true)

            // When
            viewModel.files(mediaUiModelList)

            // Then
            assert(awaitItem().compressedImages.isNotEmpty())
        }
    }

    @Test
    fun `it should be cannot get a compressed image path`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(true)
            `image compression is`(true)

            // When
            viewModel.files(emptyList())

            // Then
            assert(awaitItem().compressedImages.isEmpty())
        }
    }

    @Test
    fun `it should ignore save to gallery on cache file when editor is true`() = runBlocking {
        viewModel.result.test {
            // Given
            `file format by mime type is`(true)
            `image compression is`(true)
            `param manager get param detail with editor`()

            // When
            viewModel.files(mediaUiModelList)

            // Then
            assert(awaitItem().compressedImages.isNotEmpty())
        }
    }

    private fun `param manager get param detail`() {
        every { paramCache.get() } returns PickerParam()
    }

    private fun `param manager get param detail with editor`() {
        every { paramCache.get() } returns PickerParam().apply {
            withEditor {  }
        }
    }

    private fun `file format by mime type is`(value: Boolean) {
        every { getFileFormatByMimeType(any(), any(), any()) } returns value
    }

    private fun `save to gallery disabled`() {
        every { saveToGalleryRepository.dispatch(any()) } returns null
    }

    private fun `image compression is`(enabled: Boolean) {
        val compressor = coEvery { imageCompressorRepository.compress(any()) }

        if (enabled) {
            compressor answers { firstArg() }
        } else {
            compressor returns listOf()
        }
    }

    companion object {
        private val videoOnlyUiModelList = listOf(
            MediaUiModel(5, PickerFile("/path/vid5.mp4")),
            MediaUiModel(6, PickerFile("/path/vid6.mp4")),
        )

        private val imageGalleryOnlyUiModelList = listOf(
            MediaUiModel(3, PickerFile("/path/img3.jpg")),
            MediaUiModel(4, PickerFile("/path/img4.jpg")),
        )

        private val imageCameraOnlyUiModelList = listOf(
            MediaUiModel(1, PickerFile("/path/img1.jpg"), isFromPickerCamera = true),
            MediaUiModel(2, PickerFile("/path/img2.jpg"), isFromPickerCamera = true),
        )

        private val mediaUiModelList = imageCameraOnlyUiModelList
            .plus(imageGalleryOnlyUiModelList)
            .plus(videoOnlyUiModelList)
    }
}
