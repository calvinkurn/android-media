package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.tokopedia.logisticaddaddress.common.AddressConstants
import org.junit.Test

import org.junit.Assert.*

class AddNewAddressUtilsTest {

    @Test
    fun givenExactValue_whenExecuted_thenReturnTrue() {
        // Default Monas
        val tLat = AddressConstants.DEFAULT_LAT
        val tLong = AddressConstants.DEFAULT_LONG

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

    @Test
    fun givenSlightlyMiss_whenExecuted_thenReturnTrue() {
        val tLat = AddressConstants.DEFAULT_LAT - 0.000009
        val tLong = AddressConstants.DEFAULT_LONG + 0.000006

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        assertTrue(actual)
    }

}