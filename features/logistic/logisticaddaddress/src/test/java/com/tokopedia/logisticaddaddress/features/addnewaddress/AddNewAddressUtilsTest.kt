package com.tokopedia.logisticaddaddress.features.addnewaddress

import org.junit.Test

import org.junit.Assert.*

class AddNewAddressUtilsTest {

    @Test
    fun givenExactValue_whenExecuted_thenReturnTrue() {
        // Default Monas
        val tLat = -6.175794
        val tLong = 106.826457

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun givenSlightlyMiss_whenExecuted_thenReturnTrue() {
        val tLat = -6.1757931839
        val tLong = 106.8264570490

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun givenValueFromMapIdle_whenExecuted_thenReturnTrue() {
        val tLat = -6.175793971019989
        val tLong = 106.82645704597235

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun givenSlightlyDrag_whenExecuted_thenReturnFalse() {
        val tLat = -6.175644639000966
        val tLong = 106.82704713195562

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertFalse(actual)
    }


}