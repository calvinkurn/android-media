package com.tokopedia.content.test.util

import org.assertj.core.api.Assertions
import org.assertj.core.api.ListAssert
import kotlin.reflect.KProperty

/**
 * Created By : Jonathan Darwin on November 20, 2023
 */
fun <T : Any?> T.assertEqualTo(expected: T?) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

fun <T : Any?> T.assertNotEqualTo(expected: T?) {
    Assertions
        .assertThat(this)
        .isNotEqualTo(expected)
}

fun <T : Any> T.isEqualToComparingFieldByField(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualToComparingFieldByField(expected)
}

inline fun <reified T : Any?> List<T>.isContentEqualTo(expected: List<T?>) {
    ListAssert(this)
        .containsExactly(*expected.toTypedArray())
}

fun <T : Any> List<T>.isEqualToIgnoringFields(expected: List<T>, vararg ignoredFields: KProperty<*>) {
    Assertions
        .assertThat(this)
        .usingElementComparatorIgnoringFields(*ignoredFields.map(KProperty<*>::name).toTypedArray())
        .isEqualTo(expected)
}

fun <T : Any> List<T>.assertNotEmpty() {
    Assertions
        .assertThat(this)
        .isNotEmpty
}

fun <T : Any> List<T>.assertEmpty() {
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

fun <T : Any> List<T>.assertCount(count: Int) {
    Assertions
        .assertThat(size)
        .isEqualTo(count)
}

fun <K : Any, V : Any> Map<K, V>.assertNotEmpty() {
    Assertions
        .assertThat(this)
        .isNotEmpty
}

fun <K : Any, V : Any> Map<K, V>.assertEmpty() {
    Assertions
        .assertThat(this)
        .isEmpty()
}

fun <K : Any, V : Any> Map<K, V>.assertCount(count: Int) {
    Assertions
        .assertThat(size)
        .isEqualTo(count)
}

fun <T : Throwable> T.isErrorType(error: Class<out T>) {
    Assertions
        .assertThat(this)
        .isInstanceOf(error)
}

inline fun <reified T> Any.assertType(
    whenType: (T) -> Unit = {}
) {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)

    whenType(this as T)
}

infix fun <T : Any> T.equalTo(expected: T) {
    this.assertEqualTo(expected)
}
