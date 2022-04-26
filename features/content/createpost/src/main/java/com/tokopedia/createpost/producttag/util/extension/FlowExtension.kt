package com.tokopedia.createpost.producttag.util.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */

data class CachedState<T>(val prevValue: T? = null, val value: T) {

    fun <V> isValueChanged(prop: KProperty1<T, V>): Boolean {
        val prevState = this.prevValue
        val currState = this.value

        return when {
            currState == null -> false
            prevState == null -> true
            else -> {
                val prevValue = prop.get(prevState)
                val currentValue = prop.get(currState)
                prevValue != currentValue
            }
        }
    }
}

fun <T: Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue : T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}