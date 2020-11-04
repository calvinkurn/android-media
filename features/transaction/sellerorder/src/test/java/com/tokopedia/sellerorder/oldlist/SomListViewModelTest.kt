package com.tokopedia.sellerorder.oldlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.presenter.model.SomListOrderParam
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.oldlist.data.model.*
import com.tokopedia.sellerorder.oldlist.domain.list.*
import com.tokopedia.sellerorder.oldlist.presentation.viewmodel.SomListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Field

/**
 * Created by fwidjaja on 2020-05-07.
 */

@RunWith(JUnit4::class)
class SomListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = SomTestDispatcherProvider()
    private lateinit var somListViewModel: SomListViewModel
    private var listTickers = listOf<SomListTicker.Data.OrderTickers.Tickers>()
    private var listOrderStatus = listOf<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>()
    private var listFilter = listOf<SomListFilter.Data.OrderFilterSom.StatusList>()
    private var listOrder = listOf<SomListOrder.Data.OrderList.Order>()

    @RelaxedMockK
    lateinit var somGetTickerListUseCase: SomGetTickerListUseCase

    @RelaxedMockK
    lateinit var somGetOrderStatusListUseCase: SomGetOrderStatusListUseCase

    @RelaxedMockK
    lateinit var somGetFilterListUseCase: SomGetFilterListUseCase

    @RelaxedMockK
    lateinit var somGetOrderListUseCase: SomGetOrderListUseCase

    @RelaxedMockK
    lateinit var somGetUserRoleUseCase: SomGetUserRoleUseCase

    @RelaxedMockK
    lateinit var topAdsGetShopInfoUseCase: SomTopAdsGetShopInfoUseCase

    private lateinit var topAdsGetShopInfoField: Field
    private lateinit var getTopAdsGetShopInfoJobField: Field

    private val shopID: Int = 123456

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somListViewModel = SomListViewModel(dispatcher, somGetTickerListUseCase,
                somGetOrderStatusListUseCase, somGetFilterListUseCase, somGetOrderListUseCase,
                somGetUserRoleUseCase, topAdsGetShopInfoUseCase)

        val ticker1 = SomListTicker.Data.OrderTickers.Tickers(123)
        val ticker2 = SomListTicker.Data.OrderTickers.Tickers(456)
        val ticker3 = SomListTicker.Data.OrderTickers.Tickers(789)
        listTickers = arrayListOf(ticker1, ticker2, ticker3).toMutableList()

        val statusOrder1 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(220)
        val statusOrder2 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(221)
        val statusOrder3 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(222)
        listOrderStatus = arrayListOf(statusOrder1, statusOrder2, statusOrder3).toMutableList()

        val filter1 = SomListFilter.Data.OrderFilterSom.StatusList(orderStatus = "400")
        val filter2 = SomListFilter.Data.OrderFilterSom.StatusList(orderStatus = "401")
        val filter3 = SomListFilter.Data.OrderFilterSom.StatusList(orderStatus = "402")
        listFilter = arrayListOf(filter1, filter2, filter3).toMutableList()

        val order1 = SomListOrder.Data.OrderList.Order("123")
        val order2 = SomListOrder.Data.OrderList.Order("456")
        val order3 = SomListOrder.Data.OrderList.Order("789")
        listOrder = listOf(order1, order2, order3)

        topAdsGetShopInfoField = SomListViewModel::class.java.getDeclaredField("topAdsGetShopInfo").apply {
            isAccessible = true
        }
        getTopAdsGetShopInfoJobField = SomListViewModel::class.java.getDeclaredField("getTopAdsGetShopInfoJob").apply {
            isAccessible = true
        }
    }

    // ticker_list
    @Test
    fun getTickerData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetTickerListUseCase.execute(any())
        } returns Success(SomListTicker.Data.OrderTickers("", "", "", -1, listTickers).listTicker.toMutableList())

        //when
        somListViewModel.loadTickerList("")

        //then
        assert(somListViewModel.tickerListResult.value is Success)
        assert((somListViewModel.tickerListResult.value as Success<MutableList<SomListTicker.Data.OrderTickers.Tickers>>).data[0].tickerId == 123)
    }

    @Test
    fun getTickerData_shouldReturnFail() {
        //given
        coEvery {
            somGetTickerListUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somListViewModel.loadTickerList("")

        //then
        assert(somListViewModel.tickerListResult.value is Fail)
    }

    @Test
    fun getTickerData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetTickerListUseCase.execute(any())
        } returns Success(SomListTicker.Data.OrderTickers("", "", "", -1, listTickers).listTicker.toMutableList())

        //when
        somListViewModel.loadTickerList("")

        //then
        assert(somListViewModel.tickerListResult.value is Success)
        assert((somListViewModel.tickerListResult.value as Success<MutableList<SomListTicker.Data.OrderTickers.Tickers>>).data.size > 0)
    }

    // status_order_list
    @Test
    fun getOrderStatusData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetOrderStatusListUseCase.execute(any())
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(listOrderStatus).statusList.toMutableList())

        //when
        somListViewModel.loadStatusOrderList("")

        //then
        assert(somListViewModel.statusOrderListResult.value is Success)
        assert((somListViewModel.statusOrderListResult.value as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data[0].id == 220)
    }

    @Test
    fun getOrderStatusData_shouldReturnFail() {
        //given
        coEvery {
            somGetOrderStatusListUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somListViewModel.loadStatusOrderList("")

        //then
        assert(somListViewModel.statusOrderListResult.value is Fail)
    }

    @Test
    fun getOrderStatusData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetOrderStatusListUseCase.execute(any())
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(listOrderStatus).statusList.toMutableList())

        //when
        somListViewModel.loadStatusOrderList("")

        //then
        assert(somListViewModel.statusOrderListResult.value is Success)
        assert((somListViewModel.statusOrderListResult.value as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data.size > 0)
    }

    // filter_list
    @Test
    fun getFilterListData_shouldReturnSuccess() {
        val waitingPaymentCounter = SomListFilter.Data.OrderFilterSom.WaitingPaymentCounter("Menunggu Pembayaran", 10)

        coEvery {
            somGetFilterListUseCase.execute()
        } returns Success(SomListFilter.Data.OrderFilterSom(listFilter, waitingPaymentCounter))

        somListViewModel.loadFilter()

        assert(somListViewModel.filterResult.value is Success)
        assert((somListViewModel.filterResult.value as Success<SomListFilter.Data.OrderFilterSom>).data.statusList.firstOrNull()?.orderStatus == "400")
        assert((somListViewModel.filterResult.value as Success<SomListFilter.Data.OrderFilterSom>).data.waitingPaymentCounter.amount > 0)
    }

    @Test
    fun getFilterListData_shouldReturnFail() {
        //given
        coEvery {
            somGetFilterListUseCase.execute()
        } returns Fail(Throwable())

        //when
        somListViewModel.loadFilter()

        //then
        assert(somListViewModel.filterResult.value is Fail)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun getFilterListData_shouldNotReturnEmpty() {
        val waitingPaymentCounter = SomListFilter.Data.OrderFilterSom.WaitingPaymentCounter("Menunggu Pembayaran", 10)

        coEvery {
            somGetFilterListUseCase.execute()
        } returns Success(SomListFilter.Data.OrderFilterSom(listFilter, waitingPaymentCounter))

        somListViewModel.loadFilter()

        assert(somListViewModel.filterResult.value is Success)
        assert((somListViewModel.filterResult.value as Success<SomListFilter.Data.OrderFilterSom>).data.statusList.isNotEmpty())
        assert((somListViewModel.filterResult.value as Success<SomListFilter.Data.OrderFilterSom>).data.waitingPaymentCounter.amount > 0)
    }

    // order_list
    @Test
    fun getOrderListData_shouldReturnSuccessAndWaitForTopAdsGetInfo() = runBlocking {
        val topAdsGetShopInfoResult = Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo())
        coEvery {
            topAdsGetShopInfoUseCase.execute(shopID)
        } returns topAdsGetShopInfoResult

        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Success(SomListOrder.Data.OrderList(-1, listOrder))

        somListViewModel.loadTopAdsShopInfo(shopID)
        somListViewModel.loadOrderList(SomListOrderParam(), true)

        somListViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(somListViewModel.orderListResult.value is Success)
        assert((somListViewModel.orderListResult.value as Success<SomListOrder.Data.OrderList>).data.orders[0].orderId == "123")
        assert(topAdsGetShopInfoField.get(somListViewModel) == topAdsGetShopInfoResult)
    }

    @Test
    fun getOrderListData_shouldReturnFail() {
        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Fail(Throwable())

        somListViewModel.loadOrderList(SomListOrderParam(), true)

        assert(somListViewModel.orderListResult.value is Fail)
    }

    @Test
    fun getOrderListData_shouldNotReturnEmptyAndWaitForTopAdsGetInfo() {
        val topAdsGetShopInfoResult = Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo())
        coEvery {
            topAdsGetShopInfoUseCase.execute(shopID)
        } returns topAdsGetShopInfoResult

        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Success(SomListOrder.Data.OrderList(-1, listOrder))

        somListViewModel.loadTopAdsShopInfo(shopID)
        somListViewModel.loadOrderList(SomListOrderParam(), true)

        assert(somListViewModel.orderListResult.value is Success)
        assert((somListViewModel.orderListResult.value as Success<SomListOrder.Data.OrderList>).data.orders.isNotEmpty())
        assert(topAdsGetShopInfoField.get(somListViewModel) == topAdsGetShopInfoResult)
    }

    @Test
    fun loadUserRoles_shouldReturnSuccess() {
        //given
        coEvery {
            somGetUserRoleUseCase.execute()
        } returns Success(SomGetUserRoleUiModel())

        //when
        somListViewModel.loadUserRoles(123456)

        //then
        assert(somListViewModel.userRoleResult.value is Success)
    }

    @Test
    fun loadUserRoles_shouldReturnFail() {
        //given
        coEvery {
            somGetUserRoleUseCase.execute()
        } returns Fail(Throwable())

        //when
        somListViewModel.loadUserRoles(123456)

        //then
        assert(somListViewModel.userRoleResult.value is Fail)
    }

    @Test
    fun loadTopAdsShopInfo_shouldReturnSuccess() {
        coEvery {
            topAdsGetShopInfoUseCase.execute(shopID)
        } returns Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo())

        somListViewModel.loadTopAdsShopInfo(shopID)

        coVerify {
            topAdsGetShopInfoUseCase.execute(shopID)
        }

        assert(topAdsGetShopInfoField.get(somListViewModel) != null)
        assert(topAdsGetShopInfoField.get(somListViewModel) is Success<*>)
    }

    @Test
    fun loadTopAdsShopInfo_shouldNeverSendRequestWhenAnotherLoadTopAdsShopInfoRequestIsOnProgress() {
        val getTopAdsGetShopInfoJob = mockk<Job>()
        getTopAdsGetShopInfoJobField.set(somListViewModel, getTopAdsGetShopInfoJob)

        coEvery {
            topAdsGetShopInfoUseCase.execute(shopID)
        } returns Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo())

        every {
            getTopAdsGetShopInfoJob.isCompleted
        } returns false

        somListViewModel.loadTopAdsShopInfo(shopID)

        coVerify(inverse = true) {
            topAdsGetShopInfoUseCase.execute(shopID)
        }
    }

    @Test
    fun loadTopAdsShopInfo_shouldReturnFail() {
        coEvery {
            topAdsGetShopInfoUseCase.execute(shopID)
        } returns Fail(Throwable())

        somListViewModel.loadTopAdsShopInfo(shopID)

        coVerify {
            topAdsGetShopInfoUseCase.execute(shopID)
        }

        assert(topAdsGetShopInfoField.get(somListViewModel) != null)
        assert(topAdsGetShopInfoField.get(somListViewModel) is Fail)
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenUserHasNoProduct() {
        topAdsGetShopInfoField.set(
                somListViewModel,
                Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo(SomConsts.TOPADS_NO_PRODUCT)))
        val result = somListViewModel.isTopAdsActive()
        assert(!result)
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenUserHasNoTopadsProduct() {
        topAdsGetShopInfoField.set(
                somListViewModel,
                Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo(SomConsts.TOPADS_NO_ADS)))
        val result = somListViewModel.isTopAdsActive()
        assert(!result)
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUserUseManualAds() {
        topAdsGetShopInfoField.set(
                somListViewModel,
                Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo(SomConsts.TOPADS_MANUAL_ADS)))
        val result = somListViewModel.isTopAdsActive()
        assert(result)
    }

    @Test
    fun isTopAdsActive_shouldReturnTrueWhenUserUseAutoAds() {
        topAdsGetShopInfoField.set(
                somListViewModel,
                Success(SomTopAdsGetShopInfoResponse.Data.TopAdsGetShopInfo.SomTopAdsShopInfo(SomConsts.TOPADS_AUTO_ADS)))
        val result = somListViewModel.isTopAdsActive()
        assert(result)
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenNoTopAdsGetShopInfoResult() {
        topAdsGetShopInfoField.set(somListViewModel, null)
        val result = somListViewModel.isTopAdsActive()
        assert(!result)
    }

    @Test
    fun isTopAdsActive_shouldReturnFalseWhenTopAdsGetShopInfoResultIsFail() {
        topAdsGetShopInfoField.set(somListViewModel, Fail(Throwable()))
        val result = somListViewModel.isTopAdsActive()
        assert(!result)
    }
}
