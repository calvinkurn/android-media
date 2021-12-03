package com.tokopedia.test.application.graphql

import org.junit.Assert.*
import org.junit.Test

class GqlMockUtilKtTest {

    @Test
    fun `toGqlPair returns correct pair`() {
        val example = ExampleResponse(1, "Lorem")
        val expected = hashMapOf(ExampleResponse::class.java to example)

        val actual = hashMapOf(example.toGqlPair())

        assertEquals(expected, actual)
    }

    data class ExampleResponse(val id: Int, val title: String)
}
