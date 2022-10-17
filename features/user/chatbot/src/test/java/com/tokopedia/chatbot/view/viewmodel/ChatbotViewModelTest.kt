package com.tokopedia.chatbot.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ChatbotViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var ticketListContactUsUsecase: TicketListContactUsUsecase

    private lateinit var viewModel: ChatbotViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        ticketListContactUsUsecase = mockk(relaxed = true)
        viewModel =
            ChatbotViewModel(ticketListContactUsUsecase, testRule.dispatchers)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTicketList success if isActive is true`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns true

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertEquals(
            (viewModel.ticketList.value as TicketListState.BottomSheetData).noticeData,
            actual
        )
    }

    @Test
    fun `getTicketList success if isActive is false`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if noticeData is null`() {
        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if ticket data is null`() {
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            response.ticket?.TicketData
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList success if ticket is null`() {
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            response.ticket
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(), any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `getTicketList failure`() {
        coEvery {
            ticketListContactUsUsecase.getTicketList(any(), captureLambda())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )
    }

    @Test
    fun `onCleared success`() {
        every {
            ticketListContactUsUsecase.cancelJobs()
        } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { ticketListContactUsUsecase.cancelJobs() }
    }
}
