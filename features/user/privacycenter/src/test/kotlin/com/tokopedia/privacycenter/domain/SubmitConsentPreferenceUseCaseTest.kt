package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.data.SubmitConsentPreferenceDataModel
import com.tokopedia.privacycenter.data.SubmitConsentPurposeReq
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubmitConsentPreferenceUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: SubmitConsentPreferenceUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = SubmitConsentPreferenceUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `submit consent preference then success`() = runBlocking {
        val parameter = SubmitConsentPurposeReq()
        val data = SubmitConsentDataModel(isSuccess = true)
        val response = createSuccessResponse(SubmitConsentPreferenceDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(data, result.data)
    }

    @Test
    fun `submit consent preference then failed`() = runBlocking {
        val parameter = SubmitConsentPurposeReq()
        val data = SubmitConsentDataModel(isSuccess = false, errorMessages = listOf("Error"))
        val response = createSuccessResponse(SubmitConsentPreferenceDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.errorMessages.toString(), result.error.message)
    }

}
