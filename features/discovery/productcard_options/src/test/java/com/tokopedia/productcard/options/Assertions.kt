package com.tokopedia.productcard.options

internal inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}

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

internal infix fun <T> Collection<T>?.shouldContain(predicate: (T) -> Boolean) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (!this.any(predicate)) {
        throw AssertionError("Collection does not contain element with the given predicate")
    }
}

internal infix fun <T> Collection<T>?.shouldNotContain(predicate: (T) -> Boolean) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (this.any(predicate)) {
        throw AssertionError("Collection contains element with the given predicate")
    }
}