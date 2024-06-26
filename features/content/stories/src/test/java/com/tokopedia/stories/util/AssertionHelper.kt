package com.tokopedia.stories.util

import org.assertj.core.api.Assertions

fun <T : Any> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

fun <T : Any> T.assertNotEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isNotEqualTo(expected)
}

fun Boolean.assertTrue() {
    Assertions
        .assertThat(this)
        .isTrue
}

fun Boolean.assertFalse() {
    Assertions
        .assertThat(this)
        .isFalse
}

inline fun <reified T> Any.assertType(
    whenType: (T) -> Unit
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
}
