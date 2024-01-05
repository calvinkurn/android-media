package com.tokopedia.kyc_centralized.gotoKyc.domain

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.KycProjectInfo
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.ProjectInfoResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class AccountLinkingStatusUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: AccountLinkingStatusUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = AccountLinkingStatusUseCase(repository, context, dispatcher)
    }

    @Test
    fun `get account linking status then account linked`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = true,
                    accountLinked = 1
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.Linked)
    }

    @Test
    fun `get account linking status then account not linked`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = true,
                    accountLinked = -1
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.NotLinked)
    }

    @Test
    fun `get account linking status then account not eligible goto kyc`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isGotoKyc = false,
                    accountLinked = 1
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.TokoKyc)
    }

    @Test
    fun `get account linking status then failed`() = runBlocking {
        val projectId = 7
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    errorCode = "123",
                    errorMessages = listOf("Error")
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.Failed)
    }

    @Test
    fun `get account linking status then return blocked multiple account - reason 1`() = runBlocking {
        val projectId = 7
        val isBlocked = true
        val nonEligibleGoToKYCReason = "ALREADY_REGISTERED_OTHER_PROFILE"
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isBlocked = isBlocked,
                    nonEligibleGoToKYCReason = nonEligibleGoToKYCReason
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.Blocked)
        assertTrue(result.isMultipleAccount)
    }

    @Test
    fun `get account linking status then return blocked multiple account - reason 2`() = runBlocking {
        val projectId = 7
        val isBlocked = true
        val nonEligibleGoToKYCReason = "NOT_ELIGIBLE_ACC_LINK"
        val response = createSuccessResponse(
            ProjectInfoResponse(
                KycProjectInfo(
                    isBlocked = isBlocked,
                    nonEligibleGoToKYCReason = nonEligibleGoToKYCReason
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(projectId)
        assertTrue(result is AccountLinkingStatusResult.Blocked)
        assertTrue(result.isMultipleAccount)
    }

}
