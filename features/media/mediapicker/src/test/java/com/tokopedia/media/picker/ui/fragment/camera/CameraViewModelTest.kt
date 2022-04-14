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
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CameraViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()
    private val testCoroutineScope = TestCoroutineScope(coroutineScopeRule.dispatchers.main)

    private lateinit var viewModel: CameraViewModel

    @Before
    fun setup() {
        mockkStatic(EventFlowFactory::class)
        viewModel = CameraViewModel(CoroutineTestDispatchers)
    }

    @Test
    fun `validate camera state`() {
        // Given
        var eventState: EventState? = null

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnCameraCapturePublished(mediaUiModelMock)
        assert(eventState is EventPickerState.CameraCaptured)
        EventFlowFactory.reset()
    }

    @Test
    fun `validate selection change state`() {
        // Given
        var eventState: EventState? = null

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
        var eventState: EventState? = null

        // When
        testCoroutineScope.launch {
            viewModel.uiEvent.collect {
                eventState = it
            }
        }

        // Then
        stateOnRemovePublished(mediaUiModelMock)
        assert(eventState is EventPickerState.SelectionRemoved)
        EventFlowFactory.reset()
    }

    companion object {
        val mediaUiModelMock =
            MediaUiModel(0, "media 0", "sdcard/images/media0.jpg")
        val mediaUiModelMockCollection = listOf(
            MediaUiModel(1, "media 1", "sdcard/images/media1.jpg"),
            MediaUiModel(2, "media 2", "sdcard/images/media2.jpg"),
            MediaUiModel(3, "media 3", "sdcard/images/media3.jpg"),
            MediaUiModel(4, "media 4", "sdcard/images/media4.jpg")
        )
    }

}