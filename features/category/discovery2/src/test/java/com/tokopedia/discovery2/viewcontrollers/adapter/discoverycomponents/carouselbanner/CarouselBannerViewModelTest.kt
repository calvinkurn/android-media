package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CarouselBannerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: CarouselBannerViewModel by lazy {
        spyk(CarouselBannerViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { componentsItem.data } returns null
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentLiveData().value == componentsItem)
    }

    @Test
    fun `position test`() {
        assert(viewModel.getComponentPosition() == 99)
    }

    @Test
    fun `get list data test`() {
//        data is null
        var viewModelTest = viewModel
        assert(viewModelTest.getListDataLiveData().value == null)
//        data is empty list
        val list: ArrayList<DataItem> = ArrayList()
        every { componentsItem.data } returns list
        viewModelTest = spyk(CarouselBannerViewModel(application, componentsItem, 99))
        assert(viewModelTest.getListDataLiveData().value == null)
//        data is non empty list
        list.add(DataItem())
        val componentList: ArrayList<ComponentsItem> = ArrayList()
        mockkObject(DiscoveryDataMapper)
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns componentList
        viewModelTest = spyk(CarouselBannerViewModel(application, componentsItem, 99))
        verify { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        assert(viewModelTest.getListDataLiveData().value == componentList)
        unmockkObject(DiscoveryDataMapper)
    }

    @Test
    fun `get see all button data test`() {
//        data is null
        var viewModelTest = viewModel
        assert(viewModelTest.getSeeAllButtonLiveData().value == null)
//        data is empty list
        val list: ArrayList<DataItem> = ArrayList()
        every { componentsItem.data } returns list
        viewModelTest = spyk(CarouselBannerViewModel(application, componentsItem, 99))
        assert(viewModelTest.getSeeAllButtonLiveData().value == null)
//        data is non empty list
        list.add(DataItem(buttonApplink = "testVal"))
        val componentList: ArrayList<ComponentsItem> = ArrayList()
        mockkObject(DiscoveryDataMapper)
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns componentList
        viewModelTest = spyk(CarouselBannerViewModel(application, componentsItem, 99))
        assert(viewModelTest.getSeeAllButtonLiveData().value == "testVal")
        unmockkObject(DiscoveryDataMapper)
    }
}