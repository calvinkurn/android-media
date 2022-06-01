package com.tokopedia.analyticsdebugger.cassava

import com.tokopedia.analyticsdebugger.cassava.utils.AnalyticsParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsParserTest {

    private val uut = AnalyticsParser()

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

    @Test
    fun `given big double when parsed should return exponential notation and vice versa`() {
        val bigDouble = 2000000000.0
        val parsed = """
            {
              "price": 2.0E9
            }
        """.trimIndent()
        val case = mapOf<String, Any>(
            "price" to bigDouble
        )
        val actualStr = uut.parse(case)
        val actualMap = uut.toJsonMap(parsed)

        assertEquals(parsed, actualStr)
        assertEquals(bigDouble, actualMap["price"])
        assertTrue(actualMap["price"] is Double)
    }
}