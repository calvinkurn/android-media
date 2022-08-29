package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopbannerinfinite

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
class ShopBannerInfiniteItemModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ShopBannerInfiniteItemViewModel =
        spyk(ShopBannerInfiniteItemViewModel(application, componentsItem, 99))

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    /**************************** getNavigationUrl() test *******************************************/


    @Test
    fun `test for getNavigationUrl when list is null`() {
        val list = null
        every { componentsItem.data } returns list

        assert(viewModel.getNavigationUrl() == null)
    }

    @Test
    fun `test for getNavigationUrl when list is empty`() {
        val list = mutableListOf<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getNavigationUrl() == null)
    }

    @Test
    fun `test for getNavigationUrl`() {
        val list = mutableListOf(DataItem(applinks = "xyz"))
        every { componentsItem.data } returns list

        assert(viewModel.getNavigationUrl() == "xyz")
    }

    /**************************** end of getNavigationUrl() test *******************************************/


    @Test
    fun `test for getComponentLiveData`() {
        viewModel.onAttachToViewHolder()

        assert(viewModel.getComponentLiveData().value == componentsItem)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.components == componentsItem)
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

}