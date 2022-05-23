package com.tokopedia.chatbot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.chatbot.view.viewmodel.ChatbotViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatbotViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var ticketListContactUsUsecase : TicketListContactUsUsecase

    private lateinit var viewModel : ChatbotViewModel

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
    fun `getTicketList success` ()  {

        val response = mockk<GraphqlResponse>()
        val ticketList = mockk<InboxTicketListResponse>()

        coEvery {
            ticketListContactUsUsecase.getTicketList()
        } returns response

        every {
            response.getData<InboxTicketListResponse>(InboxTicketListResponse::class.java)
        } returns ticketList

        viewModel.getTicketList()

        assertEquals(
            (viewModel.ticketList.value as Success).data,
            ticketList
        )

    }

    @Test
    fun `getTicketList failure` () {

        val exception = mockk<Exception>()

        coEvery {
            ticketListContactUsUsecase.getTicketList()
        } throws exception


        viewModel.getTicketList()

        assertEquals(
            (viewModel.ticketList.value as Fail).throwable,
            exception
        )

    }


}