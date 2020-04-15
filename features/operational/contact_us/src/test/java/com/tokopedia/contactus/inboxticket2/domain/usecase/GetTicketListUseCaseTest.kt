package com.tokopedia.contactus.inboxticket2.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse
import com.tokopedia.network.data.model.response.DataResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTicketListUseCaseTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var getTicketDetailUseCase = spyk(GetTicketListUseCase(contactUsRepository))

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
    fun `check invocation of setQueryMap with parameters value greater than 0`() {

        val status = 1
        val read = 3
        val rating = 5

        val map = getTicketDetailUseCase.setQueryMap(status, read, rating)

        assertEquals(map[STATUS], 1)
        assertEquals(map[READ], 3)
        assertEquals(map[RATING], 5)

    }


    @Test
    fun `check invocation of setQueryMap with parameters value less than 0`() {

        val status = -1
        val read = -3
        val rating = -5

        val map = getTicketDetailUseCase.setQueryMap(status, read, rating)

        assertNull(map[STATUS])
        assertNull(map[READ])
        assertNull(map[RATING])

    }

    /****************************************** setQueryMap() ***************************************/



    /*************************************** getTicketListResponse() ********************************/

    @Test
    fun `check function invocation getTicketListResponse`() {
        runBlockingTest {
            coEvery {
                (contactUsRepository.getRestData(any(),
                        object : TypeToken<DataResponse<TicketListResponse>>() {}.type,
                        any(),
                        any()) as DataResponse<TicketListResponse>).data
            } returns mockk()

            getTicketDetailUseCase.getTicketListResponse(mockk(),"")

            coVerify(exactly = 1) {
                (contactUsRepository.getRestData(any(),
                        object : TypeToken<DataResponse<TicketListResponse>>() {}.type,
                        any(),
                        any()) as DataResponse<TicketListResponse>).data
            }
        }
    }

    /*************************************** getTicketListResponse() ********************************/
}