package com.tokopedia.logisticaddaddress.features.addnewaddress

import org.junit.Test

import org.junit.Assert.*

class AddNewAddressUtilsTest {

    @Test
    fun whenGivenExactReturnTrue() {
        // Default Monas
        val tLat = -6.175794
        val tLong = 106.826457

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun whenGivenSlightMissReturnTrue() {
        val tLat = -6.1757931839
        val tLong = 106.8264570490

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun whenGivenValueFromMapIdleReturnTrue() {
        val tLat = -6.175793971019989
        val tLong = 106.82645704597235

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun whenGivenSlightDragReturnFalse() {
        val tLat = -6.175644639000966
        val tLong = 106.82704713195562

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertFalse(actual)
    }


}