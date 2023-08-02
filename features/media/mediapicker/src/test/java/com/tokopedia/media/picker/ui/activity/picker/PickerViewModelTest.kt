package com.tokopedia.media.picker.ui.activity.picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.mapper.toModel
import com.tokopedia.media.picker.data.repository.BitmapConverterRepository
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaFileRepository
import com.tokopedia.media.picker.ui.publisher.EventPickerState
import com.tokopedia.media.picker.ui.publisher.PickerEventBus
import com.tokopedia.media.picker.ui.publisher.PickerEventBusImpl
import com.tokopedia.media.picker.utils.internal.NetworkStateManager
import com.tokopedia.media.picker.utils.internal.ResourceManager
import com.tokopedia.media.util.awaitItem
import com.tokopedia.media.util.collectIntoChannel
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PickerViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val testCoroutineScope = TestCoroutineScope(
        coroutineScopeRule.dispatchers.main
    )

    private val bitmapConverterRepository = mockk<BitmapConverterRepository>(relaxed = true)
    private val deviceInfoRepository = mockk<DeviceInfoRepository>()
    private val mediaRepository = mockk<MediaFileRepository>()
    private val paramCacheManager = mockk<PickerCacheManager>()
    private val networkStateManager = mockk<NetworkStateManager>()
    private val resourcesManager = mockk<ResourceManager>()

    private lateinit var eventBus: PickerEventBus
    private lateinit var viewModel: PickerViewModel

    @Before
    fun setup() {
        mockkStatic(::mediaToUiModel)
        every { mediaToUiModel(any()) } returns mediaUiModelList
        every { paramCacheManager.get() } returns PickerParam()

        eventBus = PickerEventBusImpl(
            paramCacheManager
        )

        viewModel = PickerViewModel(
            deviceInfoRepository,
            mediaRepository,
            bitmapConverterRepository,
            paramCacheManager,
            networkStateManager,
            resourcesManager,
            coroutineScopeRule.dispatchers,
            eventBus
        )
    }

    @Test
    fun `ui event should be invoked the CameraCapture when camera state is published`() = runBlocking {
        // Given
        eventBus.cameraCaptureEvent(mediaUiModelList.first())

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.CameraCaptured)
        eventBus.reset()
    }

    @Test
    fun `ui event should be invoked the SelectionChanged when camera state is published`() = runBlocking {
        // Given
        eventBus.notifyDataOnChangedEvent(mediaUiModelList)

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.SelectionChanged)
        eventBus.reset()
    }

    @Test
    fun `ui event should be not invoked the SelectionChanged when includeMedias is does not exist`() = coroutineScopeRule.runTest {
        // When
        every {
            paramCacheManager.get().includeMedias()
        } returns emptyList()

        // Then
        viewModel.preSelectedMedias(paramCacheManager.get())
        verify { bitmapConverterRepository.convert(any()) wasNot Called }
    }

    @Test
    fun `ui event should be not invoked the SelectionChanged when includeMedias is exist but no internet connection`() = coroutineScopeRule.runTest {
        // When
        val noInternetMessage = "Opps!"
        val mockImageUrl = "https://isfa.com/sample.png"

        val includeMedias = listOf(
            mockImageUrl,
            "/DCIM/AnotherSample/download.jpeg"
        )

        // When
        every { networkStateManager.isNetworkConnected() } returns false
        every { resourcesManager.string(any()) } returns noInternetMessage
        every { paramCacheManager.get().includeMedias() } returns includeMedias

        // Then
        viewModel.preSelectedMedias(paramCacheManager.get())

        assert(viewModel.connectionIssue.value == noInternetMessage)
        verify { bitmapConverterRepository.convert(any()) wasNot Called }
    }

    @Test
    fun `ui event should be invoked the SelectionChanged when includeMedias is exist`() = coroutineScopeRule.runTest {
        // Given
        val includeMedias = listOf(
            "https://isfa.com/sample.png",
            "/DCIM/AnotherSample/download.jpeg"
        )

        val convertedResultMedias = listOf(
            "/DCIM/Camera/sample.png",
            "/DCIM/AnotherSample/download.jpeg"
        )

        // When
        every { networkStateManager.isNetworkConnected() } returns true
        every { paramCacheManager.get().includeMedias() } returns includeMedias
        every { bitmapConverterRepository.convert(any()) } returns flow {
            emit(includeMedias.zip(convertedResultMedias))
        }

        // Then
        viewModel.preSelectedMedias(paramCacheManager.get())

        assert(viewModel.includeMedias.value != null)
        assert(viewModel.isLoading.value != null)
    }

    @Test
    fun `ui event should be invoked the SelectionRemoved when camera state is published`() = runBlocking {
        // Given
        eventBus.removeMediaEvent(mediaUiModelList.first())

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.SelectionRemoved)
        eventBus.reset()
    }

    @Test
    fun `device storage validation should be return almost full state`() = coroutineScopeRule.runTest {
        // Given
        val expectedValue = true

        // When
        every { paramCacheManager.get() } returns PickerParam()
        every { deviceInfoRepository.isDeviceStorageAlmostFull(any()) } returns expectedValue

        val isStorageLimit = viewModel.isDeviceStorageAlmostFull()

        // Then
        assertEquals(isStorageLimit, expectedValue)
    }

    @Test
    fun `device storage validation should be not return almost full state`() = coroutineScopeRule.runTest {
        // Given
        val expectedValue = false

        // When
        every { paramCacheManager.get() } returns PickerParam()
        every { deviceInfoRepository.isDeviceStorageAlmostFull(any()) } returns expectedValue

        val isStorageLimit = viewModel.isDeviceStorageAlmostFull()

        // Then
        assertEquals(isStorageLimit, expectedValue)
    }

    @Test
    fun `fetch local gallery data should be return list of media`() = coroutineScopeRule.runTest {
        // Given
        every { mediaRepository.maxLimitSize() } returns 3
        every { mediaRepository.invoke(any(), any()) } returns flow {
            emit(mediaList)
        }

        // When
        viewModel.loadMedia(-1)

        // Then
        assert(viewModel.medias.value?.size == mediaList.size)
        assert(viewModel.isFetchMediaLoading.value != null)
        assert(viewModel.isMediaEmpty.value != null)
    }

    @Test
    fun `fetch local gallery data should be return list of media more than threshold size`() = coroutineScopeRule.runTest {
        // Given
        every { mediaRepository.maxLimitSize() } returns 5
        every { mediaRepository.invoke(any(), any()) } returns flow {
            emit(mediaList)
        }

        // When
        viewModel.loadMedia(-1)

        // Then
        assert(viewModel.medias.value?.size == mediaList.size)
        assert(viewModel.isFetchMediaLoading.value != null)
        assert(viewModel.isMediaEmpty.value != null)
    }

    @Test
    fun `fetch local gallery data should be throw an exception`() = coroutineScopeRule.runTest {
        // Given
        every { mediaRepository.invoke(any(), any()) } returns flow { throw Throwable("") }

        // When
        viewModel.loadMedia(-1)

        // Then
        assertEquals(false, viewModel.isFetchMediaLoading.value)
    }

    @Test
    fun `it should be able to navigate to editor page`() {
        // Given
        val result = PickerResult()
        every { paramCacheManager.get().getEditorParam() } returns EditorParam()

        // When
        viewModel.navigateToEditorPage(result)

        // Then
        assert(viewModel.editorParam.value?.first == result)
    }

    @Test
    fun `it should be able to navigate to editor page with editor param is null`() {
        // Given
        val result = PickerResult()
        every { paramCacheManager.get().getEditorParam() } returns null

        // When
        viewModel.navigateToEditorPage(result)

        // Then
        assert(viewModel.editorParam.value?.first == result)
    }

    @Test
    fun `it should not be able to apply the picker param`() {
        // When
        viewModel.setPickerParam(null)

        // Then
        assert(viewModel.pickerParam.value == null)
    }

    @Test
    fun `it should be able to apply the picker param`() {
        // Given
        val param = PickerParam()

        every { paramCacheManager.set(any()) } returns param

        // When
        viewModel.setPickerParam(param)

        // Then
        assert(viewModel.pickerParam.value != null)
    }

    @Test
    fun `it should be able to change state to true of the video recording`() {
        // Given
        val expectedValue = true

        // When
        viewModel.isOnVideoRecording(expectedValue)

        // Then
        assert(viewModel.isOnVideoRecording.value == expectedValue)
    }

    @Test
    fun `it should be able to change state to false of the video recording`() {
        // Given
        val expectedValue = false

        // When
        viewModel.isOnVideoRecording(expectedValue)

        // Then
        assert(viewModel.isOnVideoRecording.value == expectedValue)
    }

    companion object {
        val mediaUiModelList = listOf(
            MediaUiModel(1, PickerFile("sdcard/images/media1.jpg")),
            MediaUiModel(2, PickerFile("sdcard/images/media2.jpg")),
            MediaUiModel(3, PickerFile("sdcard/images/media3.jpg")),
            MediaUiModel(4, PickerFile("sdcard/images/media4.jpg"))
        )

        val mediaList = mediaUiModelList.map {
            it.toModel()
        }

        private val eventTestKey = PageSource.Unknown
    }
}
