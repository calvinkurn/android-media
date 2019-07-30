package com.tokopedia.kotlin.extensions.view

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