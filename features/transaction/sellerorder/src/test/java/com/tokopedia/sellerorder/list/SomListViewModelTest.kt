package com.tokopedia.sellerorder.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.list.SomGetFilterListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderStatusListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetTickerListUseCase
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somListViewModel = SomListViewModel(dispatcher, somGetTickerListUseCase,
                somGetOrderStatusListUseCase, somGetFilterListUseCase, somGetOrderListUseCase, somGetUserRoleUseCase)

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
    }

    // ticker_list
    @Test
    fun getTickerData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetTickerListUseCase.execute(any(), any())
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
            somGetTickerListUseCase.execute(any(), any())
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
            somGetTickerListUseCase.execute(any(), any())
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
        //given
        coEvery {
            somGetFilterListUseCase.execute(any())
        } returns Success(SomListFilter.Data.OrderFilterSom(listFilter).statusList.toMutableList())

        //when
        somListViewModel.loadFilterList("")

        //then
        assert(somListViewModel.filterListResult.value is Success)
        assert((somListViewModel.filterListResult.value as Success<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>).data[0].orderStatus == "400")
    }

    @Test
    fun getFilterListData_shouldReturnFail() {
        //given
        coEvery {
            somGetFilterListUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somListViewModel.loadFilterList("")

        //then
        assert(somListViewModel.filterListResult.value is Fail)
    }

    @Test
    fun getFilterListData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetFilterListUseCase.execute(any())
        } returns Success(SomListFilter.Data.OrderFilterSom(listFilter).statusList.toMutableList())

        //when
        somListViewModel.loadFilterList("")

        //then
        assert(somListViewModel.filterListResult.value is Success)
        assert((somListViewModel.filterListResult.value as Success<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>).data.size > 0)
    }

    // order_list
    @Test
    fun getOrderListData_shouldReturnSuccess() {
        //given
        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Success(SomListOrder.Data.OrderList(-1, listOrder))

        //when
        somListViewModel.loadOrderList(SomListOrderParam())

        //then
        assert(somListViewModel.orderListResult.value is Success)
        assert((somListViewModel.orderListResult.value as Success<SomListOrder.Data.OrderList>).data.orders[0].orderId == "123")
    }

    @Test
    fun getOrderListData_shouldReturnFail() {
        //given
        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somListViewModel.loadOrderList(SomListOrderParam())

        //then
        assert(somListViewModel.orderListResult.value is Fail)
    }

    @Test
    fun getOrderListData_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetOrderListUseCase.execute(any())
        } returns Success(SomListOrder.Data.OrderList(-1, listOrder))

        //when
        somListViewModel.loadOrderList(SomListOrderParam())

        //then
        assert(somListViewModel.orderListResult.value is Success)
        assert((somListViewModel.orderListResult.value as Success<SomListOrder.Data.OrderList>).data.orders.isNotEmpty())
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
}
