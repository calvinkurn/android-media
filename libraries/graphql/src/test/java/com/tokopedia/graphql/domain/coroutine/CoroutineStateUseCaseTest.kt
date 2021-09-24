package com.tokopedia.graphql.domain.coroutine

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.MockUtil
import com.tokopedia.graphql.domain.example.FooModel
import com.tokopedia.graphql.domain.example.GetNoParamStateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineStateUseCaseTest {

    val repository = mockk<GraphqlRepository>()
    val dispatcher = TestCoroutineDispatcher()
    lateinit var uut: GetNoParamStateUseCase

    @Before
    fun setup() {
        uut = GetNoParamStateUseCase(repository, dispatcher)
        clearAllMocks()
    }

    @Test
    fun `when given success response should return Success with id`() = dispatcher.runBlockingTest {
        val case = FooModel(1, "message")
        coEvery { repository.response(any(), any()) } returns MockUtil.createSuccessResponse(
            case
        )

        val result = uut(Unit)

        assertTrue(result is Success)
        assertTrue((result as Success).data.id == case.id)
    }

    @Test
    fun `when given error answer should return Fail object`() = dispatcher.runBlockingTest {
        val case = RuntimeException("err")
        coEvery { repository.response(any(), any()) } throws case

        val result = uut(Unit)

        assertTrue(result is Fail)
        assertTrue((result as Fail).throwable.message == case.message)
    }

}