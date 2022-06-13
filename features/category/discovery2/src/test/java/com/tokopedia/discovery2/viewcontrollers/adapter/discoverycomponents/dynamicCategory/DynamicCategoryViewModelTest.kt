package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.BannerCarouselViewModel
import io.mockk.*
import junit.framework.TestCase
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
        mockkObject(DiscoveryDataMapper)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        unmockkObject(DiscoveryDataMapper)
    }

    /**************************** test getComponentLiveData() *******************************************/
    @Test
    fun `test for component List when data is null`() {
        every { componentsItem.data } returns null

        TestCase.assertEquals(viewModel.getComponentLiveData().value == null, true)

    }

    @Test
    fun `test for component List when data is empty`() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns null

        viewModel.getComponentLiveData()

        verify(inverse = true) { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test for component List when data is not empty`() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        val dataList = ArrayList<DataItem>()
        val dataItem = DataItem()
        dataList.add(dataItem)
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns dataList

        val viewModelTest: DynamicCategoryViewModel = spyk(DynamicCategoryViewModel(application, componentsItem, 0))

        assert(viewModelTest.getComponentLiveData().value == list)

    }

    /**************************** end of getComponentLiveData() *******************************************/

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }
}