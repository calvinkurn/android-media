package com.tokopedia.kyc_centralized.gotoKyc.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.gotoKyc.utils.createSuccessResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.AccountLinkingStatusResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.AccountsLinkerStatus
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.LinkStatusItem
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
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = AccountLinkingStatusUseCase(repository, dispatcher)
    }

    @Test
    fun `get account linking status then account linked`() = runBlocking {
        val response = createSuccessResponse(
            AccountLinkingStatusResponse(
                AccountsLinkerStatus(
                    listOf(
                        LinkStatusItem(
                            status = "linked",
                            linkedTime = "2022-11-29T02:19:39.579143Z"
                        )
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is AccountLinkingStatusResult.Linked)
    }

    @Test
    fun `get account linking status then account not linked`() = runBlocking {
        val response = createSuccessResponse(
            AccountLinkingStatusResponse()
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is AccountLinkingStatusResult.NotLinked)
    }

}
