package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BannerCarouselItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BannerCarouselItemViewModel by lazy {
        spyk(BannerCarouselItemViewModel(application, componentsItem, 0))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentLiveData().value == componentsItem)
    }

    @Test
    fun `test getDataItem using getBannerData`() {
        every { componentsItem.data } returns null
        assert(viewModel.getBannerData() == null)
        every { componentsItem.data } returns ArrayList()
        assert(viewModel.getBannerData() == null)
        val list = ArrayList<DataItem>()
        val dataItem = DataItem()
        list.add(dataItem)
        every { componentsItem.data } returns list
        assert(viewModel.getBannerData() == dataItem)
    }

    @Test
    fun `test image click url`() {
        every { componentsItem.data } returns null
        assert(viewModel.getNavigationUrl() == null)
        every { componentsItem.data } returns ArrayList()
        assert(viewModel.getNavigationUrl() == null)
        val list = ArrayList<DataItem>()
        val dataItem = DataItem()
        list.add(dataItem)
        every { componentsItem.data } returns list
        assert(viewModel.getNavigationUrl() == "")
        dataItem.imageClickUrl = null
        assert(viewModel.getNavigationUrl() == null)
        dataItem.imageClickUrl = "testUrl"
        assert(viewModel.getNavigationUrl() == "testUrl")
    }


}