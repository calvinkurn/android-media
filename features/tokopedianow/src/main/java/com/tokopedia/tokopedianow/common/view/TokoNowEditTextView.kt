package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.Typography

class TokoNowEditTextView constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var focusChangedListener: ((focused: Boolean) -> Unit)? = null

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
        focusChangedListener?.invoke(focused)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

}
