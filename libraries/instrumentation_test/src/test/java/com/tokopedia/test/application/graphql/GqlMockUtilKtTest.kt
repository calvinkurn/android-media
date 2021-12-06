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

    data class ExampleResponse(val id: Int, val title: String)
}
