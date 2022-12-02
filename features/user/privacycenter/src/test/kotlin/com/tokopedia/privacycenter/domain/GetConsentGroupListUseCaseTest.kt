package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentGroupDataModel
import com.tokopedia.privacycenter.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.data.GetConsentGroupListDataModel
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

class GetConsentGroupListUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetConsentGroupListUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = GetConsentGroupListUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get consent group list then success`() = runBlocking {
        val data = ConsentGroupListDataModel(
            success = true,
            groups = listOf(ConsentGroupDataModel(), ConsentGroupDataModel())
        )
        val response = createSuccessResponse(GetConsentGroupListDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(data, result.data)
    }

    @Test
    fun `get consent group list then failed`() = runBlocking {
        val data = ConsentGroupListDataModel(
            success = false,
            errorMessages = listOf("Error")
        )
        val response = createSuccessResponse(GetConsentGroupListDataModel(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.errorMessages.toString(), result.error.message)
    }

}
