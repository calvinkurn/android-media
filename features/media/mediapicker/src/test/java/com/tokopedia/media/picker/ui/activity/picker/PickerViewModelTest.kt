package com.tokopedia.media.picker.ui.activity.picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.mapper.toModel
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.ui.observer.*
import com.tokopedia.media.util.awaitItem
import com.tokopedia.media.util.collectIntoChannel
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val deviceInfoRepository = mockk<DeviceInfoRepository>()
    private val mediaRepository = mockk<MediaRepository>()
    private val paramCacheManager = mockk<ParamCacheManager>()

    private lateinit var viewModel: PickerViewModel

    @Before
    fun setup() {
        mockkStatic(::mediaToUiModel)

        every { mediaToUiModel(any()) } returns mediaUiModelList

        viewModel = PickerViewModel(
            deviceInfoRepository,
            mediaRepository,
            paramCacheManager,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `ui event should be invoked the CameraCapture when camera state is published`() = runBlocking {
        // Given
        stateOnCameraCapturePublished(mediaUiModelList.first())

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.CameraCaptured)
        EventFlowFactory.reset()
    }

    @Test
    fun `ui event should be invoked the SelectionChanged when camera state is published`() = runBlocking {
        // Given
        stateOnChangePublished(mediaUiModelList)

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.SelectionChanged)
        EventFlowFactory.reset()
    }

    @Test
    fun `ui event should be invoked the SelectionRemoved when camera state is published`() = runBlocking {
        // Given
        stateOnRemovePublished(mediaUiModelList.first())

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.SelectionRemoved)
        EventFlowFactory.reset()
    }

    @Test
    fun `ui event should be invoked the SelectionAdded when camera state is published`() = runBlocking {
        // Given
        val givenFile = mockk<PickerFile>(relaxed = true)
        every { givenFile.exists() } returns true
        every { givenFile.path } returns ""

        stateOnAddPublished(givenFile.toUiModel())

        // When
        val result = viewModel.uiEvent
            .collectIntoChannel(testCoroutineScope)
            .awaitItem()

        // Then
        assert(result is EventPickerState.SelectionAdded)
        EventFlowFactory.reset()
    }

    @Test
    fun `device storage validation should be return almost full state`() = coroutineScopeRule.runBlockingTest {
        // Given
        val expectedValue = true

        // When
        every { paramCacheManager.get() } returns PickerParam()
        every { deviceInfoRepository.execute(any()) } returns expectedValue

        val isStorageLimit = viewModel.isDeviceStorageAlmostFull()

        // Then
        assertEquals(isStorageLimit, expectedValue)
    }

    @Test
    fun `device storage validation should be not return almost full state`() = coroutineScopeRule.runBlockingTest {
        // Given
        val expectedValue = false

        // When
        every { paramCacheManager.get() } returns PickerParam()
        every { deviceInfoRepository.execute(any()) } returns expectedValue

        val isStorageLimit = viewModel.isDeviceStorageAlmostFull()

        // Then
        assertEquals(isStorageLimit, expectedValue)
    }

    @Test
    fun `fetch local gallery data should be return list of media`() = coroutineScopeRule.runBlockingTest {
        // Given
        coEvery { mediaRepository.invoke(any()) } returns mediaList

        // When
        viewModel.loadLocalGalleryBy(-1)

        // Then
        assert(viewModel.medias.value?.size == mediaList.size)
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
    }
}
