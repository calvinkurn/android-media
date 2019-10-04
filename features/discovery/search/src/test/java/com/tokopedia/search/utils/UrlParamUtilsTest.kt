package com.tokopedia.search.utils

import org.junit.Test

class UrlParamUtilsTest {
    
    @Test
    fun `test generate url param string`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to true
        )

        executeAndValidate(inputMap, "q=Samsung&official=true")
    }

    private fun executeAndValidate(inputMap: Map<String?, Any?>?, expected: String) {
        val actual = UrlParamUtils.generateUrlParamString(inputMap)

        assert(actual == expected) {
            "Test failed. Expected: $expected, Actual: $actual"
        }
    }

    @Test
    fun `test generate url param with null inputs`() {
        val inputMap = null
        executeAndValidate(inputMap, "")
    }

    @Test
    fun `test generate url param with empty map`() {
        val inputMap = hashMapOf<String?, Any?>()
        executeAndValidate(inputMap, "")
    }

    @Test
    fun `test generate url param string with some null values`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to null,
                "sc" to null
        )

        executeAndValidate(inputMap, "q=Samsung")
    }

    @Test
    fun `test generate url param string with null keys`() {
        val inputMap = hashMapOf<String?, Any?>(
                null to "Samsung",
                "official" to null,
                "sc" to 32
        )

        executeAndValidate(inputMap, "sc=32")
    }

    @Test
    fun `test generate url param string with spaces`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "samsung galaxy s8",
                "official" to true,
                "pmin" to 100000
        )

        executeAndValidate(inputMap, "q=samsung+galaxy+s8&official=true&pmin=100000")
    }
}