package com.tokopedia.media.picker.ui.fragment.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.ui.observer.EventPickerState
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val testCoroutineScope = TestCoroutineScope(
        coroutineScopeRule.dispatchers.main
    )

    private val galleryRepository = mockk<MediaRepository>()

    private lateinit var viewModel: GalleryViewModel

    @Before
    fun setup() {
        mockkStatic(::mediaToUiModel)
        every { mediaToUiModel(any()) } returns mockMediaUiModel

        viewModel = GalleryViewModel(
            galleryRepository,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `check fetch device media`() = coroutineScopeRule.runBlockingTest {
        // When
        coEvery { galleryRepository.invoke(any()) } returns mockMediaModel
        viewModel.fetch(-1)

        // Then
        assert(viewModel.medias.value?.size == mockMediaUiModel.size)
        assert(viewModel.medias.value?.size != null)
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
        stateOnCameraCapturePublished(mockMediaUiModel.first())
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
        stateOnChangePublished(mockMediaUiModel)
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
        stateOnRemovePublished(mockMediaUiModel.first())
        assert(eventState is EventPickerState.SelectionRemoved)
        EventFlowFactory.reset()
    }

    companion object {
        val mockMediaModel = listOf(
            Media(12, PickerFile("sdcard/images/sample_1.png")),
            Media(13, PickerFile("sdcard/images/sample_2.png")),
            Media(14, PickerFile("sdcard/images/sample_3.png"))
        )

        val mockMediaUiModel = listOf(
            MediaUiModel(12, PickerFile("sdcard/images/sample_1.png")),
            MediaUiModel(13, PickerFile("sdcard/images/sample_2.png")),
            MediaUiModel(14, PickerFile("sdcard/images/sample_3.png")),
        )
    }

}