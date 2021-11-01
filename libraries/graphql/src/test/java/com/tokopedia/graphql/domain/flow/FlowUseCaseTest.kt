package com.tokopedia.graphql.domain.flow

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.MockUtil
import com.tokopedia.graphql.domain.example.FooModel
import com.tokopedia.graphql.domain.example.GetNoParamFlowUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowUseCaseTest {

    val repository = mockk<GraphqlRepository>()
    val dispatcher = TestCoroutineDispatcher()
    lateinit var uut: GetNoParamFlowUseCase

    @Before
    fun setup() {
        uut = GetNoParamFlowUseCase(repository, dispatcher)
        clearAllMocks()
    }

    @Test
    fun `when given success response should return output model with id`() =
        dispatcher.runBlockingTest {
            val case = FooModel(1, "message")
            coEvery { repository.response(any(), any()) } returns
                    MockUtil.createSuccessResponse(case)

            val result = uut(Unit).single()

            assertTrue(result.id == case.id)
            assertTrue(result.msg == case.msg)
        }

    @Test(expected = RuntimeException::class)
    fun `when given error response should throw Runtime exception`() = dispatcher.runBlockingTest {
        val case = RuntimeException()
        coEvery { repository.response(any(), any()) } throws case

        uut(Unit).single()
    }

}