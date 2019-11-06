package com.tokopedia.logisticaddaddress.utils

import org.junit.Test

import org.junit.Assert.*

class LocationUtilsKtTest {

    @Test
    fun givenDecimalValueReturnRoundedString() {
        val given = "0.4311"
        val actual = given.toKilometers()

        assertEquals("0.4 km", actual)
    }

    @Test
    fun givenSpacedDecimalValueReturnRoundedString() {
        val given = " 0.4311 "
        val actual = given.toKilometers()

        assertEquals("0.4 km", actual)
    }

    @Test
    fun givenCeilingValueReturnRoundedString() {
        val given = "1.89"
        val actual = given.toKilometers()

        assertEquals("1.9 km", actual)
    }

    @Test
    fun givenNonNumberValueReturnZeroKmString() {
        val given = "1,32km"
        val actual = given.toKilometers()

        assertEquals("0.0 km", actual)
    }
}