package com.tokopedia.media.preview.ui.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val saveToGalleryManagerMock = mockk<SaveToGalleryManager>()
    private val imageCompressorMock = mockk<ImageCompressionManager>()

    @ExperimentalCoroutinesApi
    private val testCoroutinesDispatcher = CoroutineTestDispatchers

    @ExperimentalCoroutinesApi
    private val testCoroutineScope = TestCoroutineScope(testCoroutinesDispatcher.main)

    @ExperimentalCoroutinesApi
    private lateinit var viewModelMock: PreviewViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutinesDispatcher.main)
        mockkStatic(::isVideoFormat)

        viewModelMock = PreviewViewModel(
            imageCompressorMock,
            saveToGalleryManagerMock
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun clean() {
        Dispatchers.resetMain()
        testCoroutinesDispatcher.main.cleanupTestCoroutines()
        testCoroutineScope.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check isLoading not empty`() {
        // Given

        // When
        every { imageCompressorMock.compress(any()) } returns flow { }
        viewModelMock.files(mediaUiModelMockCollection)

        // Then
        assert(viewModelMock.isLoading.value != null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get result original asset`() {
        // Given
        var pickerResult: PickerResult? = null

        // When
        every { isVideoFormat(any()) } returns false
        every { saveToGalleryManagerMock.dispatch(any()) } returns null
        every { imageCompressorMock.compress(any()) } returns flow {
            emit(
                listOf("")
            )
        }

        // Then
        testCoroutineScope.launch {
            viewModelMock.result
                .shareIn(this, SharingStarted.WhileSubscribed(), 1)
                .collect {
                    pickerResult = it
                    this.coroutineContext.cancel()
                }
        }

        viewModelMock.files(mediaUiModelMockCollection)
        assertEquals(mediaUiModelMockCollection.size, pickerResult?.originalPaths?.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get result compressed image`() {
        // Given
        var pickerResult: PickerResult? = null

        // When
        every { isVideoFormat(any()) } returns false
        every { saveToGalleryManagerMock.dispatch(any()) } returns null
        every { imageCompressorMock.compress(any()) } answers {
            flow {
                emit(firstArg())
            }
        }

        // Then
        testCoroutineScope.launch {
            viewModelMock.result
                .shareIn(this, SharingStarted.WhileSubscribed(), 1)
                .collect {
                    pickerResult = it
                    this.coroutineContext.cancel()
                }
        }

        viewModelMock.files(mediaUiModelMockCollection)
        assertEquals(expectedNumberOfCompressedImage, pickerResult?.compressedImages?.size)
    }

    companion object {
        // please update the expected number according to ModelMockCollection isFromPickerCamera when mock data is updated
        const val expectedNumberOfCompressedImage = 2

        val mediaUiModelMockCollection = listOf(
            MediaUiModel(
                13,
                "img1",
                "/storage/emulated/0/Pictures/img1.jpg",
                isFromPickerCamera = true
            ),
            MediaUiModel(14, "img2", "/storage/emulated/0/Pictures/img2.jpg"),
            MediaUiModel(
                15,
                "img3",
                "/storage/emulated/0/Pictures/img3.jpg",
                isFromPickerCamera = true
            ),
            MediaUiModel(16, "vid1", "/storage/emulated/0/Pictures/vid1.mp4"),
            MediaUiModel(17, "vid2", "/storage/emulated/0/Pictures/vid2.mp4"),
            MediaUiModel(18, "vid3", "/storage/emulated/0/Pictures/vid3.mp4"),
        )
    }

}