package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.tokopedia.unifycomponents.TextFieldUnify2
import org.jetbrains.annotations.NotNull

class TextField3 @JvmOverloads constructor(
    @NotNull context: Context, attrs: AttributeSet) : TextFieldUnify2(context, attrs) {

    var keyImeChangeListener: KeyImeChange? = null

    interface KeyImeChange {
        fun onPreKeyIme(event: KeyEvent)
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        keyImeChangeListener?.run {
            onPreKeyIme(event)
        }
        return super.dispatchKeyEventPreIme(event)
    }
}