package com.tokopedia.navigation.presentation.util

import android.view.View
import androidx.core.view.ViewCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend inline fun View.awaitLayout() = suspendCancellableCoroutine { cont ->
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        cont.resume(Unit)
    } else {
        val listener = object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                view.removeOnLayoutChangeListener(this)
                if (cont.isActive) cont.resume(Unit)
            }
        }

        cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
        addOnLayoutChangeListener(listener)
    }
}
