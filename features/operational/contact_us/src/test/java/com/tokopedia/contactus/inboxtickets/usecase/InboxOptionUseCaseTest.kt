package com.tokopedia.contactus.inboxtickets.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.domain.usecase.InboxOptionUseCase
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
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class InboxOptionUseCaseTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val contactUsRepository: ContactUsRepository = mockk(relaxed = true)

    private var inboxOptionUseCase = spyk(InboxOptionUseCase(anyString(), contactUsRepository))

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
    fun createRequestParams() {
        val caseId = "1234"
        val params = inboxOptionUseCase.createRequestParams(caseId)

        assertEquals(params.parameters["caseID"], caseId)
    }

    @Test
    fun `check function invokation of getData`() {
        runBlockingTest {
            coEvery {
                contactUsRepository.getGQLData(
                    "",
                    ChipInboxDetails::class.java,
                    any()
                ).chipGetInboxDetail
            } returns mockk()

            inboxOptionUseCase.getChipInboxDetail(mockk(relaxed = true))
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
