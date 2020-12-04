package com.tokopedia.logisticCommon.util


import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationHelperKtTest {

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

    @Test
    fun givenValidLatLongStringProducesValidLatLng() {
        val given = mapOf(
                "lat" to "126.3921",
                "long" to "-80.3124"
        )
        val expected = mapOf(
                "lat" to 126.3921,
                "long" to -80.3124
        )
        val actual = getLatLng(given.getValue("lat"), given.getValue("long"))
        val expectedLatLng = LatLng(expected.getValue("lat"), expected.getValue("long"))
        assertEquals(expectedLatLng, actual)
    }

    @Test
    fun givenInValidLatLongStringProducesEmptyLatLng() {
        val given = mapOf(
                "lat" to "someRandomString",
                "long" to "-80.3124"
        )
        val expected = mapOf(
                "lat" to 0.0,
                "long" to -80.3124
        )
        val actual = getLatLng(given.getValue("lat"), given.getValue("long"))
        val expectedLatLng = LatLng(expected.getValue("lat"), expected.getValue("long"))
        assertEquals(expectedLatLng, actual)
    }

    @Test
    fun givenEmptySpacedLatLongStringProducesValidLatLng() {
        val given = mapOf(
                "lat" to "126.3921   ",
                "long" to "     -80.3124"
        )
        val expected = mapOf(
                "lat" to 126.3921,
                "long" to -80.3124
        )
        val actual = getLatLng(given.getValue("lat"), given.getValue("long"))
        val expectedLatLng = LatLng(expected.getValue("lat"), expected.getValue("long"))
        assertEquals(expectedLatLng, actual)
    }
}