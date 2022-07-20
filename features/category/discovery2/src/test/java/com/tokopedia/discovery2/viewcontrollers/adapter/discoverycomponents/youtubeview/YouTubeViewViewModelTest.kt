package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class YouTubeViewViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: YouTubeViewViewModel by lazy {
        spyk(YouTubeViewViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
    /**************************** test for getVideoId() *******************************************/
    @Test
    fun `test for getVideoId when data is null`() {
        every { componentsItem.data } returns null

        assert(viewModel.getVideoId().value == null)

    }

    @Test
    fun `test for getVideoId when datalist is empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getVideoId().value == null)

    }

    @Test
    fun `test for getVideoId when data is not empty`() {
        val list = ArrayList<DataItem>()

        every { componentsItem.data } returns list

        val item = DataItem()
        list.add(item)

        assert(viewModel.getVideoId().value === item)
    }

    /**************************** end of getVideoId() *******************************************/

    @Test
    fun `test for position passed to VM`() {
        assert(viewModel.position == 99)
    }

    /**************************** test for shouldAutoPlay() *******************************************/
    @Test
    fun `test for shouldAutoPlay when autoPlayController is true`() {
        every { componentsItem.autoPlayController?.shouldAutoPlay(componentsItem.id) } returns true

        assert(viewModel.shouldAutoPlay())
    }

    @Test
    fun `test for shouldAutoPlay when autoPlayController is false`() {
        every { componentsItem.autoPlayController?.shouldAutoPlay(componentsItem.id) } returns false

        assert(!viewModel.shouldAutoPlay())
    }

    @Test
    fun `test for shouldAutoPlay when autoPlayController is null`() {
        every { componentsItem.autoPlayController } returns null

        assert(!viewModel.shouldAutoPlay())
    }

    /**************************** end of shouldAutoPlay() *******************************************/

    /**************************** test for shouldPause() *******************************************/
    @Test
    fun `test for shouldPause when autoPlayController is true`() {
        every { componentsItem.autoPlayController?.shouldPause(componentsItem.id) } returns true

        assert(viewModel.shouldPause())
    }

    @Test
    fun `test for shouldPause when autoPlayController is false`() {
        every { componentsItem.autoPlayController?.shouldPause(componentsItem.id) } returns false

        assert(!viewModel.shouldPause())
    }

    @Test
    fun `test for shouldPause when autoPlayController is null`() {
        every { componentsItem.autoPlayController } returns null

        assert(!viewModel.shouldPause())
    }

    /**************************** end of shouldPause() *******************************************/

    /**************************** test for autoPlayNext() *******************************************/
    @Test
    fun `test for autoPlayNext when autoPlayController is not null`() {
        viewModel.autoPlayNext()

        verify { componentsItem.autoPlayController?.autoPlayNext(componentsItem.id) }
    }

    @Test
    fun `test for autoPlayNext when autoPlayController is null`() {
        every { componentsItem.autoPlayController } returns null

        viewModel.autoPlayNext()

        verify(inverse = true) { componentsItem.autoPlayController?.autoPlayNext(componentsItem.id) }
    }

    /**************************** end of autoPlayNext() *******************************************/

    /**************************** test for isAutoPlayEnabled() *******************************************/
    @Test
    fun `test for isAutoPlayEnabled when autoPlayController is true`() {
        every { componentsItem.autoPlayController?.isAutoPlayEnabled } returns true

        assert(viewModel.isAutoPlayEnabled())
    }

    @Test
    fun `test for isAutoPlayEnabled when autoPlayController is false`() {
        every { componentsItem.autoPlayController?.isAutoPlayEnabled } returns false

        assert(!viewModel.isAutoPlayEnabled())
    }

    @Test
    fun `test for isAutoPlayEnabled when autoPlayController is null`() {
        every { componentsItem.autoPlayController } returns null

        assert(!viewModel.isAutoPlayEnabled())
    }

    /**************************** end of isAutoPlayEnabled() *******************************************/

    /**************************** test for disableAutoplay() *******************************************/
    @Test
    fun `test for disableAutoplay when autoPlayController is not null `() {
        viewModel.disableAutoplay()

        TestCase.assertEquals(componentsItem.autoPlayController?.isAutoPlayEnabled == false, true)

    }

    @Test
    fun `test for disableAutoplay when autoPlayController is null`() {
        every { componentsItem.autoPlayController } returns null

        viewModel.disableAutoplay()

        TestCase.assertEquals(componentsItem.autoPlayController?.isAutoPlayEnabled == null, true)
    }

    /**************************** end of disableAutoplay() *******************************************/

    /**************************** test for pauseOtherVideos() *******************************************/
    @Test
    fun `test for pauseOtherVideos when autoPlayController is true`() {
        every { componentsItem.autoPlayController?.pauseAutoPlayedVideo(componentsItem.id) } returns true

        assert(viewModel.pauseOtherVideos())
    }

    @Test
    fun `test for pauseOtherVideos when autoPlayController is false`() {
        every { componentsItem.autoPlayController?.pauseAutoPlayedVideo(componentsItem.id) } returns false

        assert(!viewModel.pauseOtherVideos())
    }

    @Test
    fun `test for pauseOtherVideos when autoPlayController is null`() {

        every { componentsItem.autoPlayController } returns null
        assert(!viewModel.pauseOtherVideos())
    }

    /**************************** end of pauseOtherVideos() *******************************************/

    /**************************** test for currentlyAutoPlaying() *******************************************/
    @Test
    fun `test for currentlyAutoPlaying when autoPlayController is null `() {
        every { componentsItem.autoPlayController } returns null

        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == null, true)

    }

    @Test
    fun `test for currentlyAutoPlaying when currentlyAutoPlaying is null`() {
        every { componentsItem.autoPlayController?.currentlyAutoPlaying } returns null

        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == null, true)
    }

    @Test
    fun `test for currentlyAutoPlaying when currentlyAutoPlaying is not null`() {
        every { componentsItem.autoPlayController?.currentlyAutoPlaying?.value } returns "currentLvAuto"

        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == "currentLvAuto", true)

    }
    /**************************** end of currentlyAutoPlaying() *******************************************/

}