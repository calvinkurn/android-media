package com.tokopedia.media.picker.ui.fragment.camera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.ui.observer.EventPickerState
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class CameraViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private lateinit var viewModel: CameraViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(CoroutineTestDispatchers.main)

        mockkStatic(EventFlowFactory::class)

        viewModel = CameraViewModel(CoroutineTestDispatchers)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check camera UI state`() {
        // Given
        var eventState: MutableList<EventState> = MutableList(0){EventState.Idle}

        // When
        val job = CoroutineScope(CoroutineTestDispatchers.main).launch {
            viewModel.uiEvent.collect {
                eventState.add(it)
            }
        }

        // Then
        stateOnCameraCapturePublished(mediaUiModelSample)
        stateOnChangePublished(collectionMediaUiModelSample)
        stateOnRemovePublished(mediaUiModelSample)
        assert(eventState[0] is EventPickerState.CameraCaptured)
        assert(eventState[1] is EventPickerState.SelectionChanged)
        assert(eventState[2] is EventPickerState.SelectionRemoved)
        job.cancel()
    }

    companion object{
        val mediaUiModelSample = MediaUiModel(12, "Test Single MediaUiModel", "sdcard/images/test.jgp")
        val collectionMediaUiModelSample = listOf(
            MediaUiModel(12, "Test Collection MediaUiModel 1", "sdcard/images/test_1.jgp"),
            MediaUiModel(13, "Test Collection MediaUiModel 2", "sdcard/images/test_2.jgp"),
            MediaUiModel(14, "Test Collection MediaUiModel 3", "sdcard/images/test_3.jgp"),
            MediaUiModel(15, "Test Collection MediaUiModel 4", "sdcard/images/test_4.jgp"),
        )
    }

}