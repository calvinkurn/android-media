package com.tokopedia.people.utils

import android.view.View
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

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

internal fun <T : Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue: T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

fun View.showErrorToast(
    message: String,
    duration: Int = Toaster.LENGTH_LONG,
    type: Int = Toaster.TYPE_ERROR,
) {
    showToast(message, duration, type)
}

fun View.showToast(
    message: String,
    duration: Int = Toaster.LENGTH_LONG,
    type: Int = Toaster.TYPE_NORMAL,
) {
    Toaster.build(
        this,
        message,
        duration,
        type,
    ).show()
}
