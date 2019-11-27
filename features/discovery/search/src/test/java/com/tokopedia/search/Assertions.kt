package com.tokopedia.search

internal infix fun Any?.shouldBe(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}

internal infix fun Map<String, Any?>?.shouldContain(expectedKey: String) {
    if (this == null) {
        throw AssertionError("Map is null")
    }

    if (!this.containsKey(expectedKey)) {
        throw AssertionError("Map does not contain key \"$expectedKey\"")
    }
}

internal infix fun Map<String, Any?>?.shouldNotContain(expectedKey: String) {
    if (this == null) {
        throw AssertionError("Map is null")
    }

    if (this.containsKey(expectedKey)) {
        throw AssertionError("Map still contain key \"$expectedKey\"")
    }
}

internal infix fun Map<*, *>.shouldHaveSize(expectedSize: Int) {
    if (this.size != expectedSize) {
        throw AssertionError("Map size is ${this.size}, expected size: $expectedSize")
    }
}

internal fun Map<String, Any>.shouldHaveKeyValue(key: String, value: Any) {
    if (this[key] != value) {
        throw AssertionError("Value of key $key is ${this[key]}, expected value: $value")
    }
}

internal infix fun Collection<Any>?.shouldHaveSize(expectedSize: Int) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (this.size != expectedSize) {
        throw AssertionError("Collection size is ${this.size}, expected $expectedSize")
    }
}

internal infix fun <T> Collection<T>?.shouldContain(expectedValue: T) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (!this.contains(expectedValue)) {
        throw AssertionError("Collection does not contain \"$expectedValue\"")
    }
}

internal infix fun Collection<Any>?.shouldNotContain(expectedValue: Any) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (this.contains(expectedValue)) {
        throw AssertionError("Collection still contain \"$expectedValue\"")
    }
}
