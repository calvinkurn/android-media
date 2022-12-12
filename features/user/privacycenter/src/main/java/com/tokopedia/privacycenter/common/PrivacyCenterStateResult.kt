package com.tokopedia.privacycenter.common

sealed class PrivacyCenterStateResult<T> {
    class Loading<T> : PrivacyCenterStateResult<T>()
    data class Success<T>(val data: T) : PrivacyCenterStateResult<T>()
    data class Fail<T>(val error: Throwable) : PrivacyCenterStateResult<T>()
}
