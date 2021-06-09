package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModel
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DynamicCategoryViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: DynamicCategoryViewModel by lazy {
        spyk(DynamicCategoryViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test for component List`() {
        var viewModelTest = viewModel
        mockkObject(DiscoveryDataMapper)
        val list: ArrayList<ComponentsItem> = ArrayList()
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns null
        viewModelTest.getComponentLiveData()
        verify(inverse = true) { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        val dataList = ArrayList<DataItem>()
        val dataItem = DataItem()
        dataList.add(dataItem)
        every { componentsItem.data } returns dataList
        viewModelTest = spyk(DynamicCategoryViewModel(application, componentsItem, 0))
        verify { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        assert(viewModelTest.getComponentLiveData().value == list)
        unmockkObject(DiscoveryDataMapper)

    }
}