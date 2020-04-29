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
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

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

    /****************************************** setQueryMap() ***************************************/

    @Test
    fun `check invocation of setQueryMap`() {

        val fileUploaded = "file"
        val postKey = "key"

        val map = postMessageUseCase2.setQueryMap(fileUploaded, postKey)

        assertEquals(map[FILE_UPLOADED], fileUploaded)
        assertEquals(map[POST_KEY], postKey)
    }

    /****************************************** setQueryMap() ***************************************/



    /************************************* getInboxDataResponse() **********************************/
    @Test
    fun `check function invocation getInboxDataResponse`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.postRestData(any(),
                        object : TypeToken<InboxDataResponse<StepTwoResponse>>() {}.type,
                        any(),
                        any()) as InboxDataResponse<StepTwoResponse>
            } returns mockk()

            postMessageUseCase2.getInboxDataResponse(mockk())

            coVerify(exactly = 1) {
                contactUsRepository.postRestData(any(),
                        object : TypeToken<InboxDataResponse<StepTwoResponse>>() {}.type,
                        any(),
                        any()) as InboxDataResponse<StepTwoResponse>
            }
        }
    }

    /************************************* getInboxDataResponse() **********************************/

}