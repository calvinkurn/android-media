package com.tokopedia.search.result.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

internal class NonSwipeableViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
): ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}