package com.tokopedia.sellerorder.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.common.domain.model.*
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-11.
 */

@RunWith(JUnit4::class)
class SomDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = SomTestDispatcherProvider()
    private lateinit var somDetailViewModel: SomDetailViewModel
    private var listProducts = listOf<SomDetailOrder.Data.GetSomDetail.Products>()
    private var listMsg = listOf<String>()
    private var listReasonReject = listOf(SomReasonRejectData.Data.SomRejectReason())

    @RelaxedMockK
    lateinit var somGetOrderDetailUseCase: SomGetOrderDetailUseCase

    @RelaxedMockK
    lateinit var somAcceptOrderUseCase: SomAcceptOrderUseCase

    @RelaxedMockK
    lateinit var somReasonRejectUseCase: SomReasonRejectUseCase

    @RelaxedMockK
    lateinit var somRejectOrderUseCase: SomRejectOrderUseCase

    @RelaxedMockK
    lateinit var somEditRefNumUseCase: SomEditRefNumUseCase

    @RelaxedMockK
    lateinit var somSetDeliveredUseCase: SomSetDeliveredUseCase

    @RelaxedMockK
    lateinit var somRejectCancelOrderUseCase: SomRejectCancelOrderUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somDetailViewModel = SomDetailViewModel(dispatcher, userSessionInterface, somGetOrderDetailUseCase,
                somAcceptOrderUseCase, somReasonRejectUseCase, somRejectOrderUseCase,
                somEditRefNumUseCase, somSetDeliveredUseCase, somRejectCancelOrderUseCase)

        val product1 = SomDetailOrder.Data.GetSomDetail.Products(123)
        listProducts = arrayListOf(product1).toMutableList()

        listMsg = arrayListOf("msg1")

        val reasonReject1 = SomReasonRejectData.Data.SomRejectReason(1)
        listReasonReject = arrayListOf(reasonReject1).toMutableList()
    }

    // order_detail
    @Test
    fun getOrderDetail_shouldReturnSuccess() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(getSomDetail = SomDetailOrder.Data.GetSomDetail(123)))

        //when
        somDetailViewModel.loadDetailOrder("")

        //then
        assert(somDetailViewModel.orderDetailResult.value is Success)
        assert((somDetailViewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.orderId == 123)
    }

    @Test
    fun getOrderDetail_shouldReturnFail() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somDetailViewModel.loadDetailOrder("")

        //then
        assert(somDetailViewModel.orderDetailResult.value is Fail)
    }

    @Test
    fun getOrderDetail_shouldNotReturnEmpty() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(
                getSomDetail = SomDetailOrder.Data.GetSomDetail(listProduct = listProducts)))

        //when
        somDetailViewModel.loadDetailOrder("")

        //then
        assert(somDetailViewModel.orderDetailResult.value is Success)
        assert((somDetailViewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.listProduct?.isNotEmpty() ?: false)
    }

    // accept_order
    @Test
    fun acceptOrder_shouldReturnSuccess() {
        //given
        coEvery {
            somAcceptOrderUseCase.execute()
        } returns Success(SomAcceptOrderResponse.Data(SomAcceptOrderResponse.Data.AcceptOrder(success = 1)))

        //when
        somDetailViewModel.acceptOrder("")

        //then
        assert(somDetailViewModel.acceptOrderResult.value is Success)
        assert((somDetailViewModel.acceptOrderResult.value as Success<SomAcceptOrderResponse.Data>).data.acceptOrder.success == 1)
    }

    @Test
    fun acceptOrder_shouldReturnFail() {
        //given
        coEvery {
            somAcceptOrderUseCase.execute()
        } returns Fail(Throwable())

        //when
        somDetailViewModel.acceptOrder("")

        //then
        assert(somDetailViewModel.acceptOrderResult.value is Fail)
    }

    @Test
    fun acceptOrder_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somAcceptOrderUseCase.execute()
        } returns Success(SomAcceptOrderResponse.Data(SomAcceptOrderResponse.Data.AcceptOrder(listMessage = listMsg)))

        //when
        somDetailViewModel.acceptOrder("")

        //then
        assert(somDetailViewModel.acceptOrderResult.value is Success)
        assert((somDetailViewModel.acceptOrderResult.value as Success<SomAcceptOrderResponse.Data>).data.acceptOrder.listMessage.isNotEmpty())
    }

    // reason_reject
    @Test
    fun getReasonReject_shouldReturnSuccess() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        //when
        somDetailViewModel.getRejectReasons("")

        //then
        assert(somDetailViewModel.rejectReasonResult.value is Success)
        assert((somDetailViewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.first().reasonCode == 1)
    }

    @Test
    fun getReasonReject_shouldReturnFail() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Fail(Throwable())

        //when
        somDetailViewModel.getRejectReasons("")

        //then
        assert(somDetailViewModel.rejectReasonResult.value is Fail)
    }

    @Test
    fun getReasonReject_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } returns Success(SomReasonRejectData.Data(listSomRejectReason = listReasonReject))

        //when
        somDetailViewModel.getRejectReasons("")

        //then
        assert(somDetailViewModel.rejectReasonResult.value is Success)
        assert((somDetailViewModel.rejectReasonResult.value as Success<SomReasonRejectData.Data>).data.listSomRejectReason.isNotEmpty())
    }

    // reject_order
    @Test
    fun rejectOrder_shouldReturnSuccess() {
        //given
        coEvery {
            somRejectOrderUseCase.execute(any())
        } returns Success(SomRejectOrderResponse.Data(SomRejectOrderResponse.Data.RejectOrder(success = 1)))

        //when
        somDetailViewModel.rejectOrder(SomRejectRequestParam())

        //then
        assert(somDetailViewModel.rejectOrderResult.value is Success)
        assert((somDetailViewModel.rejectOrderResult.value as Success<SomRejectOrderResponse.Data>).data.rejectOrder.success == 1)
    }

    @Test
    fun rejectOrder_shouldReturnFail() {
        //given
        coEvery {
            somRejectOrderUseCase.execute(any())
        } returns Fail(Throwable())

        //when
        somDetailViewModel.rejectOrder(SomRejectRequestParam())

        //then
        assert(somDetailViewModel.rejectOrderResult.value is Fail)
    }

    @Test
    fun rejectOrder_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somRejectOrderUseCase.execute(any())
        } returns Success(SomRejectOrderResponse.Data(SomRejectOrderResponse.Data.RejectOrder(message = listMsg)))

        //when
        somDetailViewModel.rejectOrder(SomRejectRequestParam())

        //then
        assert(somDetailViewModel.rejectOrderResult.value is Success)
        assert((somDetailViewModel.rejectOrderResult.value as Success<SomRejectOrderResponse.Data>).data.rejectOrder.message.isNotEmpty())
    }

    // edit_awb
    @Test
    fun editAwb_shouldReturnSuccess() {
        //given
        coEvery {
            somEditRefNumUseCase.execute()
        } returns Success(SomEditRefNumResponse.Data(SomEditRefNumResponse.Data.MpLogisticEditRefNum(listMessage = listMsg)))

        //when
        somDetailViewModel.editAwb("123", "12345")

        //then
        assert(somDetailViewModel.editRefNumResult.value is Success)
        assert((somDetailViewModel.editRefNumResult.value as Success<SomEditRefNumResponse.Data>).data.mpLogisticEditRefNum.listMessage.first() == "msg1")
    }

    @Test
    fun editAwb_shouldReturnFail() {
        //given
        coEvery {
            somEditRefNumUseCase.execute()
        } returns Fail(Throwable())

        //when
        somDetailViewModel.editAwb("123", "12345")

        //then
        assert(somDetailViewModel.editRefNumResult.value is Fail)
    }

    @Test
    fun editAwb_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somEditRefNumUseCase.execute()
        } returns Success(SomEditRefNumResponse.Data(SomEditRefNumResponse.Data.MpLogisticEditRefNum(listMessage = listMsg)))

        //when
        somDetailViewModel.editAwb("123", "12345")

        //then
        assert(somDetailViewModel.editRefNumResult.value is Success)
        assert((somDetailViewModel.editRefNumResult.value as Success<SomEditRefNumResponse.Data>).data.mpLogisticEditRefNum.listMessage.first() == "msg1")
    }

    // set_delivered
    @Test
    fun setDelivered_shouldReturnSuccess() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(success = 1)))

        //when
        somDetailViewModel.setDelivered("","","")

        //then
        assert(somDetailViewModel.setDelivered.value is Success)
        assert((somDetailViewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.success == 1)
    }

    @Test
    fun setDelivered_shouldReturnFail() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        somDetailViewModel.setDelivered("","","")

        //then
        assert(somDetailViewModel.setDelivered.value is Fail)
    }

    @Test
    fun setDelivered_msgShouldNotReturnEmpty() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } returns Success(SetDeliveredResponse(SetDelivered(message = listMsg)))

        //when
        somDetailViewModel.setDelivered("","","")

        //then
        assert(somDetailViewModel.setDelivered.value is Success)
        assert((somDetailViewModel.setDelivered.value as Success<SetDeliveredResponse>).data.setDelivered.message.first() == "msg1")
    }

    @Test
    fun rejectCancelOrder_shouldReturnSuccess() {
        coEvery {
            somRejectCancelOrderUseCase.execute(any())
        } returns Success(SomRejectCancelOrderResponse.Data())

        somDetailViewModel.rejectCancelOrder("123456")

        assert(somDetailViewModel.rejectCancelOrderResult.value is Success)
    }

    @Test
    fun rejectCancelOrder_shouldReturnFail() {
        coEvery {
            somRejectCancelOrderUseCase.execute(any())
        } returns Fail(Throwable())

        somDetailViewModel.rejectCancelOrder("123456")

        assert(somDetailViewModel.rejectCancelOrderResult.value is Fail)
    }
}

