package com.tokopedia.feedplus.view.util

/**
 * @author by yoasfs on 2019-12-30
 */

fun <T> MutableList<T>.copy() : MutableList<T> {
    val arr = mutableListOf<T>()
    for(i in this) {
        arr.add(i)
    }
    return arr
}