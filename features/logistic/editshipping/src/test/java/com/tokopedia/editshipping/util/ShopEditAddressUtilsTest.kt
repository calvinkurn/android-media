package com.tokopedia.editshipping.util

import org.junit.Assert
import org.junit.Test

class ShopEditAddressUtilsTest {

    @Test
    fun `normalize String Success`() {
        val address = "Jln. Dr. Satrio no, 364. Jakarta Selatan"

        val expectedResult = "dr satrio jakarta selatan"

        val actualResult = ShopEditAddressUtils.normalize(address)

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Validator Address return true`() {
        val address1 = "Jln. Dr. Satrio no, 364. Jakarta Selatan"
        val address2 = "Jln. Dr. Satrio no, 364, Setiabudi, Jakarta Selatan"

        val expectedResult = true

        val actualResult = ShopEditAddressUtils.validatorAddress(address1, address2)

        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Validator Address return false`() {
        val address1 = "Jln. Dr. Satrio no, 364. Jakarta Selatan"
        val address2 = "Jalan Karet Kuningan, Setiabudi, jakarta Selatan"

        val expectedResult = false

        val actualResult = ShopEditAddressUtils.validatorAddress(address1, address2)

        Assert.assertEquals(expectedResult, actualResult)
    }
}