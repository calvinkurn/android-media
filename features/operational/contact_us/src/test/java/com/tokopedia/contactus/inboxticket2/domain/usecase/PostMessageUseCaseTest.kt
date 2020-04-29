package com.tokopedia.contactus.inboxticket2.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.orderquery.data.CreateTicketResult
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PostMessageUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var postMessageUseCase = spyk(PostMessageUseCase(contactUsRepository))

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

    /****************************************** setQueryMap() ***************************************/

    @Test
    fun `check invocation of setQueryMap with parameter image`() {

        val id = "ticket_id"
        val message = "test message"
        val photo = 1
        val photoAll = "image in string form"

        val map = postMessageUseCase.setQueryMap(id, message, photo, photoAll)

        assertEquals(map[TICKET_ID], id)
        assertEquals(map[MESSAGE], message)
        assertEquals(map[IS_IMAGE], photo)
        assertEquals(map[IMAGE_AS_STRING], photoAll)

    }

    @Test
    fun `check invocation of setQueryMap without parameter image`() {

        val id = "ticket_id"
        val message = "test message"
        val photo = 0
        val photoAll = ""

        val map = postMessageUseCase.setQueryMap(id, message, photo, photoAll)

        assertEquals(map[TICKET_ID], id)
        assertEquals(map[MESSAGE], message)
        assertNull(map[IS_IMAGE])
        assertNull(map[IMAGE_AS_STRING])

    }

    /****************************************** setQueryMap() ***************************************/



    /************************************* getCreateTicketResult() **********************************/
    @Test
    fun `check function invocation getCreateTicketResult`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.postRestData(any(),
                        object : TypeToken<InboxDataResponse<CreateTicketResult>>() {}.type,
                        any(),
                        any()) as InboxDataResponse<CreateTicketResult>
            } returns mockk()

            postMessageUseCase.getCreateTicketResult(mockk())

            coVerify(exactly = 1) {
                contactUsRepository.postRestData(any(),
                        object : TypeToken<InboxDataResponse<CreateTicketResult>>() {}.type,
                        any(),
                        any()) as InboxDataResponse<CreateTicketResult>
            }
        }
    }

    /************************************* getCreateTicketResult() **********************************/

}