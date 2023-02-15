package com.tokopedia.contactus.inboxtickets.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.domain.usecase.*
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class PostMessageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var postMessageUseCase = spyk(PostMessageUseCase(anyString()))

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

    @Test
    fun `check value of createRequestParam`() {
        val id = "ticket_id"
        val message = "test message"
        val photo = 1
        val photoAll = "image in string form"
        val agentReply = "lastReply"
        val userId = "1234"

        val requestParam = postMessageUseCase.createRequestParams(
            id,
            message,
            photo,
            photoAll,
            agentReply,
            userId
        )

        assertEquals(requestParam.parameters[TICKET_ID], id)
        assertEquals(requestParam.parameters[MESSAGE], message)
        assertEquals(requestParam.parameters[IS_IMAGE], photo)
        assertEquals(requestParam.parameters[IMAGE_AS_STRING], photoAll)
        assertEquals(requestParam.parameters[AGENT_REPLY], agentReply)
        assertEquals(requestParam.parameters[USER_ID], userId)
    }
}
