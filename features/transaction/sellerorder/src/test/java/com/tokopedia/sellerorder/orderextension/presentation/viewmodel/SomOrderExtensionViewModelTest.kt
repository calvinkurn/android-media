package com.tokopedia.sellerorder.orderextension.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.domain.usecases.GetOrderExtensionRequestInfoUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.SendOrderExtensionRequestUseCase
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SomOrderExtensionViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var somGetOrderExtensionRequestInfoUseCase: GetOrderExtensionRequestInfoUseCase

    @RelaxedMockK
    lateinit var somSendOrderRequestExtensionUseCase: SendOrderExtensionRequestUseCase

    @RelaxedMockK
    lateinit var somGetOrderExtensionRequestInfoMapper: GetOrderExtensionRequestInfoResponseMapper

    private val dispatcher = CoroutineTestDispatchersProvider
    private val orderId = "1234567890"
    private val sampleInvalidComment = "@$%`~"
    private val sampleValidComment = "Ini adalah contoh valid comment"
    private val successOrderExtensionRequestResult = SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData("", 1)
    private val failedOrderExtensionRequestResult = SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData("", 0)

    private lateinit var viewModel: SomOrderExtensionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SomOrderExtensionViewModel(
            dispatcher,
            userSessionInterface,
            somGetOrderExtensionRequestInfoUseCase,
            somSendOrderRequestExtensionUseCase,
            somGetOrderExtensionRequestInfoMapper,
            OrderExtensionRequestResultResponseMapper()
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    @Test
    fun getSomRequestExtensionInfo_shouldSuccess() {
        onGetOrderExtensionRequestInfoSuccess_thenReturn()

        viewModel.getSomRequestExtensionInfo(orderId)

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any(), any())
        }

        assert(viewModel.requestExtensionInfo.value is Success)
    }

    @Test
    fun getSomRequestExtensionInfo_shouldFail() {
        onGetOrderExtensionRequestInfoFail()

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any(), any())
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
        val selectedOption = (initialOrderExtensionRequestInfo.items[1] as OrderExtensionRequestInfoUiModel.OptionUiModel)
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

    @Test
    fun requestDismissOrderExtensionRequestInfoBottomSheet_shouldUpdateProcessingToFalseAndCompletedToTrue() {
        val initialOrderExtensionRequestInfo = createSampleOrderExtensionRequestInfo()
        val expectedUpdatedOrderRequestExtensionInfo = createSampleOrderExtensionRequestInfo().apply {
            processing = false
            completed = true
        }
        onGetOrderExtensionRequestInfoSuccess_thenReturn(mockk(), initialOrderExtensionRequestInfo)

        viewModel.getSomRequestExtensionInfo(orderId)
        viewModel.requestDismissOrderExtensionRequestInfoBottomSheet()

        assert((viewModel.requestExtensionInfo.value as Success).data == expectedUpdatedOrderRequestExtensionInfo)
    }

    @Test
    fun onNeedToUpdateOrderExtensionRequestInfo_ifRequestExtensionInfoIsNull_shouldNotUpdateOrderRequestExtensionInfo() {
        val initialOrderExtensionRequestInfo = viewModel.requestExtensionInfo.value

        viewModel.updateOrderRequestExtensionInfoOnCommentChanged(mockk())

        assert(viewModel.requestExtensionInfo.value == initialOrderExtensionRequestInfo)
    }

    @Test
    fun onNeedToUpdateOrderExtensionRequestInfo_ifRequestExtensionInfoIsFail_shouldNotUpdateOrderRequestExtensionInfo() {
        onGetOrderExtensionRequestInfoFail()
        val initialOrderExtensionRequestInfo = viewModel.requestExtensionInfo.value

        viewModel.updateOrderRequestExtensionInfoOnCommentChanged(mockk())

        assert(viewModel.requestExtensionInfo.value == initialOrderExtensionRequestInfo)
    }

    private fun onGetOrderExtensionRequestInfoSuccess_thenReturn(
        orderRequestInfoData: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData = mockk(),
        orderRequestInfoUiModel: OrderExtensionRequestInfoUiModel = mockk()
    ) {
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any(), any())
        } returns orderRequestInfoData
        every {
            somGetOrderExtensionRequestInfoMapper.mapSuccessResponseToUiModel(any())
        } returns orderRequestInfoUiModel
    }

    private fun onGetOrderExtensionRequestInfoFail() {
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any(), any())
        } throws Throwable()

        viewModel.getSomRequestExtensionInfo(orderId)
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