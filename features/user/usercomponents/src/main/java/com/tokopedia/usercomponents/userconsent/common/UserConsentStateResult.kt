package com.tokopedia.usercomponents.userconsent.common

sealed class UserConsentStateResult <T> {
    class Loading<T> : UserConsentStateResult<T>()
    data class Success<T>(val data: T?): UserConsentStateResult<T>()
    data class Fail<T>(val error: Throwable) : UserConsentStateResult<T>()
}