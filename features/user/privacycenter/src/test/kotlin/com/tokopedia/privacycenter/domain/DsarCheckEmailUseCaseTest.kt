package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.data.DsarCheckEmailResponse
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DsarCheckEmailUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: DsarCheckEmailUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = DsarCheckEmailUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get check email then success`() = runBlocking {
        val parameter = "email"
        val data = DsarCheckEmailResponse()
        val response = createSuccessResponse(data)

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertEquals(data, result)
    }

}
