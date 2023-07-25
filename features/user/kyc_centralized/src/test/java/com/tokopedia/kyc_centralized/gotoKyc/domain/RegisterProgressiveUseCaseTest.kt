package com.tokopedia.kyc_centralized.gotoKyc.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveKYC
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveKYCData
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegisterProgressiveUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: RegisterProgressiveUseCase

    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = RegisterProgressiveUseCase(repository, dispatcher)
    }

    @Test
    fun `get register progressive then return failed`() = runBlocking {
        val parameter = RegisterProgressiveParam()
        val message = listOf("Terjadi kesalahan!")
        val response = createSuccessResponse(
            RegisterProgressiveResponse(
                RegisterProgressiveKYC(
                    errorMessages = message
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is RegisterProgressiveResult.Failed)
        assertEquals(message.joinToString(), result.throwable.message)
    }

    @Test
    fun `get register progressive then return risky user`() = runBlocking {
        val parameter = RegisterProgressiveParam()
        val challengeId = "QwErTy"
        val response = createSuccessResponse(
            RegisterProgressiveResponse(
                RegisterProgressiveKYC(
                    data = RegisterProgressiveKYCData(
                        challengeID = challengeId
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is RegisterProgressiveResult.RiskyUser)
        assertEquals(challengeId, result.challengeId)
    }

    @Test
    fun `get register progressive then return not risky user`() = runBlocking {
        val parameter = RegisterProgressiveParam()
        val challengeId = ""
        val status = -1
        val rejectionReason = "Terjadi Kesalahan!"
        val response = createSuccessResponse(
            RegisterProgressiveResponse(
                RegisterProgressiveKYC(
                    data = RegisterProgressiveKYCData(
                        challengeID = challengeId,
                        status = status,
                        rejectionReasonMessage = rejectionReason
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is RegisterProgressiveResult.NotRiskyUser)
        assertEquals(status, result.status)
        assertEquals(rejectionReason, result.rejectionReason)
    }

    @Test
    fun `get register progressive then return exhausted`() = runBlocking {
        val parameter = RegisterProgressiveParam()
        val message = "KYC_CHALLENGE_CREATION_QUOTA_EXCEEDED"
        val cooldownTimeInSeconds = "3600"
        val maximumAttemptsAllowed = "3"
        val response = createSuccessResponse(
            RegisterProgressiveResponse(
                RegisterProgressiveKYC(
                    data = RegisterProgressiveKYCData(
                        message = message,
                        cooldownTimeInSeconds = cooldownTimeInSeconds,
                        maximumAttemptsAllowed = maximumAttemptsAllowed
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is RegisterProgressiveResult.Exhausted)
        assertEquals(cooldownTimeInSeconds, result.cooldownTimeInSeconds)
        assertEquals(maximumAttemptsAllowed, result.maximumAttemptsAllowed)
    }
}
