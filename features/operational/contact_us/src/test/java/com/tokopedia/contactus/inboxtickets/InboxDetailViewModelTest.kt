package com.tokopedia.contactus.inboxtickets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.contactus.inboxtickets.data.model.*
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig.ChipUploadHostConfig.ChipUploadHostConfigData
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig.ChipUploadHostConfig.ChipUploadHostConfigData.GeneratedHost
import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.domain.CreatedBy
import com.tokopedia.contactus.inboxtickets.domain.StepTwoResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.*
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.InboxDetailUiEffect
import com.tokopedia.contactus.inboxtickets.view.utils.Utils
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InboxDetailViewModelTest {

    companion object {
        const val SUCCESS = 1
        const val FAILED = 0
    }

    @RelaxedMockK
    lateinit var postMessageUseCase: PostMessageUseCase

    @RelaxedMockK
    lateinit var postMessageUseCase2: PostMessageUseCase2

    @RelaxedMockK
    lateinit var inboxOptionUseCase: InboxDetailUseCase

    @RelaxedMockK
    lateinit var submitRatingUseCase: SubmitRatingUseCase

    @RelaxedMockK
    lateinit var closeTicketByUserUseCase: CloseTicketByUserUseCase

    @RelaxedMockK
    lateinit var getFileUseCase: GetFileUseCase

    @RelaxedMockK
    lateinit var chipUploadHostConfigUseCase: ChipUploadHostConfigUseCase

    @RelaxedMockK
    lateinit var secureUploadUseCase: SecureUploadUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val taskRule = CoroutineTestRule()

    private lateinit var viewModel: InboxDetailViewModel

    private lateinit var utlis: Utils

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = InboxDetailViewModel(
            postMessageUseCase,
            postMessageUseCase2,
            inboxOptionUseCase,
            submitRatingUseCase,
            closeTicketByUserUseCase,
            getFileUseCase,
            chipUploadHostConfigUseCase,
            secureUploadUseCase,
            userSession,
            CoroutineTestDispatchersProvider
        )

        mockkStatic(Utils::class)
        utlis = Utils()
    }

    @Test
    fun `get Ticket Detail`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            statusTickets = "open"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail but failed because server`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns response
            viewModel.ticketId("1234")
            coEvery { response.isSuccess() } returns 0
            coEvery { response.getErrorListMessage() } returns arrayListOf("error")

            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            val actualEvent = emittedValues.last()
            val isGetDetailTicketFailed =
                actualEvent is InboxDetailUiEffect.GetDetailInboxDetailFailed
            assertEquals(true, isGetDetailTicketFailed)
            job.cancel()
        }
    }

    @Test
    fun `get Ticket Detail current situation`() {
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            isEmpty = true,
            statusTickets = "open",
            isAllowCloseTicket = true,
            isNeedAttachmentTicket = true,
            rating = "good"
        )
        viewModel.ticketId("1234")
        val actualIsAllowToClose = viewModel.isTicketAllowClose()
        assertEquals(true, actualIsAllowToClose)
        val statusTicket = viewModel.getTicketStatus()
        assertEquals("open", statusTicket)
        val commentIsEmpty = viewModel.isCommentEmpty()
        assertEquals(true, commentIsEmpty)
    }

    @Test
    fun `check Ticket Detail situation`() {
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            isEmpty = false,
            statusTickets = "open",
            isAllowCloseTicket = true,
            isNeedAttachmentTicket = true,
            rating = "good"
        )
        viewModel.ticketId("1234")
        val isNeedAttachmentTicket = viewModel.isNeedAttachment()
        assertEquals(true, isNeedAttachmentTicket)
        val actualRating = viewModel.getTicketRating()
        assertEquals("good", actualRating)
        val commentItemOnPosition = viewModel.getCommentOnPosition(2)
        assertEquals("message-1", commentItemOnPosition.message)
        val lastCommentId = viewModel.getFirstCommentId()
        assertEquals("6", lastCommentId)
        viewModel.setLastCommentAsOfficialStore(true)
        viewModel.setLastCommentAsClosed()
        val lastCommentsItem = viewModel.getCommentOnPosition(0)
        assertEquals(true, lastCommentsItem.priorityLabel)
        assertEquals("closed", lastCommentsItem.ticketStatus)
        coEvery { userSession.userId } returns "1"
        val userId = viewModel.getUserId()
        assertEquals("1", userId)
    }

    @Test
    fun `get Ticket Detail and get CSATBadReasonList`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            numberTicket = "dsa"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
        val actualList = viewModel.getCSATBadReasonList()
        assertEquals(csatTarget.size, actualList.size)
        val actualListCsatAsString = viewModel.getReasonListAsString(arrayListOf("1", "2", "3"))
        assertEquals("message-1", actualListCsatAsString.get(0))
        val actualGetShortRating = viewModel.isShowRating()
        assertEquals(false, actualGetShortRating)
        val actualNumber = viewModel.getTicketNumber()
        assertEquals("dsa", actualNumber)
        val actualIsAllowToClose = viewModel.isTicketAllowClose()
        assertEquals(false, actualIsAllowToClose)
        val commentIsEmpty = viewModel.isCommentEmpty()
        assertEquals(false, commentIsEmpty)
        val isNeedAttachmentTicket = viewModel.isNeedAttachment()
        assertEquals(false, isNeedAttachmentTicket)
    }

    @Test
    fun `get Ticket Detail status SOLVED`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            statusTickets = "solved"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail status OPEN`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            statusTickets = "open"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail status NEW`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            statusTickets = "new"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail status CLOSED and is Need Showing Rating`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            showRating = true,
            statusTickets = "closed"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail status CLOSED and is Not Need Showing Rating`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            showRating = false,
            statusTickets = "closed"
        )
        viewModel.ticketId("1234")
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail status Need Showing Rating`() {
        val csatTarget = createBadCsatReasonListItem()
        val ticketsTarget = createTicketDetail().getDataTicket()
        coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
            showRating = true
        )
        viewModel.ticketId("1234")
        val actualGetShortRating = viewModel.isShowRating()
        assertEquals(true, actualGetShortRating)
        val actualResult = viewModel.uiState.value
        assertEquals(ticketsTarget.id, actualResult.ticketDetail.id)
        assertEquals(csatTarget.size, actualResult.csatReasonListBadReview.size)
    }

    @Test
    fun `get Ticket Detail Failed`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                FAILED
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            val actualEvent = emittedValues.last()
            val isGetDetailTicketFailed =
                actualEvent is InboxDetailUiEffect.GetDetailInboxDetailFailed
            assertEquals(true, isGetDetailTicketFailed)
            job.cancel()
        }
    }

    @Test
    fun `get Ticket Detail Failed coroutine`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } throws Error()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            val actualEvent = emittedValues.last()
            val isGetDetailTicketFailed =
                actualEvent is InboxDetailUiEffect.GetDetailInboxDetailFailed
            assertEquals(true, isGetDetailTicketFailed)
            val actualEventAsEffect = actualEvent as InboxDetailUiEffect.GetDetailInboxDetailFailed
            assertNotNull(actualEventAsEffect.throwable)
            job.cancel()
        }
    }

    @Test
    fun `send text message with Image but serverId is 0 or failed to get `() {
        runBlockingTest {
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf()
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `send text message with Image but failed because coroutine`() {
        runBlockingTest {
            coEvery { getFileUseCase.getFilePath(any()) } throws Exception()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                showRating = true
            )
            viewModel.ticketId("1234")
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `send text with Image but serverId is 1, then failed send it because get file process`() {
        runBlockingTest {
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf()
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                false,
                "1"
            )

            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `refresh data`() {
        viewModel.ticketId("123")
        viewModel.refreshLayout()
    }

    @Test
    fun `sendMessage without Image but failed and message exist`() {
        runBlockingTest {
            val successData: TicketReplyResponse = mockk(relaxed = true)
            val graphResponseSend: TicketReplyResponse = mockk(relaxed = true)
            coEvery { postMessageUseCase(any()) } returns graphResponseSend
            coEvery { successData.getTicketReplay().getTicketReplayData().status } returns "FAILED"
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                isEmpty = true,
                statusTickets = "open",
                isAllowCloseTicket = true,
                isNeedAttachmentTicket = true,
                rating = "good"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `sendMessage without Image but failed and message empty`() {
        runBlockingTest {
            val successData: TicketReplyResponse = mockk(relaxed = true)
            val graphResponseSend: TicketReplyResponse = mockk(relaxed = true)
            val error: List<GraphqlError> = arrayListOf()
            coEvery { postMessageUseCase(any()) } returns graphResponseSend
            coEvery { successData.getTicketReplay().getTicketReplayData().status } returns "FAILED"
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                isEmpty = true,
                statusTickets = "open",
                isAllowCloseTicket = true,
                isNeedAttachmentTicket = true,
                rating = "good"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `sendMessage without Image but failed and message is null`() {
        runBlockingTest {
            val successData: TicketReplyResponse = mockk(relaxed = true)
            val graphResponseSend: TicketReplyResponse = mockk(relaxed = true)
            coEvery { postMessageUseCase(any()) } returns graphResponseSend
            coEvery { successData.getTicketReplay().getTicketReplayData().status } returns "FAILED"
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                isEmpty = true,
                statusTickets = "open",
                isAllowCloseTicket = true,
                isNeedAttachmentTicket = true,
                rating = "good"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `sendMessage without Image but failed and message is null when all message from customer`() {
        runBlockingTest {
            val successData: TicketReplyResponse = mockk(relaxed = true)
            val graphResponseSend: TicketReplyResponse = mockk(relaxed = true)
            coEvery { postMessageUseCase(any()) } returns graphResponseSend
            coEvery { successData.getTicketReplay().getTicketReplayData().status } returns "FAILED"
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                isEmpty = false,
                statusTickets = "open",
                isAllowCloseTicket = true,
                isNeedAttachmentTicket = true,
                rating = "good",
                isAllCustomer = true
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `sendMessage without Image but failed by coroutine`() {
        runBlockingTest {
            coEvery { postMessageUseCase(any()) } throws Exception()

            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isErrorSendMessageType = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isErrorSendMessageType)
            job.cancel()
        }
    }

    @Test
    fun `sendMessage without Image but failed and message is success`() {
        runBlockingTest {
            val successData: TicketReplyResponse = mockk(relaxed = true)
            coEvery { postMessageUseCase(any()) } returns successData
            coEvery { successData.getTicketReplay().getTicketReplayData().status } returns "OK"
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                isEmpty = false,
                statusTickets = "open",
                isAllowCloseTicket = true,
                isNeedAttachmentTicket = true,
                rating = "good"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendMessage(0, arrayListOf(), "asdad")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeSuccess = actualEvent is InboxDetailUiEffect.SendTextMessageSuccess
            assertEquals(true, isSendMessageTypeSuccess)
            job.cancel()
        }
    }

    @Test
    fun `check when sendMessageIsInvalid`() {
        viewModel.sendMessage(-1, arrayListOf(), "aas")
        val isInvalid = viewModel.isImageInvalid.value
        assertEquals(true, isInvalid)
    }

    @Test
    fun `check when hit closeTicket but error`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = false)
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { closeTicketByUserUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf("Error")
            coEvery { response.getErrorMessage() } returns "Error"
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.closeTicket()
            val actualEvent = emittedValues.last()
            val isOnCloseTicketFailed = actualEvent is InboxDetailUiEffect.OnCloseInboxDetailFailed
            assertEquals(true, isOnCloseTicketFailed)
            job.cancel()
        }
    }

    @Test
    fun `check when hit closeTicket but error on coroutine`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { closeTicketByUserUseCase(any()) } throws Exception()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.closeTicket()
            val actualEvent = emittedValues.last()
            val isOnCloseTicketFailed = actualEvent is InboxDetailUiEffect.OnCloseInboxDetailFailed
            assertEquals(true, isOnCloseTicketFailed)
            job.cancel()
        }
    }

    @Test
    fun `check when hit closeTicket and success`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = false)
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { closeTicketByUserUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.closeTicket()
            val actualEvent = emittedValues.last()
            val isOnCloseTicketSuccess =
                actualEvent is InboxDetailUiEffect.OnCloseInboxDetailSuccess
            assertEquals(true, isOnCloseTicketSuccess)
            job.cancel()
        }
    }

    @Test
    fun `check sendRating when agree and hit is success`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendRating(true, 2, "1")
            val actualEvent = emittedValues.last()
            val isSendRatingSuccess = actualEvent is InboxDetailUiEffect.OnSendRatingSuccess
            assertEquals(true, isSendRatingSuccess)
            job.cancel()
        }
    }

    @Test
    fun `check sendRating when not agree and hit is success`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)

            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendRating(false, 2, "1")
            val actualEvent = emittedValues.last()
            val isSendRatingSuccess = actualEvent is InboxDetailUiEffect.OnSendRatingSuccess
            assertEquals(true, isSendRatingSuccess)
            job.cancel()
        }
    }

    @Test
    fun `check sendRating when not agree and hit is failed`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf("Error")
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendRating(false, 2, "1")
            val actualEvent = emittedValues.last()
            val isSendRatingFailed = actualEvent is InboxDetailUiEffect.OnSendRatingFailed
            assertEquals(true, isSendRatingFailed)
            job.cancel()
        }
    }

    @Test
    fun `check sendRating when not agree and hit is failed because coroutine`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()) } throws Exception()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.sendRating(false, 2, "1")
            val actualEvent = emittedValues.last()
            val isSendRatingFailed = actualEvent is InboxDetailUiEffect.OnSendRatingFailed
            assertEquals(true, isSendRatingFailed)
            job.cancel()
        }
    }

    @Test
    fun `submit csat ratting is success`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)

            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.submitCsatRating("adsas", 4)

            val actualEvent = emittedValues.last()
            val isSendRatingSuccess = actualEvent is InboxDetailUiEffect.SendCSATRatingSuccess
            assertEquals(true, isSendRatingSuccess)

            job.cancel()
        }
    }

    @Test
    fun `submit csat ratting is failed`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)

            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()).getInboxDetail() } returns response
            coEvery { response.getErrorListMessage() } returns arrayListOf("Error")
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.submitCsatRating("adsas", 4)

            val actualEvent = emittedValues.last()
            val isSendRatingError = actualEvent is InboxDetailUiEffect.SendCSATRatingFailed
            assertEquals(true, isSendRatingError)

            job.cancel()
        }
    }

    @Test
    fun `submit csat ratting is failed because coroutine`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()) } throws Exception()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.submitCsatRating("adsas", 4)

            val actualEvent = emittedValues.last()
            val isSendRatingError = actualEvent is InboxDetailUiEffect.SendCSATRatingFailed
            assertEquals(true, isSendRatingError)

            job.cancel()
        }
    }

    @Test
    fun `search word on chat but it's empty`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("")
            val isEmpty = viewModel.searchIndices.size == 0
            assertEquals(true, isEmpty)

            job.cancel()
        }
    }

    @Test
    fun `search word on chat`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")

            val actualEvent = emittedValues.last()
            val isOnSearchTicketKeyword =
                actualEvent is InboxDetailUiEffect.OnSearchInboxDetailKeyword
            assertEquals(true, isOnSearchTicketKeyword)

            val indexSize = viewModel.searchIndices
            assertEquals(arrayListOf(2, 3, 5, 6, 7), indexSize)
            job.cancel()
        }
    }

    @Test
    fun `search word on chat on next but it is out of bond`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", -2)
            viewModel.getNextResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(1, actualResult)
        }
    }

    @Test
    fun `search word on chat on next but it is out of bond 2`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", 6)
            viewModel.getNextResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(1, actualResult)
        }
    }

    @Test
    fun `search word on chat on next but not any found text`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("dasdasdasda")
            viewModel.getNextResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(0, actualResult)
        }
    }

    @Test
    fun `search word on chat on next and find`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", 1)
            viewModel.getNextResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(2, actualResult)
        }
    }

    @Test
    fun `search word on chat on prev is out of bond`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", -1)
            viewModel.getPreviousResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(1, actualResult)
        }
    }

    @Test
    fun `search word on chat on prev is out of bond2`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", 6)
            viewModel.getPreviousResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(1, actualResult)
        }
    }

    @Test
    fun `search word on chat on prev but none`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("dsadasdasdas")
            viewModel.getPreviousResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(0, actualResult)
        }
    }

    @Test
    fun `search word on chat on prev and find`() {
        runBlockingTest {
            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )

            viewModel.ticketId("1234")
            viewModel.onSearchSubmitted("message")
            viewModel.setPrivateProperty("next", 1)
            viewModel.getPreviousResult()

            val actualResult = viewModel.onFindKeywordAtTicket.value?.status.orZero()
            assertEquals(2, actualResult)
        }
    }

    @Test
    fun `refresh data after closing ticket`() {
        runBlockingTest {
            val response: ChipGetInboxDetail = mockk(relaxed = true)
            val responseRating: ChipInboxDetails = mockk(relaxed = true)

            coEvery { inboxOptionUseCase(any()).getInboxDetail() } returns createTicketDetail(
                statusTickets = "open"
            )
            coEvery { submitRatingUseCase(any()) } returns responseRating
            coEvery { response.getErrorListMessage() } returns arrayListOf()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.ticketId("1234")
            viewModel.submitCsatRating("adsas", 4)
            viewModel.refreshLayout()
            val actualEvent = emittedValues.last()
            val isSendRatingSuccess = actualEvent is InboxDetailUiEffect.SendCSATRatingSuccess
            assertEquals(true, isSendRatingSuccess)
            val state = viewModel.uiState.value
            assertEquals(false, state.isIssueClose)
            job.cancel()
        }
    }

    @Test
    fun `get short time for empty input`() {
        val method =
            viewModel.javaClass.getDeclaredMethod("getShortTime", java.lang.String::class.java)

        method.isAccessible = true
        val result = method.invoke(viewModel, "") as String
        assertEquals("", result)
    }

    @Test
    fun `replay message with Image and it's success`(){
        runBlockingTest {
            val replayMessageResponse: TicketReplyResponse = mockk(relaxed = true)
            val sendingUrlAttachmentOfReplayMessage: StepTwoResponse = mockk(relaxed = true)
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns "dasdasdasdsad"
            coEvery { postMessageUseCase(any()) } returns replayMessageResponse
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().status } returns "OK"
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().postKey } returns "daspdopasdoasd"
            coEvery { postMessageUseCase2(any()) } returns sendingUrlAttachmentOfReplayMessage
            coEvery { sendingUrlAttachmentOfReplayMessage.getTicketAttach().getAttachment().isSuccess } returns 1
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeSuccess = actualEvent is InboxDetailUiEffect.SendTextMessageSuccess
            assertEquals(true, isSendMessageTypeSuccess)
            job.cancel()
        }
    }

    @Test
    fun `replay message with image but upload image is failed`(){
        runBlockingTest {
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns ""
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeFailed = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isSendMessageTypeFailed)
            job.cancel()
        }
    }

    @Test
    fun `replay message with image but upload image is failed because failed to send message`(){
        runBlockingTest {
            val replayMessageResponse: TicketReplyResponse = mockk(relaxed = true)
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns "dasdadas"
            coEvery { postMessageUseCase(any()) } returns replayMessageResponse
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().status } returns "FAILED"
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeFailed = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isSendMessageTypeFailed)
            job.cancel()
        }
    }

    @Test
    fun `replay message with Image but it's failed because send message return empty postkey`(){
        runBlockingTest {
            val replayMessageResponse: TicketReplyResponse = mockk(relaxed = true)
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns "dasdasdasdsad"
            coEvery { postMessageUseCase(any()) } returns replayMessageResponse
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().status } returns "OK"
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().postKey } returns ""
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeFailed = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isSendMessageTypeFailed)
            job.cancel()
        }
    }

    @Test
    fun `replay message with Image but it's failed because failed to send attachment url`(){
        runBlockingTest {
            val replayMessageResponse: TicketReplyResponse = mockk(relaxed = true)
            val sendingUrlAttachmentOfReplayMessage: StepTwoResponse = mockk(relaxed = true)
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns "dasdasdasdsad"
            coEvery { postMessageUseCase(any()) } returns replayMessageResponse
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().status } returns "OK"
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().postKey } returns "daspdopasdoasd"
            coEvery { postMessageUseCase2(any()) } returns sendingUrlAttachmentOfReplayMessage
            coEvery { sendingUrlAttachmentOfReplayMessage.getTicketAttach().getAttachment().isSuccess } returns 0
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeFailed = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isSendMessageTypeFailed)
            job.cancel()
        }
    }

    @Test
    fun `replay message with Image but it's failed because send attachment url is down`(){
        runBlockingTest {
            val replayMessageResponse: TicketReplyResponse = mockk(relaxed = true)
            val sendingUrlAttachmentOfReplayMessage: StepTwoResponse = mockk(relaxed = true)
            coEvery { getFileUseCase.getFilePath(any()) } returns arrayListOf("dsadsa")
            coEvery { chipUploadHostConfigUseCase(Unit) } returns createConfigServerUploadResponse(
                isError = false, serverId = "1"
            )
            coEvery { secureUploadUseCase.uploadSecureImage(any(), any(), any()) } returns "dasdasdasdsad"
            coEvery { postMessageUseCase(any()) } returns replayMessageResponse
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().status } returns "OK"
            coEvery { replayMessageResponse.getTicketReplay().getTicketReplayData().postKey } returns "daspdopasdoasd"
            coEvery { postMessageUseCase2(any()) } throws Exception()
            val emittedValues = arrayListOf<InboxDetailUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.sendMessage(1, arrayListOf(ImageUpload()), "aa")
            val actualEvent = emittedValues.last()
            val isSendMessageTypeFailed = actualEvent is InboxDetailUiEffect.SendTextMessageFailed
            assertEquals(true, isSendMessageTypeFailed)
            job.cancel()
        }
    }

    private fun createTicketDetail(
        isSuccess: Int = SUCCESS,
        statusTickets: String = "",
        showRating: Boolean = false,
        idTicket: String = "",
        messageTicket: String = "",
        numberTicket: String = "",
        isEmpty: Boolean = false,
        isAllowCloseTicket: Boolean = false,
        isNeedAttachmentTicket: Boolean = false,
        rating: String = "",
        isAllCustomer: Boolean = false
    ): ChipGetInboxDetail {
        val response = ChipGetInboxDetail(
            Data(
                isSuccess = isSuccess,
                tickets = Tickets().apply {
                    badCsatReasonList = createBadCsatReasonListItem()
                    isShowRating = showRating
                    id = idTicket
                    message = messageTicket
                    status = statusTickets
                    number = numberTicket
                    comments =
                        if (isEmpty) null else createCommentsItems(rating, isAllCustomer)
                    isAllowClose = isAllowCloseTicket
                    isNeedAttachment = isNeedAttachmentTicket
                }
            )
        )

        return response
    }

    private fun createBadCsatReasonListItem(): ArrayList<BadCsatReasonListItem> {
        val badCsatReasonListItem: ArrayList<BadCsatReasonListItem> = arrayListOf()
        for (i in 1..6) {
            badCsatReasonListItem.add(
                BadCsatReasonListItem().apply {
                    id = i.toLong()
                    messageEn = "message-$i"
                    message = "message-$i"
                }
            )
        }
        return badCsatReasonListItem
    }

    private fun createCommentsItems(
        rating: String,
        isAllCustomer: Boolean = false
    ): ArrayList<CommentsItem> {
        val commentItems = arrayListOf<CommentsItem>()
        for (i in 1..6) {
            commentItems.add(
                CommentsItem(
                    id = i.toString(),
                    messagePlaintext = if (i == 3) "mes" else "message-$i",
                    rating = rating,
                    message = if (i == 3) "mes" else "message-$i",
                    createdBy = if (isAllCustomer) CreatedBy(role = "customer") else if (i % 2 == 0) CreatedBy(
                        role = "agent"
                    ) else CreatedBy(role = "customer"),
                    createTime = if (i == 2) "" else "2023-01-04T06:09:08.000+0000"
                )
            )
        }
        return commentItems
    }

    private fun createConfigServerUploadResponse(
        isError: Boolean = true,
        serverId: String = "0"
    ): ChipUploadHostConfig {
        val responseChipUploadHosting: ChipUploadHostConfig.ChipUploadHostConfig = if (isError) {
            ChipUploadHostConfig.ChipUploadHostConfig(
                messageError = arrayListOf("Error cuy"),
                chipUploadHostConfigData = ChipUploadHostConfigData(
                    generatedHost = GeneratedHost(serverId = serverId)
                )
            )
        } else {
            ChipUploadHostConfig.ChipUploadHostConfig(
                chipUploadHostConfigData = ChipUploadHostConfigData(
                    generatedHost = GeneratedHost(serverId = serverId)
                )
            )
        }
        return ChipUploadHostConfig(responseChipUploadHosting)
    }

    private inline fun <reified T> T.setPrivateProperty(name: String, value: Any?) {
        T::class.java.getDeclaredField(name)
            .apply { isAccessible = true }
            .set(this, value)
    }
}
