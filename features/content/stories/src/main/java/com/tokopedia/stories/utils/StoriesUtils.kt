package com.tokopedia.stories.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

internal fun <T: Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue : T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

internal data class CachedState<T>(val prevValue: T? = null, val value: T) {

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

internal fun Int.getRandomNumber(): Int {
    val oldValue = this
    val newValue = (1 until 100).random()
    return if (oldValue == newValue) newValue.plus(1) else newValue
}
