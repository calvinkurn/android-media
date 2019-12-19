package com.tokopedia.common.travel.widget

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet

/**
 * @author by jessica on 2019-09-16
 */

class InstantAutoCompleteTextView: AppCompatAutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setOnTouchListener { _, _ ->
            performFiltering(text, 0)
            false
        }
    }

    override fun enoughToFilter(): Boolean = true

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) performFiltering(text, 0)
    }
}