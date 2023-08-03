package com.tokopedia.kyc_centralized.gotoKyc.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.KycProjectInfo
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectInfoUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: ProjectInfoUseCase

    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = ProjectInfoUseCase(repository, dispatcher)
    }

    @Test
    fun `get project info then return toko kyc`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = false
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is ProjectInfoResult.TokoKyc)
    }

    @Test
    fun `get project info then return not verified and account not linked`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = true,
                    status = "3",
                    accountLinked = 0
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(false, result.isAccountLinked)
    }

    @Test
    fun `get project info then return not verified and account linked`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = true,
                    status = "3",
                    accountLinked = 1
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(true, result.isAccountLinked)
    }

    @Test
    fun `get project info then return status submission`() = runBlocking {
        val projectId = 7
        val rejectionReason = listOf("Terjadi Kesalahan!")
        val messageWaiting = "Tunggu 24 Jam ya!"
        val status = "0"
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = true,
                    status = status,
                    reason = rejectionReason,
                    waitMessage = messageWaiting
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is ProjectInfoResult.StatusSubmission)
        assertEquals(status, result.status)
        assertEquals(rejectionReason.joinToString(), result.rejectionReason)
        assertEquals(messageWaiting, result.waitMessage)
    }
}
