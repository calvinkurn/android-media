package com.tokopedia.chatbot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.chatbot.view.viewmodel.ChatbotViewModel
import com.tokopedia.chatbot.view.viewmodel.TicketListState
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ChatbotViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var ticketListContactUsUsecase : TicketListContactUsUsecase

    private lateinit var viewModel : ChatbotViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    @Throws(Exception::class)
    fun setUp(){
        MockKAnnotations.init(this)
        ticketListContactUsUsecase = mockk(relaxed = true)
        viewModel =
            ChatbotViewModel(ticketListContactUsUsecase,testRule.dispatchers)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTicketList success if isActive is true` ()  {

        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns true

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(),any())
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
    fun `getTicketList success if isActive is false` ()  {

        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns actual

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(),any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )

    }

    @Test
    fun `getTicketList success if noticeData is null` ()  {

        val actual = mockk<InboxTicketListResponse.Ticket.Data.NoticeItem>(relaxed = true)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        every {
            actual.isActive
        } returns false

        every {
            response.ticket?.TicketData?.notice
        } returns null

        coEvery {
            ticketListContactUsUsecase.getTicketList(captureLambda(),any())
        } coAnswers {
            firstArg<(InboxTicketListResponse) -> Unit>().invoke(response)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )

    }

    @Test
    fun `getTicketList failure` () {

        coEvery {
            ticketListContactUsUsecase.getTicketList(any(),captureLambda())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getTicketList()

        assertTrue(
            (viewModel.ticketList.value is TicketListState.ShowContactUs)
        )

    }


}