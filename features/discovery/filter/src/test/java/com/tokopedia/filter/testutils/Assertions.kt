package com.tokopedia.filter.testutils

internal infix fun Any?.shouldBe(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}

internal fun Any?.shouldBe(expectedValue: Any?, customFailMessage: String = "") {
    if (this != expectedValue) {
        val message = if (customFailMessage.isNotEmpty()) customFailMessage else "$this should be $expectedValue"
        throw AssertionError(message)
    }
}