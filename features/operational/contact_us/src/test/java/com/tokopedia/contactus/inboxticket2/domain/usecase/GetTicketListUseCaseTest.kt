package com.tokopedia.contactus.inboxticket2.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.user.session.UserSessionInterface
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
class GetTicketListUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private var getTicketDetailUseCase = spyk(GetTicketListUseCase(userSession, contactUsRepository))

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

    /****************************************** getRequestParams() ***************************************/

    @Test
    fun `check invocation of getRequestParams with parameters value greater than 0`() {

        val page = 1
        val status = 1
        val rating = 5

        val map = getTicketDetailUseCase.getRequestParams(page, status, rating)

        assertEquals(map.parameters[STATUS], 1)
        assertEquals(map.parameters["page"], 1)
        assertEquals(map.parameters[RATING], 5)

    }


    @Test
    fun `check invocation of getRequestParams with parameters value less than 0`() {

        val page = -2
        val status = -1
        val rating = 0

        val map = getTicketDetailUseCase.getRequestParams(page, status, rating)

        assertEquals(map.parameters[STATUS],-1)
        assertNull(map.parameters[RATING])
        assertEquals(map.parameters["page"],-2)

    }

    /****************************************** setQueryMap() ***************************************/


    /*************************************** getTicketListResponse() ********************************/

    @Test
    fun `check function invocation getTicketListResponse`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.getGQLData<InboxTicketListResponse>(any(),any(),any())
            } returns mockk()

            getTicketDetailUseCase.getTicketListResponse(mockk(relaxed = true))

            coVerify(exactly = 1) {
                contactUsRepository.getGQLData<InboxTicketListResponse>(any(),any(),any())
            }
        }
    }

    /*************************************** getTicketListResponse() ********************************/
}