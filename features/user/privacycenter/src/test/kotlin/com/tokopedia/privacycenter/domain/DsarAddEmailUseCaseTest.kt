package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.data.AddEmailParam
import com.tokopedia.privacycenter.data.AddEmailResponse
import com.tokopedia.privacycenter.data.UserProfileCompletionUpdateEmail
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DsarAddEmailUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: DsarAddEmailUseCase
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = DsarAddEmailUseCase(
            repository,
            userSession,
            dispatcher
        )
    }

    @Test
    fun `get dsar add email then success and save user session`() = runBlocking {
        val parameter = AddEmailParam(
            email = "habibi@tokopedia.com",
            otpCode = "123456",
            validateToken = "qwerty"
        )
        val data = AddEmailResponse(UserProfileCompletionUpdateEmail(isSuccess = true))
        val response = createSuccessResponse(data)

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertEquals(data, result)
        verify(exactly = 1) {
            userSession.email = parameter.email
        }
    }

}
