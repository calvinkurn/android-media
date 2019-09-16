package com.tokopedia.common.travel.widget

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.widget.AutoCompleteTextView

/**
 * @author by jessica on 2019-09-16
 */

class InstantAutoCompleteTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AutoCompleteTextView(context, attrs, defStyleAttr) {
    override fun enoughToFilter(): Boolean = true

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) performFiltering(text, 0)
    }
}