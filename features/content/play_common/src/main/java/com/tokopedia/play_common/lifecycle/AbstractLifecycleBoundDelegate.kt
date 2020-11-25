package com.tokopedia.play_common.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by jegul on 22/09/20
 */
abstract class AbstractLifecycleBoundDelegate<LO: LifecycleOwner, T: Any> internal constructor(
        private val creator: (LO) -> T,
        private val onLifecycle: DefaultOnLifecycle<T>? = null
) : ReadOnlyProperty<LO, T> {

    private var mInstance: T? = null

    abstract val lock: Any

    override fun getValue(thisRef: LO, property: KProperty<*>): T {
        mInstance?.let { return it }

        return synchronized(lock) {
            mInstance?.let { return it }

            addObserver(thisRef, getMandatoryLifecycleObserver(onLifecycle))

            if (!getValidLifecycleOwner(thisRef).lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                throw IllegalStateException("${property.name} has not been initialized")
            }

            creator(thisRef).also {
                mInstance = it
            }
        }
    }

    abstract fun addObserver(owner: LO, observer: LifecycleObserver)

    abstract fun getValidLifecycleOwner(thisRef: LO): LifecycleOwner

    private fun getMandatoryLifecycleObserver(onLifecycle: DefaultOnLifecycle<T>?) = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            mInstance?.let {
                onLifecycle?.mOnCreate?.invoke(it)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            mInstance?.let {
                onLifecycle?.mOnStart?.invoke(it)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            mInstance?.let {
                onLifecycle?.mOnResume?.invoke(it)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            mInstance?.let {
                onLifecycle?.mOnPause?.invoke(it)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            mInstance?.let {
                onLifecycle?.mOnStop?.invoke(it)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            mInstance?.let {
                onLifecycle?.mOnDestroy?.invoke(it)
            }
            mInstance = null
        }
    }
}