package com.tokopedia.purchase_platform.common.utils

import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class UtilsTest : Spek({

    describe("convert list to string") {

        describe("given null") {

            it("returns empty string") {
                assertEquals("", convertToString(null))
            }
        }

        describe("given empty list") {

            it("returns empty string") {
                assertEquals("", convertToString(emptyList()))
            }
        }

        describe("given list of strings") {

            val listOfStrings = listOf("message_1", "message_2", "message_3")
            val joinedString = "message_1, message_2, message_3"

            it("returns joined string") {
                assertEquals(joinedString, convertToString(listOfStrings))
            }
        }
    }
})