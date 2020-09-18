package com.tokopedia.play_common.util.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by jegul on 07/09/20
 */
suspend fun LifecycleOwner.awaitResume() {
    lifecycle.awaitResume()
}

suspend fun Lifecycle.awaitResume() = suspendCancellableCoroutine<Unit> { cont ->
    if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        cont.resume(Unit)
        return@suspendCancellableCoroutine
    }

    val observer = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            removeObserver(this)
            cont.resume(Unit)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            cont.cancel()
        }
    }

    cont.invokeOnCancellation {
        removeObserver(observer)
    }

    addObserver(observer)
}