package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.After
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
    fun `test for position passed to VM`(){
        assert(viewModel.position == 99)
    }
}