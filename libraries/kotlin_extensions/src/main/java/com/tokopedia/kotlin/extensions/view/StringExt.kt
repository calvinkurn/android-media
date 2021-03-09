package com.tokopedia.kotlin.extensions.view

import android.text.Html
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

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

fun String?.toZeroStringIfNull(): String {
    return this ?: "0"
}

fun String?.toZeroStringIfNullOrBlank(): String {
    return when {
        this == null -> {
            this.toZeroStringIfNull()
        }
        this.isBlank() -> {
            "0"
        }
        else -> {
            this
        }
    }
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

fun String?.convertStrObjToHashMap(): HashMap<String, Any> {
    val arr = this?.split(",")
    val map = HashMap<String, Any>()
    if (arr != null) {
        for (str in arr) {
            val newStr = str.replace("{", "").replace("}", "").replace("\"", "")
            val splited = newStr.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            map[splited[0]] = splited[1].trim { it <= ' ' }

        }
    }
    return map
}

fun String.parseAsHtml(): CharSequence {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.asUpperCase(): String {
    val locale = Locale("id")
    return this.toUpperCase(locale)
}

fun String?.toEmpty(): String = ""