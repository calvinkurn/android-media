package com.tokopedia.travelhomepage.homepage

/**
 * @author by furqan on 04/02/2020
 */

internal infix fun Any?.shouldBeEquals(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be equals to $expectedValue")
    }
}