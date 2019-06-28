package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.text.Spannable
import android.util.AttributeSet
import com.tokopedia.design.component.TextViewCompat

class SelectionTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextViewCompat(context, attrs, defStyleAttr){

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        if (selStart == -1 && selEnd == -1) {
            val text = text
            if (text is Spannable) {
                clearFocus()
            }
        } else {
            super.onSelectionChanged(selStart, selEnd)
        }
    }
}