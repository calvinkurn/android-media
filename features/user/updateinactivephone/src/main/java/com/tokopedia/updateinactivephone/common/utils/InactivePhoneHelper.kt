package com.tokopedia.updateinactivephone.common.utils

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS

const val REGEX_PHONE_NUMBER = """[+()\-\s]"""
const val REGEX_PHONE_NUMBER_REGION = "^(\\+\\d{1,2})"

fun String.reformatPhoneNumber(): String {
    val regionRegex = Regex(REGEX_PHONE_NUMBER_REGION)
    val symbolRegex = Regex(REGEX_PHONE_NUMBER)
    if (this.contains(regionRegex)) {
        return this.replace(regionRegex, "0")
    }

    return this.replace(symbolRegex, "")
}

fun String.getValidEmail(): String? {
    return when {
        this.contains('*') -> ""
        EMAIL_ADDRESS.matcher(this).matches() -> this
        else -> null
    }
}