package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.kotlin.extensions.view.EMPTY
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CircularSliderBannerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val TEST_STRING = "testData"
    private val viewModel: CircularSliderBannerViewModel by lazy {
        spyk(CircularSliderBannerViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test title`() {
        every { componentsItem.title } returns TEST_STRING
        assert(viewModel.getTitleLiveData().value == TEST_STRING)
    }

    @Test
    fun `test position value`() {
        assert(viewModel.getComponentPosition() == 99)
    }

    @Test
    fun `test item list`() {
//      When data is null
        every { componentsItem.data } returns null
        assert(viewModel.getItemsList() == null)
//      When data is not null
        every { componentsItem.data } returns ArrayList()
        mockkObject(DiscoveryDataMapper)
        val list = ArrayList<CircularModel>()
        every { DiscoveryDataMapper.discoveryDataMapper.mapProductListToCircularModel(any()) } returns list
        assert(viewModel.getItemsList() == list)
        verify { DiscoveryDataMapper.discoveryDataMapper.mapProductListToCircularModel(any()) }
        unmockkObject(DiscoveryDataMapper)
    }

    @Test
    fun `test banner item`() {
//        Data list is null
        every { componentsItem.data } returns null
        assert(viewModel.getBannerItem(0) == null)
//        data list is empty
        val dataList: ArrayList<DataItem> = ArrayList()
        every { componentsItem.data } returns dataList
        assert(viewModel.getBannerItem(0) == null)
//        data list is not empty
        val dataItem = DataItem()
        dataList.add(dataItem)
        every { componentsItem.name } returns TEST_STRING
        assert(viewModel.getBannerItem(0)?.parentComponentName == TEST_STRING)
    }

    @Test
    fun `test property type`() {
        // properties has value
        val properties = Properties(type = Constant.PropertyType.ATF_BANNER)
        every { componentsItem.properties } returns properties
        assert(viewModel.getPropertyType() == properties.type)

        // properties is null
        every { componentsItem.properties } returns null
        assert(viewModel.getPropertyType() == String.EMPTY)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(DiscoveryDataMapper)
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
    }


}
