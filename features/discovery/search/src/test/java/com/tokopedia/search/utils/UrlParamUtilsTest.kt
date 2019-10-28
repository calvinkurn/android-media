package com.tokopedia.search.utils

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UrlParamUtilsTest: Spek({

    describe("Generate url param string") {

        fun Map<String?, Any?>?.generateUrlParamStringAndValidate(expected: String) {
            val actual = UrlParamUtils.generateUrlParamString(this)

            if (actual != expected) {
                throw Exception("Test failed. Expected: $expected, Actual: $actual")
            }
        }

        describe("Generate url param with null inputs") {
            val inputMap = null

            it("should generate an empty string") {
                inputMap.generateUrlParamStringAndValidate("")
            }
        }

        describe("Generate url param with empty map") {
            val inputMap = hashMapOf<String?, Any?>()

            it("should generate an empty string") {
                inputMap.generateUrlParamStringAndValidate("")
            }
        }

        describe("Generate url param string regular case") {
            val inputMap = hashMapOf<String?, Any?>(
                    "q" to "Samsung",
                    "official" to true
            )

            it("Should generate string from all map entries") {
                inputMap.generateUrlParamStringAndValidate("q=Samsung&official=true")
            }
        }

        describe("Generate url param string with some null values") {
            val inputMap = hashMapOf<String?, Any?>(
                    "q" to "Samsung",
                    "official" to null,
                    "sc" to null
            )

            it("Should generate string from non-null value map entries") {
                inputMap.generateUrlParamStringAndValidate("q=Samsung")
            }
        }

        describe("Generate url param string with null keys") {
            val inputMap = hashMapOf<String?, Any?>(
                    null to "Samsung",
                    "official" to null,
                    "sc" to 32
            )

            it("Should generate string from non-null key map entries") {
                inputMap.generateUrlParamStringAndValidate("sc=32")
            }
        }

        describe("Generate url param string with spaces") {
            val inputMap = hashMapOf<String?, Any?>(
                    "q" to "samsung galaxy s8",
                    "official" to true,
                    "pmin" to 100000
            )

            it("Should generate string from all map entries, with spaces replaced with +") {
                inputMap.generateUrlParamStringAndValidate("q=samsung+galaxy+s8&official=true&pmin=100000")
            }
        }
    }
})