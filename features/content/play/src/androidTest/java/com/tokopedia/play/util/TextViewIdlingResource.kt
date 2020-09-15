package com.tokopedia.play.util

import android.text.TextUtils
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.IdlingResource


/**
 * Created by mzennis on 14/09/20.
 */
class TextViewIdlingResource(
        private val textView: AppCompatTextView,
        private val name: String,
        private val exactString: String = "" // idle until the text is match with this exact string
): IdlingResource {

    override fun getName(): String = name

    private var callback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val isIdle = isIdle(textView.text.toString())
        if (isIdle) callback?.onTransitionToIdle()
        return isIdle
    }

    private fun isIdle(textString: String): Boolean = if (!TextUtils.isEmpty(exactString))
        !TextUtils.isEmpty(exactString) && !TextUtils.isEmpty(textString)
    else !TextUtils.isEmpty(textString)

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}