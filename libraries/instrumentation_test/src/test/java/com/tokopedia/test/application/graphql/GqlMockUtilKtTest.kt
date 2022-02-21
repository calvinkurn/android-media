package com.tokopedia.test.application.graphql

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock

class GqlMockUtilKtTest {

    @Test
    fun `toGqlPair returns correct pair`() {
        val example = ExampleResponse(1, "Lorem")
        val expected = hashMapOf(ExampleResponse::class.java to example)

        val actual = hashMapOf(example.toGqlPair())

        assertEquals(expected, actual)
    }

    @Test
    fun `get success response test`() {
        val example = ExampleResponse(1, "Lorem")

        val actual = GqlMockUtil.createSuccessResponse(example)

        assertThat(actual.getData(ExampleResponse::class.java), `is`(example))
    }

    @Test
    fun `multiple response test`() {
        val example = ExampleResponse(1, "Lorem")
        val example2 = Example2Response(1, "Lorem")

        val maps = hashMapOf(example.toGqlPair(), example2.toGqlPair())
        val actual = maps.toSuccessGqlResponse()

        assertThat(actual.getData(ExampleResponse::class.java), `is`(example))
        assertThat(actual.getData(Example2Response::class.java), `is`(example2))
    }

    data class ExampleResponse(val id: Int, val title: String)
    data class Example2Response(val id: Int, val title: String)
}
