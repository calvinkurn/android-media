package com.tokopedia.utils.phonenumber

import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform
import org.junit.Assert.assertEquals
import org.junit.Test

class PhoneNumberUtilTest {

    @Test
    fun transformFromCommonPhoneNumber() {
        val input = "08123456789"
        val expected = "0812-3456-789"
        val output: String
        output = transform(input)
        assertEquals(expected, output)
    }

    @Test
    fun transformFromCountryCodePhoneNumber() {
        val input = "628123456789"
        val expected = "0812-3456-789"
        val output: String
        output = transform(input)
        assertEquals(expected, output)
    }

    @Test
    fun transformFromCountryCodePlusPhoneNumber() {
        val input = "+628123456789"
        val expected = "0812-3456-789"
        val output: String
        output = transform(input)
        assertEquals(expected, output)
    }


}