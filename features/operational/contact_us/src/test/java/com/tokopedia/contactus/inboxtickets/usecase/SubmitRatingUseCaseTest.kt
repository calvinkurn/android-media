package com.tokopedia.contactus.inboxtickets.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.domain.usecase.COMMENT_ID
import com.tokopedia.contactus.inboxtickets.domain.usecase.CSAT_RATING
import com.tokopedia.contactus.inboxtickets.domain.usecase.REASON
import com.tokopedia.contactus.inboxtickets.domain.usecase.SubmitRatingUseCase
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

@ExperimentalCoroutinesApi
class SubmitRatingUseCaseTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var submitRatingUseCase = spyk(SubmitRatingUseCase(anyString(), contactUsRepository))

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
    fun createRequestParamsTest() {
        val commentId = "1234"
        val csatRating = 5
        val reason = "reason"

        val variables = submitRatingUseCase.createRequestParams(commentId, csatRating, reason)

        assertEquals(variables.parameters[COMMENT_ID], commentId)
        assertEquals(variables.parameters[CSAT_RATING], csatRating)
        assertEquals(variables.parameters[REASON], reason)
    }

    @Test
    fun `check function invokation of getChipInboxDetail`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.getGQLData(
                    "",
                    ChipInboxDetails::class.java,
                    any()
                ).chipGetInboxDetail
            } returns mockk()

            submitRatingUseCase.getChipInboxDetail(mockk(relaxed = true))
            coVerify(exactly = 1) {
                contactUsRepository.getGQLData(
                    "",
                    ChipInboxDetails::class.java,
                    any()
                ).getInboxDetail()
            }
        }
    }
}
