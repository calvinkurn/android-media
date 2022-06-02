package com.tokopedia.usercomponents.userconsent.common

sealed class ConsentStateResult <T> {
    class Loading<T> : ConsentStateResult<T>()
    data class Success<T>(val data: T?): ConsentStateResult<T>()
    data class Fail<T>(val error: Throwable) : ConsentStateResult<T>()
}