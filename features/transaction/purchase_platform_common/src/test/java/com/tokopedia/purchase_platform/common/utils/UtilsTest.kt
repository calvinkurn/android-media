package com.tokopedia.purchase_platform.common.utils

import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun `Remove decimal suffix from currency string, when given empty string, should returns empty string`() {
        Assert.assertEquals("", "".removeDecimalSuffix())
    }

    @Test
    fun `Remove decimal suffix from currency string, when given currency string with decimal suffix, should returns currency string without decimal suffix`() {
        Assert.assertEquals("Rp12.000", "Rp12.000.00".removeDecimalSuffix())
    }

    @Test
    fun `Remove decimal suffix from currency string, when given currency string without decimal suffix, should returns joined string`() {
        Assert.assertEquals("Rp13.000", "Rp13.000".removeDecimalSuffix())
    }
}
