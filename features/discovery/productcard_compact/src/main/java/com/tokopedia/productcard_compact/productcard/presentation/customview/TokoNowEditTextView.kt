package com.tokopedia.productcard_compact.productcard.presentation.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.Typography

class TokoNowEditTextView constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var onFocusChangedListener: ((focused: Boolean) -> Unit)? = null

    /**
     * only hard button will listen (ex: enter, erase, etc)
     */
    var onEnterClickedListener: (() -> Unit)? = null
    var onAnyKeysClickedListener: (() -> Unit)? = null

    init {
        typeface = Typography.getFontType(
            context = context,
            isBold = false,
            fontVariant = Typography.DISPLAY_2
        )

        setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_NN950))
    }

    override fun onFocusChanged(
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        onFocusChangedListener?.invoke(focused)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            clearFocus()
            onEnterClickedListener?.invoke()
        } else {
            onAnyKeysClickedListener?.invoke()
        }
        return true
    }

}
