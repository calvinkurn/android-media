package com.tokopedia.play.util

import android.os.Handler
import androidx.annotation.Nullable


/**
 * Created by mzennis on 15/09/20.
 */
object PlayMessageDelayer {

    private const val DELAY_MILLIS = 3000L

    interface DelayerCallback {
        fun onDone(text: String?)
    }

    /**
     * Takes a String and returns it after [.DELAY_MILLIS] via a [DelayerCallback].
     * @param message the String that will be returned via the callback
     * @param callback used to notify the caller asynchronously
     */
    fun processMessage(message: String?, callback: DelayerCallback?,
                       @Nullable idlingResource: PlaySimpleIdlingResource?) {
        // The IdlingResource is null in production.
        idlingResource?.setIdleState(false)

        // Delay the execution, return message via callback.
        Handler().postDelayed({
            if (callback != null) {
                callback.onDone(message)
                idlingResource?.setIdleState(true)
            }
        }, DELAY_MILLIS)
    }
}