package com.tokopedia.createpost.view.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.tokopedia.design.component.EditTextCompat

class ClearFocusEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : EditTextCompat(context, attrs, defStyleAttr){

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK )
            clearFocus()
        return super.onKeyPreIme(keyCode, event)
    }
}
