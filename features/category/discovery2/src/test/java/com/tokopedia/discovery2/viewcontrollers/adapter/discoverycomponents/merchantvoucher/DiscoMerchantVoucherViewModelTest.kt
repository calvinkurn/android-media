package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
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

class DiscoMerchantVoucherViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
    private val list = ArrayList<DataItem>()
    private val mockDataItem:DataItem = mockk()
    private val useCase: MVCSummaryUseCase by lazy {
        mockk()
    }

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
    fun `test for shopId`(){
        every { componentsItem.data } returns null
        assert(viewModel.getShopID().isEmpty())
        every { componentsItem.data } returns list
        assert(viewModel.getShopID().isEmpty())
        list.add(mockDataItem)
        every { mockDataItem.shopIds } returns null
        assert(viewModel.getShopID().isEmpty())
        val listOfShopIds = ArrayList<Int>()
        every { mockDataItem.shopIds} returns listOfShopIds
        assert(viewModel.getShopID().isEmpty())
        listOfShopIds.add(101)
        listOfShopIds.add(102)
        assert(viewModel.getShopID()=="101")

    }

}