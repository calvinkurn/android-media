package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BrandRecommendationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BrandRecommendationViewModel by lazy {
        spyk(BrandRecommendationViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }
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

    @Test
    fun `position test`() {
        assert(viewModel.getComponentPosition() == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentDataLiveData().value == componentsItem)
    }

    /****************************************** mapBrandRecomItems() ****************************************/
    @Test
    fun `mapBrandRecomItems test when data is null`() {
        every { componentsItem.data } returns null
        viewModel.mapBrandRecomItems()

        assert(viewModel.getListDataLiveData().value == null)
    }

    @Test
    fun `mapBrandRecomItems test when list empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        viewModel.mapBrandRecomItems()

        assert(viewModel.getListData()?.isEmpty() == true)

    }
//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.

    @Test
    fun `mapBrandRecomItems test when list is not empty`() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        val dataList = ArrayList<DataItem>()
        val dataItem = DataItem()
        dataList.add(dataItem)
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns dataList
        val viewModel1 = spyk(BrandRecommendationViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }

        viewModel1.mapBrandRecomItems()
//        This returned list must not contain item with empty imageUrlMobile
        assert(viewModel1.getListData() == list)

    }

    /****************************************** end of mapBrandRecomItems() ****************************************/

    @Test
    fun `get Component id`(){
        every { componentsItem.id } returns "999"
        assert(viewModel.getComponentID() == "999")
    }


}