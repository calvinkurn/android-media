package com.tokopedia.kotlin.extensions.view

/**
 * Created by Ade Fulki on 2019-07-18.
 * ade.hadian@tokopedia.com
 */

fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}