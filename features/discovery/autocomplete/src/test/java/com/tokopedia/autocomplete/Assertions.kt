package com.tokopedia.autocomplete

internal infix fun Any?.shouldBe(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}