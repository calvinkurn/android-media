package com.tokopedia.graphql.coroutines.data.extensions

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.MockUtil
import com.tokopedia.graphql.domain.example.FooModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExtKtTest {

    private val repository = mockk<GraphqlRepository>()
    private val case = FooModel(1, "message")

    @Before
    fun setup() {
        coEvery { repository.response(any(), any()) } returns
                MockUtil.createSuccessResponse(case)
    }

    @Test
    fun `given true map should return as expected`() {
        runBlockingTest {
            val input = mapOf("var1" to "foo")

            val actual = repository.request<Map<String, Any>, FooModel>("", input)

            assertEquals(case, actual)
        }
    }

    @Test
    fun `given true map should return as expected flow`() {
        val input = mapOf("var1" to "foo")

        val actual = repository.requestAsFlow<Map<String, Any>, FooModel>("", input)

        runBlockingTest {
            assertEquals(case, actual.single())
        }
    }

    @Test
    fun `given unit param should returns as expected`() {
        runBlockingTest {
            val actual = repository.request<Unit, FooModel>("", Unit)
            assertEquals(case, actual)
        }
    }

    @Test
    fun `given data class param should returns as expected`() {
        runBlockingTest {
            val param = FooModel(1, "")
            val actual = repository.request<FooModel, FooModel>("", param)
            assertEquals(case, actual)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given map of arbitrary type should throw exception`() {
        runBlockingTest {
            repository.request<String, FooModel>("", "foo")
        }
    }
}
