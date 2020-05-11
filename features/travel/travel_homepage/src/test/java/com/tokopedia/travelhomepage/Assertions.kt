package com.tokopedia.travelhomepage

/**
 * @author by furqan on 04/02/2020
 */

internal infix fun Any?.shouldBe(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be equals to $expectedValue")
    }
}