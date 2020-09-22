package com.tokopedia.play_common.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created by jegul on 21/09/20
 */
class DefaultOnLifecycle<T: Any> internal constructor(): LifecycleObserver {

    internal var mOnCreate: (T) -> Unit = {}
    internal var mOnStart: (T) -> Unit = {}
    internal var mOnResume: (T) -> Unit = {}
    internal var mOnPause: (T) -> Unit = {}
    internal var mOnStop: (T) -> Unit = {}
    internal var mOnDestroy: (T) -> Unit = {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(handler: (T) -> Unit) {
        mOnCreate = handler
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(handler: (T) -> Unit) {
        mOnStart = handler
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(handler: (T) -> Unit) {
        mOnResume = handler
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(handler: (T) -> Unit) {
        mOnPause = handler
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(handler: (T) -> Unit) {
        mOnStop = handler
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(handler: (T) -> Unit) {
        mOnDestroy = handler
    }
}