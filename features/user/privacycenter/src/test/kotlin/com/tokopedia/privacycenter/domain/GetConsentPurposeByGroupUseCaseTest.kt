package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentPurposeGroupDataModel
import com.tokopedia.privacycenter.data.GetConsentPurposeDataModel
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

class GetConsentPurposeByGroupUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetConsentPurposeByGroupUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = GetConsentPurposeByGroupUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get consent purpose then success`() = runBlocking {
        val parameter = mapOf("Parameter" to  1)
        val data = ConsentPurposeGroupDataModel(isSuccess = true)
        val response = createSuccessResponse(GetConsentPurposeDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(data, result.data)
    }

    @Test
    fun `get consent purpose then failed`() = runBlocking {
        val parameter = mapOf("Parameter" to  1)
        val data = ConsentPurposeGroupDataModel(isSuccess = false, errorMessages = listOf("Error"))
        val response = createSuccessResponse(GetConsentPurposeDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.errorMessages.toString(), result.error.message)
    }

}
