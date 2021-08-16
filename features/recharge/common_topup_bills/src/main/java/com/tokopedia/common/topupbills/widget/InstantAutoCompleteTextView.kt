package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

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