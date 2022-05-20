package com.tokopedia.chatbot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.chatbot.view.viewmodel.ChatbotViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.junit.*

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
        viewModel = spyk(
            ChatbotViewModel(ticketListContactUsUsecase,testRule.dispatchers)
        )

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

}