package com.tokopedia.play_common.lifecycle

/**
 * Created by jegul on 21/09/20
 */
class DefaultOnLifecycle<T: Any> internal constructor() {

    internal var mOnCreate: (T) -> Unit = {}
    internal var mOnStart: (T) -> Unit = {}
    internal var mOnResume: (T) -> Unit = {}
    internal var mOnPause: (T) -> Unit = {}
    internal var mOnStop: (T) -> Unit = {}
    internal var mOnDestroy: (T) -> Unit = {}

    fun onCreate(handler: (T) -> Unit) {
        mOnCreate = handler
    }

    fun onStart(handler: (T) -> Unit) {
        mOnStart = handler
    }

    fun onResume(handler: (T) -> Unit) {
        mOnResume = handler
    }

    fun onPause(handler: (T) -> Unit) {
        mOnPause = handler
    }

    fun onStop(handler: (T) -> Unit) {
        mOnStop = handler
    }

    fun onDestroy(handler: (T) -> Unit) {
        mOnDestroy = handler
    }
}