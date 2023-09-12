package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Constant.CompType.SHOP_CARD
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.kotlin.extensions.view.EMPTY
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BannerCarouselItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BannerCarouselItemViewModel by lazy {
        spyk(BannerCarouselItemViewModel(application, componentsItem, 99))
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
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for component type`() {
        // properties has shop card comp type
        val propertiesShopCardCompType = Properties(compType = SHOP_CARD)
        every { componentsItem.properties } returns propertiesShopCardCompType
        assertEquals(propertiesShopCardCompType.compType, viewModel.getCompType())
        assertTrue(viewModel.isCompTypeShopCard())

        // properties has empty comp type
        val propertiesEmptyCompType = Properties(compType = String.EMPTY)
        every { componentsItem.properties } returns propertiesEmptyCompType
        assertEquals(propertiesEmptyCompType.compType, viewModel.getCompType())

        // properties has null comp type
        val propertiesNullCompType = Properties(compType = null)
        every { componentsItem.properties } returns propertiesNullCompType
        assertEquals(String.EMPTY, viewModel.getCompType())

        // properties is null
        every { componentsItem.properties } returns null
        assertEquals(String.EMPTY, viewModel.getCompType())
    }
}
