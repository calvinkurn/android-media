package com.tokopedia.design.widget

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log

class ObservableNestedScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr){
    private var isLoading: Boolean = false
    var listener: ScrollViewListener? = null

    fun startLoad() { isLoading = true }
    fun completeLoad() { isLoading = false }

    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)
        Log.e(javaClass.canonicalName, "scroll ended $x $y $oldX $oldY")
        val diff = getChildAt(childCount - 1).bottom - (height + scrollY)
        listener?.let {
            if (diff == 0 && !isLoading) it.onScrollEnded(this, x, y, oldX, oldY)
        }
    }

    interface ScrollViewListener{
        fun onScrollEnded(scrollView: ObservableNestedScrollView, x: Int, y: Int, oldX: Int, oldY: Int)
    }

}