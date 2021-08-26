package com.tokopedia.analyticsdebugger.cassava

import org.junit.Test
import java.math.BigDecimal

class AnalyticsMapParserTest {

    @Test
    fun `parse test`() {
        val priceTest = 2000000000.0
        println("toString:\n${BigDecimal.valueOf(priceTest).toPlainString()}")
        val case = mapOf<String, Any>(
            "price" to priceTest
        )
        val uut = AnalyticsMapParser()
        val actual = uut.parse(case)
        println("Parsed:\n$actual")
    }
}