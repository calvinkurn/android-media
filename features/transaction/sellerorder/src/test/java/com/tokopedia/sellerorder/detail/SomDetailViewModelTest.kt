package com.tokopedia.sellerorder.detail

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.sellerorder.common.SomOrderBaseViewModelTest
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.SomGetOrderDetailUseCase
import com.tokopedia.sellerorder.detail.domain.SomReasonRejectUseCase
import com.tokopedia.sellerorder.detail.domain.SomSetDeliveredUseCase
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.domain.usecases.GetOrderExtensionRequestInfoUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.SendOrderExtensionRequestUseCase
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
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
    private val sampleInvalidComment = "@$%`~"
    private val sampleValidComment = "Ini adalah contoh valid comment"
    private val successOrderExtensionRequestResult = SendOrderExtensionRequestResponse.Data.OrderExtensionRequest("", 1)
    private val failedOrderExtensionRequestResult = SendOrderExtensionRequestResponse.Data.OrderExtensionRequest("", 0)
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

    @RelaxedMockK
    lateinit var somGetOrderExtensionRequestInfoUseCase: GetOrderExtensionRequestInfoUseCase

    @RelaxedMockK
    lateinit var somSendOrderRequestExtensionUseCase: SendOrderExtensionRequestUseCase

    @RelaxedMockK
    lateinit var somGetOrderExtensionRequestInfoMapper: GetOrderExtensionRequestInfoResponseMapper

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = SomDetailViewModel(
            somAcceptOrderUseCase,
            somRejectOrderUseCase,
            somEditRefNumUseCase,
            somRejectCancelOrderUseCase,
            somValidateOrderUseCase,
            userSessionInterface,
            dispatcher,
            somGetOrderDetailUseCase,
            somReasonRejectUseCase,
            somSetDeliveredUseCase,
            somGetOrderExtensionRequestInfoUseCase,
            somSendOrderRequestExtensionUseCase,
            somGetOrderExtensionRequestInfoMapper,
            OrderExtensionRequestResultResponseMapper(),
            authorizeSomDetailAccessUseCase,
            authorizeChatReplyAccessUseCase
        )

        val product1 = SomDetailOrder.Data.GetSomDetail.Products("123")
        listProducts = arrayListOf(product1).toMutableList()

        val reasonReject1 = SomReasonRejectData.Data.SomRejectReason(1)
        listReasonReject = arrayListOf(reasonReject1).toMutableList()
    }

    // order_detail
    @Test
    fun getOrderDetail_shouldCancelOldProcess() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } returns Success(GetSomDetailResponse(getSomDetail = SomDetailOrder.Data.GetSomDetail("123")))

        //when
        viewModel.loadDetailOrder("")
        viewModel.loadDetailOrder("")

        //then
        assert(viewModel.orderDetailResult.value is Success)
        assert((viewModel.orderDetailResult.value as Success<GetSomDetailResponse>).data.getSomDetail?.orderId == "123")
    }

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
    fun getOrderDetail_ifThrowableThrown_shouldFail() {
        //given
        coEvery {
            somGetOrderDetailUseCase.execute(any())
        } throws Throwable()

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
    fun getReasonReject_ifThrowableThrown_shouldReturnFail() {
        //given
        coEvery {
            somReasonRejectUseCase.execute(any(), any())
        } throws Throwable()

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
    fun setDelivered_ifThrowableThrown_shouldReturnFail() {
        //given
        coEvery {
            somSetDeliveredUseCase.execute(any(), any(), any())
        } throws Throwable()

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

    @Test
    fun getSomRequestExtensionInfo_shouldSuccess() {
        onGetOrderExtensionRequestInfoSuccess_thenReturn()

        viewModel.getSomRequestExtensionInfo(orderId)

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        }

        assert(viewModel.requestExtensionInfo.value is Success)
    }

    @Test
    fun getSomRequestExtensionInfo_shouldFail() {
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        } throws Throwable()

        viewModel.getSomRequestExtensionInfo(orderId)

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        }

        assert(viewModel.requestExtensionInfo.value is Fail)
    }

    @Test
    fun sendOrderExtensionRequest_ifOrderExtensionRequestInfoValid_shouldSuccess() {
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), createSampleOrderExtensionRequestInfo())
        coEvery {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        } returns successOrderExtensionRequestResult

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.sendOrderExtensionRequest(orderId)

        coVerify {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        }

        assert(viewModel.requestExtensionResult.value is Success)
    }

    @Test
    fun sendOrderExtensionRequest_ifSuccessWithFailMessageCode_shouldUpdateOrderExtensionRequestInfoProcessingStatusToFalse() {
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), createSampleOrderExtensionRequestInfo())
        coEvery {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        } returns failedOrderExtensionRequestResult

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.sendOrderExtensionRequest(orderId)

        coVerify {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        }

        assert(viewModel.requestExtensionResult.value is Success)
        assert(!(viewModel.requestExtensionInfo.value as Success).data.processing)
    }

    @Test
    fun sendOrderExtensionRequest_ifOrderExtensionRequestInfoInvalid_shouldNotSendRequest() {
        val invalidOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo().apply {
            (items[3] as OrderExtensionRequestInfoUiModel.CommentUiModel).apply {
                value = sampleInvalidComment
                validateComment()
            }
        }
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), invalidOrderExtensionRequestInfo)
        coEvery {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        } returns mockk()

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.sendOrderExtensionRequest(orderId)

        coVerify(inverse = true) {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        }

        assert(viewModel.requestExtensionResult.value == null)
    }

    @Test
    fun sendOrderExtensionRequest_ifOrderExtensionRequestInfoValid_shouldFailedAndUpdateOrderExtensionRequestInfoProcessingStatusToFalse() {
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), createSampleOrderExtensionRequestInfo())
        coEvery {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        } throws Throwable()

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.sendOrderExtensionRequest(orderId)

        coVerify {
            somSendOrderRequestExtensionUseCase.execute(any(), any(), any(), any(), any())
        }

        assert(viewModel.requestExtensionResult.value is Fail)
        assert(!(viewModel.requestExtensionInfo.value as Success).data.processing)
    }

    @Test
    fun updateOrderRequestExtensionInfoOnCommentChanged_shouldUpdateOrderRequestExtensionInfo() {
        val initialOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo()
        val expectedUpdatedOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo().apply {
            (items[3] as OrderExtensionRequestInfoUiModel.CommentUiModel).apply {
                value = sampleInvalidComment
                validateComment()
            }
        }
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), initialOrderExtensionRequestInfo)

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.updateOrderRequestExtensionInfoOnCommentChanged(
            (initialOrderExtensionRequestInfo.items[3] as OrderExtensionRequestInfoUiModel.CommentUiModel).apply {
                value = sampleInvalidComment
            }
        )

        assert((viewModel.requestExtensionInfo.value as Success).data == expectedUpdatedOrderExtensionRequestInfo)
    }

    @Test
    fun updateOrderRequestExtensionInfoOnCommentChanged_ifChangedCommentIsNull_shouldNotUpdateOrderRequestExtensionInfo() {
        val initialOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo()
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), initialOrderExtensionRequestInfo)

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.updateOrderRequestExtensionInfoOnCommentChanged(null)

        assert((viewModel.requestExtensionInfo.value as Success).data == initialOrderExtensionRequestInfo)
    }

    @Test
    fun updateOrderRequestExtensionInfoOnSelectedOptionChanged_shouldUpdateOrderRequestExtensionInfo() {
        val initialOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo()
        val expectedUpdatedOrderRequestExtensionInfo = createSampleOrderExtensionRequestInfo().apply {
            (items[1] as OrderExtensionRequestInfoUiModel.OptionUiModel).select()
            (items[2] as OrderExtensionRequestInfoUiModel.OptionUiModel).deselect()
            (items[3] as OrderExtensionRequestInfoUiModel.CommentUiModel).updateToHide()
        }
        val selectedOption = (initialOrderExtensionRequestInfo.items[1] as OrderExtensionRequestInfoUiModel.OptionUiModel).apply {
            select()
        }
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), initialOrderExtensionRequestInfo)

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.updateOrderRequestExtensionInfoOnSelectedOptionChanged(selectedOption)

        assert((viewModel.requestExtensionInfo.value as Success).data == expectedUpdatedOrderRequestExtensionInfo)
    }

    @Test
    fun updateOrderRequestExtensionInfoOnSelectedOptionChanged_ifSelectedOptionIsNull_shouldNotUpdateOrderRequestExtensionInfo() {
        val initialOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo()
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), initialOrderExtensionRequestInfo)

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.updateOrderRequestExtensionInfoOnSelectedOptionChanged(null)

        assert((viewModel.requestExtensionInfo.value as Success).data == initialOrderExtensionRequestInfo)
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

    private fun onGetOrderExtensionRequestInfoSuccess_thenReturn(
        orderRequestInfo: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo = mockk(),
        orderRequestInfoUiModel: OrderExtensionRequestInfoUiModel = mockk()
    ) {
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        } returns orderRequestInfo
        every {
            somGetOrderExtensionRequestInfoMapper.mapSuccessResponseToUiModel(any())
        } returns orderRequestInfoUiModel
    }

    private fun createSampleOrderExtensionRequestInfo(): OrderExtensionRequestInfoUiModel {
        return OrderExtensionRequestInfoUiModel(
            items = listOf(
                OrderExtensionRequestInfoUiModel.DescriptionUiModel(description = ""),
                OrderExtensionRequestInfoUiModel.OptionUiModel(code = 0, name = "", selected = false, mustComment = false),
                OrderExtensionRequestInfoUiModel.OptionUiModel(code = 1, name = "", selected = true, mustComment = true),
                OrderExtensionRequestInfoUiModel.CommentUiModel(
                    optionCode = 1, show = true, errorCheckers = listOf(
                        OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                            "^.{0,14}$",
                            "Minimal 15 karakter"
                        )
                    ), value = sampleValidComment
                )
            )
        )
    }
}

