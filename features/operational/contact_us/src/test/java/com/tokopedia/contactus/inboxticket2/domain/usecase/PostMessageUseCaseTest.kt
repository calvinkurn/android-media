package com.tokopedia.contactus.inboxticket2.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.model.TicketReplyResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class PostMessageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var postMessageUseCase = spyk(PostMessageUseCase(anyString(), contactUsRepository))

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /****************************************** createRequestParam() ***************************************/
    @Test
    fun `check value of createRequestParam`() {

        val id = "ticket_id"
        val message = "test message"
        val photo = 1
        val photoAll = "image in string form"
        val agentReply = "lastReply"
        val userId = "1234"

        val requestParam = postMessageUseCase.createRequestParams(id, message, photo, photoAll, agentReply, userId)

        assertEquals(requestParam.parameters[TICKET_ID], id)
        assertEquals(requestParam.parameters[MESSAGE], message)
        assertEquals(requestParam.parameters[IS_IMAGE], photo)
        assertEquals(requestParam.parameters[IMAGE_AS_STRING], photoAll)
        assertEquals(requestParam.parameters[AGENT_REPLY], agentReply)
        assertEquals(requestParam.parameters[USER_ID], userId)

    }
    /****************************************** createRequestParam() ***************************************/


    /************************************* getCreateTicketResult() **********************************/
    @Test
    fun `check function invocation getCreateTicketResult`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.getGQLData("",
                        TicketReplyResponse::class.java,
                        any())
            } returns mockk()

            postMessageUseCase.getCreateTicketResult(mockk(relaxed = true))

            coVerify(exactly = 1) {
                contactUsRepository.getGQLData("",
                        TicketReplyResponse::class.java,
                        any())
            }
        }
    }

    /************************************* getCreateTicketResult() **********************************/

}