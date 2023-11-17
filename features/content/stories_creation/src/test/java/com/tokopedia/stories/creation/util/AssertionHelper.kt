package com.tokopedia.stories.creation.util

import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
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

fun <T: Any> List<T>.assertNotEmpty() {
    Assertions
        .assertThat(this)
        .isNotEmpty
}

fun <T: Any> List<T>.assertEmpty() {
    Assertions
        .assertThat(this)
        .isEmpty()
}

fun String.assertEmpty() {
    Assertions
        .assertThat(this)
        .isEmpty()
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
    whenType: (T) -> Unit = {}
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
}
