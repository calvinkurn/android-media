package com.tokopedia.search.utils

import org.junit.Test

internal class UrlParamUtilsTest {

    private fun Map<String?, Any?>?.generateUrlParamStringAndValidate(expected: String) {
        val actual = UrlParamUtils.generateUrlParamString(this)

        if (actual != expected) {
            throw Exception("Test failed. Expected: $expected, Actual: $actual")
        }
    }

    @Test
    fun `Generate url param with null inputs`() {
        val inputMap = null

        inputMap.generateUrlParamStringAndValidate("")
    }

    @Test
    fun `Generate url param with empty map`() {
        val inputMap = hashMapOf<String?, Any?>()

        inputMap.generateUrlParamStringAndValidate("")
    }

    @Test
    fun `Generate url param string regular case`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to true
        )

        inputMap.generateUrlParamStringAndValidate("q=Samsung&official=true")
    }

    @Test
    fun `Generate url param string with some null values`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to null,
                "sc" to null
        )

        inputMap.generateUrlParamStringAndValidate("q=Samsung")
    }

    @Test
    fun `Generate url param string with null keys`() {
        val inputMap = hashMapOf<String?, Any?>(
                null to "Samsung",
                "official" to null,
                "sc" to 32
        )

        inputMap.generateUrlParamStringAndValidate("sc=32")
    }

    @Test
    fun `Generate url param string with spaces`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "samsung galaxy s8",
                "official" to true,
                "pmin" to 100000
        )

        inputMap.generateUrlParamStringAndValidate("q=samsung+galaxy+s8&official=true&pmin=100000")
    }
}