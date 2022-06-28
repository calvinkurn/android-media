package com.tokopedia.people.utils

import com.tokopedia.kotlin.extensions.view.thousandFormatted
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.math.RoundingMode
import kotlin.reflect.KProperty1

object UserProfileUtils {
    public fun getFormattedNumber(number: Long): String {
        return if (number >= 10000) {
            number.thousandFormatted(hasSpace = true, digit = 0, roundingMode = RoundingMode.DOWN)
        } else {
            number.thousandFormatted(hasSpace = true, digit = 1, roundingMode = RoundingMode.DOWN)
        }
    }
}

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

internal fun <T: Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue : T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

internal fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}