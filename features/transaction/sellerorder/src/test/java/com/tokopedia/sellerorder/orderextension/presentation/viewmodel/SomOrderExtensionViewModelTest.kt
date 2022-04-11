package com.tokopedia.sellerorder.orderextension.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.domain.usecases.GetOrderExtensionRequestInfoUseCase
import com.tokopedia.sellerorder.orderextension.domain.usecases.SendOrderExtensionRequestUseCase
import com.tokopedia.sellerorder.orderextension.presentation.mapper.GetOrderExtensionRequestInfoResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.mapper.OrderExtensionRequestResultResponseMapper
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.model.StringComposer
import com.tokopedia.sellerorder.orderextension.presentation.util.ResourceProvider
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SomOrderExtensionViewModelTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/som_get_order_extension_request_info_success_response.json"

        private const val REGEX_COMMENT_NOT_EMPTY = "^\\s*\$"
        private const val REGEX_COMMENT_CANNOT_LESS_THAN = "^.{0,14}\$"
        private const val REGEX_COMMENT_ALLOWED_CHARACTERS = "[^,.?!\\n A-Za-z0-9]+"

        private const val ERROR_MESSAGE_COMMENT_CANNOT_EMPTY = "Isi alasan terlebih dulu, ya."
        private const val ERROR_MESSAGE_COMMENT_CANNOT_LESS_THAN = "Minimal 15 karakter"
        private const val ERROR_MESSAGE_COMMENT_CANNOT_CONTAINS_ILLEGAL_CHAR = "Hindari karakter spesial (@#\$%^*)."

        private const val ORDER_REQUEST_EXTENSION_INFO_TITLE = "Yakin mau perpanjang waktu proses pesanan ini?"
        private const val ORDER_REQUEST_EXTENSION_INFO_REASON_TITLE = "Pilih alasan perpanjangan"
        private const val ORDER_REQUEST_EXTENSION_INFO_FOOTER = "*Pesanan batal otomatis jika pengajuan ditolak pembeli. Tokomu berpotensi kena penalti jika keseringan perpanjang. Selengkapnya"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var somGetOrderExtensionRequestInfoUseCase: GetOrderExtensionRequestInfoUseCase

    @RelaxedMockK
    lateinit var somSendOrderExtensionRequestUseCase: SendOrderExtensionRequestUseCase

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    private val orderId = "1234567890"
    private val sampleInvalidComment = "@$%`~"
    private val sampleValidComment = "Ini adalah contoh valid comment"
    private val sampleErrorMessage = "Error!"
    private val successOrderExtensionRequestResult =
        SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData(
            "",
            1
        )
    private val failedOrderExtensionRequestResult =
        SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData(
            sampleErrorMessage,
            0
        )

    private lateinit var viewModel: SomOrderExtensionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(TrackApp::class)
        every { TrackApp.getInstance() } returns mockk(relaxed = true)
        viewModel = SomOrderExtensionViewModel(
            coroutineTestRule.dispatchers,
            userSessionInterface,
            somGetOrderExtensionRequestInfoUseCase,
            somSendOrderExtensionRequestUseCase,
            GetOrderExtensionRequestInfoResponseMapper(resourceProvider),
            OrderExtensionRequestResultResponseMapper()
        )
        every {
            resourceProvider.getOrderExtensionRequestCommentNotEmptyRegex()
        } returns REGEX_COMMENT_NOT_EMPTY
        every {
            resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotEmpty()
        } returns StringComposer { ERROR_MESSAGE_COMMENT_CANNOT_EMPTY }
        every {
            resourceProvider.getOrderExtensionRequestCommentCannotLessThanRegex()
        } returns REGEX_COMMENT_CANNOT_LESS_THAN
        every {
            resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotLessThan()
        } returns StringComposer { ERROR_MESSAGE_COMMENT_CANNOT_LESS_THAN }
        every {
            resourceProvider.getOrderExtensionRequestCommentAllowedCharactersRegex()
        } returns REGEX_COMMENT_ALLOWED_CHARACTERS
        every {
            resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters()
        } returns StringComposer { ERROR_MESSAGE_COMMENT_CANNOT_CONTAINS_ILLEGAL_CHAR }
        every {
            resourceProvider.getOrderExtensionRequestBottomSheetTitleComposer()
        } returns StringComposer { ORDER_REQUEST_EXTENSION_INFO_TITLE }
        every {
            resourceProvider.getOrderExtensionRequestBottomSheetOptionsTitleComposer()
        } returns StringComposer { ORDER_REQUEST_EXTENSION_INFO_REASON_TITLE }
        every {
            resourceProvider.getOrderExtensionRequestBottomSheetFooterComposer()
        } returns StringComposer { ORDER_REQUEST_EXTENSION_INFO_FOOTER }
        every {
            resourceProvider.getOrderExtensionDescriptionComposer(any(), any())
        } answers {
            StringComposer { "${firstArg<String?>().orEmpty()} ${secondArg<String?>().orEmpty()}." }
        }
    }

    @After
    fun finish() {
        unmockkAll()
    }

    @Test
    fun getSomOrderExtensionRequestInfoLoadingState_shouldReturnLoadingState() =
        coroutineTestRule.runBlockingTest {
            viewModel.getSomOrderExtensionRequestInfoLoadingState()

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun getSomOrderExtensionRequestInfo_shouldSuccess() = coroutineTestRule.runBlockingTest {
        getSomOrderExtensionRequestInfoLoadingState_shouldReturnLoadingState()
        onGetOrderExtensionRequestInfoSuccess_thenReturn()

        viewModel.getSomOrderExtensionRequestInfo(orderId)

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        }

        assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
        assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
        assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
        assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
    }

    @Test
    fun getSomOrderExtensionRequestInfo_shouldFail() = coroutineTestRule.runBlockingTest {
        getSomOrderExtensionRequestInfoLoadingState_shouldReturnLoadingState()
        onGetOrderExtensionRequestInfoFail()

        coVerify {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        }

        assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.success)
        assertTrue(viewModel.orderExtensionRequestInfo.value!!.completed)
        assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
        assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
        assertTrue(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
        assertNotNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
    }

    @Test
    fun updateOrderExtensionRequestInfoOnSelectedOptionChanged_shouldUpdateOrderExtensionRequestInfo() =
        coroutineTestRule.runBlockingTest {
            getSomOrderExtensionRequestInfo_shouldSuccess()
            val previousOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!
            val previousSelectedOption = previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.selected
                }
            val optionToChoose = previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.mustComment
                }

            viewModel.updateOrderExtensionRequestInfoOnSelectedOptionChanged(optionToChoose)

            val currentOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!
            val currentSelectedOption = currentOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.selected
                }
            val commentUiModel = currentOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.CommentUiModel>().first {
                    it.optionCode == currentSelectedOption.code
                }
            assertNotEquals(previousOrderExtensionRequestInfo, currentOrderExtensionRequestInfo)
            assertNotEquals(previousSelectedOption.code, currentSelectedOption.code)
            assertEquals(optionToChoose.code, currentSelectedOption.code)
            assertEquals(currentSelectedOption.code, commentUiModel.optionCode)
            assertNotNull(commentUiModel)

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun updateOrderExtensionRequestInfoOnSelectedOptionChanged_ifSelectedOptionIsNull_shouldNotUpdateOrderExtensionRequestInfo() =
        coroutineTestRule.runBlockingTest {
            getSomOrderExtensionRequestInfo_shouldSuccess()
            viewModel.orderExtensionRequestInfo.value!!.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.selected
                }.run { selected = false }

            viewModel.updateOrderExtensionRequestInfoOnSelectedOptionChanged(null)

            val noSelectedOption = viewModel.orderExtensionRequestInfo.value!!.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>()
                .all { !it.selected }
            assertTrue(noSelectedOption)

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun givenValidComment_updateOrderExtensionRequestInfoOnCommentChanged_shouldUpdateOrderExtensionRequestInfoToValid() =
        coroutineTestRule.runBlockingTest {
            updateOrderExtensionRequestInfoOnSelectedOptionChanged_shouldUpdateOrderExtensionRequestInfo() // select an option that need a comment
            val previousOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!
            val previousSelectedOption = previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.selected
                }
            val previousCommentUiModel = previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.CommentUiModel>().first {
                    it.optionCode == previousSelectedOption.code
                }.apply { value = sampleValidComment }

            viewModel.updateOrderExtensionRequestInfoOnCommentChanged(previousCommentUiModel)

            val currentOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!

            assertTrue(currentOrderExtensionRequestInfo.isValid())

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun updateOrderExtensionRequestInfoOnCommentChanged_ifChangedCommentIsNull_shouldNotUpdateOrderExtensionRequestInfo() =
        coroutineTestRule.runBlockingTest {
            givenValidComment_updateOrderExtensionRequestInfoOnCommentChanged_shouldUpdateOrderExtensionRequestInfoToValid()
            val previousOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!
            val previousSelectedOption = previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.OptionUiModel>().first {
                    it.selected
                }
            // make orderExtensionRequestInfo invalid forcefully
            previousOrderExtensionRequestInfo.items
                .filterIsInstance<OrderExtensionRequestInfoUiModel.CommentUiModel>().first {
                    it.optionCode == previousSelectedOption.code
                }.run {
                    value = sampleInvalidComment
                    validateComment()
                }

            // try to update orderExtensionRequestInfo but somehow the comment ui model given by view holder is null
            viewModel.updateOrderExtensionRequestInfoOnCommentChanged(null)

            val currentOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value!!

            // therefore orderExtensionRequestInfo remains invalid
            assertFalse(currentOrderExtensionRequestInfo.isValid())

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun sendOrderExtensionRequest_ifOrderExtensionRequestInfoValid_shouldUpdateOrderExtensionRequestInfoToCompleteSuccess() =
        coroutineTestRule.runBlockingTest {
            givenValidComment_updateOrderExtensionRequestInfoOnCommentChanged_shouldUpdateOrderExtensionRequestInfoToValid()

            coEvery {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            } returns successOrderExtensionRequestResult

            viewModel.sendOrderExtensionRequest(orderId)

            coVerify {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            }

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun sendOrderExtensionRequest_ifSuccessWithFailMessageCode_shouldUpdateOrderExtensionRequestInfoProcessingStatusToFalse() =
        coroutineTestRule.runBlockingTest {
            givenValidComment_updateOrderExtensionRequestInfoOnCommentChanged_shouldUpdateOrderExtensionRequestInfoToValid()

            coEvery {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            } returns failedOrderExtensionRequestResult

            viewModel.sendOrderExtensionRequest(orderId)

            coVerify {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            }

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun sendOrderExtensionRequest_ifOrderExtensionRequestInfoInvalid_shouldNotSendRequest() =
        coroutineTestRule.runBlockingTest {
            updateOrderExtensionRequestInfoOnCommentChanged_ifChangedCommentIsNull_shouldNotUpdateOrderExtensionRequestInfo()

            viewModel.sendOrderExtensionRequest(orderId)

            coVerify(inverse = true) {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            }

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun sendOrderExtensionRequest_ifSomSendOrderExtensionRequestUseCaseThrows_shouldFailed() =
        coroutineTestRule.runBlockingTest {
            givenValidComment_updateOrderExtensionRequestInfoOnCommentChanged_shouldUpdateOrderExtensionRequestInfoToValid()

            coEvery {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            } throws Throwable()

            viewModel.sendOrderExtensionRequest(orderId)

            coVerify {
                somSendOrderExtensionRequestUseCase.execute(any(), any(), any(), any())
            }

            assertTrue(viewModel.orderExtensionRequestInfo.value!!.items.isNotEmpty())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.success)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNotNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    @Test
    fun onNeedToUpdateOrderExtensionRequestInfo_ifOrderExtensionRequestInfoIsNull_shouldNotUpdateOrderExtensionRequestInfo() =
        coroutineTestRule.runBlockingTest {
            val initialOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value

            viewModel.updateOrderExtensionRequestInfoOnCommentChanged(mockk())

            assert(viewModel.orderExtensionRequestInfo.value == initialOrderExtensionRequestInfo)
        }

    @Test
    fun onNeedToUpdateOrderExtensionRequestInfo_ifOrderExtensionRequestInfoIsFail_shouldNotUpdateOrderExtensionRequestInfo() =
        coroutineTestRule.runBlockingTest {
            onGetOrderExtensionRequestInfoFail()
            val initialOrderExtensionRequestInfo = viewModel.orderExtensionRequestInfo.value

            viewModel.updateOrderExtensionRequestInfoOnCommentChanged(mockk())

            assert(viewModel.orderExtensionRequestInfo.value == initialOrderExtensionRequestInfo)
        }

    @Test
    fun requestDismissOrderExtensionRequestInfoBottomSheet_shouldUpdateOrderExtensionRequestToCompleted() =
        coroutineTestRule.runBlockingTest {
            getSomOrderExtensionRequestInfo_shouldSuccess()

            viewModel.requestDismissOrderExtensionRequestInfoBottomSheet()

            assertFalse(viewModel.orderExtensionRequestInfo.value!!.processing)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.success)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.completed)
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.refreshOnDismiss)
            assertTrue(viewModel.orderExtensionRequestInfo.value!!.message.isBlank())
            assertFalse(viewModel.orderExtensionRequestInfo.value!!.isLoadingOrderExtensionRequestInfo())
            assertNull(viewModel.orderExtensionRequestInfo.value!!.throwable)
        }

    private fun onGetOrderExtensionRequestInfoSuccess_thenReturn() {
        val response = TestHelper.createSuccessResponse<GetOrderExtensionRequestInfoResponse.Data>(SUCCESS_RESPONSE)
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        } returns response.orderExtensionRequestInfo.data
    }

    private fun onGetOrderExtensionRequestInfoFail() {
        coEvery {
            somGetOrderExtensionRequestInfoUseCase.execute(any(), any())
        } throws MessageErrorException(sampleErrorMessage)

        viewModel.getSomOrderExtensionRequestInfo(orderId)
    }
}