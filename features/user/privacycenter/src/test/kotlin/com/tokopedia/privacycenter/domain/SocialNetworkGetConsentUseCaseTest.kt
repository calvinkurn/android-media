package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.data.DataUsage
import com.tokopedia.privacycenter.data.SocialNetworkGetConsent
import com.tokopedia.privacycenter.data.SocialNetworkGetConsentResponse
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SocialNetworkGetConsentUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: SocialNetworkGetConsentUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = SocialNetworkGetConsentUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get consent then return false`() = runBlocking {
        val response = createSuccessResponse(SocialNetworkGetConsentResponse())

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is GetRecommendationFriendState.Success)
        assertNotNull(result.isAllowed)
        assertTrue(result.isAllowed == false)
    }

    @Test
    fun `get consent then return true`() = runBlocking {
        val response = createSuccessResponse(
            SocialNetworkGetConsentResponse(
                SocialNetworkGetConsent(
                    data = DataUsage(
                        optIn = true
                    )
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is GetRecommendationFriendState.Success)
        assertNotNull(result.isAllowed)
        assertTrue(result.isAllowed == true)
    }

}
