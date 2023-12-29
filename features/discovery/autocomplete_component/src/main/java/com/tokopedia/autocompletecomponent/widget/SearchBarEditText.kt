package com.tokopedia.autocompletecomponent.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

class SearchBarEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var shouldPreventDismissKeyboard: Boolean = false

    fun setPreventDismissKeyboard(shouldPreventDismissKeyboard: Boolean) {
        this.shouldPreventDismissKeyboard = shouldPreventDismissKeyboard
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (shouldPreventDismissKeyboard && event?.keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }
}
