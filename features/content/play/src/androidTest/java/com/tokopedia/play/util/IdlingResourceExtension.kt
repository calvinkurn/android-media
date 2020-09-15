package com.tokopedia.play.util

import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.IdlingResource


/**
 * Created by mzennis on 15/09/20.
 */
fun textViewIdlingResource(textView: AppCompatTextView, name: String) = object : IdlingResource {

    override fun getName(): String = name

    private var callback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val isIdle = textView.isClickable
        if (isIdle) callback?.onTransitionToIdle()
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}