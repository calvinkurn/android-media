package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import io.mockk.*
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
    private val mvcSummaryUseCase: MVCSummaryUseCase by lazy {
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

    /**************************** test for update data *******************************************/
    @Test
    fun`test for update data`(){
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        every { componentsItem.data } returns list
        list.add(mockDataItem)
        val listOfShopIds = ArrayList<Int>()
        every { mockDataItem.shopIds} returns listOfShopIds
        listOfShopIds.add(101)
        listOfShopIds.add(102)

        viewModel.updateData("105",false, mockk())

        assert(viewModel.errorState.value == null)

        viewModel.updateData("101",false, mockk())
        assert(viewModel.errorState.value == true)
        assert(viewModel.mvcData.value == null)
        viewModel.updateData("101",true, mockk())
        assert(viewModel.errorState.value == false)
        assert(viewModel.mvcData.value != null)
    }
    /**************************** end of update data *******************************************/

    /**************************** test for fetchDataForCoupons *******************************************/
    @Test
    fun`test for fetchDataForCoupons`() {
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        val listOfShopIds = ArrayList<Int>()
        listOfShopIds.add(101)
        listOfShopIds.add(102)
        every { mockDataItem.shopIds} returns listOfShopIds
        list.add(mockDataItem)
        every { componentsItem.data } returns list
        val data = TokopointsCatalogMVCSummary(resultStatus = null, isShown = true,counterTotal = null,animatedInfoList = listOf())
        val tokopointsCatalogMVCSummaryResponse = TokopointsCatalogMVCSummaryResponse(data = data)
        viewModel.mvcSummaryUseCase = mvcSummaryUseCase
        coEvery {
            mvcSummaryUseCase.getQueryParams(
                any())
        } returns HashMap()
        coEvery {
            mvcSummaryUseCase.getResponse(
                any())
        } returns tokopointsCatalogMVCSummaryResponse

        viewModel.fetchDataForCoupons()

        assert(viewModel.mvcData.value != null)

    }

    @Test
    fun`test for fetchDataForCoupons when shopID is empty`() {
        val viewModel: DiscoMerchantVoucherViewModel = spyk(DiscoMerchantVoucherViewModel(application, componentsItem, 99))
        list.clear()
        every { mockDataItem.shopIds} returns listOf()
        list.add(mockDataItem)
        every { componentsItem.data } returns list

        viewModel.fetchDataForCoupons()

        assert(viewModel.errorState.value == true)

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
        val data = TokopointsCatalogMVCSummary(resultStatus = null, isShown = true,counterTotal = null,animatedInfoList = listOf())
        val tokopointsCatalogMVCSummaryResponse = TokopointsCatalogMVCSummaryResponse(data = data)
        viewModel.mvcSummaryUseCase = mvcSummaryUseCase
        coEvery {
            mvcSummaryUseCase.getResponse(
                any())
        } returns tokopointsCatalogMVCSummaryResponse

        viewModel.fetchDataForCoupons()

        assert(viewModel.errorState.value == true)

    }
}
