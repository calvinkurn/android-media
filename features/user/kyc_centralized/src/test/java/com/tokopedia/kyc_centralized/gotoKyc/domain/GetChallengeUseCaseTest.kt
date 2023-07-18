package com.tokopedia.kyc_centralized.gotoKyc.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetOneKYCChallenge
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.QuestionsData
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetChallengeUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetChallengeUseCase

    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = GetChallengeUseCase(repository, dispatcher)
    }

    @Test
    fun `get challenge then return success`() = runBlocking {
        val challengeId = "qWerTy"
        val questionId = "1234"
        val response = createSuccessResponse(
            GetChallengeResponse(
                GetOneKYCChallenge(
                    isSuccess = true,
                    data = listOf(
                        QuestionsData(
                            id = "4567",
                            questionType = "NIK",
                        ),
                        QuestionsData(
                            id = questionId,
                            questionType = "Date of Birth",
                        )
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(challengeId)
        assertTrue(result is GetChallengeResult.Success)
        assertEquals(questionId, result.questionId)
    }

    @Test
    fun `get challenge then return failed cause have not dob question`() = runBlocking {
        val challengeId = "qWerTy"
        val message = "Terjadi kesalahan"
        val response = createSuccessResponse(
            GetChallengeResponse(
                GetOneKYCChallenge(
                    isSuccess = true,
                    data = listOf(
                        QuestionsData(
                            id = "4567",
                            questionType = "NIK",
                        )
                    ),
                    errorMessages = listOf(message)
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(challengeId)
        assertTrue(result is GetChallengeResult.Failed)
        assertEquals(message, result.throwable.message)
    }

    @Test
    fun `get challenge then return failed`() = runBlocking {
        val challengeId = "qWerTy"
        val message = "Terjadi kesalahan"
        val response = createSuccessResponse(
            GetChallengeResponse(
                GetOneKYCChallenge(
                    isSuccess = false,
                    errorMessages = listOf(message)
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(challengeId)
        assertTrue(result is GetChallengeResult.Failed)
        assertEquals(message, result.throwable.message)
    }
}
