package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BannerCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BannerCarouselViewModel by lazy {
        spyk(BannerCarouselViewModel(application, componentsItem, 0))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponents().value == componentsItem)
    }

    @Test
    fun `title value`() {
        var viewModelTest = viewModel
        assert(viewModelTest.getTitleLiveData().value == "")
        every { componentsItem.properties?.bannerTitle } returns null
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "")
        every { componentsItem.properties?.bannerTitle } returns "testTitle"
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "testTitle")
    }

    @Test
    fun `test lihat url`() {
        every { componentsItem.properties } returns null
        assert(viewModel.getLihatUrl() == "")
        every { componentsItem.properties?.ctaApp } returns null
        assert(viewModel.getLihatUrl() == "")
        every { componentsItem.properties?.ctaApp } returns "testUrl"
        assert(viewModel.getLihatUrl() == "testUrl")
    }

    @Test
    fun `test for carousel Banner List`() {
        var viewModelTest = viewModel
        mockkObject(DiscoveryDataMapper)
        val list: ArrayList<ComponentsItem> = ArrayList()
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns null
        viewModelTest.getComponentData()
        verify(inverse = true) { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        val dataList = ArrayList<DataItem>()
        val dataItem = DataItem()
        dataList.add(dataItem)
        every { componentsItem.data } returns dataList
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        verify { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        assert(viewModelTest.getComponentData().value == list)
        unmockkObject(DiscoveryDataMapper)

    }


}