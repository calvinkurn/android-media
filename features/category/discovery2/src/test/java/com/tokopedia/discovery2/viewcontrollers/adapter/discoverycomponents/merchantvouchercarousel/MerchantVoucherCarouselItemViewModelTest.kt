package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

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
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherCarouselItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel:MerchantVoucherCarouselItemViewModel = spyk(MerchantVoucherCarouselItemViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for mapping and live data`(){
        every { componentsItem.data } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.multiShopModel.value == null)
        val list:MutableList<DataItem> = ArrayList()
        every { componentsItem.data } returns list
        viewModel.onAttachToViewHolder()
        assert(viewModel.multiShopModel.value == null)
        list.add(DataItem())
        viewModel.onAttachToViewHolder()
        assert(viewModel.multiShopModel.value != null)
    }
}