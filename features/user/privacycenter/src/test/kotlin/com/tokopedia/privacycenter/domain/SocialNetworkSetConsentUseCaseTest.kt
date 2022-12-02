package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.DataSetConsent
import com.tokopedia.privacycenter.data.SocialNetworkGetConsentResponse
import com.tokopedia.privacycenter.data.SocialNetworkSetConsent
import com.tokopedia.privacycenter.data.SocialNetworkSetConsentResponse
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SocialNetworkSetConsentUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: SocialNetworkSetConsentUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = SocialNetworkSetConsentUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `set consent then success`() = runBlocking {
        val isAllowed = true
        val response = createSuccessResponse(
            SocialNetworkSetConsentResponse(
                SocialNetworkSetConsent(
                    data = DataSetConsent(isSuccess = 1)
                )
            )
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(isAllowed)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data)
    }

    @Test
    fun `set consent then failed from BE`() = runBlocking {
        val isAllowed = true
        val response = createSuccessResponse(SocialNetworkSetConsentResponse())

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(isAllowed)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertFalse(result.data)
    }

}
