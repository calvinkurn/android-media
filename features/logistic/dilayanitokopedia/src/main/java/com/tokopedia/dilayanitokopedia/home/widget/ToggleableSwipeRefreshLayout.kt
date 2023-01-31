package com.tokopedia.dilayanitokopedia.home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ToggleableSwipeRefreshLayout : SwipeRefreshLayout {
    private var canChildScrollUp = false
    private var mTouchSlop: Int
    private val mPrevX = 0f

    constructor(context: Context) : super(context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(event)
    }

    fun setCanChildScrollUp(canChildScrollUp: Boolean) {
        this.canChildScrollUp = canChildScrollUp
    }

    override fun canChildScrollUp(): Boolean {
        return canChildScrollUp
    }
}
