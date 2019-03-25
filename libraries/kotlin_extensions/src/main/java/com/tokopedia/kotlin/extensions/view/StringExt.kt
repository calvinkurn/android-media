package com.tokopedia.kotlin.extensions.view

/**
 * @author by nisie on 12/02/19.
 */

fun String?.toIntOrZero(): Int {
    return this?.toIntOrNull()?: 0
}
fun String?.toLongOrZero(): Long {
    return this?.toLongOrNull()?: 0
}