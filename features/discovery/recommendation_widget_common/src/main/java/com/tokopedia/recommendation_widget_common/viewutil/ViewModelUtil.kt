package com.tokopedia.recommendation_widget_common.viewutil

import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by yfsx on 02/08/21.
 */

fun <T : Any> T.asSuccess(): Success<T> = Success(this)
fun Throwable.asFail(): Fail = Fail(this)
fun <T : Any> Result<T>.doSuccessOrFail(success: (Success<T>) -> Unit, fail: (Fail: Throwable) -> Unit) {
    when (this) {
        is Success -> {
            success.invoke(this)
        }
        is Fail -> {
            fail.invoke(this.throwable)
        }
    }
}
