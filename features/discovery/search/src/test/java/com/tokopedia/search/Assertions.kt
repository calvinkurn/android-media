package com.tokopedia.search

internal infix fun Any?.shouldBe(expectedValue: Any) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}