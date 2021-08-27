package com.tokopedia.analyticsdebugger.cassava

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsMapParserTest {

    private val uut = AnalyticsMapParser()

    @Test
    fun `given double when parsed should return json primitive`() {
        val case = mapOf<String, Any>("doubleVal" to 25.34)
        val expected = """
            {
              "doubleVal": 25.34
            }
        """.trimIndent()
        val actual = uut.parse(case)
        assertEquals(expected, actual)
    }

    @Test
    fun `given big double when parsed should return exact json primitive`() {
        val priceTest = 2000000000.0
        val expected = """
            {
              "price": 2000000000
            }
        """.trimIndent()
        val case = mapOf<String, Any>(
            "price" to priceTest
        )
        val actual = uut.parse(case)
        assertEquals(expected, actual)
    }

    @Test
    fun `given json string when called toJsonMap converts to map object`() {
        val case = """
            {
              "doubleVal": 25.34
            }
        """.trimIndent()

        val actual = uut.toJsonMap(case)
        val expected = mapOf<String, Any>("doubleVal" to 25.34)

        assertEquals(expected, actual)
    }
}