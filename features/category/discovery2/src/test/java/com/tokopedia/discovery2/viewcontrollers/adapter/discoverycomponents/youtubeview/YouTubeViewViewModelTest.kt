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

    @Test
    fun `test for video id`() {
        every { componentsItem.data } returns null

        assert(viewModel.getVideoId().value == null)

        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getVideoId().value == null)

        val item = DataItem()
        list.add(item)
        assert(viewModel.getVideoId().value === item)
    }

    @Test
    fun `test for position passed to VM`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for shouldAutoPlay`() {
        every { componentsItem.autoPlayController?.shouldAutoPlay(componentsItem.id) } returns true
        assert(viewModel.shouldAutoPlay())

        every { componentsItem.autoPlayController } returns null
        assert(!viewModel.shouldAutoPlay())
    }

    @Test
    fun `test for shouldPause`() {
        every { componentsItem.autoPlayController?.shouldPause(componentsItem.id) } returns true
        assert(viewModel.shouldPause())

        every { componentsItem.autoPlayController } returns null
        assert(!viewModel.shouldPause())
    }

    @Test
    fun `test for autoPlayNext`() {
        viewModel.autoPlayNext()
        verify { componentsItem.autoPlayController?.autoPlayNext(componentsItem.id) }

        every { componentsItem.autoPlayController } returns null
        viewModel.autoPlayNext()
        verify { componentsItem.autoPlayController?.autoPlayNext(componentsItem.id) }
    }

    @Test
    fun `test for isAutoPlayEnabled`() {
        every { componentsItem.autoPlayController?.isAutoPlayEnabled } returns true
        assert(viewModel.isAutoPlayEnabled())

        every { componentsItem.autoPlayController } returns null
        assert(!viewModel.isAutoPlayEnabled())
    }

    @Test
    fun `test for disableAutoplay`() {
        viewModel.disableAutoplay()

        TestCase.assertEquals(componentsItem.autoPlayController?.isAutoPlayEnabled == false, true)

        viewModel.disableAutoplay()

        TestCase.assertEquals(componentsItem.autoPlayController?.isAutoPlayEnabled == false, true)

        every { componentsItem.autoPlayController } returns null
        viewModel.disableAutoplay()

        TestCase.assertEquals(componentsItem.autoPlayController?.isAutoPlayEnabled == null, true)
    }

    @Test
    fun `test for pauseOtherVideos`() {
        every { componentsItem.autoPlayController?.pauseAutoPlayedVideo(componentsItem.id) } returns true
        assert(viewModel.pauseOtherVideos())

        every { componentsItem.autoPlayController } returns null
        assert(!viewModel.pauseOtherVideos())
    }

    @Test
    fun `test for currentlyAutoPlaying`() {

        every { componentsItem.autoPlayController } returns null
        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == null, true)

        every { componentsItem.autoPlayController?.currentlyAutoPlaying } returns null
        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == null, true)

        every { componentsItem.autoPlayController?.currentlyAutoPlaying?.value } returns "currentLvAuto"
        TestCase.assertEquals(viewModel.currentlyAutoPlaying()?.value == "currentLvAuto", true)

    }

}