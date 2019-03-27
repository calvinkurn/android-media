package com.tokopedia.design.widget

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet

class ObservableNestedScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr){
    private var isLoading: Boolean = false
    var listener: ScrollViewListener? = null

    fun startLoad() { isLoading = true }
    fun completeLoad() { isLoading = false }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val diff = getChildAt(childCount - 1).bottom - (height + scrollY)
        listener?.let {
            if (diff == 0 && !isLoading) it.onScrollEnded(this, l, t, oldl, oldt)
        }
    }

    interface ScrollViewListener{
        fun onScrollEnded(scrollView: ObservableNestedScrollView, x: Int, y: Int, oldX: Int, oldY: Int)
    }

}