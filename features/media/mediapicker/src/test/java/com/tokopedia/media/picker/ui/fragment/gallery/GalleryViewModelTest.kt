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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()
    private val testCoroutineScope = TestCoroutineScope(coroutineScopeRule.dispatchers.main)

    private val galleryRepository = mockk<MediaRepository>()
    private lateinit var viewModel: GalleryViewModel

    @Before
    fun setup() {
        mockkStatic(::mediaToUiModel)
        every { mediaToUiModel(any()) } returns mediaUiMockCollection

        viewModel = GalleryViewModel(
            galleryRepository,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `check fetch device media`() = coroutineScopeRule.runBlockingTest {
        // When
        every { galleryRepository.execute(any()) } returns mediaMockCollection
        coEvery { galleryRepository.invoke(any()) } returns mediaMockCollection
        viewModel.fetch(-1)

        // Then
        assert(viewModel.medias.value?.size == mediaUiMockCollection.size)
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
        stateOnCameraCapturePublished(mediaUiMockCollection.first())
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
        stateOnChangePublished(mediaUiMockCollection)
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
        stateOnRemovePublished(mediaUiMockCollection.first())
        assert(eventState is EventPickerState.SelectionRemoved)
        EventFlowFactory.reset()
    }

    companion object {
        val mediaMockCollection = listOf(
            Media(12, "media sample 1", "sdcard/images/sample_1.png"),
            Media(13, "media sample 2", "sdcard/images/sample_2.png"),
            Media(14, "media sample 3", "sdcard/images/sample_3.png")
        )

        val mediaUiMockCollection = listOf(
            MediaUiModel(12, "media sample 1", "sdcard/images/sample_1.png"),
            MediaUiModel(13, "media sample 2", "sdcard/images/sample_2.png"),
            MediaUiModel(14, "media sample 3", "sdcard/images/sample_3.png"),
        )
    }

}