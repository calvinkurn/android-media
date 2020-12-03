package com.tokopedia.sellerorder.oldlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.oldlist.data.model.SomListAllFilter
import com.tokopedia.sellerorder.oldlist.domain.filter.SomGetAllFilterUseCase
import com.tokopedia.sellerorder.oldlist.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.util.observeAwaitValue
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
 * Created by fwidjaja on 2020-05-12.
 */
@RunWith(JUnit4::class)
class SomFilterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = SomTestDispatcherProvider()
    private lateinit var somFilterViewModel: SomFilterViewModel
    private var listShipping = listOf<SomListAllFilter.Data.ShippingList>()
    private var listStatusOrder = listOf<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>()
    private var listOrderType = listOf<SomListAllFilter.Data.OrderType>()

    @RelaxedMockK
    lateinit var somGetAllFilterUseCase: SomGetAllFilterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somFilterViewModel = SomFilterViewModel(dispatcher, somGetAllFilterUseCase)

        val shipping1 = SomListAllFilter.Data.ShippingList(123)
        val shipping2 = SomListAllFilter.Data.ShippingList(456)
        val shipping3 = SomListAllFilter.Data.ShippingList(789)
        listShipping = arrayListOf(shipping1, shipping2, shipping3)

        val statusOrder1 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(123)
        val statusOrder2 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(456)
        val statusOrder3 = SomListAllFilter.Data.OrderFilterSomSingle.StatusList(789)
        listStatusOrder = arrayListOf(statusOrder1, statusOrder2, statusOrder3)

        val orderType1 = SomListAllFilter.Data.OrderType(123)
        val orderType2 = SomListAllFilter.Data.OrderType(456)
        val orderType3 = SomListAllFilter.Data.OrderType(789)
        listOrderType = arrayListOf(orderType1, orderType2, orderType3)
    }

    // shipping_list
    @Test
    fun getShippingList_shouldReturnSuccess() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Success(SomListAllFilter.Data(orderShippingList = listShipping).orderShippingList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val shippingListResult = somFilterViewModel.shippingListResult.observeAwaitValue()

        assert(shippingListResult is Success)
        assert((shippingListResult as Success<MutableList<SomListAllFilter.Data.ShippingList>>).data.first().shippingId == 123)
        assert(filterListResult is Success && filterListResult.data)
    }

    @Test
    fun getShippingList_shouldReturnFail() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Fail(Throwable())

        somFilterViewModel.loadSomFilterData("")

        assert(somFilterViewModel.shippingListResult.observeAwaitValue() is Fail)
        assert(somFilterViewModel.filterListResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getShippingList_shouldNotReturnEmpty() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Success(SomListAllFilter.Data(orderShippingList = listShipping).orderShippingList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val shippingListResult = somFilterViewModel.shippingListResult.observeAwaitValue()

        assert(shippingListResult is Success)
        assert((shippingListResult as Success<MutableList<SomListAllFilter.Data.ShippingList>>).data.size > 0)
        assert(filterListResult is Success && filterListResult.data)
    }

    // status_order_list
    @Test
    fun getStatusOrderList_shouldReturnSuccess() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(statusList = listStatusOrder).statusList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val statusOrderListResult = somFilterViewModel.statusOrderListResult.observeAwaitValue()

        assert(statusOrderListResult is Success)
        assert((statusOrderListResult as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data.first().id == 123)
        assert(filterListResult is Success && filterListResult.data)
    }

    @Test
    fun getStatusOrderList_shouldReturnFail() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Fail(Throwable())

        somFilterViewModel.loadSomFilterData("")

        assert(somFilterViewModel.statusOrderListResult.observeAwaitValue() is Fail)
        assert(somFilterViewModel.filterListResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getStatusOrderList_shouldNotReturnEmpty() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(statusList = listStatusOrder).statusList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val statusOrderListResult = somFilterViewModel.statusOrderListResult.observeAwaitValue()

        assert(statusOrderListResult is Success)
        assert((statusOrderListResult as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data.size > 0)
        assert(filterListResult is Success && filterListResult.data)
    }

    // order_type_list
    @Test
    fun getOrderTypeList_shouldReturnSuccess() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Success(SomListAllFilter.Data(orderTypeList = listOrderType).orderTypeList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val orderTypeListResult = somFilterViewModel.orderTypeListResult.observeAwaitValue()

        assert(orderTypeListResult is Success)
        assert((orderTypeListResult as Success<MutableList<SomListAllFilter.Data.OrderType>>).data.first().id == 123)
        assert(filterListResult is Success && filterListResult.data)
    }

    @Test
    fun getOrderTypeList_shouldReturnFail() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Fail(Throwable())

        somFilterViewModel.loadSomFilterData("")

        assert(somFilterViewModel.orderTypeListResult.observeAwaitValue() is Fail)
        assert(somFilterViewModel.filterListResult.observeAwaitValue() is Fail)
    }

    @Test
    fun getOrderTypeList_shouldNotReturnEmpty() {
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Success(SomListAllFilter.Data(orderTypeList = listOrderType).orderTypeList.toMutableList())

        somFilterViewModel.loadSomFilterData("")
        val filterListResult = somFilterViewModel.filterListResult.observeAwaitValue()
        val orderTypeListResult = somFilterViewModel.orderTypeListResult.observeAwaitValue()

        assert(orderTypeListResult is Success)
        assert((orderTypeListResult as Success<MutableList<SomListAllFilter.Data.OrderType>>).data.size > 0)
        assert(filterListResult is Success && filterListResult.data)
    }
}
