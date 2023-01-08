package com.tokopedia.loginregister.common.utils

import android.text.TextUtils
import android.util.Patterns
import androidx.core.util.PatternsCompat
import com.tokopedia.sessioncommon.util.PasswordUtils

/**
 * Created by nisie on 1/27/17.
 */
object RegisterUtil {
    private const val MAX_PHONE_NUMBER = 15
    private const val MIN_PHONE_NUMBER = 8
    private const val MAX_NAME = 35
    private const val MIN_NAME = 3

    var PASSWORD_MINIMUM_LENGTH = 8
    private const val PASSWORD_MAXIMUM_LENGTH = 32

    fun isValidEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkRegexNameLocal(param: String): Boolean {
        val regex = "[A-Za-z]+".toRegex()
        return !param.replace("\\s".toRegex(), "").matches(regex)
    }

    fun isExceedMaxCharacter(text: String): Boolean {
        return text.length > MAX_NAME
    }

    fun isBelowMinChar(text: String): Boolean {
        return text.length < MIN_NAME
    }

    fun isValidPhoneNumber(phoneNo: String): Boolean {
        for (i in MIN_PHONE_NUMBER..MAX_PHONE_NUMBER) {
            if (phoneNo.matches("\\d{$i}".toRegex())) return true
        }
        return false
    }

    fun isPhoneTooShortLength(phone: String): Boolean = phone.length < MIN_PHONE_NUMBER
    fun isPhoneExceedMaximumLength(phone: String): Boolean = phone.length > MAX_PHONE_NUMBER

    fun isPasswordTooShortLength(password: String): Boolean =
        password.length < PasswordUtils.PASSWORD_MINIMUM_LENGTH

    fun isPasswordExceedMaximumLength(password: String): Boolean =
        password.length > PasswordUtils.PASSWORD_MAXIMUM_LENGTH

    fun isCanRegister(name: String, email: String, password: String): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(password)) {
            isValid = false
        } else if (password.length < PASSWORD_MINIMUM_LENGTH) {
            isValid = false
        }
        if (TextUtils.isEmpty(name)) {
            isValid = false
        } else if (checkRegexNameLocal(name)) {
            isValid = false
        } else if (isBelowMinChar(name)) {
            isValid = false
        } else if (isExceedMaxCharacter(name)) {
            isValid = false
        }
        if (TextUtils.isEmpty(email)) {
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
        }
        return isValid
    }

    private const val PREFIX_ERROR_CODE = "Kode Error"
    fun String.removeErrorCode() : String {
        if (this.contains(PREFIX_ERROR_CODE)) {
            return this.substringBefore(PREFIX_ERROR_CODE).trim()
        }
        return this
    }
}
