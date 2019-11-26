package com.tokopedia.kotlin.extensions.view

import java.net.URLDecoder
import java.net.URLEncoder

/**
 * @author by nisie on 12/02/19.
 */

fun String?.toIntOrZero(): Int {
    return this?.toIntOrNull() ?: 0
}

fun String?.toLongOrString() = this?.toLongOrNull() ?: this

fun String?.toLongOrZero(): Long {
    return this?.toLongOrNull() ?: 0
}

fun String?.toFloatOrZero(): Float {
    return this?.toFloatOrNull() ?: 0f
}

fun String?.toDoubleOrZero(): Double {
    return this?.toDoubleOrNull() ?: 0f.toDouble()
}

fun String?.toEmptyStringIfNull(): String {
    return this ?: ""
}

fun CharSequence?.hasValue(): Boolean {
    return !this.isNullOrBlank()
}

fun String.decodeToUtf8(): String = URLDecoder.decode(this, "UTF-8")
fun String.encodeToUtf8(): String = URLEncoder.encode(this, "UTF-8")

fun String.isEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.toBlankOrString(): String {
    return this?:""
}

private const val NUMBER_ONLY_REGEX = "[^\\d]"

fun String.getDigits(): Int? {
    return try {
        val rex = Regex(NUMBER_ONLY_REGEX)
        rex.replace(this, "").toInt()
    } catch (e: Exception) {
        null
    }
}