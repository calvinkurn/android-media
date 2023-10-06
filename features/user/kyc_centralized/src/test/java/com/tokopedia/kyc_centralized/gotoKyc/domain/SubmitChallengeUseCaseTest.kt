package com.tokopedia.kyc_centralized.gotoKyc.domain

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitKYCChallenge
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubmitChallengeUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: SubmitChallengeUseCase

    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = SubmitChallengeUseCase(repository, context, dispatcher)
    }

    @Test
    fun `get submit challenge then return failed`() = runBlocking {
        val parameter = SubmitChallengeParam()
        val message = listOf("Terjadi kesalahan!")
        val response = createSuccessResponse(
            SubmitChallengeResponse(
                SubmitKYCChallenge(
                    errorMessages = message
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SubmitChallengeResult.Failed)
        assertEquals("${message.joinToString()} ", result.throwable.message)
    }

    @Test
    fun `get submit challenge then return wrong answer`() = runBlocking {
        val parameter = SubmitChallengeParam()
        val attemptsRemaining = "2"
        val response = createSuccessResponse(
            SubmitChallengeResponse(
                SubmitKYCChallenge(
                    message = "KYC_CHALLENGE_SUBMITTED_WRONG_ANSWERS",
                    attemptsRemaining = attemptsRemaining
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SubmitChallengeResult.WrongAnswer)
        assertEquals(attemptsRemaining, result.attemptsRemaining)
    }

    @Test
    fun `get submit challenge then return exhausted`() = runBlocking {
        val parameter = SubmitChallengeParam()
        val cooldownTimeInSeconds = "200000"
        val maximumAttemptsAllowed = "3"
        val response = createSuccessResponse(
            SubmitChallengeResponse(
                SubmitKYCChallenge(
                    message = "KYC_CHALLENGE_ATTEMPTS_EXHAUSTED",
                    cooldownTimeInSeconds = cooldownTimeInSeconds,
                    maximumAttemptsAllowed = maximumAttemptsAllowed
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SubmitChallengeResult.Exhausted)
        assertEquals(cooldownTimeInSeconds, result.cooldownTimeInSeconds)
        assertEquals(maximumAttemptsAllowed, result.maximumAttemptsAllowed)
    }

    @Test
    fun `get submit challenge then return success`() = runBlocking {
        val parameter = SubmitChallengeParam()
        val response = createSuccessResponse(
            SubmitChallengeResponse(
                SubmitKYCChallenge()
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SubmitChallengeResult.Success)
    }

}
