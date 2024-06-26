package com.tokopedia.play.util

import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import org.assertj.core.api.Assertions
import org.assertj.core.api.ListAssert
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * Created by jegul on 11/05/21
 */
@Deprecated(message = "Use assertEqualTo")
fun <T : Any?> T.isEqualTo(expected: T) {
    Assertions
            .assertThat(this)
            .isEqualTo(expected)
}

fun <T : Any?> T.assertEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isEqualTo(expected)
}

fun <T : Any?> T.assertNotEqualTo(expected: T) {
    Assertions
        .assertThat(this)
        .isNotEqualTo(expected)
}

fun <T : Any?> T.isEqualToComparingFieldByField(expected: T) {
    Assertions
            .assertThat(this)
            .isEqualToComparingFieldByField(expected)
}

fun <T: Any?> T.isEqualToIgnoringFields(expected: T, vararg ignoreFields: String) {
    Assertions
            .assertThat(this)
            .isEqualToIgnoringGivenFields(expected, *ignoreFields)
}

fun <T : Any?> T.isEqualToIgnoringFields(expected: T, vararg field: KProperty1<*, *>) {
    isEqualToIgnoringFields(expected, *field.map { it.name }.toTypedArray())
}

inline fun <reified T : Any> List<T>.isContentEqualTo(expected: List<T>) {
    ListAssert(this)
            .containsExactly(*expected.toTypedArray())
}

fun <T : Any> List<T>.isEqualToIgnoringFields(expected: List<T>, vararg ignoredFields: KProperty<*>) {
    Assertions
            .assertThat(this)
            .usingElementComparatorIgnoringFields(*ignoredFields.map(KProperty<*>::name).toTypedArray())
            .isEqualTo(expected)
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

fun <T: Any> List<T>.assertCount(count: Int) {
    Assertions
            .assertThat(size)
            .isEqualTo(count)
}

fun <K: Any, V: Any> Map<K, V>.assertNotEmpty() {
    Assertions
            .assertThat(this)
            .isNotEmpty
}

fun <K: Any, V: Any> Map<K, V>.assertEmpty() {
    Assertions
            .assertThat(this)
            .isEmpty()
}

fun <K: Any, V: Any> Map<K, V>.assertCount(count: Int) {
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
        whenType: (T) -> Unit
) {
    Assertions
            .assertThat(this)
            .isInstanceOf(T::class.java)

    whenType(this as T)
}

@Deprecated(
    message = "Use assertTrue()",
    replaceWith = ReplaceWith("assertTrue()"),
)
fun Boolean.isTrue() {
    assertTrue()
}

@Deprecated(
    message = "Use assertFalse()",
    replaceWith = ReplaceWith("assertFalse()"),
)
fun Boolean.isFalse() {
    assertFalse()
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

@Deprecated(
    message = "Use assertInstanceOf()",
    replaceWith = ReplaceWith("assertInstanceOf()"),
)
fun <T : Any?> Any.isInstanceOf(expected: Class<T>) {
    Assertions
            .assertThat(this)
            .isInstanceOf(expected)
}

inline fun <reified T : Any?> Any.assertInstanceOf() {
    Assertions
        .assertThat(this)
        .isInstanceOf(T::class.java)
}

inline fun <reified T: Throwable> throwsException(fn: () -> Any) {
    try {
        fn()
    } catch (e: Throwable) {
        Assertions
                .assertThat(e)
                .isInstanceOf(T::class.java)
    }
}

/**
 * Network Result
 */
inline fun <T> NetworkResult<T>.assertWhenSuccess(
        onSuccess: (T) -> Unit
) {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Success::class.java)

    onSuccess((this as NetworkResult.Success<T>).data)
}

fun NetworkResult<*>.assertSuccess() = assertWhenSuccess {  }

inline fun NetworkResult<*>.assertWhenFailed(
        onFailed: (Throwable) -> Unit
) {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Fail::class.java)

    onFailed((this as NetworkResult.Fail).error)
}

fun NetworkResult<*>.assertFailed() = assertWhenFailed {  }

fun NetworkResult<*>.assertLoading() {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Loading::class.java)
}

/**
 * Page Result
 */
fun <T> PageResult<T>.assertWhenLoading(
        onLoading: (data: T) -> Unit
) {
    Assertions
            .assertThat(state)
            .isInstanceOf(PageResultState.Success::class.java)

    onLoading(currentValue)
}

fun <T> PageResult<T>.assertLoading() = assertWhenLoading {  }

fun <T> PageResult<T>.assertWhenSuccess(
        onSuccess: (state: PageResultState.Success, data: T) -> Unit
) {
    Assertions
            .assertThat(state)
            .isInstanceOf(PageResultState.Success::class.java)

    onSuccess(state as PageResultState.Success, currentValue)
}

fun <T> PageResult<T>.assertSuccess() = assertWhenSuccess { _, _ ->  }

fun <T> PageResult<T>.assertWhenFailed(
        onFailed: (state: PageResultState.Fail, data: T) -> Unit
) {
    Assertions
            .assertThat(state)
            .isInstanceOf(PageResultState.Fail::class.java)

    onFailed(state as PageResultState.Fail, currentValue)
}

fun <T> PageResult<T>.assertFailed() = assertWhenFailed { _, _ ->  }