package com.tokopedia.play.util

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
fun <T: Any> CachedState<T>.isNotChanged(fn: (T) -> Any?): Boolean {
    val prevValue = this.prevValue ?: return false
    return fn(prevValue) == fn(value)
}

fun <T: Any> CachedState<T>.isNotChanged(vararg fns: (T) -> Any?): Boolean {
    val prevValue = this.prevValue ?: return false
    return fns.all { it(prevValue) == it(value) }
}

fun <T: Any> CachedState<T>.isAnyChanged(vararg fns: (T) -> Any?): Boolean {
    val prevValue = this.prevValue ?: return true
    return fns.any { it(prevValue) != it(value) }
}

fun <T: Any> CachedState<T>.isChanged(fn: (T) -> Any?) = !isNotChanged(fn)