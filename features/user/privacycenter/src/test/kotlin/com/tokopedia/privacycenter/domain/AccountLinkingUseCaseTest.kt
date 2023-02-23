package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.AccountLinkingResponse
import com.tokopedia.privacycenter.data.AccountsLinkerStatus
import com.tokopedia.privacycenter.data.LinkStatusItem
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountLinkingUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: AccountLinkingUseCase
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        mockkObject(DateUtil)
        useCase = AccountLinkingUseCase(
            repository,
            userSession,
            dispatcher
        )
    }

    @Test
    fun `get account linking status then account unlinked`() = runBlocking {
        val type = "account_linking"
        val response = createSuccessResponse(AccountLinkingResponse())

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(type)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertFalse(result.data.isLinked)
    }

    @Test
    fun `get account linking status then account linked`() = runBlocking {
        val type = "account_linking"
        val response = createSuccessResponse(
            AccountLinkingResponse(
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
        val phone = "62821123456789"
        val linkedTime = "29 Nov 2022"

        coEvery { repository.response(any(), any()) } returns response
        every { DateUtil.formatDate(any(), any(), any()) } returns linkedTime
        every { userSession.phoneNumber } returns phone

        val result = useCase(type)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data.isLinked)
        assertEquals(phone, result.data.phoneNumber)
        assertEquals(linkedTime, result.data.linkedTime)
    }

}
