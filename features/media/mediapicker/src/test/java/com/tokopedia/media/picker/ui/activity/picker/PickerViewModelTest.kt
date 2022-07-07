package com.tokopedia.media.picker.ui.activity.picker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
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
import io.mockk.every
import io.mockk.mockk
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

    private val deviceRepo = mockk<DeviceInfoRepository>()
    private val param = mockk<ParamCacheManager>()

    private lateinit var viewModel: PickerViewModel

    @Before
    fun setup() {
        viewModel = PickerViewModel(
            deviceRepo,
            param,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `validate camera state`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnCameraCapturePublished(mediaUiModelMockCollection.first())
        assert(eventState is EventPickerState.CameraCaptured)

        EventFlowFactory.reset()
    }

    @Test
    fun `validate selection change state`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnChangePublished(mediaUiModelMockCollection)
        assert(eventState is EventPickerState.SelectionChanged)

        EventFlowFactory.reset()
    }

    @Test
    fun `validate selection removed state`() {
        // Given
        lateinit var eventState: EventState

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnRemovePublished(mediaUiModelMockCollection.first())
        assert(eventState is EventPickerState.SelectionRemoved)

        EventFlowFactory.reset()
    }

    @Test
    fun `check storage threshold`() = coroutineScopeRule.runBlockingTest {
        // When
        every { param.get() } returns PickerParam()
        every { deviceRepo.execute(any()) } returns true
        val isStorageLimit = viewModel.isDeviceStorageFull()

        // Then
        assertEquals(isStorageLimit, true)
    }

    companion object {
        val mediaUiModelMockCollection = listOf(
            MediaUiModel(1, PickerFile("sdcard/images/media1.jpg")),
            MediaUiModel(2, PickerFile("sdcard/images/media2.jpg")),
            MediaUiModel(3, PickerFile("sdcard/images/media3.jpg")),
            MediaUiModel(4, PickerFile("sdcard/images/media4.jpg"))
        )
    }
}