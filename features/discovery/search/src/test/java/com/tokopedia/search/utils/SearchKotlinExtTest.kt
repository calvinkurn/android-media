package com.tokopedia.search.utils

import com.tokopedia.search.shouldHaveKeyValue
import com.tokopedia.search.shouldHaveSize
import org.junit.Test

internal class SearchKotlinExtTest {

    @Test
    fun `Convert map values to string - Origin Map is null`() {
        val originalMap : Map<String, Any>? = null

        val mapValuesInString = originalMap.convertValuesToString()

        mapValuesInString shouldHaveSize 0
    }

    @Test
    fun `Convert map values to string - Original Map is not null`() {
        val originalMap = mutableMapOf<String, Any>().also {
            it["string"] = "string"
            it["integer"] = 1
            it["boolean"] = false
            it["double"] = 1.0
        }

        val mapValuesInString = originalMap.convertValuesToString()

        mapValuesInString.shouldHaveKeyValue("string", "string")
        mapValuesInString.shouldHaveKeyValue("integer", "1")
        mapValuesInString.shouldHaveKeyValue("boolean", "false")
        mapValuesInString.shouldHaveKeyValue("double", "1.0")
    }
}