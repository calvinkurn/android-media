package com.tokopedia.product.addedit.detail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.tokopedia.unifycomponents.TextFieldUnify2

class TextFieldWithImeCallback(context: Context, attrs: AttributeSet) : TextFieldUnify2(context, attrs) {

    private var imeCallback: () -> Unit = {}

    fun setKeyImeChangeCallback(callback: () -> Unit) {
        this.imeCallback = callback
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            imeCallback.invoke()
            return false
        }
        return super.dispatchKeyEventPreIme(event)
    }
}