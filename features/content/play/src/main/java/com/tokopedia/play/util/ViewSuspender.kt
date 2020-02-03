package com.tokopedia.play.util

import android.view.View
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by jegul on 02/01/20
 */
suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->
    // This lambda is invoked immediately, allowing us to create
    // a callback/listener

    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(view: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            // The next layout has happened!
            // First remove the listener to not leak the coroutine
            view.removeOnLayoutChangeListener(this)
            // Finally resume the continuation, and
            // wake the coroutine up
            cont.resume(Unit)
        }
    }
    // If the coroutine is cancelled, remove the listener
    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
    // And finally add the listener to view
    addOnLayoutChangeListener(listener)

    // The coroutine will now be suspended. It will only be resumed
    // when calling cont.resume() in the listener above
}