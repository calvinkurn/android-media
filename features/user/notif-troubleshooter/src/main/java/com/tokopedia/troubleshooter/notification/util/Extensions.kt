package com.tokopedia.troubleshooter.notification.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.lang.Exception

fun <T> MutableList<T>.dropFirst() {
    if (isEmpty()) return
    removeAt(0)
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun String.prefixToken(): String {
    return try {
        this.substring(this.length - 8)
    } catch (e: Exception) {
        ""
    }
}

inline fun <T> Iterable<T>.getWithIndex(predicate: (T) -> Boolean): Pair<Int, T>? {
    forEachIndexed { index, element -> if (predicate(element)) return Pair(index, element) }
    return null
}

fun <T, K, J, R> LiveData<T>.combineWith(
        f1: LiveData<K>,
        f2: LiveData<J>,
        block: (T?, K?, J?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, f1.value, f2.value)
    }
    result.addSource(f1) {
        result.value = block(this.value, f1.value, f2.value)
    }
    result.addSource(f2) {
        result.value = block(this.value, f1.value, f2.value)
    }
    return result
}