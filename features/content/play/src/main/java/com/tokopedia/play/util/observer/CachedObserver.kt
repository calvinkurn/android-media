package com.tokopedia.play.util.observer

import androidx.lifecycle.Observer
import kotlin.reflect.KProperty1

/**
 * Created by jegul on 05/07/21
 */
class CachedObserver<T: Any>(private val onChanged: CachedObserver<T>.(T?, T) -> Unit) : Observer<T> {

    private var prevState : T? = null
    private var currState: T? = null

    override fun onChanged(newState: T) {
        prevState = currState
        currState = newState
        onChanged(prevState, newState)
    }

    fun <V> isValueChanged(prop: KProperty1<T, V>): Boolean {
        val prevState = this.prevState
        val currState = this.currState

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