package com.tokopedia.search

internal infix fun Any?.shouldBe(expectedValue: Any) {
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

internal infix fun Collection<Any>?.shouldHaveSize(expectedSize: Int) {
    if (this == null) {
        throw AssertionError("Collection is null")
    }

    if (this.size != expectedSize) {
        throw AssertionError("Collection size is ${this.size}, expected $expectedSize")
    }
}

internal infix fun Collection<Any>?.shouldContain(expectedValue: Any) {
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
