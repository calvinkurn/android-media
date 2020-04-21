package com.tokopedia.product.manage.verification

import androidx.lifecycle.LiveData
import junit.framework.TestCase.assertEquals

fun LiveData<*>.verifyValueEquals(expected: Any?) {
    val actual = value
    assertEquals(expected, actual)
}