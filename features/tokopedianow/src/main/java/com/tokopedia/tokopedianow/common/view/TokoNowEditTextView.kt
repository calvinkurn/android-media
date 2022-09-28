package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.tokopedia.unifyprinciples.Typography

class TokoNowEditTextView(context: Context, attributeSet: AttributeSet? = null) : AppCompatEditText(context, attributeSet) {
    var focusChangedListener: ((focused: Boolean) -> Unit)? = null

    init {
        typeface = Typography.getFontType(context, false, Typography.DISPLAY_2)
    }

    override fun onFocusChanged(
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        focusChangedListener?.invoke(focused)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }
}
