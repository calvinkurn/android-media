package com.tokopedia.home_account.explicitprofile.wrapper

import com.tokopedia.network.exception.MessageErrorException

sealed class ExplicitProfileResult<T> {
    class Loading<T> : ExplicitProfileResult<T>()
    data class Success<T>(val data: T) : ExplicitProfileResult<T>()
    data class Failure<T>(val error: MessageErrorException) : ExplicitProfileResult<T>()
}