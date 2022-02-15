package com.tokopedia.media.picker.ui.fragment.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.common.PickerParam
import com.tokopedia.media.picker.ui.observer.EventFlowFactory
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()
    @get:Rule val coroutineTestRule = CoroutineTestRule()

    private val repository = mockk<MediaRepository>()
    private val filesObserver = mockk<Observer<List<Media>>>(relaxed = true)

    private val viewModel = GalleryViewModel(
        repository,
        coroutineTestRule.dispatchers
    )

    @Before
    fun setUp() {
        mockkStatic("com.tokopedia.media.picker.utils.PickerUtilities")
//        viewModel.files.observeForever(filesObserver)
    }

    @Test
    fun it_should_be_fetch_media() {
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { repository(any(), any()) } returns mockMediaData

            // when
            viewModel.fetch(-1, PickerParam())

            // then
            verify(atLeast = 1) {
                filesObserver.onChanged(any())
            }
        }
    }

    @Test
    fun it_should_be_return_empty_when_fetch_media() {
        coroutineTestRule.runBlockingTest {
            // given
            coEvery { repository(any(), any()) } returns listOf()

            // when
            viewModel.fetch(-1, PickerParam())

            // then
//            assert(viewModel.files.value.isNullOrEmpty())

            verify(atLeast = 1) {
                filesObserver.onChanged(any())
            }
        }
    }

    @Test
    fun it_should_be_publish_media_selected() {
        // given
        mockkObject(EventFlowFactory)

//        justRun { EventBusFactory.emit(any()) }

        // when
//        viewModel.publishSelectionDataChanged(listOf())

        // then
        verify(exactly = 1) {
//            EventBusFactory.emit(any())
        }
    }

    companion object {
        private val mockMediaData = listOf(
            Media(1, "IMG_Test_1.jpg", "/sdcard/DCIM/Camera/IMG_Test_1.jpg"),
            Media(2, "IMG_Test_2.jpg", "/sdcard/DCIM/Camera/IMG_Test_2.jpg"),
            Media(3, "IMG_Test_3.jpg", "/sdcard/DCIM/Camera/IMG_Test_3.jpg"),
            Media(4, "videoplayback_1.mp4", "/sdcard/DCIM/Video/videoplayback_1.mp4"),
            Media(5, "videoplayback_2.mp4", "/sdcard/DCIM/Video/videoplayback_2.mp4"),
            Media(6, "videoplayback_3.mp4", "/sdcard/DCIM/Video/videoplayback_3.mp4"),
        )
    }

}