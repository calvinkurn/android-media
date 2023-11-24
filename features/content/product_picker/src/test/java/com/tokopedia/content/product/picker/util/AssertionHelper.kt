package com.tokopedia.content.product.picker.util

import org.assertj.core.api.Assertions

/**
 * Created By : Jonathan Darwin on September 27, 2023
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

fun <T : Any> T.isEqualToComparingFieldByField(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualToComparingFieldByField(expected)
}

inline fun <reified T> Any.assertType(
    whenType: (T) -> Unit = {}
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
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
