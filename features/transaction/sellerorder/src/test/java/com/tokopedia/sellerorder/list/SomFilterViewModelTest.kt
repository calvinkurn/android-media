package com.tokopedia.sellerorder.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.filter.SomGetAllFilterUseCase
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
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
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Success(SomListAllFilter.Data(orderShippingList = listShipping).orderShippingList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.shippingListResult.value is Success)
        assert((somFilterViewModel.shippingListResult.value as Success<MutableList<SomListAllFilter.Data.ShippingList>>).data.first().shippingId == 123)
    }

    @Test
    fun getShippingList_shouldReturnFail() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Fail(Throwable())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.shippingListResult.value is Fail)
    }

    @Test
    fun getShippingList_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getShippingListResult()
        } returns Success(SomListAllFilter.Data(orderShippingList = listShipping).orderShippingList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.shippingListResult.value is Success)
        assert((somFilterViewModel.shippingListResult.value as Success<MutableList<SomListAllFilter.Data.ShippingList>>).data.size > 0)
    }

    // status_order_list
    @Test
    fun getStatusOrderList_shouldReturnSuccess() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(statusList = listStatusOrder).statusList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.statusOrderListResult.value is Success)
        assert((somFilterViewModel.statusOrderListResult.value as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data.first().id == 123)
    }

    @Test
    fun getStatusOrderList_shouldReturnFail() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Fail(Throwable())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.statusOrderListResult.value is Fail)
    }

    @Test
    fun getStatusOrderList_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getStatusOrderListResult()
        } returns Success(SomListAllFilter.Data.OrderFilterSomSingle(statusList = listStatusOrder).statusList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.statusOrderListResult.value is Success)
        assert((somFilterViewModel.statusOrderListResult.value as Success<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>).data.size > 0)
    }

    // order_type_list
    @Test
    fun getOrderTypeList_shouldReturnSuccess() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Success(SomListAllFilter.Data(orderTypeList = listOrderType).orderTypeList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.orderTypeListResult.value is Success)
        assert((somFilterViewModel.orderTypeListResult.value as Success<MutableList<SomListAllFilter.Data.OrderType>>).data.first().id == 123)
    }

    @Test
    fun getOrderTypeList_shouldReturnFail() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Fail(Throwable())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.orderTypeListResult.value is Fail)
    }

    @Test
    fun getOrderTypeList_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetAllFilterUseCase.execute(any())
        } returns Unit

        coEvery {
            somGetAllFilterUseCase.getOrderTypeListResult()
        } returns Success(SomListAllFilter.Data(orderTypeList = listOrderType).orderTypeList.toMutableList())

        //when
        somFilterViewModel.loadSomFilterData("")

        //then
        assert(somFilterViewModel.orderTypeListResult.value is Success)
        assert((somFilterViewModel.orderTypeListResult.value as Success<MutableList<SomListAllFilter.Data.OrderType>>).data.size > 0)
    }
}
