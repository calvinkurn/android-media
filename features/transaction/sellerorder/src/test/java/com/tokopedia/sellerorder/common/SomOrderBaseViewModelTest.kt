package com.tokopedia.sellerorder.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.common.domain.model.*
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.util.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class SomOrderBaseViewModelTest<T: SomOrderBaseViewModel> {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var somAcceptOrderUseCase: SomAcceptOrderUseCase

    @RelaxedMockK
    lateinit var somRejectOrderUseCase: SomRejectOrderUseCase

    @RelaxedMockK
    lateinit var somEditRefNumUseCase: SomEditRefNumUseCase

    @RelaxedMockK
    lateinit var somRejectCancelOrderUseCase: SomRejectCancelOrderUseCase

    @RelaxedMockK
    lateinit var somValidateOrderUseCase: SomValidateOrderUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var viewModel: T

    protected var listMsg = listOf<String>()
    var orderId = "1234567890"
    var invoice = "INV/20200922/XX/IX/123456789"

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
        listMsg = arrayListOf("msg1")
    }

    @Test
    open fun acceptOrder_shouldReturnSuccess() {
        every {
            userSessionInterface.shopId
        } returns "1234567890"

        coEvery {
            somAcceptOrderUseCase.execute()
        } returns SomAcceptOrderResponse.Data(SomAcceptOrderResponse.Data.AcceptOrder(success = 1, listMessage = listMsg))

        viewModel.acceptOrder(orderId, invoice)

        assert(viewModel.acceptOrderResult.value is Success)
        assert((viewModel.acceptOrderResult.value as Success<SomAcceptOrderResponse.Data>).data.acceptOrder.success == 1)
        assert((viewModel.acceptOrderResult.value as Success<SomAcceptOrderResponse.Data>).data.acceptOrder.listMessage.isNotEmpty())
    }

    @Test
    open fun acceptOrder_shouldReturnFail() {
        every {
            userSessionInterface.shopId
        } returns null

        coEvery {
            somAcceptOrderUseCase.execute()
        } throws Throwable()

        viewModel.acceptOrder(orderId)

        assert(viewModel.acceptOrderResult.value is Fail)
    }

    @Test
    open fun rejectOrder_shouldReturnSuccess() {
        coEvery {
            somRejectOrderUseCase.execute(any())
        } returns SomRejectOrderResponse.Data(SomRejectOrderResponse.Data.RejectOrder(success = 1, message = listMsg))

        viewModel.rejectOrder(SomRejectRequestParam(orderId = orderId), invoice)

        assert(viewModel.rejectOrderResult.value is Success)
        assert((viewModel.rejectOrderResult.value as Success<SomRejectOrderResponse.Data>).data.rejectOrder.success == 1)
        assert((viewModel.rejectOrderResult.value as Success<SomRejectOrderResponse.Data>).data.rejectOrder.message.isNotEmpty())
        Assert.assertEquals(null, null)
    }

    @Test
    open fun rejectOrder_shouldReturnFail() {
        coEvery {
            somRejectOrderUseCase.execute(any())
        } throws Throwable()

        viewModel.rejectOrder(SomRejectRequestParam(orderId = orderId))

        assert(viewModel.rejectOrderResult.value is Fail)
    }

    @Test
    open fun editAwb_shouldReturnSuccess() {
        coEvery {
            somEditRefNumUseCase.execute()
        } returns SomEditRefNumResponse.Data(SomEditRefNumResponse.Data.MpLogisticEditRefNum(listMessage = listMsg))

        viewModel.editAwb(orderId, "12345", invoice)

        assert(viewModel.editRefNumResult.value is Success)
        assert((viewModel.editRefNumResult.value as Success<SomEditRefNumResponse.Data>).data.mpLogisticEditRefNum.listMessage.first() == "msg1")
    }

    @Test
    open fun editAwb_shouldReturnFail() {
        coEvery {
            somEditRefNumUseCase.execute()
        } throws Throwable()

        viewModel.editAwb(orderId, "12345")

        assert(viewModel.editRefNumResult.value is Fail)
    }

    @Test
    open fun rejectCancelOrder_shouldReturnSuccess() {
        coEvery {
            somRejectCancelOrderUseCase.execute(any())
        } returns SomRejectCancelOrderResponse.Data()

        viewModel.rejectCancelOrder(orderId, invoice)

        assert(viewModel.rejectCancelOrderResult.value is Success)
    }

    @Test
    open fun rejectCancelOrder_shouldReturnFail() {
        coEvery {
            somRejectCancelOrderUseCase.execute(any())
        } throws Throwable()

        viewModel.rejectCancelOrder(orderId)

        assert(viewModel.rejectCancelOrderResult.value is Fail)
    }

    @Test
    fun validateOrders_shouldReturnSuccess() {
        val orderIds = listOf(orderId)
        val param = SomValidateOrderRequest(orderIds)
        coEvery {
            somValidateOrderUseCase.execute(param)
        } returns true

        viewModel.validateOrders(orderIds)

        coVerify {
            somValidateOrderUseCase.execute(param)
        }

        val result = viewModel.validateOrderResult.observeAwaitValue()
        assert(result is Success && result.data)
    }

    @Test
    fun validateOrders_shouldReturnFail() {
        val orderIds = listOf(orderId)
        val param = SomValidateOrderRequest(orderIds)
        coEvery {
            somValidateOrderUseCase.execute(param)
        } throws Throwable()

        viewModel.validateOrders(orderIds)

        coVerify {
            somValidateOrderUseCase.execute(param)
        }

        assert(viewModel.validateOrderResult.observeAwaitValue() is Fail)
    }
}