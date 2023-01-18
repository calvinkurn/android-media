package com.tokopedia.tokopedia.feedplus.helper

import org.assertj.core.api.Assertions

/**
 * @author by astidhiyaa on 23/09/22
 */
inline fun <reified T> Any.assertType(
    whenType: (T) -> Unit
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
}

fun <T : Any?> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

fun <T : Boolean> T.assertTrue() {
    Assertions
        .assertThat(this)
        .isEqualTo(true)
}

fun <T : Boolean> T.assertFalse() {
    Assertions
        .assertThat(this)
        .isEqualTo(false)
}
