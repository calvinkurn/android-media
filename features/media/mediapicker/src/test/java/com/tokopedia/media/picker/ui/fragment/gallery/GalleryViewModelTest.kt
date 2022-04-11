package com.tokopedia.media.picker.ui.fragment.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.mapper.mediaToUiModel
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.ui.fragment.camera.CameraViewModelTest
import com.tokopedia.media.picker.ui.observer.EventPickerState
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.observer.stateOnChangePublished
import com.tokopedia.media.picker.ui.observer.stateOnRemovePublished
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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

class GalleryViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val galleryRepository = mockk<MediaRepository>()

    @ExperimentalCoroutinesApi
    private lateinit var viewModel: GalleryViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(CoroutineTestDispatchers.main)

        mockkStatic(EventFlowFactory::class)

        mockkStatic(::mediaToUiModel)
        every { mediaToUiModel(any()) } returns mediaUiCollection

        viewModel = GalleryViewModel(
            galleryRepository,
            CoroutineTestDispatchers)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check gallery UI state`() {
        // Given
        var eventState: MutableList<EventState> = MutableList(0){ EventState.Idle}

        // When
        val job = CoroutineScope(CoroutineTestDispatchers.main).launch {
            viewModel.uiEvent.collect {
                eventState.add(it)
            }
        }

        // Then
        stateOnCameraCapturePublished(CameraViewModelTest.mediaUiModelSample)
        stateOnChangePublished(CameraViewModelTest.collectionMediaUiModelSample)
        stateOnRemovePublished(CameraViewModelTest.mediaUiModelSample)
        assert(eventState[0] is EventPickerState.CameraCaptured)
        assert(eventState[1] is EventPickerState.SelectionChanged)
        assert(eventState[2] is EventPickerState.SelectionRemoved)
        job.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check fetch device media`() {
        // Given

        // When
        every { galleryRepository.execute(any()) } returns mediaCollection
        coEvery { galleryRepository.invoke(any()) } returns mediaCollection
        viewModel.fetch(-1)

        // Then
        assert(viewModel.medias.value?.size == mediaUiCollection.size)
        assert(viewModel.medias.value?.size != null)
    }

    companion object{
        val mediaCollection = listOf(
            Media(12, "media sample 1", "sdcard/images/sample_1.png"),
            Media(13, "media sample 2", "sdcard/images/sample_2.png"),
            Media(14, "media sample 3", "sdcard/images/sample_3.png")
        )

        val mediaUiCollection = listOf(
            MediaUiModel(12, "media sample 1", "sdcard/images/sample_1.png"),
            MediaUiModel(13, "media sample 2", "sdcard/images/sample_2.png"),
            MediaUiModel(14, "media sample 3", "sdcard/images/sample_3.png"),
        )
    }

}