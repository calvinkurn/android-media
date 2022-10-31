package com.tokopedia.loginregister.login.view.custom

import android.text.NoCopySpan
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class NonCopyClickableSpan(private val onClick: () -> Unit, val updateDrawableStateCallback: (TextPaint) -> Unit): ClickableSpan(), NoCopySpan {
    override fun onClick(widget: View) {
        onClick()
    }

    override fun updateDrawState(ds: TextPaint) {
        updateDrawableStateCallback(ds)
    }
}
