package com.tokopedia.play.util

import android.view.View
import androidx.test.espresso.IdlingResource


/**
 * Created by mzennis on 14/09/20.
 */
class ViewIdlingResource(
        private val view: View,
        private val name: String
): IdlingResource {

    override fun getName(): String = name

    private var callback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val isIdle = view.isClickable
        if (isIdle) callback?.onTransitionToIdle()
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}