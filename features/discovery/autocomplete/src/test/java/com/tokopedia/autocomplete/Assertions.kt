package com.tokopedia.autocomplete

internal infix fun Any?.shouldBe(expectedValue: Any?) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}

internal inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
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