package com.tokopedia.media.picker.ui.activity.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.media.picker.ui.fragment.camera.CameraViewModel
import com.tokopedia.media.picker.ui.fragment.camera.CameraViewModelTest
import com.tokopedia.media.picker.ui.observer.EventPickerState
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.Answer
import io.mockk.Call
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class PickerViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val deviceRepo = mockk<DeviceInfoRepository>()
    private val param = mockk<ParamCacheManager>()

    @ExperimentalCoroutinesApi
    private lateinit var viewModel: PickerViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(CoroutineTestDispatchers.main)

        mockkStatic(EventFlowFactory::class)

        viewModel = PickerViewModel(
            deviceRepo,
            param,
            CoroutineTestDispatchers
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun reset() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check picker UI state`() {
        // Given
        var cameraEventState: EventPickerState.CameraCaptured? = null
        var selectionChangedEventState: EventPickerState.SelectionChanged? = null
        var selectionRemovedEventState: EventPickerState.SelectionRemoved? = null

        // When
        val job = CoroutineScope(CoroutineTestDispatchers.main).launch {
            viewModel.uiEvent.collect {
                when(it){
                    is EventPickerState.CameraCaptured -> cameraEventState = it
                    is EventPickerState.SelectionChanged -> selectionChangedEventState = it
                    is EventPickerState.SelectionRemoved -> selectionRemovedEventState = it
                }
            }
        }

        // Then
        stateOnCameraCapturePublished(CameraViewModelTest.mediaUiModelSample)
        stateOnChangePublished(CameraViewModelTest.collectionMediaUiModelSample)
        stateOnRemovePublished(CameraViewModelTest.mediaUiModelSample)
        assert(cameraEventState != null)
        assert(selectionChangedEventState != null)
        assert(selectionRemovedEventState != null)
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check storage threshold`() {
        // Given
        var isStorageLimit = false
        var pickerParam = PickerParam()

        // When
        every { param.get() } returns pickerParam
        every { deviceRepo.execute(any()) } returns true
        isStorageLimit = viewModel.isDeviceStorageFull()

        // Then
        assertEquals(isStorageLimit, true)
    }

    companion object {
        val mediaUiModelSample =
            MediaUiModel(12, "Test Single MediaUiModel", "sdcard/images/test.jgp")
        val collectionMediaUiModelSample = listOf(
            MediaUiModel(12, "Test Collection MediaUiModel 1", "sdcard/images/test_1.jgp"),
            MediaUiModel(13, "Test Collection MediaUiModel 2", "sdcard/images/test_2.jgp"),
            MediaUiModel(14, "Test Collection MediaUiModel 3", "sdcard/images/test_3.jgp"),
            MediaUiModel(15, "Test Collection MediaUiModel 4", "sdcard/images/test_4.jgp"),
        )
    }

}