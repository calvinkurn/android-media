package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.tokopedia.logisticaddaddress.common.AddressConstants
import org.junit.Assert
import org.junit.Test

class AddNewAddressUtilsTest {

    @Test
    fun `given exact value when execute`() {
        val tLat = AddressConstants.DEFAULT_LAT
        val tLong = AddressConstants.DEFAULT_LONG

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        Assert.assertTrue(actual)
    }

    @Test
    fun `given slightly miss when execute`() {
        val tLat = AddressConstants.DEFAULT_LAT - 0.000009
        val tLong = AddressConstants.DEFAULT_LONG + 0.000006

        val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

        Assert.assertTrue(actual)
    }
}