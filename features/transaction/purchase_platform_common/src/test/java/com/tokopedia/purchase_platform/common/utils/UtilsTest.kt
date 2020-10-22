package com.tokopedia.purchase_platform.common.utils

import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object UtilsTest : Spek({

    describe("convert list to string") {

        describe("given null") {

            it("returns empty string") {
                Assert.assertEquals("", convertToString(null))
            }
        }

        describe("given empty list") {

            it("returns empty string") {
                Assert.assertEquals("", convertToString(emptyList()))
            }
        }

        describe("given list of strings") {

            val listOfStrings = listOf("message_1", "message_2", "message_3")
            val joinedString = "message_1, message_2, message_3"

            it("returns joined string") {
                Assert.assertEquals(joinedString, convertToString(listOfStrings))
            }
        }
    }

    describe("remove decimal suffix from currency string") {

        describe("given empty string") {

            it("returns empty string") {
                Assert.assertEquals("", "".removeDecimalSuffix())
            }
        }

        describe("given currency string with decimal suffix") {

            it("returns currency string without decimal suffix") {
                Assert.assertEquals("Rp12.000", "Rp12.000.00".removeDecimalSuffix())
            }
        }

        describe("given currency string without decimal suffix") {

            it("returns joined string") {
                Assert.assertEquals("Rp13.000", "Rp13.000".removeDecimalSuffix())
            }
        }
    }
})