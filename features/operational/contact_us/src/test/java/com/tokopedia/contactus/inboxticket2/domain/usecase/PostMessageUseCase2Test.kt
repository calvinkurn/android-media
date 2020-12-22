package com.tokopedia.contactus.inboxticket2.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.domain.InboxDataResponse
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
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
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class PostMessageUseCase2Test {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var postMessageUseCase2 = spyk(PostMessageUseCase2(contactUsRepository))

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

    /****************************************** createRequestParams() ***************************************/

    @Test
    fun `check invocation of setQueryMap`() {

        val fileUploaded = "file"
        val postKey = "key"
        val userId = "uid"
        val ticketId = "tid"

        val parmas = postMessageUseCase2.createRequestParams(ticketId, userId, fileUploaded, postKey)

        assertEquals(parmas.parameters[FILE_UPLOADED], fileUploaded)
        assertEquals(parmas.parameters[POST_KEY], postKey)
        assertEquals(parmas.parameters[USER_ID], userId)
        assertEquals(parmas.parameters[TICKETID], ticketId)

    }

    /****************************************** createRequestParams() ***************************************/


    /************************************* getInboxDataResponse() **********************************/
    @Test
    fun `check function invocation getInboxDataResponse`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.getGQLData("",
                        StepTwoResponse::class.java,
                        any())
            } returns mockk()

            postMessageUseCase2.getInboxDataResponse(mockk(relaxed = true))

            coVerify(exactly = 1) {
                contactUsRepository.getGQLData(any(),
                        StepTwoResponse::class.java,
                        any())
            }
        }
    }

    /************************************* getInboxDataResponse() **********************************/

}