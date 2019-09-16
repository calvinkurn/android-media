package com.tokopedia.search.utils

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe

class UrlParamUtilsSpekTest: Spek({

    describe("Generate url param string") {

        fun Suite.validateGenerateUrlParamString(inputMap: Map<String?, Any?>?, expected: String) {
            val actual = UrlParamUtils.generateUrlParamString(inputMap)

            if (actual != expected) {
                throw Exception("Test failed. Expected: $expected, Actual: $actual")
            }
        }

        describe("Generate url param string") {
            val inputMap = hashMapOf<String?, Any?>(
                    "q" to "Samsung",
                    "official" to true
            )

            validateGenerateUrlParamString(inputMap, "q=Samsung&official=true")
        }
    }
})