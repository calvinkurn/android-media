package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.MockUtil
import com.tokopedia.graphql.domain.example.FooModel
import com.tokopedia.graphql.domain.example.GetNoParamUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class CoroutineUseCaseTest {

    val repository = mockk<GraphqlRepository>()
    val dispatcher = TestCoroutineDispatcher()
    lateinit var uut: GetNoParamUseCase


    @Before
    fun setup() {
        uut = GetNoParamUseCase(repository, dispatcher)
    }

    @Test
    fun `no param test`() {
        dispatcher.runBlockingTest {
            val case = FooModel(1, "message")
            coEvery { repository.getReseponse(any(), any()) } returns MockUtil.createSuccessResponse(case)

            val result = uut(Unit)

            assert(result.id == case.id)
            assert(result.msg == case.msg)
        }

    }


    @Test
    fun `a a`() {
        dispatcher.runBlockingTest {
            val result = repository.getReseponse(listOf())
            print(result)
        }
    }
}