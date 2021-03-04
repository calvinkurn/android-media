package com.tokopedia.officialstore.common

import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by Lukas on 10/11/20.
 */
fun Result<Any>.handleResult(callback: (Boolean, Throwable?) -> Unit) {
    when (this) {
        is Success -> callback.invoke(true, null)
        is Fail -> callback.invoke(false, throwable)
    }
}