package com.tokopedia.play.broadcaster.util

import com.tokopedia.play_common.model.result.NetworkResult
import org.assertj.core.api.Assertions
import kotlin.reflect.KProperty


/**
 * Created by jegul on 11/05/21
 */
fun <T : Any> T.isEqualTo(expected: T) {
    Assertions
            .assertThat(this)
            .isEqualTo(expected)
}

fun <T : Any> T.isEqualToComparingFieldByField(expected: T) {
    Assertions
            .assertThat(this)
            .isEqualToComparingFieldByField(expected)
}

fun <T : Any> List<T>.isEqualToIgnoringFields(expected: T, vararg ignoredFields: KProperty<*>) {
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

fun <T : Throwable> T.isErrorType(error: Class<out T>) {
    Assertions
            .assertThat(this)
            .isInstanceOf(error)
}

inline fun <T> NetworkResult<T>.assertWhenSuccess(
        onSuccess: (T) -> Unit
) {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Success::class.java)

    onSuccess((this as NetworkResult.Success<T>).data)
}

inline fun NetworkResult<*>.assertWhenFailed(
        onFailed: (Throwable) -> Unit
) {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Fail::class.java)

    onFailed((this as NetworkResult.Fail).error)
}

fun NetworkResult<*>.assertIsLoading() {
    Assertions
            .assertThat(this)
            .isInstanceOf(NetworkResult.Loading::class.java)
}