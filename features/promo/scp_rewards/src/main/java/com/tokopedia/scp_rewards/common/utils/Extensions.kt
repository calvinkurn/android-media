package com.tokopedia.scp_rewards.common.utils

import android.view.View
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(
    context: CoroutineContext = coroutineContext,
    block: suspend CoroutineScope.() -> Unit,
    onError: suspend (Throwable) -> Unit
) =
    launch(context) {
        try {
            block()
        } catch (t: Throwable) {
            if (t is CancellationException) {
                throw t
            } else {
                try {
                    onError(t)
                } catch (e: Throwable) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

fun Long?.isNullOrZero(defaultValue: Long): Long {
    if (this == null || this == 0L) return defaultValue
    return this
}

fun Int?.isNullOrZero(defaultValue: Int): Int {
    if (this == null || this == 0) return defaultValue
    return this
}

fun View.show() {
    if (visibility == View.GONE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}


