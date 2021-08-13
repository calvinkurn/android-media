package com.tokopedia.sessioncommon.util

object PasswordUtils {

    const val PASSWORD_MINIMUM_LENGTH = 8
    const val PASSWORD_MAXIMUM_LENGTH = 32

    fun isValidPassword(password: String): Boolean = password.isNotEmpty() && isValidMinimumLength(password) && isValidMaxLength(password)
    fun isValidMinimumLength(password: String): Boolean = password.length >= PASSWORD_MINIMUM_LENGTH
    fun isValidMaxLength(password: String): Boolean = password.length <= PASSWORD_MAXIMUM_LENGTH
    fun isTooShortLength(password: String): Boolean = password.length < PASSWORD_MINIMUM_LENGTH
    fun isExceedMaximumLength(password: String): Boolean = password.length > PASSWORD_MAXIMUM_LENGTH
}