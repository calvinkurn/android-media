package com.tokopedia.sellerorder.detail

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-11.
 */

@RunWith(JUnit4::class)
class SomDetailViewModelTest: SomOrderBaseViewModelTest<SomDetailViewModel>() {

    private val dispatcher = CoroutineDispatchersProvider
    private var listProducts = listOf<SomDetailOrder.Data.GetSomDetail.Products>()
    private var listReasonReject = listOf(SomReasonRejectData.Data.SomRejectReason())

    @RelaxedMockK
    lateinit var somGetOrderDetailUseCase: SomGetOrderDetailUseCase

    @RelaxedMockK
    lateinit var somReasonRejectUseCase: SomReasonRejectUseCase

    @RelaxedMockK
    lateinit var somSetDeliveredUseCase: SomSetDeliveredUseCase

    @RelaxedMockK
    lateinit var somGetUserRoleUseCase: SomGetUserRoleUseCase

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = SomDetailViewModel(somAcceptOrderUseCase, somRejectOrderUseCase, somEditRefNumUseCase,
                somRejectCancelOrderUseCase, userSessionInterface, dispatcher,
                somGetOrderDetailUseCase, somReasonRejectUseCase, somSetDeliveredUseCase,
                somGetUserRoleUseCase)

        val product1 = SomDetailOrder.Data.GetSomDetail.Products("123")
        listProducts = arrayListOf(product1).toMutableList()

        val reasonReject1 = SomReasonRejectData.Data.SomRejectReason(1)
        listReasonReject = arrayListOf(reasonReject1).toMutableList()
    }

    // order_detail
    @Test
    fun getOrderDetail_shouldReturnSuccess() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(getSomDetail = SomDetailOrder.Data.GetSomDetail("123")))

        //when
        viewModel.loadDetailOrder("")

        //then
        assert(viewModel.orderDetailResult.value is Success)
        assert((viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.orderId == "123")
    }

    @Test
    fun getOrderDetail_shouldReturnFail() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        viewModel.loadDetailOrder("")

        //then
        assert(viewModel.orderDetailResult.value is Fail)
    }

    @Test
    fun getOrderDetail_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(
                getSomDetail = SomDetailOrder.Data.GetSomDetail(listProduct = listProducts)))

        //when
        viewModel.loadDetailOrder("")

        //then
        assert(viewModel.orderDetailResult.value is Success)
        assert((viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.listProduct?.isNotEmpty() ?: false)
    }

    // reason_reject
    @Test
    fun getReasonReject_shouldReturnSuccess() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        //when
        viewModel.getRejectReasons("")

        //then
        assert(viewModel.rejectReasonResult.value is Success)
        assert((viewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.first().reasonCode == 1)
    }

    @Test
    fun getReasonReject_shouldReturnFail() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        viewModel.getRejectReasons("")

        //then
        assert(viewModel.rejectReasonResult.value is Fail)
    }

    @Test
    fun getReasonReject_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        //when
        viewModel.getRejectReasons("")

        //then
        assert(viewModel.rejectReasonResult.value is Success)
        assert((viewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.isNotEmpty())
    }

    // set_delivered
    @Test
    fun setDelivered_shouldReturnSuccess() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(success = 1)))

        //when
        viewModel.setDelivered("","","")

        //then
        assert(viewModel.setDelivered.value is Success)
        assert((viewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.success == 1)
    }

    @Test
    fun setDelivered_shouldReturnFail() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        viewModel.setDelivered("","","")

        //then
        assert(viewModel.setDelivered.value is Fail)
    }

    @Test
    fun setDelivered_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(message = listMsg)))

        //when
        viewModel.setDelivered("","","")

        //then
        assert(viewModel.setDelivered.value is Success)
        assert((viewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.message.first() == "msg1")
    }

    @Test
    fun loadUserRoles_shouldReturnSuccess() {
        //given
        coEvery {
            somGetUserRoleUseCase.execute()
        } returns SomGetUserRoleUiModel()

        //when
        viewModel.loadUserRoles(123456)

        //then
        assert(viewModel.userRoleResult.value is Success)
    }

    @Test
    fun loadUserRoles_shouldReturnFail() {
        //given
        coEvery {
            somGetUserRoleUseCase.execute()
        } throws Throwable()

        //when
        viewModel.loadUserRoles(123456)

        //then
        assert(viewModel.userRoleResult.value is Fail)
    }
}

