package com.tokopedia.play_common.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 09/09/20
 */
class LifecycleBoundDelegate<LO: LifecycleOwner, T: Any>(
        private val creator: (LO) -> T,
        private val onDestroy: (T) -> Unit
) : ReadOnlyProperty<LO, T> {

    private var mInstance: T? = null

    override fun getValue(thisRef: LO, property: KProperty<*>): T {
        mInstance?.let { return it }

        thisRef.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy(owner: LifecycleOwner) {
                mInstance?.let { onDestroy(it) }
                mInstance = null
            }
        })

        if (!thisRef.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("${property.name} has not been initialized")
        }

        return creator(thisRef).also {
            mInstance = it
        }
    }
}