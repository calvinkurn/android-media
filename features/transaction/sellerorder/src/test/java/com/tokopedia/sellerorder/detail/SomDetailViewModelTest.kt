package com.tokopedia.sellerorder.detail

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-05-11.
 */

@RunWith(JUnit4::class)
class SomDetailViewModelTest: SomOrderBaseViewModelTest<SomDetailViewModel>() {

    private val dispatcher = CoroutineTestDispatchersProvider
    private var listProducts = listOf<SomDetailOrder.Data.GetSomDetail.Products>()
    private var listReasonReject = listOf(SomReasonRejectData.Data.SomRejectReason())

    @RelaxedMockK
    lateinit var somGetOrderDetailUseCase: SomGetOrderDetailUseCase

    @RelaxedMockK
    lateinit var somReasonRejectUseCase: SomReasonRejectUseCase

    @RelaxedMockK
    lateinit var somSetDeliveredUseCase: SomSetDeliveredUseCase

    @RelaxedMockK
    lateinit var authorizeSomDetailAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeChatReplyAccessUseCase: AuthorizeAccessUseCase

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = SomDetailViewModel(somAcceptOrderUseCase, somRejectOrderUseCase, somEditRefNumUseCase,
                somRejectCancelOrderUseCase, somValidateOrderUseCase, userSessionInterface, dispatcher,
                somGetOrderDetailUseCase, somReasonRejectUseCase, somSetDeliveredUseCase,
                authorizeSomDetailAccessUseCase, authorizeChatReplyAccessUseCase)

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
    fun checkAdminAccess_ifShopOwner_shouldReturnTruePair() {
        coEvery {
            userSessionInterface.isShopOwner
        } returns true

        viewModel.getAdminPermission()

        assertDetailChatEligibilityEquals(true to true)
    }

    @Test
    fun checkAdminAccess_ifShopAdmin_shouldSuccess() {
        val isSomDetailRole = true
        val isReplyChatRole = true
        onAuthorizeSomDetailAccessSuccess_thenReturn(isSomDetailRole)
        onAuthorizeReplyChatAccessSuccess_thenReturn(isReplyChatRole)
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        assertDetailChatEligibilityEquals(isSomDetailRole to isReplyChatRole)
    }

    @Test
    fun checkAdminAccess_ifShopAdmin_shouldFail() {
        onAuthorizeReplyChatAccessFail_thenThrow(ResponseErrorException())
        coEvery {
            userSessionInterface.isShopOwner
        } returns false
        coEvery {
            userSessionInterface.isShopAdmin
        } returns true

        viewModel.getAdminPermission()

        assert(viewModel.somDetailChatEligibility.value is Fail)
    }

    private fun onAuthorizeSomDetailAccessSuccess_thenReturn(isEligible: Boolean) {
        coEvery {
            authorizeSomDetailAccessUseCase.execute(any())
        } returns isEligible
    }

    private fun onAuthorizeReplyChatAccessSuccess_thenReturn(isEligible: Boolean) {
        coEvery {
            authorizeChatReplyAccessUseCase.execute(any())
        } returns isEligible
    }

    private fun onAuthorizeSomDetailAccessFail_thenThrow(throwable: Throwable) {
        coEvery {
            authorizeSomDetailAccessUseCase.execute(any())
        } throws throwable
    }

    private fun onAuthorizeReplyChatAccessFail_thenThrow(throwable: Throwable) {
        coEvery {
            authorizeChatReplyAccessUseCase.execute(any())
        } throws throwable
    }

    private fun assertDetailChatEligibilityEquals(pairs: Pair<Boolean, Boolean>) {
        Assert.assertEquals(pairs, (viewModel.somDetailChatEligibility.value as? Success)?.data)
    }
}

