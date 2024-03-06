package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
    private val mvcSummaryUseCase: MerchantVoucherUseCase by lazy {
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

    @Test
    fun `test for getProductId`(){
        every { componentsItem.data } returns null
        assert(viewModel.getProductId().isEmpty())
        every { componentsItem.data } returns list
        assert(viewModel.getProductId().isEmpty())
        list.add(mockDataItem)
        every { mockDataItem.productId } returns ""
        assert(viewModel.getProductId().isEmpty())
        every { mockDataItem.productId} returns "1283"
        assert(viewModel.getProductId()=="1283")

    }

    /**************************** end of update data *******************************************/

    /**************************** test for fetchDataForCoupons *******************************************/

    @Test
    fun`test for fetchDataForCoupons when response data is null`() {
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        val listOfShopIds = ArrayList<Int>()
        listOfShopIds.add(101)
        listOfShopIds.add(102)
        every { mockDataItem.shopIds} returns listOfShopIds
        list.add(mockDataItem)
        every { componentsItem.data } returns list
        viewModel.merchantVoucherUseCase = mvcSummaryUseCase
        coEvery {
            mvcSummaryUseCase.loadFirstPageComponents(
                any(), any())
        } returns true

        viewModel.fetchDataForCoupons()

        assert(viewModel.coupon.value == null)

    }

    @Test
    fun`test for fetchDataForCoupons when shopID is empty`() {
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        every { mockDataItem.shopIds} returns listOf()
        list.add(mockDataItem)
        every { componentsItem.data } returns list

        viewModel.fetchDataForCoupons()

        assert(viewModel.loadError.value == true)

    }

    @Test
    fun`test for fetchDataForCoupons when mvcSummaryUseCase throws exception`() {
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        val listOfShopIds = ArrayList<Int>()
        listOfShopIds.add(101)
        listOfShopIds.add(102)
        every { mockDataItem.shopIds} returns listOfShopIds
        list.add(mockDataItem)
        every { componentsItem.data } returns list
        viewModel.merchantVoucherUseCase = mvcSummaryUseCase
        coEvery {
            mvcSummaryUseCase.loadFirstPageComponents(
                any(), any())
        } returns false

        viewModel.fetchDataForCoupons()

        assert(viewModel.loadError.value == true)

    }
}
