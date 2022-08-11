package com.tokopedia.media.picker.ui.activity.picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.ui.observer.EventPickerState
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.*
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
        every { mediaToUiModel(any()) } returns mockMediaUiModelList

        viewModel = PickerViewModel(
            deviceInfoRepository,
            mediaRepository,
            paramCacheManager,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `uiEvent should be invoke to CameraCapture when camera state is published`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnCameraCapturePublished(mockMediaUiModelList.first())
        assert(eventState is EventPickerState.CameraCaptured)

        EventFlowFactory.reset()
    }

    @Test
    fun `uiEvent should be invoke to SelectionChanged when item state is changed`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnChangePublished(mockMediaUiModelList)
        assert(eventState is EventPickerState.SelectionChanged)

        EventFlowFactory.reset()
    }

    @Test
    fun `uiEvent should be invoke to SelectionRemoved when item state is removed`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnRemovePublished(mockMediaUiModelList.first())
        assert(eventState is EventPickerState.SelectionRemoved)

        EventFlowFactory.reset()
    }

    @Test
    fun `device storage capacity should be almost full`() = coroutineScopeRule.runBlockingTest {
        // When
        every { paramCacheManager.get() } returns PickerParam()
        every { deviceInfoRepository.execute(any()) } returns true

        val isStorageLimit = viewModel.isDeviceStorageAlmostFull()

        // Then
        assertEquals(isStorageLimit, true)
    }

    @Test
    fun `fetch local gallery storage should be return list of media properly`() = coroutineScopeRule.runBlockingTest {
        // When
        coEvery { mediaRepository.invoke(any()) } returns mockMediaModel
        viewModel.loadLocalGalleryBy(-1)

        // Then
        assert(viewModel.medias.value?.size == mockMediaUiModelList.size)
        assert(viewModel.medias.value?.size != null)
    }

    companion object {
        val mockMediaModel = listOf(
            Media(1, PickerFile("sdcard/images/media1.png")),
            Media(2, PickerFile("sdcard/images/media2.png")),
            Media(3, PickerFile("sdcard/images/media3.png")),
            Media(4, PickerFile("sdcard/images/media4.png"))
        )

        val mockMediaUiModelList = listOf(
            MediaUiModel(1, PickerFile("sdcard/images/media1.jpg")),
            MediaUiModel(2, PickerFile("sdcard/images/media2.jpg")),
            MediaUiModel(3, PickerFile("sdcard/images/media3.jpg")),
            MediaUiModel(4, PickerFile("sdcard/images/media4.jpg"))
        )
    }
}